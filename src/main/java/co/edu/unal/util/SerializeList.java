package co.edu.unal.util;

import co.edu.unal.model.Playlist;
import co.edu.unal.model.Song;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerializeList implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public static void serialize(String fileName, Playlist ob) {
    try {
      FileOutputStream output = new FileOutputStream(fileName);
      ObjectOutputStream oboutput = new ObjectOutputStream(output);
      oboutput.writeObject(ob);
      oboutput.close();
      output.close();
    } catch (FileNotFoundException e) {
      log.error("File not found", e);
    } catch (IOException e) {
      log.error("Error while serializing", e);
    }
  }

  public static ArrayList<Song> deserialize(String fileName) {
    Playlist obret = null;
    try {
      FileInputStream input = new FileInputStream(fileName);
      ObjectInputStream obinput = new ObjectInputStream(input);

      obret = (Playlist) obinput.readObject();
      obinput.close();
      input.close();
    } catch (Exception e) {
      log.error("Could not load any playlist", e);
      return new ArrayList<>();
    }
    return obret.getFileSong();
  }
}