package co.edu.unal.handlers;

import co.edu.unal.model.Playlist;
import co.edu.unal.ui.Choose;
import co.edu.unal.ui.Mp3Window;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UIEventHandler {
  private final Playlist playlist;
  private final Mp3Window view;
  private final TableManagerHandler tableManagerHandler;

  public UIEventHandler(Playlist playlist, Mp3Window view, TableManagerHandler tableManagerHandler) {
    this.playlist = playlist;
    this.view = view;
    this.tableManagerHandler = tableManagerHandler;
  }

  public void handleOpenFile() {
    playlist.addSongList(Choose.getChoose());
    if (!playlist.getFileSong().isEmpty()) {
      loadFirstSong();
    }
    tableManagerHandler.populateLocalTable();
    selectFirstTableRow();
  }

  public void handleOpenDirectory() {
    try {
      playlist.addSongDir(Choose.getDirectory());
      if (!playlist.getFileSong().isEmpty()) {
        loadFirstSong();
      }
    } catch (Exception e) {
      log.error("Error opening directory: {}", e.getMessage());
    }
    tableManagerHandler.populateLocalTable();
    selectFirstTableRow();
  }

  public void handleOpenPlaylist() {
    playlist.addSongDir(Choose.getOpenList());
    if (!playlist.getFileSong().isEmpty()) {
      loadFirstSong();
    }
    tableManagerHandler.populateLocalTable();
    selectFirstTableRow();
  }

  public void handleSavePlaylist() {
    Choose.getSaveList(playlist);
  }

  public void handleDeleteSong(int selectedRow) {
    if (selectedRow >= 0) {
      playlist.delete(selectedRow);
      int currentIndex = playlist.getCurrentIndexSong();
      if (currentIndex < playlist.getFileSong().size()) {
        updateSongNameDisplay(currentIndex);
      }
      tableManagerHandler.populateLocalTable();
      selectCurrentTableRow();
    }
  }

  public void handleShowSongInfo(int selectedRow) {
    if (selectedRow < 0) return;
    
    int modelIndex = view.getTableListSong().convertRowIndexToModel(selectedRow);
    var song = playlist.getFileSong().get(modelIndex);

    String info = String.format(
        "Título: %s\nAutor: %s\nÁlbum: %s\nDuración: %s\nAño: %s",
        song.getTitle(), song.getAuthor(), song.getAlbum(),
        song.getTime(), song.getYear()
    );

    JOptionPane.showMessageDialog(null, info, "Información", JOptionPane.INFORMATION_MESSAGE);
  }

  public void handleClearPlaylist() {
    playlist.deleteAll();
    view.getNameSongs().setText("Bienvenido");
    tableManagerHandler.populateLocalTable();
  }

  public void handleShowAbout() {
    String aboutText = "Desarrollado por: Jhonatan Guzmán\nPara Programación Orientada a Objetos 2013 - I";
    JOptionPane.showMessageDialog(null, aboutText, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
  }

  public void handleSetPlaybackMode(int mode) {
    playlist.setOption(mode);
  }

  private void loadFirstSong() {
    try {
      playlist.putInMemoryFirst();
      updateSongNameDisplay(playlist.getCurrentIndexSong());
      playlist.play();
      view.getBtnPlay().setText("||");
      playlist.setRunning(true);
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Empty List");
    }
  }

  private void updateSongNameDisplay(int songIndex) {
    if (songIndex >= 0 && songIndex < playlist.getFileSong().size()) {
      view.getNameSongs().setText(
          playlist.getFileSong().get(songIndex).getSelectedSong().getName()
      );
    }
  }

  private void selectFirstTableRow() {
    view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
  }

  private void selectCurrentTableRow() {
    int currentIndex = playlist.getCurrentIndexSong();
    if (currentIndex >= 0) {
      view.getTableListSong().getSelectionModel()
          .setSelectionInterval(currentIndex, currentIndex);
    }
  }
}
