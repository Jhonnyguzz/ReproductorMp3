package co.edu.unal.handlers;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SongImageLoaderHandler {

  private static final String DEFAULT_ICON_PATH = "/note.png";

  public static void loadAndDisplayImage(File songFile, Component targetComponent) {
    if (songFile == null || !songFile.exists()) {
      setDefaultIcon(targetComponent);
      return;
    }

    try {
      Mp3File mp3file = new Mp3File(songFile.getAbsolutePath());
      
      if (mp3file.hasId3v2Tag()) {
        ID3v2 id3Tag = mp3file.getId3v2Tag();
        byte[] albumImageData = id3Tag.getAlbumImage();
        
        if (albumImageData != null) {
          setImageIcon(targetComponent, albumImageData);
        } else {
          setDefaultIcon(targetComponent);
        }
      } else {
        setDefaultIcon(targetComponent);
      }
    } catch (UnsupportedTagException | InvalidDataException | IOException e) {
      log.error("Error leyendo metadata MP3: {}", e.getMessage());
      setDefaultIcon(targetComponent);
    }
  }

  private static void setImageIcon(Component component, byte[] imageData) {
    try {
      ImageIcon icon = new ImageIcon(imageData);
      Icon scaledIcon = new ImageIcon(icon.getImage()
          .getScaledInstance(component.getWidth(), component.getHeight(), Image.SCALE_DEFAULT));
      
      if (component instanceof JButton) {
        ((JButton) component).setIcon(scaledIcon);
      } else if (component instanceof JLabel) {
        ((JLabel) component).setIcon(scaledIcon);
      }
    } catch (Exception e) {
      log.error("Error procesando imagen: {}", e.getMessage());
      setDefaultIcon(component);
    }
  }

  private static void setDefaultIcon(Component component) {
    try {
      ImageIcon icon = new ImageIcon(SongImageLoaderHandler.class.getResource(DEFAULT_ICON_PATH));
      if (icon.getImage() != null) {
        Icon scaledIcon = new ImageIcon(icon.getImage()
            .getScaledInstance(component.getWidth(), component.getHeight(), Image.SCALE_DEFAULT));
        
        if (component instanceof JButton) {
          ((JButton) component).setIcon(scaledIcon);
        } else if (component instanceof JLabel) {
          ((JLabel) component).setIcon(scaledIcon);
        }
      }
    } catch (Exception e) {
      log.error("Error cargando icono por defecto: {}", e.getMessage());
    }
  }
}
