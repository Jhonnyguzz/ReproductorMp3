package co.edu.unal.controller;

import co.edu.unal.handlers.PlaybackHandler;
import co.edu.unal.handlers.SearchHandler;
import co.edu.unal.handlers.SongImageLoaderHandler;
import co.edu.unal.handlers.TableManagerHandler;
import co.edu.unal.handlers.UIEventHandler;
import co.edu.unal.model.Playlist;
import co.edu.unal.services.SpotifyService;
import co.edu.unal.ui.Mp3Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

@Slf4j
public class Controller implements ActionListener, ChangeListener, BasicPlayerListener,
    MouseListener, MouseMotionListener, KeyListener {

  private final Playlist playlist;
  private final Mp3Window view;
  private final PlaybackHandler playbackHandler;
  private final UIEventHandler uiEventHandler;
  private final TableManagerHandler tableManagerHandler;
  private final SearchHandler searchHandler;
  private final SpotifyService spotifyService;

  public Controller(Mp3Window view, Playlist playlist) {
    this.playlist = playlist;
    this.view = view;
    this.tableManagerHandler = new TableManagerHandler(playlist, view);
    this.playbackHandler = new PlaybackHandler(playlist, view);
    this.uiEventHandler = new UIEventHandler(playlist, view, tableManagerHandler);
    this.searchHandler = new SearchHandler(view);
    this.spotifyService = new SpotifyService();

    registerListeners();
  }

  private void registerListeners() {
    registerActionListeners();
    registerChangeListeners();
    registerMouseListeners();
    registerKeyListeners();
    registerPlayerListener();
  }

  private void registerActionListeners() {
    view.getBtnPlay().addActionListener(this);
    view.getBtnStop().addActionListener(this);
    view.getBtnPrev().addActionListener(this);
    view.getBtnNext().addActionListener(this);
    view.getMntmOpen().addActionListener(this);
    view.getMntmOpenDir().addActionListener(this);
    view.getMntmOpenList().addActionListener(this);
    view.getMntmSaveList().addActionListener(this);
    view.getMntmExit().addActionListener(this);
    view.getMntmRemoveList().addActionListener(this);
    view.getMntmAbout().addActionListener(this);
    view.getBtnDel().addActionListener(this);
    view.getBtnInfo().addActionListener(this);
    view.getBtnConnectSpotify().addActionListener(this);
    view.getBtnLoadTracks().addActionListener(this);
    view.getBtnRefreshToken().addActionListener(this);
    view.getRdbtnNormal().addActionListener(this);
    view.getRdbtnLoopList().addActionListener(this);
    view.getRdbtnLoopSong().addActionListener(this);
    view.getRdbtnRandom().addActionListener(this);
    view.getRdbtnJustOnce().addActionListener(this);
    view.getRepPopmenu().addActionListener(this);
  }

  private void registerChangeListeners() {
    view.getSliderVol().addChangeListener(this);
  }

  private void registerMouseListeners() {
    view.getSliderRep().addMouseMotionListener(this);
    view.getTableListSong().addMouseListener(this);
    view.getSpotifyTable().addMouseListener(this);
    view.getPopmenu().addMouseListener(this);
  }

  private void registerKeyListeners() {
    view.getTextFieldSearch().addKeyListener(this);
    view.getTextFieldSpotify().addKeyListener(this);
  }

  private void registerPlayerListener() {
    playlist.getPlayer().addBasicPlayerListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    // Playback controls
    if (source == view.getBtnPlay()) playbackHandler.playOrPause();
    if (source == view.getBtnStop()) playbackHandler.stop();
    if (source == view.getBtnPrev()) playbackHandler.previousSong();
    if (source == view.getBtnNext()) playbackHandler.nextSong();

    // File operations
    if (source == view.getMntmOpen()) uiEventHandler.handleOpenFile();
    if (source == view.getMntmOpenDir()) uiEventHandler.handleOpenDirectory();
    if (source == view.getMntmOpenList()) uiEventHandler.handleOpenPlaylist();
    if (source == view.getMntmSaveList()) uiEventHandler.handleSavePlaylist();
    if (source == view.getMntmExit()) System.exit(0);
    if (source == view.getMntmRemoveList()) uiEventHandler.handleClearPlaylist();

    // UI operations
    if (source == view.getMntmAbout()) uiEventHandler.handleShowAbout();
    if (source == view.getBtnDel()) uiEventHandler.handleDeleteSong(view.getTableListSong().getSelectedRow());
    if (source == view.getBtnInfo()) uiEventHandler.handleShowSongInfo(view.getTableListSong().getSelectedRow());

    // Playback modes
    if (source == view.getRdbtnNormal()) uiEventHandler.handleSetPlaybackMode(0);
    if (source == view.getRdbtnLoopList()) uiEventHandler.handleSetPlaybackMode(1);
    if (source == view.getRdbtnLoopSong()) uiEventHandler.handleSetPlaybackMode(2);
    if (source == view.getRdbtnRandom()) uiEventHandler.handleSetPlaybackMode(3);
    if (source == view.getRdbtnJustOnce()) uiEventHandler.handleSetPlaybackMode(4);

    // Spotify operations
    if (source == view.getBtnConnectSpotify()) initializeSpotify();
    if (source == view.getBtnLoadTracks()) loadSpotifyTracks();
    if (source == view.getBtnRefreshToken()) spotifyService.refreshAccessToken();

    // Context menu
    if (source == view.getRepPopmenu()) playbackHandler.playFromTable(view.getTableListSong().getSelectedRow());
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == view.getSliderVol()) {
      double volume = view.getSliderVol().getValue() / 100.0;
      playlist.setVolume(volume);
      applyVolume();
      view.getLblVol().setText(view.getSliderVol().getValue() + "%");
    }
  }

  @Override
  public void opened(Object arg0, Map properties) {
    playlist.setBytesLength(0);
    if (properties.containsKey("audio.length.bytes")) {
      playlist.setBytesLength(
          Double.parseDouble(properties.get("audio.length.bytes").toString()));
    }
    loadSongImage();
  }

  @Override
  public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
    float progressUpdate = (float) (bytesread * 1.0f / playlist.getBytesLength() * 1.0f);
    playlist.setProgressSong((int) (playlist.getBytesLength() * progressUpdate));

    view.getSliderRep().setMaximum((int) playlist.getBytesLength());
    view.getSliderRep().setValue(playlist.getProgressSong());
    view.getLblTime().setText(formatTime(microseconds));
  }

  @Override
  public void stateUpdated(BasicPlayerEvent event) {
    if (event.getCode() == 8) {
      handleSongEnd();
    }
  }

  @Override
  public void setController(BasicController arg0) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Object source = e.getSource();

    if (source == view.getTableListSong()) {
      if (e.getClickCount() == 2 && javax.swing.SwingUtilities.isLeftMouseButton(e)) {
        playbackHandler.playFromTable(view.getTableListSong().getSelectedRow());
      }
    } else if (source == view.getSpotifyTable()) {
      if (e.getClickCount() == 2 && javax.swing.SwingUtilities.isLeftMouseButton(e)) {
        tableManagerHandler.openSpotifyLink(e);
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
  }

  @Override
  public void mousePressed(MouseEvent arg0) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getClickCount() == 1 && javax.swing.SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
      showContextMenu(e);
    }
  }

  @Override
  public void mouseDragged(MouseEvent arg0) {
    try {
      playlist.getPlayer().seek(view.getSliderRep().getValue());
    } catch (BasicPlayerException e) {
      log.error("Error al buscar en canción: {}", e.getMessage());
    }
  }

  @Override
  public void mouseMoved(MouseEvent arg0) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    Object source = e.getSource();

    if (source == view.getTextFieldSearch()) {
      searchHandler.applyLocalPlaylistFilter(view.getTextFieldSearch().getText());
    } else if (source == view.getTextFieldSpotify()) {
      searchHandler.applySpotifyPlaylistFilter(view.getTextFieldSpotify().getText());
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  public void printSpotifyTable(Map<String, List<Track>> tracks) {
    tableManagerHandler.populateSpotifyTable(tracks);
  }

  public void initializeSpotify() {
    spotifyService.initialize();
  }

  public void loadSpotifyTracks() {
    //TODO: Delegate this to CompletableFuture, check the executeAsync from spotify
    List<PlaylistSimplified> playlists = spotifyService.getCurrentUserPlaylists();
    Map<String, List<Track>> tracks = spotifyService.getTracksFromPlaylists(playlists);
    printSpotifyTable(tracks);
  }

  public void playOrPause() {
    playbackHandler.playOrPause();
  }

  public void theNextSong() {
    playbackHandler.nextSong();
  }

  public void thePrevSong() {
    playbackHandler.previousSong();
  }

  public void stopAllSong() {
    playbackHandler.stop();
  }

  public void handleSongEnd() {
    switch (playlist.getOption()) {
      case 0 -> handleNormalModeEnd();
      case 1 -> handleLoopListModeEnd();
      case 2 -> handleLoopSongModeEnd();
      case 3 -> handleRandomModeEnd();
      case 4 -> handleJustOnceModeEnd();
    }
  }

  private void handleNormalModeEnd() {
    if (playlist.getCurrentIndexSong() == playlist.getFileSong().size() - 1) {
      playbackHandler.stop();
      applyVolume();
    } else {
      playbackHandler.nextSong();
    }
  }

  private void handleLoopListModeEnd() {
    playbackHandler.nextSong();
  }

  private void handleLoopSongModeEnd() {
    playbackHandler.playFromTable(view.getTableListSong().getSelectedRow());
  }

  private void handleRandomModeEnd() {
    playbackHandler.nextSong();
  }

  private void handleJustOnceModeEnd() {
    playbackHandler.stop();
    applyVolume();
  }

  private void loadSongImage() {
    try {
      var component = view.getBtnImgSong();
      SongImageLoaderHandler.loadAndDisplayImage(
          playlist.getFileSong().get(playlist.getCurrentIndexSong()).getSelectedSong(),
          component
      );
    } catch (Exception e) {
      log.error("Error cargando imagen de canción: {}", e.getMessage());
    }
  }

  public void applyVolume() {
    try {
      playlist.getPlayer().setGain(playlist.getVolume());
    } catch (BasicPlayerException e) {
      log.error("Error aplicando volumen: {}", e.getMessage());
    }
  }

  private String formatTime(long microseconds) {
    int milliseconds = (int) (microseconds / 1000);
    int seconds = (milliseconds / 1000) % 60;
    int minutes = (milliseconds / 1000) / 60;
    
    return String.format("%d:%02d", minutes, seconds);
  }

  private void showContextMenu(MouseEvent e) {
    int row = view.getTableListSong().rowAtPoint(e.getPoint());
    if (row >= 0) {
      view.getTableListSong().getSelectionModel().setSelectionInterval(row, row);
      view.getPopmenu().show(e.getComponent(), e.getX(), e.getY());
    }
  }
}
