package co.edu.unal.ui;

import co.edu.unal.model.Playlist;
import co.edu.unal.model.Song;
import co.edu.unal.util.SerializeList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Choose {

  private static JFileChooser chooseSong;
  private static JFileChooser chooseDir;
  private static JFileChooser openList;
  private static JFileChooser saveList;
  private static File selectedSong;
  private static String readArchive;
  private static String fileName;

  public static Song getChoose() {
    chooseSong = new JFileChooser();
    chooseSong.setFileFilter(new FileNameExtensionFilter("Archivos mp3", "mp3"));
    int a = chooseSong.showOpenDialog(null);
    Song songSelected;
    if (a == JFileChooser.APPROVE_OPTION) {
      selectedSong = chooseSong.getSelectedFile();
    }
    songSelected = new Song(selectedSong);
    return songSelected;
  }

  public static List<Song> getDirectory() throws IOException {
    chooseDir = new JFileChooser();
    chooseDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int a = chooseDir.showOpenDialog(null);

    if (a == JFileChooser.APPROVE_OPTION) {
      Path folder = chooseDir.getSelectedFile().toPath();
      try (Stream<Path> s = Files.list(folder)) {
        return s.parallel()
            .filter(path -> path.toString().endsWith(".mp3"))
            .map(path -> new Song(path.toFile()))
            .toList();
      }
    }
    return new ArrayList<>();
  }

  public static void getSaveList(Playlist ob) {
    saveList = new JFileChooser();
    saveList.setFileFilter(new FileNameExtensionFilter("Listas de reproducción POO", "lrp"));
    int a = saveList.showSaveDialog(null);
    if (a == JFileChooser.APPROVE_OPTION) {
      fileName = saveList.getSelectedFile().getAbsolutePath();

      if (!(fileName.endsWith(".lrp"))) {
        fileName = fileName + ".lrp";
      }

      SerializeList.serialize(fileName, ob);
    }
  }

  public static ArrayList<Song> getOpenList() {
    openList = new JFileChooser();
    openList.setFileFilter(new FileNameExtensionFilter("Listas de reproducción POO", "lrp"));
    int a = openList.showOpenDialog(null);
    if (a == JFileChooser.APPROVE_OPTION) {
      readArchive = openList.getSelectedFile().getAbsolutePath();
    }
    return SerializeList.deserialize(readArchive);
  }

}