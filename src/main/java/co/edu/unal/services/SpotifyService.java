package co.edu.unal.services;

import co.edu.unal.config.SpotifyConfig;
import io.javalin.Javalin;
import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

@Slf4j
public class SpotifyService {
  private SpotifyApi spotifyApi;
  private static final Set<String> ALLOWED_USERS = Set.of("22d6cnnf44imvjxjnxjhbja4y", "osuarezd1994");
  private static final int PLAYLIST_ITEMS_LIMIT = 100;

  public void initialize() {
    startAuthenticationServer();
    buildSpotifyApi();
  }

  private void startAuthenticationServer() {
    Javalin.create(config ->
        config.routes.get("/callback", ctx -> {
          String code = ctx.queryParam("code");
          if (code != null) {
            handleAuthorizationCode(code, ctx);
          } else {
            ctx.result("Error: el code parameter no fue enviado en la solicitud");
          }
        })
    ).start();
  }

  private void handleAuthorizationCode(String code, io.javalin.http.Context ctx) {
    try {
      AuthorizationCodeCredentials credentials = spotifyApi.authorizationCode(code)
          .build()
          .execute();

      spotifyApi.setAccessToken(credentials.getAccessToken());
      spotifyApi.setRefreshToken(credentials.getRefreshToken());

      log.debug("✅ Access Token: {}", credentials.getAccessToken());
      log.debug("🔄 Refresh Token: {}", credentials.getRefreshToken());
      log.debug("Token expires in: {}", credentials.getExpiresIn());

      ctx.result("Ya puede cerrar esta ventana.");
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      ctx.result("Error: " + e.getMessage());
    }
  }

  private void buildSpotifyApi() {
    spotifyApi = new SpotifyApi.Builder()
        .setClientId(SpotifyConfig.getClientId())
        .setClientSecret(SpotifyConfig.getClientSecret())
        .setRedirectUri(SpotifyHttpManager.makeUri(SpotifyConfig.getRedirectUri()))
        .build();

    spotifyApi.authorizationCodeUri()
        .scope("user-read-private,user-read-email,playlist-read-private")
        .show_dialog(true)
        .build()
        .executeAsync()
        .thenAccept(authorizationUrl -> {
          log.info("Para continuar con Spotify, inicia sesión en: {}", authorizationUrl);
          try {
            Desktop.getDesktop().browse(authorizationUrl);
          } catch (IOException e) {
            log.error("Error abriendo navegador: {}", e.getMessage());
          }
        });
  }

  public CompletableFuture<List<PlaylistSimplified>> getCurrentUserPlaylistsAsync() {
      return spotifyApi.getListOfCurrentUsersPlaylists()
              .build()
              .executeAsync()
              .thenApplyAsync(Paging::getItems)
              .thenApplyAsync(items -> Arrays.stream(items)
                  .filter(playlist -> ALLOWED_USERS.contains(playlist.getOwner().getId()))
                  .toList()
              );
  }

  public Map<String, List<Track>> getTracksFromPlaylistsAsync(List<PlaylistSimplified> playlists) {
    log.info("Loading tracks from playlists asynchronously...");
    return playlists.parallelStream()
        .collect(Collectors.toConcurrentMap(
            PlaylistSimplified::getName,
            this::fetchPlaylistTracksAsync
        ));
  }

  private List<Track> fetchPlaylistTracksAsync(PlaylistSimplified playlistSimplified) {
    return spotifyApi.getPlaylistsItems(playlistSimplified.getId())
        .offset(0)
        .limit(PLAYLIST_ITEMS_LIMIT)
        .build()
        .executeAsync()
        .thenApplyAsync(myFirstPage -> {
          List<PlaylistTrack> allTracks = new ArrayList<>(
              Arrays.asList(myFirstPage.getItems()));

          int totalPages = (int) Math.ceil(myFirstPage.getTotal() / (double) PLAYLIST_ITEMS_LIMIT);
          if (totalPages > 1) {
            allTracks.addAll(fetchRemainingPagesAsync(playlistSimplified.getId(), totalPages));
          }
          return allTracks.stream()
              .map(PlaylistTrack::getTrack)
              .filter(track -> track instanceof Track)
              .map(track -> (Track) track)
              .sorted(Comparator.comparing(Track::getName))
              .toList();
        }).join();
  }

  private List<PlaylistTrack> fetchRemainingPagesAsync(String playlistId, int totalPages) {
    List<CompletableFuture<PlaylistTrack[]>> pageFutures = IntStream.range(1, totalPages)
        .mapToObj(pageIndex -> {
          int offset = pageIndex * PLAYLIST_ITEMS_LIMIT;
          return spotifyApi.getPlaylistsItems(playlistId)
              .offset(offset)
              .limit(PLAYLIST_ITEMS_LIMIT)
              .build()
              .executeAsync()
              .thenApplyAsync(Paging::getItems);
        })
        .toList();

    CompletableFuture.allOf(pageFutures.toArray(CompletableFuture[]::new)).join();
    return pageFutures.stream()
        .map(CompletableFuture::join)
        .peek(elements -> log.info("Fetched {} tracks from playlist {}", elements.length, playlistId))
        .flatMap(Arrays::stream)
        .toList();
  }

  public void refreshAccessToken() {
    try {
      AuthorizationCodeCredentials credentials = spotifyApi.authorizationCodeRefresh()
          .build()
          .execute();
      
      spotifyApi.setAccessToken(credentials.getAccessToken());
      log.debug("✅ Access Token renewed: {}", credentials.getAccessToken());
      log.debug("Token expires in: {}", credentials.getExpiresIn());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      log.error("Error renovando token: {}", e.getMessage());
      throw new RuntimeException("Error renovando token: " + e.getMessage(), e);
    }
  }

  public boolean isReady() {
    return spotifyApi != null && spotifyApi.getAccessToken() != null;
  }

}
