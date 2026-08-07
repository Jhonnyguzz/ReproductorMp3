package co.edu.unal.model;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Playlist extends Action implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private ArrayList<Song> fileSong;
  private int currentIndexSong;
  private int size;
  private int progressSong;
  private int option;
  private boolean running = false;
  private double bytesLength;
  private double volume = 1.0;

  public Playlist() {
    fileSong = new ArrayList<>();
  }

  public void addSongList(Song fileSelected) {
    fileSong.add(fileSelected);
  }

  public void addSongDir(List<Song> files) {
    fileSong.addAll(files);
  }

  public void nextSong() {
    currentIndexSong = currentIndexSong + 1 >= fileSong.size() ? 0 : currentIndexSong + 1;
    try {
      if(fileSong.isEmpty()) return;
      getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
    } catch (BasicPlayerException | RuntimeException e) {
      log.error("Error occurred while opening the song", e);
    }
  }

  public void prevSong() {
    currentIndexSong = Math.max(currentIndexSong - 1, 0);
    try {
      if(fileSong.isEmpty()) return;
      getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
    } catch (BasicPlayerException | RuntimeException e) {
      log.error("Error occurred while opening the song", e);
    }
  }


  public void putInMemoryFirst() {
    currentIndexSong = 0;
    try {
      getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
    } catch (BasicPlayerException e) {
      log.error("Error occurred while opening the song", e);
      JOptionPane.showMessageDialog(null, "Error al cargar la canción", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void putInMemory(int index) {
    try {
      getPlayer().open(fileSong.get(index).getSelectedSong());
    } catch (BasicPlayerException e) {
      log.error("Error occurred while opening the song", e);
      JOptionPane.showMessageDialog(null, "Error al cargar la canción", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void delete(int i) {
    //TODO Don't stop the song if the delete file is not in open method
    fileSong.remove(i);
    currentIndexSong = i;
    try {
      getPlayer().open(this.fileSong.get(i).getSelectedSong());
    } catch (BasicPlayerException e) {
      log.error("Error occurred while opening the song", e);
      JOptionPane.showMessageDialog(null, "Error al cargar la canción", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    this.play();
  }


  public void deleteAll() {
    File arch = new File("");
    try {
      getPlayer().open(arch);
    } catch (BasicPlayerException e) {
      log.error("Error occurred while opening the song", e);
      JOptionPane.showMessageDialog(null, "Error al cargar la canción", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    fileSong.clear();
  }

  @Override
  public String toString() {
    return "Suena: " + fileSong.get(currentIndexSong).getSelectedSong().getName();
  }
}