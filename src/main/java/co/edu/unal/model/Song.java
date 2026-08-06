package co.edu.unal.model;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;

/**
 * The class Song has atributes such as title song, author song, duration song, date about song,
 * album song and source file
 *
 * @author Jhonatan Guzmán
 */
@Getter
@Setter
@NoArgsConstructor
public class Song implements Serializable, Comparable<Song> {

  @Serial
  private static final long serialVersionUID = 1L;
  private File selectedSong;
  private String title;
  private String author;
  private String time;
  private String album;
  private String year;
  private String genre;

  /**
   * Constructs a new instance of Song with File as parameter
   *
   * @param selectedSong File with mp3 song
   */
  public Song(File selectedSong) {
    this.selectedSong = selectedSong;
    Mp3File mp3file;
    long mili;
    try {
      mp3file = new Mp3File(this.selectedSong);
      mili = mp3file.getLengthInMilliseconds();

      long sec = (mili / 1000) % 60;
      int min = (int) (mili / 1000) / 60;
      this.time = sec >= 0 && sec <= 9 ? min + ":" + "0" + sec : min + ":" + sec;

      ID3v1 songTags = mp3file.hasId3v2Tag()
          ? mp3file.getId3v2Tag()
          : mp3file.hasId3v1Tag() ? mp3file.getId3v1Tag() : null;
      if (songTags != null) {
        setAuthor(songTags.getArtist());
        setTitle(songTags.getTitle());
        setAlbum(songTags.getAlbum());
        setYear(songTags.getYear());
        setGenre(songTags.getGenreDescription());
      } else {
        setAuthor("Desconocido");
        setTitle("Desconocido");
        setAlbum("Desconocido");
        setYear("Desconocido");
        setGenre("Desconocido");
      }
    } catch (UnsupportedTagException | InvalidDataException | IOException e1) {
      e1.printStackTrace();
      System.err.println("Error asignando etiquetas ID3");
    }
  }

  /**
   * Override method from Object class
   */
  @Override
  public String toString() {
    return "Suena: " + selectedSong;
  }

  /**
   * Override method for implement Comparable interface for compare two Songs to Ignore case
   *
   * @param other other Song's instance
   * @return An Integer 1 or -1 for compare two Songs
   */
  @Override
  public int compareTo(Song other) {
    return this.selectedSong.getName().compareToIgnoreCase(other.selectedSong.getName());
  }
}