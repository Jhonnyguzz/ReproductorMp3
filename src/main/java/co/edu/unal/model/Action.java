package co.edu.unal.model;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Action {

  private final BasicPlayer player;

  public Action() {
    player = new BasicPlayer();
  }

  public void play() {
    try {
      player.play();
    } catch (BasicPlayerException e) {
      log.error("Error occurred while playing the song", e);
      JOptionPane.showMessageDialog(null, "Error al reproducir la canción", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void pause() {
    try {
      player.pause();
    } catch (BasicPlayerException e) {
      log.error("Error occurred while pausing the song", e);
    }
  }

  public void resume() {
    try {
      player.resume();
    } catch (BasicPlayerException e) {
      log.error("Error occurred while resuming the song", e);
    }
  }

  public void stop() {
    try {
      player.stop();
    } catch (BasicPlayerException e) {
      log.error("Error occurred while stopping the song", e);
    }
  }
}