package co.edu.unal.controller;

import co.edu.unal.model.Playlist;
import co.edu.unal.ui.Mp3Window;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

public class ControllerForWindows extends Controller implements HotkeyListener,
    IntellitypeListener {

  public ControllerForWindows(Mp3Window view, Playlist playlist) {
    super(view, playlist);

    //Hotkeys
    JIntellitype.getInstance().addHotKeyListener(this);
    JIntellitype.getInstance().addIntellitypeListener(this);
  }

  public void onHotKey(int aIdentifier) {
    System.out.println("WM_HOTKEY message received " + aIdentifier);
  }

  public void onIntellitype(int aCommand) {
    switch (aCommand) {
      case JIntellitype.APPCOMMAND_BROWSER_BACKWARD ->
          System.out.println("BROWSER_BACKWARD message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_FAVOURITES ->
          System.out.println("BROWSER_FAVOURITES message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_FORWARD ->
          System.out.println("BROWSER_FORWARD message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_HOME ->
          System.out.println("BROWSER_HOME message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_REFRESH ->
          System.out.println("BROWSER_REFRESH message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_SEARCH ->
          System.out.println("BROWSER_SEARCH message received " + aCommand);
      case JIntellitype.APPCOMMAND_BROWSER_STOP ->
          System.out.println("BROWSER_STOP message received " + aCommand);
      case JIntellitype.APPCOMMAND_LAUNCH_APP1 ->
          System.out.println("LAUNCH_APP1 message received " + aCommand);
      case JIntellitype.APPCOMMAND_LAUNCH_APP2 ->
          System.out.println("LAUNCH_APP2 message received " + aCommand);
      case JIntellitype.APPCOMMAND_LAUNCH_MAIL ->
          System.out.println("LAUNCH_MAIL message received " + aCommand);
      case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK -> {
        System.out.println("MEDIA_NEXTTRACK message received " + aCommand);
        this.theNextSong();
      }
      case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE -> {
        System.out.println("MEDIA_PLAY_PAUSE message received " + aCommand);
        this.playOrPause();
      }
      case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK -> {
        System.out.println("MEDIA_PREVIOUSTRACK message received " + aCommand);
        this.thePrevSong();
      }
      case JIntellitype.APPCOMMAND_MEDIA_STOP -> {
        System.out.println("MEDIA_STOP message received " + aCommand);
        this.stopAllSong();
      }
      case JIntellitype.APPCOMMAND_VOLUME_DOWN ->
          System.out.println("VOLUME_DOWN message received " + aCommand);
      case JIntellitype.APPCOMMAND_VOLUME_UP ->
          System.out.println("VOLUME_UP message received " + aCommand);
      case JIntellitype.APPCOMMAND_VOLUME_MUTE ->
          System.out.println("VOLUME_MUTE message received " + aCommand);
      default -> System.out.println("Undefined INTELLITYPE message caught " + aCommand);
    }
  }
}