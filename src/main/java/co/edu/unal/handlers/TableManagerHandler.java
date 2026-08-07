package co.edu.unal.handlers;

import co.edu.unal.model.Playlist;
import co.edu.unal.ui.Mp3Window;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

@Slf4j
public class TableManagerHandler {
  private final Playlist playlist;
  private final Mp3Window view;

  public TableManagerHandler(Playlist playlist, Mp3Window view) {
    this.playlist = playlist;
    this.view = view;
  }

  public void populateLocalTable() {
    createPlaylistListTable();
    createSongDetailsTable();
  }

  private void createPlaylistListTable() {
    String[] columns = new String[]{"Lista de Reproducción"};
    DefaultTableModel model = createReadOnlyTableModel(columns, new boolean[]{false});
    
    view.getTableListSong().setModel(model);
    view.getTableListSong().getColumnModel().getColumn(0).setResizable(false);
    view.getTableListSong().getColumnModel().getColumn(0).setPreferredWidth(227);
    view.getTableListSong().setRowSorter(
        new TableRowSorter<>(view.getTableListSong().getModel()));

    for (var song : playlist.getFileSong()) {
      model.addRow(new Object[]{song.getSelectedSong().getName()});
    }
  }

  private void createSongDetailsTable() {
    String[] columns = new String[]{"Nombre", "Artista", "Álbum", "Año", "Género"};
    DefaultTableModel model = createEditableTableModel(
        columns, new boolean[]{true, true, true, true, true});
    
    view.getTable().setModel(model);

    for (var song : playlist.getFileSong()) {
      Object[] rowData = new Object[]{
          song.getTitle(),
          song.getAuthor(),
          song.getAlbum(),
          song.getYear(),
          song.getGenre()
      };
      model.addRow(rowData);
    }
  }

  public void populateSpotifyTable(Map<String, List<Track>> tracks) {
    view.getTextFieldSpotify().setText("");
    view.getSpotifyTable().setRowSorter(null);

    String[] columns = new String[]{"Nombre", "Artista", "Álbum", "Playlist", "URL"};
    DefaultTableModel model = createEditableTableModel(
        columns, new boolean[]{true, true, true, true, false});
    
    view.getSpotifyTable().setModel(model);
    renderSpotifyLinks();

    for (var entry : tracks.entrySet()) {
      for (Track track : entry.getValue()) {
        addSpotifyTrackRow(model, track, entry.getKey());
      }
    }
    
    updateSpotifyTrackCount(tracks);
  }

  private void addSpotifyTrackRow(DefaultTableModel model, Track track, String playlistName) {
    List<String> artists = Arrays.stream(track.getArtists())
        .map(ArtistSimplified::getName)
        .toList();
    
    String artistsDisplay = artists.size() == 1 
        ? artists.getFirst()
        : artists.getFirst() + " ft. " + artists.stream()
            .skip(1).collect(Collectors.joining(", "));

    Object[] rowData = new Object[]{
        track.getName(),
        artistsDisplay,
        track.getAlbum().getName(),
        playlistName,
        track.getExternalUrls().getExternalUrls().get("spotify")
    };
    
    model.addRow(rowData);
  }

  private void renderSpotifyLinks() {
    view.getSpotifyTable().getColumnModel().getColumn(4)
        .setCellRenderer(new DefaultTableCellRenderer() {
          @Override
          public Component getTableCellRendererComponent(JTable table, Object value,
              boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel label = new JLabel("<html><a href=''>" + value + "</a></html>");
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setToolTipText("Haz clic para abrir en Spotify");

            if (isSelected) {
              label.setForeground(table.getSelectionForeground());
              label.setBackground(table.getSelectionBackground());
              label.setOpaque(true);
            }

            return label;
          }
        });
  }

  public void openSpotifyLink(MouseEvent e) {
    int row = view.getSpotifyTable().rowAtPoint(e.getPoint());
    int col = view.getSpotifyTable().columnAtPoint(e.getPoint());

    if (col == 4 && row != -1) {
      Object url = view.getSpotifyTable().getValueAt(row, col);
      if (url != null) {
        try {
          Desktop.getDesktop().browse(new URI(url.toString()));
        } catch (Exception ex) {
          log.error("Error opening Spotify link: {}", ex.getMessage());
        }
      }
    }
  }

  private void updateSpotifyTrackCount(Map<String, List<Track>> tracks) {
    long totalTracks = tracks.values().stream().mapToLong(List::size).sum();
    view.getLblTotalTracks().setText("Total canciones: " + totalTracks);
  }

  private DefaultTableModel createReadOnlyTableModel(String[] columns, boolean[] editables) {
    return new DefaultTableModel(new Object[][]{}, columns) {
      @Serial
      private static final long serialVersionUID = 1L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return editables[column];
      }
    };
  }

  private DefaultTableModel createEditableTableModel(String[] columns, boolean[] editables) {
    return new DefaultTableModel(new Object[][]{}, columns) {
      @Serial
      private static final long serialVersionUID = 1L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return editables[column];
      }
    };
  }
}
