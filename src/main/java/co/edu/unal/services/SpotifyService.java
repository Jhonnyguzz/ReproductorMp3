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

  public List<PlaylistSimplified> getCurrentUserPlaylists() {
    try {
      return Arrays.stream(spotifyApi.getListOfCurrentUsersPlaylists()
          .build()
          .execute()
          .getItems())
          .filter(playlist -> ALLOWED_USERS.contains(playlist.getOwner().getId()))
          .toList();
    } catch (IOException | SpotifyWebApiException | ParseException ex) {
      log.error("Error obteniendo playlists: {}", ex.getMessage());
      throw new RuntimeException("Error obteniendo playlists: " + ex.getMessage(), ex);
    }
  }

  public Map<String, List<Track>> getTracksFromPlaylists(List<PlaylistSimplified> playlists) {
    log.info("Loading tracks from playlists...");
    return playlists.parallelStream()
        .collect(Collectors.toConcurrentMap(
            PlaylistSimplified::getName,
            this::fetchPlaylistTracks
        ));
  }

  private List<Track> fetchPlaylistTracks(PlaylistSimplified playlist) {
    try {
      Paging<PlaylistTrack> firstPage = spotifyApi.getPlaylistsItems(playlist.getId())
          .offset(0)
          .limit(PLAYLIST_ITEMS_LIMIT)
          .build()
          .execute();

      List<PlaylistTrack> allTracks = new ArrayList<>(
          Arrays.asList(firstPage.getItems()));
      
      int totalPages = (int) Math.ceil(firstPage.getTotal() / (double) PLAYLIST_ITEMS_LIMIT);

      if (totalPages > 1) {
        allTracks.addAll(fetchRemainingPages(playlist.getId(), totalPages));
      }

      return allTracks.stream()
          .map(PlaylistTrack::getTrack)
          .filter(track -> track instanceof Track)
          .map(track -> (Track) track)
          .sorted(Comparator.comparing(Track::getName))
          .toList();

    } catch (IOException | SpotifyWebApiException | ParseException e) {
      log.error("Error cargando tracks: {}", e.getMessage());
      throw new RuntimeException("Error cargando tracks: " + e.getMessage(), e);
    }
  }

  private List<PlaylistTrack> fetchRemainingPages(String playlistId, int totalPages) {
    List<PlaylistTrack> allRemainingTracks = new ArrayList<>();

    List<CompletableFuture<PlaylistTrack[]>> pageFutures = IntStream.range(1, totalPages)
        .mapToObj(pageIndex -> {
          int offset = pageIndex * PLAYLIST_ITEMS_LIMIT;
          return spotifyApi.getPlaylistsItems(playlistId)
              .offset(offset)
              .limit(PLAYLIST_ITEMS_LIMIT)
              .build()
              .executeAsync()
              .thenApply(Paging::getItems);
        })
        .toList();

    CompletableFuture.allOf(pageFutures.toArray(CompletableFuture[]::new)).join();
    pageFutures.stream()
        .map(CompletableFuture::join)
        .peek(elements -> log.info("Fetched {} tracks from playlist {}", elements.length, playlistId))
        .forEach(elements -> allRemainingTracks.addAll(Arrays.asList(elements)));

    return allRemainingTracks;
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

}
