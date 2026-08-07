package co.edu.unal.model;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
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
    } catch (UnsupportedTagException | InvalidDataException | IOException e) {
      log.error("Error assigning ID3 tags", e);
    }
  }

  @Override
  public String toString() {
    return "Suena: " + selectedSong;
  }

  @Override
  public int compareTo(Song other) {
    return this.selectedSong.getName().compareToIgnoreCase(other.selectedSong.getName());
  }
}