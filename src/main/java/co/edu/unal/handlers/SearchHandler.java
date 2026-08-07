package co.edu.unal.handlers;

import co.edu.unal.ui.Mp3Window;

import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SearchHandler {
  private final Mp3Window view;

  public SearchHandler(Mp3Window view) {
    this.view = view;
  }

  public void applyLocalPlaylistFilter(String searchText) {
    String regExp = toRegExpWithAccents(searchText);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
        (DefaultTableModel) view.getTableListSong().getModel());
    sorter.setRowFilter(RowFilter.regexFilter(regExp, 0));
    view.getTableListSong().setRowSorter(sorter);
  }

  public void applySpotifyPlaylistFilter(String searchText) {
    String regExp = toRegExpWithAccents(searchText);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
        (DefaultTableModel) view.getSpotifyTable().getModel());
    sorter.setRowFilter(RowFilter.regexFilter(regExp, 0, 1, 2, 3));
    view.getSpotifyTable().setRowSorter(sorter);
    
    updateSpotifySearchResultCount(sorter);
  }

  private void updateSpotifySearchResultCount(TableRowSorter<DefaultTableModel> sorter) {
    view.getLblTotalTracks().setText("Canciones buscadas: " + sorter.getViewRowCount());
  }

  private String toRegExpWithAccents(String input) {
    //TODO: Refactor after adding unit tests
    StringBuilder pattern = new StringBuilder();
    
    for (char c : input.toCharArray()) {
      switch (Character.toUpperCase(c)) {
        case 'A':
          pattern.append("(A|À|Á|a|à|á)");
          break;
        case 'E':
          pattern.append("(E|É|e|é)");
          break;
        case 'I':
          pattern.append("(I|Í|i|í)");
          break;
        case 'O':
          pattern.append("(O|Ó|o|ó)");
          break;
        case 'U':
          pattern.append("(U|Ú|u|ú)");
          break;
        case 'Ñ':
          pattern.append("(Ñ|ñ)");
          break;
        default:
          if (Character.isLetter(c)) {
            pattern.append("(").append(Character.toUpperCase(c))
                .append("|").append(Character.toLowerCase(c)).append(")");
          } else {
            escapeSpecialChars(pattern, c);
          }
      }
    }
    
    return ".*" + pattern + ".*";
  }

  private void escapeSpecialChars(StringBuilder sb, char c) {
    String specialChars = ".*+?^${}()|[]\\";
    if (specialChars.indexOf(c) >= 0) {
      sb.append("\\").append(c);
    } else {
      sb.append(c);
    }
  }
}
