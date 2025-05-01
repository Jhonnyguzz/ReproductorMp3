package co.edu.unal.controller;

import io.javalin.Javalin;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import co.edu.unal.model.Playlist;
import co.edu.unal.gui.Choose;
import co.edu.unal.gui.Windowgui;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

/**
 * This class is the controller of pattern MVC, implements all Listeners that need
 *
 * @author Jhonatan Guzmán
 */
public class Controller implements ActionListener, ChangeListener, BasicPlayerListener,
    MouseListener, MouseMotionListener, KeyListener {

  private Playlist listMusic;
  private Windowgui view;
  private SpotifyApi spotifyApi;

  /**
   * Constructs an instance of Controller class with instance of Windowgui and Playlist as
   * parameters for add Listeners of these
   *
   * @param view      Instance of Windowgui class
   * @param listMusic Instance of Playlist class
   */
  public Controller(Windowgui view, Playlist listMusic) {
    this.listMusic = listMusic;
    this.view = view;

    this.view.getBtnPlay().addActionListener(this);
    this.view.getBtnStop().addActionListener(this);
    this.view.getBtnPrev().addActionListener(this);
    this.view.getBtnNext().addActionListener(this);
    this.view.getMntmOpen().addActionListener(this);
    this.view.getMntmOpenDir().addActionListener(this);
    this.view.getMntmOpenList().addActionListener(this);
    this.view.getMntmSaveList().addActionListener(this);
    this.view.getMntmExit().addActionListener(this);
    this.view.getMntmOrderAz().addActionListener(this);
    this.view.getMntmOrderZa().addActionListener(this);
    this.view.getMntmRemoveList().addActionListener(this);
    this.view.getMntmAbout().addActionListener(this);
    this.view.getBtnDel().addActionListener(this);
    this.view.getBtnInfo().addActionListener(this);
    this.view.getBtnConnectSpotify().addActionListener(this);
    this.view.getBtnLoadTracks().addActionListener(this);
    this.view.getBtnRefreshToken().addActionListener(this);
    this.view.getRdbtnNormal().addActionListener(this);
    this.view.getRdbtnLoopList().addActionListener(this);
    this.view.getRdbtnLoopSong().addActionListener(this);
    this.view.getRdbtnRandom().addActionListener(this);
    this.view.getRdbtnJustOnce().addActionListener(this);
    this.view.getRepPopmenu().addActionListener(this);
    this.view.getEdtPopmenu().addActionListener(this);
    this.view.getQuitPopmenu().addActionListener(this);

    this.view.getSliderVol().addChangeListener(this);

    this.view.getSliderRep().addMouseMotionListener(this);

    this.view.getTableListSong().addMouseListener(this);
    this.view.getSpotifyTable().addMouseListener(this);
    this.view.getPopmenu().addMouseListener(this);

    this.view.getTextFieldSearch().addKeyListener(this);
    this.view.getTextFieldSpotify().addKeyListener(this);

    this.listMusic.getPlayer().addBasicPlayerListener(this);
  }

  /**
   * This method is called when you need refresh JTable with name of Songs in ArrayList
   */
  public void printTable() {
    String[] columnas = new String[]{"Lista de Reproducción"};
    Object[][] filas = new Object[][]{};
    DefaultTableModel modelTable = new DefaultTableModel(filas, columnas) {
      private static final long serialVersionUID = 1L;
      boolean[] columnEditables = new boolean[]{false};

      @Override
      public boolean isCellEditable(int row, int column) {
        return columnEditables[column];
      }
    };
    this.view.getTableListSong().setModel(modelTable);
    this.view.getTableListSong().getColumnModel().getColumn(0).setResizable(false);
    this.view.getTableListSong().getColumnModel().getColumn(0).setPreferredWidth(227);

    this.view.getTableListSong()
        .setRowSorter(new TableRowSorter<>(this.view.getTableListSong().getModel()));

    Object[] rowData = new Object[1];

    for (int i = 0; i < this.listMusic.getFileSong().size(); i++) {
      rowData[0] = this.listMusic.getFileSong().get(i).getSelectedSong().getName();
      modelTable.addRow(rowData);
    }

    String[] columnas1 = new String[]{"Nombre", "Artista", "Álbum", "Año", "Género"};
    Object[][] filas1 = new Object[][]{};
    DefaultTableModel modelTable1 = new DefaultTableModel(filas1, columnas1) {
      private static final long serialVersionUID = -5989295416281562571L;
      boolean[] columnEditables = new boolean[]{true, true, true, true, true};

      @Override
      public boolean isCellEditable(int row, int column) {
        return columnEditables[column];
      }
    };
    this.view.getTable().setModel(modelTable1);

    for (int i = 0; i < this.listMusic.getFileSong().size(); i++) {
      Object[] rowData1 = new Object[5];
      rowData1[0] = this.listMusic.getFileSong().get(i).getTitle();
      rowData1[1] = this.listMusic.getFileSong().get(i).getAuthor();
      rowData1[2] = this.listMusic.getFileSong().get(i).getAlbum();
      rowData1[3] = this.listMusic.getFileSong().get(i).getYear();
      rowData1[4] = this.listMusic.getFileSong().get(i).getGenre();

      modelTable1.addRow(rowData1);
    }
  }

  public void printSpotifyTable(Map<String, List<Track>> tracks) {

    this.view.getTextFieldSpotify().setText("");
    this.view.getSpotifyTable().setRowSorter(null);

    String[] columnas = new String[]{"Nombre", "Artista", "Álbum", "Playlist", "URL"};
    Object[][] filas = new Object[][]{};
    DefaultTableModel modelTable = new DefaultTableModel(filas, columnas) {
      private static final long serialVersionUID = -2L;
      boolean[] columnEditables = new boolean[]{true, true, true, true, false};

      @Override
      public boolean isCellEditable(int row, int column) {
        return columnEditables[column];
      }
    };
    this.view.getSpotifyTable().setModel(modelTable);
    renderingLinks();

    for (Entry<String, List<Track>> trackEntry : tracks.entrySet()) {

      for (Track track : trackEntry.getValue()) {
        Object[] rowData1 = new Object[5];
        List<String> artists = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName)
            .toList();

        rowData1[0] = track.getName();
        rowData1[1] = artists.size() == 1 ? artists.getFirst()
            : artists.getFirst() + " ft. " + artists.stream().skip(1)
                .collect(Collectors.joining(", "));
        rowData1[2] = track.getAlbum().getName();
        rowData1[3] = trackEntry.getKey();
        rowData1[4] = track.getExternalUrls().getExternalUrls().get("spotify");

        modelTable.addRow(rowData1);
      }
    }
    view.getLblTotalTracks().setText("Total canciones: " + tracks.values().stream().mapToLong(List::size).sum());
  }

  private void renderingLinks() {
    this.view.getSpotifyTable().getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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
  });}

  /**
   * This method generate a random integer for select a random song when option random is active
   *
   * @param min int
   * @param max int
   * @return A value between min and max
   */
  public int getRandomNumber(int min, int max) {
    return min + (int) (Math.random() * (max - min + 1));
  }

  /**
   * This method gets a volume balance in each song
   */
  public void principalVolume() {
    try {
      listMusic.getPlayer().setGain(listMusic.getVolume());
    } catch (BasicPlayerException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Override method for implement ActionListener interface, this method listens any event produce
   * by a JButton in GUI
   *
   * @param e ActionEvent of any JButton in GUI
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object pushButton = e.getSource();

    if (pushButton == view.getBtnPlay()) {
      this.playOrPause();
    }
    if (pushButton == view.getBtnStop()) {
      this.stopAllSong();
    }
    if (pushButton == view.getBtnPrev()) {
      this.thePrevSong();
    }
    if (pushButton == view.getBtnNext()) {
      this.theNextSong();
    }
    if (pushButton == view.getMntmOpen()) {
      listMusic.addSongList(Choose.getChoose());
      if (listMusic.getFileSong().size() == 1) {
        try {
          listMusic.putInMemoryFirst();
          view.getNameSongs().setText(
              listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
                  .getName());
        } catch (IndexOutOfBoundsException t) {
          System.err.println("Empty List");
        }
        listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
      }
      this.printTable();
      this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
    }
    if (pushButton == view.getMntmOpenDir()) {
      try {
        listMusic.addSongDir(Choose.getDir());
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
      if (listMusic.getFileSong().size() == listMusic.getTam()) {
        try {
          listMusic.putInMemoryFirst();
          view.getNameSongs().setText(
              listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
                  .getName());
        } catch (IndexOutOfBoundsException t) {
          System.err.println("Empty List");
        }
        listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
      } else {
        listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
      }
      this.printTable();
      this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
    }
    if (pushButton == view.getMntmOpenList()) {
      listMusic.addSongDir(Choose.getOpenList());
      if (listMusic.getFileSong().size() == listMusic.getTam()) {
        try {
          listMusic.putInMemoryFirst();
          view.getNameSongs().setText(
              listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
                  .getName());
        } catch (IndexOutOfBoundsException t) {
          System.err.println("Empty List");
        }
        listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
      } else {
        listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
      }
      this.printTable();
      this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
    }
    if (pushButton == view.getMntmSaveList()) {
      Choose.getSaveList(listMusic);
    }
    if (pushButton == view.getMntmExit()) {
      System.exit(0);
    }
    if (pushButton == view.getMntmOrderAz()) {
      //TODO Deprecated with RowSorter
      //Collections.sort(listmusic.getFileSong());
      //this.printTable();
    }
    if (pushButton == view.getMntmOrderZa()) {
      //TODO Deprecated with RowSorter
      //OrderList c = new OrderList();
      //Collections.sort(listmusic.getFileSong(), c);
      //this.printTable();
    }
    if (pushButton == view.getMntmRemoveList()) {
      listMusic.deleteAll();
      view.getNameSongs().setText("Bienvenido");
      this.printTable();
    }
    if (pushButton == view.getMntmAbout()) {
      JOptionPane.showMessageDialog(null,
          "Desarrollado por: Jhonatan Guzmán\nPara Programación Orientada a Objetos 2013 - I",
          "Acerca de", 1);
    }
    if (pushButton == view.getBtnDel()) {
      //TODO Don't stop the song if the delete file is not in open method
      //it is in delete method from model
      this.listMusic.delete(this.view.getTableListSong().getSelectedRow());
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.printTable();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    }
    if (pushButton == view.getBtnInfo()) {
      int file = this.view.getTableListSong().getSelectedRow();
      //necessary for filter the table
      file = this.view.getTableListSong().convertRowIndexToModel(file);

      JOptionPane.showMessageDialog(null,
          "Título: " +
              this.listMusic.getFileSong().get(file).getTitle() + "\n" +
              "Autor: " +
              this.listMusic.getFileSong().get(file).getAuthor() + "\n" +
              "Álbum: " +
              this.listMusic.getFileSong().get(file).getAlbum() + "\n" +
              "Duración: " +
              this.listMusic.getFileSong().get(file).getTime() + "\n" +
              "Año: " +
              this.listMusic.getFileSong().get(file).getYear(), "Información", 1);
    }
    if (pushButton == view.getBtnConnectSpotify()) {
      buildSpotify();
    }
    if (pushButton == view.getBtnLoadTracks()) {
      getTracksFromCurrentPlaylists(getCurrentUserSpotifyPlaylists());
    }
    if (pushButton == view.getBtnRefreshToken()) {
      refreshSpotifyToken();
    }
    if (pushButton == view.getRdbtnNormal()) {
      this.listMusic.setOption(0);
    }
    if (pushButton == view.getRdbtnLoopList()) {
      this.listMusic.setOption(1);
    }
    if (pushButton == view.getRdbtnLoopSong()) {
      this.listMusic.setOption(2);
    }
    if (pushButton == view.getRdbtnRandom()) {
      this.listMusic.setOption(3);
    }
    if (pushButton == view.getRdbtnJustOnce()) {
      this.listMusic.setOption(4);
    }

    if (pushButton == view.getRepPopmenu()) {
      this.playFromTable();
    }
    if (pushButton == view.getEdtPopmenu()) {
      //TODO: Not usage?
    }
    if (pushButton == view.getQuitPopmenu()) {
      //TODO: Not usage?
    }
  }

  /**
   * Override method for implement ChangeListener interface, this method listen any change produce
   * by a JSlider vol in GUI to control volume
   *
   * @param e ChangeEvent of JSlider vol in GUI
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    Object moveSlider = e.getSource();

    if (moveSlider == view.getSliderVol()) {
      listMusic.setVolume((double) view.getSliderVol().getValue() / 100);
      this.principalVolume();
      view.getLblVol().setText(view.getSliderVol().getValue() + "%");
    }
  }

  /**
   * Override method for implement BasicPlayerListener interface, this method run when a mp3 File is
   * opened with open method of BasicPlayer class, get BytesLength of that song, for more
   * information, view BasicPlayer 3.0 Javadoc
   *
   * @param arg0       Object
   * @param properties Properties of song
   */
  @Override
  public void opened(Object arg0, Map properties) {
    //TODO: Currently is not being used
    this.listMusic.setBytesLength(0);

    if (properties.containsKey("audio.length.bytes")) {
      this.listMusic.setBytesLength(
          Double.parseDouble(properties.get("audio.length.bytes").toString()));
    }

    String album = "Información desconocida";
    String title = "Información desconocida";
    String author = "Información desconocida";
    String time = "Información desconocida";
    String year = "Información desconocida";

    if (properties.containsKey("album")) {
      album = properties.get("album").toString();
    }
    if (properties.containsKey("title")) {
      title = properties.get("title").toString();
    }
    if (properties.containsKey("author")) {
      author = properties.get("author").toString();
    }
    if (properties.containsKey("duration")) {
      long microseconds = (long) properties.get("duration");
      int mili = (int) (microseconds / 1000);
      int sec = (mili / 1000) % 60;
      int min = (mili / 1000) / 60;
      time = min + ":" + sec;
      if (sec >= 0 && sec <= 9) {
        time = min + ":" + "0" + sec;
      }
    }
    if (properties.containsKey("date")) {
      year = properties.get("date").toString();
    }

    System.out.println("Cargando imagen");
    loadImgSong();
  }

  /**
   * Override method for implement BasicPlayerListener interface, this method run many times per
   * second to get information of Song while this sounds, for more information, view BasicPlayer 3.0
   * Javadoc
   *
   * @param bytesread    bytes read per second - int
   * @param microseconds microseconds in Song - long
   * @param pcmdata      byte
   * @param properties   Properties of song - Map
   */
  @Override
  public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
    float progressUpdate = (float) (bytesread * 1.0f / this.listMusic.getBytesLength() * 1.0f);
    this.listMusic.setProgressSong((int) (this.listMusic.getBytesLength() * progressUpdate));

    this.view.getSliderRep().setMaximum(this.listMusic.getBytesLengthInt());
    this.view.getSliderRep().setValue(this.listMusic.getProgressSong());

    int mili = (int) (microseconds / 1000);
    int sec = (mili / 1000) % 60;
    int min = (mili / 1000) / 60;
    String time = min + ":" + sec;
    if (sec >= 0 && sec <= 9) {
      time = min + ":" + "0" + sec;
    }
    this.view.getLblTime().setText(time);
  }

  /**
   * Override method for implement BasicPlayerListener interface, this method run when a song end,
   * then, sounds next song in ArrayList, for more information, view BasicPlayer 3.0 Javadoc
   *
   * @param arg0 BasicPlayerEvent
   */
  @Override
  public void stateUpdated(BasicPlayerEvent arg0) {
    if (arg0.getCode() == 8 && this.listMusic.getOption() == 0) {
      if (this.listMusic.getCurrentIndexSong() == this.listMusic.getFileSong().size() - 1) {
        this.listMusic.Stop();
        this.view.getBtnPlay().setText(">");
        this.listMusic.setRunning(false);
        this.principalVolume();
      } else {
        this.listMusic.Stop();
        this.listMusic.nextSong();
        this.view.getNameSongs().setText(
            this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
                .getName());
        this.listMusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listMusic.setRunning(true);
        this.principalVolume();
        this.view.getTableListSong().getSelectionModel()
            .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
      }
    }
    if (arg0.getCode() == 8 && this.listMusic.getOption() == 1) {
      this.listMusic.Stop();
      this.listMusic.nextSong();
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());

    }
    if (arg0.getCode() == 8 && this.listMusic.getOption() == 2) {
      this.listMusic.Stop();
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
    }
    if (arg0.getCode() == 8 && this.listMusic.getOption() == 3) {

      int max = this.listMusic.getFileSong().size() - 1;
      this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));

      this.listMusic.Stop();
      this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    }
    if (arg0.getCode() == 8 && this.listMusic.getOption() == 4) {
      this.listMusic.Stop();
      this.view.getBtnPlay().setText(">");
      this.listMusic.setRunning(false);
      this.principalVolume();
    }
  }

  /**
   * Override method for implement BasicPlayerListener interface, this method is not used, for more
   * information, view BasicPlayer 3.0 Javadoc
   */
  @Override
  public void setController(BasicController arg0) {
  }

  /**
   * Override method for implement MouseListener interface, when you click in a row of JTable,
   * select an index and play the song with that index in ArrayList
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseClicked(MouseEvent e) {

    Object source = e.getSource();

    if (source == this.view.getTableListSong()) {
      if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
        this.playFromTable();
      }
      if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
        Point p = e.getPoint();
        int rowNumber = this.view.getTableListSong().rowAtPoint(p);
        ListSelectionModel modelo = this.view.getTableListSong().getSelectionModel();
        modelo.setSelectionInterval(rowNumber, rowNumber);
        this.view.getPopmenu().show(e.getComponent(), e.getX(), e.getY());
      }
    } else if (source == this.view.getSpotifyTable()) {
      if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
        int row = view.getSpotifyTable().rowAtPoint(e.getPoint());
        int col = view.getSpotifyTable().columnAtPoint(e.getPoint());

        if (col == 4 && row != -1) {
          Object url = view.getSpotifyTable().getValueAt(row, col);
          if (url != null) {
            try {
              Desktop.getDesktop().browse(new URI(url.toString()));
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * Override method for implement MouseListener interface, this method is not used
   */
  @Override
  public void mouseEntered(MouseEvent arg0) {
  }

  /**
   * Override method for implement MouseListener interface, this method is not used
   */
  @Override
  public void mouseExited(MouseEvent arg0) {
  }

  /**
   * Override method for implement MouseListener interface, this method is not used
   */
  @Override
  public void mousePressed(MouseEvent arg0) {
  }

  /**
   * Override method for implement MouseListener interface, this method is not used
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
      Point p = e.getPoint();
      int rowNumber = this.view.getTableListSong().rowAtPoint(p);
      ListSelectionModel modelo = this.view.getTableListSong().getSelectionModel();
      modelo.setSelectionInterval(rowNumber, rowNumber);
      this.view.getPopmenu().show(e.getComponent(), e.getX(), e.getY());
    }
  }

  /**
   * Override method for implement MouseMotionListener interface, this method get the movement of
   * JSlider rep to forward or backward the Song
   *
   * @param arg0 MouseEvent
   */
  @Override
  public void mouseDragged(MouseEvent arg0) {
    try {
      listMusic.getPlayer().seek(this.view.getSliderRep().getValue());
    } catch (BasicPlayerException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Override method for implement MouseMotionListener interface, this method is not used
   */
  @Override
  public void mouseMoved(MouseEvent arg0) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    Object source = e.getSource();

    if (source == this.view.getTextFieldSearch()) {
      String regExp = toRegExpWithLowerAndUpper(this.view.getTextFieldSearch().getText());
      TableRowSorter<TableModel> trs2 = new TableRowSorter<>(
          this.view.getTableListSong().getModel());
      trs2.setRowFilter(RowFilter.regexFilter(regExp, 0));
      this.view.getTableListSong().setRowSorter(trs2);
    } else if (source == this.view.getTextFieldSpotify()) {
      String regExp = toRegExpWithLowerAndUpper(this.view.getTextFieldSpotify().getText());
      TableRowSorter<TableModel> trs2 = new TableRowSorter<>(
          this.view.getSpotifyTable().getModel());
      trs2.setRowFilter(RowFilter.regexFilter(regExp, 0, 1, 2, 3));
      this.view.getSpotifyTable().setRowSorter(trs2);
      this.view.getLblTotalTracks().setText("Canciones buscadas: " + trs2.getViewRowCount());
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  protected void theNextSong() {
    if (this.listMusic.getOption() == 3) {
      int max = this.listMusic.getFileSong().size() - 1;
      this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));

      this.listMusic.Stop();
      this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    } else {
      listMusic.nextSong();
      view.getNameSongs().setText(
          listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
      listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    }
  }

  protected void thePrevSong() {
    //Random song if I press prev button
    if (this.listMusic.getOption() == 3) {
      int max = this.listMusic.getFileSong().size() - 1;
      this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));

      this.listMusic.Stop();
      this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
      this.view.getNameSongs().setText(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getName());
      this.listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    } else {
      listMusic.prevSong();
      view.getNameSongs().setText(
          listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
      listMusic.Play();
      this.view.getBtnPlay().setText("||");
      this.listMusic.setRunning(true);
      this.principalVolume();
      this.view.getTableListSong().getSelectionModel()
          .setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
    }
  }

  protected void playOrPause() {
    switch (this.view.getBtnPlay().getText()) {
      case ">":
        if (this.listMusic.getRunning()) {
          listMusic.Continue();
          this.view.getBtnPlay().setText("||");
        }
        if (!this.listMusic.getRunning()) {
          listMusic.Play();
          this.view.getBtnPlay().setText("||");
          this.listMusic.setRunning(true);
        }
        break;

      case "||":
        listMusic.Pause();
        this.view.getBtnPlay().setText(">");
        break;
    }
  }

  protected void stopAllSong() {
    listMusic.Stop();
    this.view.getBtnPlay().setText(">");
    this.listMusic.setRunning(false);
  }

  private void playFromTable() {
    int file = this.view.getTableListSong().getSelectedRow();
    //necessary for filter the table
    file = this.view.getTableListSong().convertRowIndexToModel(file);
    this.listMusic.setCurrentIndexSong(file);

    try {
      this.listMusic.getPlayer().open(this.listMusic.getFileSong().get(file).getSelectedSong());
    } catch (BasicPlayerException e1) {
      e1.printStackTrace();
      System.err.println("Error al intentar reproducir");
    }
    this.view.getNameSongs()
        .setText(this.listMusic.getFileSong().get(file).getSelectedSong().getName());
    this.listMusic.Play();
    this.view.getBtnPlay().setText("||");
    this.listMusic.setRunning(true);
    this.principalVolume();
  }

  private String toRegExpWithLowerAndUpper(String regExp) {
    char[] aux = regExp.toCharArray();
    StringBuilder sb = new StringBuilder();
    String tmp;

    for (int i = 0; i < regExp.length(); i++) {
      if (aux[i] == 'A' || aux[i] == 'a' || (int) aux[i] == 193 || (int) aux[i] == 225) {
        tmp = "(" + "A" + "|" + (char) 193 + "|a" + "|" + (char) 225 + ")";
        sb.append(tmp);
      } else if (aux[i] == 'E' || aux[i] == 'e' || (int) aux[i] == 201 || (int) aux[i] == 233) {
        tmp = "(" + "E" + "|" + (char) 201 + "|e" + "|" + (char) 233 + ")";
        sb.append(tmp);
      } else if (aux[i] == 'I' || aux[i] == 'i' || (int) aux[i] == 205 || (int) aux[i] == 237) {
        tmp = "(" + "I" + "|" + (char) 205 + "|i" + "|" + (char) 237 + ")";
        sb.append(tmp);
      } else if (aux[i] == 'O' || aux[i] == 'o' || (int) aux[i] == 211 || (int) aux[i] == 243) {
        tmp = "(" + "O" + "|" + (char) 211 + "|o" + "|" + (char) 243 + ")";
        sb.append(tmp);
      } else if (aux[i] == 'U' || aux[i] == 'u' || (int) aux[i] == 218 || (int) aux[i] == 250) {
        tmp = "(" + "U" + "|" + (char) 218 + "|u" + "|" + (char) 250 + ")";
        sb.append(tmp);
      } else if ((int) aux[i] >= 65 && (int) aux[i] <= 90) {
        tmp = "(" + aux[i] + "|" + (char) (aux[i] + 32) + ")";
        sb.append(tmp);
      } else if ((int) aux[i] >= 97 && (int) aux[i] <= 122) {
        tmp = "(" + (char) (aux[i] - 32) + "|" + aux[i] + ")";
        sb.append(tmp);
      } else if (aux[i] == 'ñ' || aux[i] == 'Ñ') {
        tmp = "(ñ|Ñ)";
        sb.append(tmp);
      } else {
        sb.append(aux[i]);
      }
    }
    return ".*" + sb + ".*";
  }

  private void loadImgSong() {
    Mp3File mp3file = null;
    try {
      mp3file = new Mp3File(
          this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong()
              .getAbsolutePath());
    } catch (UnsupportedTagException | InvalidDataException | IOException e1) {
      e1.printStackTrace();
    }

    if (mp3file.hasId3v2Tag()) {
      ID3v2 id3v2Tag = mp3file.getId3v2Tag();

      byte[] albumImageData = id3v2Tag.getAlbumImage();
      if (albumImageData != null) {
        ImageIcon icon = new ImageIcon(albumImageData);
        Icon icono = new ImageIcon(icon.getImage()
            .getScaledInstance(this.view.getBtnImgSong().getWidth(),
                this.view.getBtnImgSong().getHeight(), Image.SCALE_DEFAULT));
        this.view.getBtnImgSong().setIcon(icono);
      } else {
        ImageIcon icon = new ImageIcon(getClass().getResource("/note.png"));
        Icon icono = new ImageIcon(icon.getImage()
            .getScaledInstance(this.view.getBtnImgSong().getWidth(),
                this.view.getBtnImgSong().getHeight(), Image.SCALE_DEFAULT));
        this.view.getBtnImgSong().setIcon(icono);
        System.out.println(
            "No hay imagen disponible para la canción " + this.listMusic.getFileSong()
                .get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());

      }
    }
  }

  private void httpServerListener() {
    //TODO: Hardcoded port
    Javalin app = Javalin.create().start(8080);

    app.get("/callback", ctx -> {
      String code = ctx.queryParam("code");

      if (code != null) {
        System.out.println("Code: " + code);
        AuthorizationCodeRequest request = spotifyApi.authorizationCode(code).build();
        AuthorizationCodeCredentials credentials = request.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());
        spotifyApi.setRefreshToken(credentials.getRefreshToken());

        System.out.println("✅ Access Token: " + credentials.getAccessToken());
        System.out.println("🔄 Refresh Token: " + credentials.getRefreshToken());
        System.out.println("Token expires in: " + credentials.getExpiresIn());

        ctx.result("Ya puede cerrar esta ventana.");
      } else {
        ctx.result("Error: el code parameter no fue enviado en la solicitud");
      }
    });
  }

  private void buildSpotify() {
    //TODO: Hardcoded values
    httpServerListener();

    this.spotifyApi = new SpotifyApi.Builder()
        .setClientId("")
        .setClientSecret("")
        .setRedirectUri(SpotifyHttpManager.makeUri("http://[::1]:8080/callback"))
        .build();

    spotifyApi.authorizationCodeUri()
        .scope("user-read-private,user-read-email,playlist-read-private")
        .show_dialog(true)
        .build().executeAsync()
        .thenAccept(authorizationCode -> {
          System.out.println(
              "Para continuar con Spotify, por favor inicia sesión en: " + authorizationCode);
          try {
            Desktop.getDesktop().browse(authorizationCode);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private List<PlaylistSimplified> getCurrentUserSpotifyPlaylists() {
    //TODO: Hardcoded values
    Set<String> allowedUsers = Set.of("22d6cnnf44imvjxjnxjhbja4y", "osuarezd1994");
    try {
      return Arrays.stream(spotifyApi.getListOfCurrentUsersPlaylists().build().execute().getItems())
          .filter(playlist -> allowedUsers.contains(playlist.getOwner().getId()))
          .toList();
    } catch (IOException | SpotifyWebApiException | ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void getTracksFromCurrentPlaylists(List<PlaylistSimplified> playlists) {
    Map<String, List<Track>> tracksByPlaylist = playlists.parallelStream()
        .collect(Collectors.toConcurrentMap(
            PlaylistSimplified::getName,
            playlistSimplified -> {

              int limit = 100;

              Paging<PlaylistTrack> firstPage = null;
              try {
                firstPage = spotifyApi.getPlaylistsItems(
                        playlistSimplified.getId())
                    .offset(0)
                    .limit(limit)
                    .build()
                    .execute();

                int total = firstPage.getTotal();
                int totalPages = (int) Math.ceil(total / (double) limit);

                List<CompletableFuture<PlaylistTrack[]>> pageFutures = IntStream.range(1,
                        totalPages)
                    .mapToObj(pageIndex -> {
                      int offset = pageIndex * limit;
                      return spotifyApi.getPlaylistsItems(playlistSimplified.getId())
                          .offset(offset)
                          .limit(limit)
                          .build()
                          .executeAsync()
                          .thenApply(Paging::getItems);
                    })
                    .toList();

                List<PlaylistTrack> allTracks = new ArrayList<>(
                    Arrays.asList(firstPage.getItems()));
                pageFutures.stream()
                    .map(CompletableFuture::join)
                    .forEach(elements -> allTracks.addAll(Arrays.asList(elements)));
                return allTracks.stream()
                    .map(PlaylistTrack::getTrack)
                    .filter(e -> e instanceof Track)
                    .map(e -> (Track) e)
                    .sorted(Comparator.comparing(Track::getName))
                    .toList();
              } catch (IOException | SpotifyWebApiException | ParseException e) {
                throw new RuntimeException(e);
              }
            }));
    printSpotifyTable(tracksByPlaylist);
  }

  private void refreshSpotifyToken() {
    AuthorizationCodeCredentials authorizationCodeCredentials = null;
    try {
      authorizationCodeCredentials = spotifyApi
          .authorizationCodeRefresh()
          .build().execute();
      spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
      System.out.println("✅ Access Token: " + authorizationCodeCredentials.getAccessToken());
      System.out.println("Token expires in: " + authorizationCodeCredentials.getExpiresIn());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      throw new RuntimeException(e);
    }
  }
}