package co.edu.unal;

import java.awt.EventQueue;

import javax.swing.*;

import com.melloware.jintellitype.JIntellitype;
import org.pushingpixels.substance.api.skin.*;

import co.edu.unal.controller.Controller;
import co.edu.unal.controller.ControllerForWindows;
import co.edu.unal.model.Playlist;
import co.edu.unal.gui.Windowgui;

/**
 * Principal class
 *
 * @author Jhonatan Guzmán
 */
public class Main {

  /**
   * Principal method for run applet
   *
   * @param args String array
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      try {
        JFrame.setDefaultLookAndFeelDecorated(true); //Decorate also TopBar with Look and Feel
        UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //Based on SO
        //UIManager.setLookAndFeel(new NimbusLookAndFeel());

        Windowgui view = new Windowgui();
        Playlist listMusic = new Playlist();
        /**
         * Only if you have Windows 64bits - check for you OS and JIntellitype.dll (64bits)
         */
        if (JIntellitype.isJIntellitypeSupported()) {
          System.out.println("Using hotkeys for windows (Only 64bits)");
          new ControllerForWindows(view, listMusic);
        } else {
          System.out.println("JIntellitype.dll was not detected on System32 (Only Windows 64bits)");
          new Controller(view, listMusic);
        }
        view.setVisible(true);
      } catch (UnsupportedLookAndFeelException e) {
        throw new RuntimeException(e);
      }
    });
  }
}