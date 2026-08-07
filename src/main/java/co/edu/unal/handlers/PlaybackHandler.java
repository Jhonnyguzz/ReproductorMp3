package co.edu.unal.handlers;

import co.edu.unal.model.Playlist;
import co.edu.unal.ui.Mp3Window;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PlaybackHandler {
  private final Playlist playlist;
  private final Mp3Window view;

  public PlaybackHandler(Playlist playlist, Mp3Window view) {
    this.playlist = playlist;
    this.view = view;
  }

  public void playOrPause() {
    switch (view.getBtnPlay().getText()) {
      case ">":
        if (playlist.isRunning()) {
          playlist.resume();
          view.getBtnPlay().setText("||");
        } else {
          playlist.play();
          view.getBtnPlay().setText("||");
          playlist.setRunning(true);
        }
        break;

      case "||":
        playlist.pause();
        view.getBtnPlay().setText(">");
        break;
    }
  }

  public void stop() {
    playlist.stop();
    view.getBtnPlay().setText(">");
    playlist.setRunning(false);
  }

  public void nextSong() {
    if (isRandomMode()) {
      playRandomSong();
    } else {
      playNextInSequence();
    }
  }

  public void previousSong() {
    if (isRandomMode()) {
      playRandomSong();
    } else {
      playPreviousInSequence();
    }
  }

  public void playFromTable(int tableRowIndex) {
    int modelIndex = view.getTableListSong().convertRowIndexToModel(tableRowIndex);
    playlist.setCurrentIndexSong(modelIndex);

    try {
      playlist.getPlayer().open(playlist.getFileSong().get(modelIndex).getSelectedSong());
    } catch (BasicPlayerException e1) {
      System.err.println("Error al intentar reproducir: " + e1.getMessage());
    }
    
    updateUIAfterPlay(modelIndex);
  }

  private void playNextInSequence() {
    playlist.nextSong();
    updateUIAfterPlay(playlist.getCurrentIndexSong());
  }

  private void playPreviousInSequence() {
    playlist.prevSong();
    updateUIAfterPlay(playlist.getCurrentIndexSong());
  }

  private void playRandomSong() {
    int maxIndex = playlist.getFileSong().size() - 1;
    int randomIndex = getRandomNumber(0, maxIndex);
    playlist.setCurrentIndexSong(randomIndex);
    playlist.putInMemory(randomIndex);
    updateUIAfterPlay(randomIndex);
  }

  private void updateUIAfterPlay(int songIndex) {
    if(playlist.getFileSong().isEmpty()) return;
    view.getNameSongs().setText(
        playlist.getFileSong().get(songIndex).getSelectedSong().getName());
    playlist.play();
    view.getBtnPlay().setText("||");
    playlist.setRunning(true);
    applyVolume();
    updateTableSelection(songIndex);
  }

  private void updateTableSelection(int songIndex) {
    view.getTableListSong().getSelectionModel()
        .setSelectionInterval(songIndex, songIndex);
  }

  private void applyVolume() {
    try {
      playlist.getPlayer().setGain(playlist.getVolume());
    } catch (BasicPlayerException e) {
      System.err.println("Error aplicando volumen: " + e.getMessage());
    }
  }

  private boolean isRandomMode() {
    return playlist.getOption() == 3;
  }

  private int getRandomNumber(int min, int max) {
    return min + (int) (Math.random() * (max - min + 1));
  }
}
