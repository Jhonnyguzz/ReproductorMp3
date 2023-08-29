package co.edu.unal.controller;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 * This class uses an instance of BasicPlayer class for using its methods easier and add
 * corresponding Exceptions
 *
 * @author Jhonatan Guzmán
 */
public class Action {

  private BasicPlayer player = new BasicPlayer();

  /**
   * Initialize an instance of Action class with another instance of BasicPlayer class
   *
   * @param player Instance of BasicPlayer class
   */
  public Action(BasicPlayer player) {
    this.player = player;
  }

  /**
   * Initialize an empty instance of Action class
   */
  public Action() {
  }

  /**
   * Setter method of BasicPlayer player
   *
   * @param player Instance of BasicPlayer class
   */
  public void setPlayer(BasicPlayer player) {
    this.player = player;
  }

  /**
   * Getter method of BasicPlayer player
   *
   * @return player
   */
  public BasicPlayer getPlayer() {
    return player;
  }

  /**
   * This method use play method from BasicPlayer class onto the instance of BasicPlayer in this
   * class and add Exceptions of this.
   *
   * @throws BasicPlayerException
   */
  public void Play() {
    try {
      player.play();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
      System.err.println("No se pudo dar play");
      JOptionPane.showMessageDialog(null, "No se pudo reproducir la canci\u00F3n", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * This method use pause method from BasicPlayer class onto the instance of BasicPlayer in this
   * class and add Exceptions of this.
   *
   * @throws BasicPlayerException
   */
  public void Pause() {
    try {
      player.pause();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method use resume method from BasicPlayer class onto the instance of BasicPlayer in this
   * class and add Exceptions of this.
   *
   * @throws BasicPlayerException
   */
  public void Continue() {
    try {
      player.resume();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method use stop method from BasicPlayer class onto the instance of BasicPlayer in this
   * class and add Exceptions of this.
   *
   * @throws BasicPlayerException
   */
  public void Stop() {
    try {
      player.stop();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }
}