package co.edu.unal;

import co.edu.unal.controller.Controller;
import co.edu.unal.controller.ControllerForWindows;
import co.edu.unal.model.Playlist;
import co.edu.unal.ui.Windowgui;
import co.edu.unal.ui.WindowguiMigLayout;
import com.formdev.flatlaf.FlatLightLaf;
import com.melloware.jintellitype.JIntellitype;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  static void main() {
    EventQueue.invokeLater(() -> {
      try {
        JFrame.setDefaultLookAndFeelDecorated(true); //Decorate also TopBar with Look and Feel

        //FlatLaf
        UIManager.setLookAndFeel(new FlatLightLaf());
        //UIManager.setLookAndFeel(new FlatDarculaLaf());
        //UIManager.setLookAndFeel(new FlatMacLightLaf());
        //UIManager.setLookAndFeel(new FlatMacDarkLaf());
        //UIManager.setLookAndFeel(new FlatDarkLaf());
        //UIManager.setLookAndFeel(new FlatIntelliJLaf());

        //Substance
        //UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //Based on SO
        //UIManager.setLookAndFeel(new NimbusLookAndFeel());

        Windowgui view = new Windowgui();
        WindowguiMigLayout viewMigLayout = new WindowguiMigLayout();
        Playlist playlist = new Playlist();

        if (true) {
          if (JIntellitype.isJIntellitypeSupported()) {
            log.info("Using hotkeys for windows (Only 64bits)");
            new ControllerForWindows(view, playlist);
          } else {
            log.info("JIntellitype.dll was not detected on System32 (Only Windows 64bits)");
            new Controller(view, playlist);
          }
          view.setVisible(true);
        } else {
          if (JIntellitype.isJIntellitypeSupported()) {
            log.info("Using hotkeys for windows (Only 64bits)");
            new ControllerForWindows(viewMigLayout, playlist);
          } else {
            log.info("JIntellitype.dll was not detected on System32 (Only Windows 64bits)");
            new Controller(viewMigLayout, playlist);
          }
          viewMigLayout.setVisible(true);
        }
      } catch (UnsupportedLookAndFeelException e) {
        throw new RuntimeException(e);
      }
    });
  }
}