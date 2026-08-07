package co.edu.unal.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Objects;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class WindowguiMigLayout extends Mp3Window {

  public WindowguiMigLayout() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logoun.png")));
    this.setTitle("Reproductor MP3");
    this.setResizable(true);
    this.setSize(724, 539);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    tabbedPane = new JTabbedPane();

    // Reproduction Panel
    repPane = new JPanel();
    repPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    repPane.setLayout(new MigLayout("fill, insets 5", "[][grow][]", "[][grow][][]"));

    // Menu bar
    jMenuBar = new JMenuBar();
    setJMenuBar(jMenuBar);

    mnFile = new JMenu("Archivo");
    jMenuBar.add(mnFile);

    mntmOpen = new JMenuItem("Abrir canción");
    mnFile.add(mntmOpen);

    mntmOpenDir = new JMenuItem("Abrir directorio");
    mnFile.add(mntmOpenDir);

    mntmOpenList = new JMenuItem("Abrir lista");
    mnFile.add(mntmOpenList);

    mntmSaveList = new JMenuItem("Guardar lista");
    mnFile.add(mntmSaveList);

    mntmExit = new JMenuItem("Salir");
    mnFile.add(mntmExit);

    mnEdit = new JMenu("Edición");
    jMenuBar.add(mnEdit);

    mnOrder = new JMenu("Ordenar");
    mnEdit.add(mnOrder);

    mntmOrderAz = new JMenuItem("Ordenar A-Z");
    mnOrder.add(mntmOrderAz);

    mntmOrderZa = new JMenuItem("Ordenar Z-A");
    mnOrder.add(mntmOrderZa);

    mntmRemoveList = new JMenuItem("Borrar Lista");
    mnEdit.add(mntmRemoveList);

    mnHelp = new JMenu("Ayuda");
    jMenuBar.add(mnHelp);

    mntmAbout = new JMenuItem("Acerca de...");
    mnHelp.add(mntmAbout);

    // Create main panel for image and song list
    JPanel mainContentPanel = new JPanel(
        new MigLayout("fill, insets 0", "[][grow]", "[grow][]"));

    // Left side: Image panel
    JPanel leftPanel = new JPanel(
        new MigLayout("fill, insets 0", "[grow]", "[grow]"));

    fondo = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/note.png")));
    btnImgSong = new JLabel("");
    btnImgSong.setIcon(new ImageIcon(fondo.getImage()
        .getScaledInstance(445, 312, Image.SCALE_DEFAULT)));
    leftPanel.add(btnImgSong, "grow");

    mainContentPanel.add(leftPanel, "grow");

    // Right side: Song list panel
    JPanel rightPanel = new JPanel(
        new MigLayout("fill, insets 0", "[grow]", "[]10[grow]10[][][]"));

    JLabel lblSearchLabel = new JLabel("Buscar");
    rightPanel.add(lblSearchLabel, "wrap");

    textFieldSearch = new JTextField();
    textFieldSearch.setColumns(15);
    rightPanel.add(textFieldSearch, "grow, wrap");

    tableListSong = new JTable(
        new DefaultTableModel(null, new String[]{"Lista de Reproducción"}));
    scroll = new JScrollPane(tableListSong);
    rightPanel.add(scroll, "grow, wrap");

    // Play mode radio buttons
    buttonGroup = new ButtonGroup();

    rdbtnNormal = new JRadioButton("Normal");
    rdbtnNormal.setSelected(true);
    buttonGroup.add(rdbtnNormal);
    rdbtnNormal.setOpaque(false);
    rightPanel.add(rdbtnNormal, "wrap");

    rdbtnRandom = new JRadioButton("Aleatorio");
    buttonGroup.add(rdbtnRandom);
    rdbtnRandom.setOpaque(false);
    rightPanel.add(rdbtnRandom, "wrap");

    rdbtnLoopList = new JRadioButton("Repetir lista");
    buttonGroup.add(rdbtnLoopList);
    rdbtnLoopList.setOpaque(false);
    rightPanel.add(rdbtnLoopList, "wrap");

    rdbtnLoopSong = new JRadioButton("Repetir canción");
    buttonGroup.add(rdbtnLoopSong);
    rdbtnLoopSong.setOpaque(false);
    rightPanel.add(rdbtnLoopSong, "wrap");

    rdbtnJustOnce = new JRadioButton("Una Vez");
    buttonGroup.add(rdbtnJustOnce);
    rdbtnJustOnce.setOpaque(false);
    rightPanel.add(rdbtnJustOnce, "wrap");

    JPanel modePanel = new JPanel(
        new MigLayout("fill, insets 0", "[grow]", "[]"));
    modePanel.add(rightPanel, "grow");

    mainContentPanel.add(modePanel, "grow, wrap");

    // Name of current song
    nameSongs = new JLabel("Bienvenido");
    mainContentPanel.add(nameSongs, "spanx 2, wrap");

    repPane.add(mainContentPanel, "grow, spanx 3, wrap");

    // Playback controls panel
    JPanel playbackPanel = new JPanel(
        new MigLayout("fill, insets 0", "[][][][] []15 [][]", "[]"));

    btnPrev = new JButton("<<");
    btnPrev.setOpaque(false);
    playbackPanel.add(btnPrev);

    btnPlay = new JButton(">");
    btnPlay.setOpaque(false);
    playbackPanel.add(btnPlay);

    btnStop = new JButton("■");
    btnStop.setOpaque(false);
    playbackPanel.add(btnStop);

    btnNext = new JButton(">>");
    btnNext.setOpaque(false);
    playbackPanel.add(btnNext);

    sliderRep = new JSlider();
    sliderRep.setValue(0);
    sliderRep.setOpaque(false);
    playbackPanel.add(sliderRep, "grow");

    lblTime = new JLabel("0:00");
    playbackPanel.add(lblTime);

    sliderVol = new JSlider();
    sliderVol.setValue(100);
    sliderVol.setOpaque(false);
    sliderVol.setPreferredSize(new java.awt.Dimension(100, 30));
    playbackPanel.add(sliderVol);

    lblVol = new JLabel("100%");
    playbackPanel.add(lblVol);

    btnDel = new JButton("Del");
    btnDel.setOpaque(false);
    playbackPanel.add(btnDel);

    repPane.add(playbackPanel, "spanx 3, grow, wrap");

    // Popup menu for context actions
    popmenu = new JPopupMenu();
    repPopmenu = new JMenuItem("Reproducir");
    btnInfo = new JMenuItem("Detalles");
    edtPopmenu = new JMenuItem("Editar");
    quitPopmenu = new JMenuItem("Quitar");

    popmenu.add(repPopmenu);
    popmenu.add(btnInfo);
    popmenu.add(edtPopmenu);
    popmenu.add(quitPopmenu);

    tabbedPane.addTab("Reproducir", repPane);

    // Info Panel
    infoPane = new JPanel();
    infoPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    infoPane.setLayout(new MigLayout("fill, insets 5", "[grow]", "[grow]"));

    table = new JTable(new DefaultTableModel(
        new Object[][]{
        },
        new String[]{
            "Nombre", "Artista", "Álbum", "Año", "Género"
        }
    ));
    scrollPane = new JScrollPane(table);
    infoPane.add(scrollPane, "grow");

    tabbedPane.addTab("Detalles", infoPane);

    // Spotify Panel
    spotifyPane = new JPanel();
    spotifyPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    spotifyPane.setLayout(new MigLayout("fill, insets 5", "[grow][][]", "[][][grow][]"));

    lblSearchSpotify = new JLabel("Buscar");
    spotifyPane.add(lblSearchSpotify);

    textFieldSpotify = new JTextField();
    textFieldSpotify.setColumns(15);
    spotifyPane.add(textFieldSpotify);

    btnConnectSpotify = new JButton("Conectar Spotify");
    spotifyPane.add(btnConnectSpotify, "wrap");

    btnLoadTracks = new JButton("Cargar Canciones");
    spotifyPane.add(btnLoadTracks, "skip 1, spanx 2, wrap");

    btnRefreshToken = new JButton("Refrescar Token");
    spotifyPane.add(btnRefreshToken, "skip 2, wrap");

    spotifyTable = new JTable(new DefaultTableModel(
        new Object[][]{
        },
        new String[]{
            "Nombre", "Artista", "Álbum", "Playlist", "URL"
        }
    ));
    scrollSpotifyPane = new JScrollPane(spotifyTable);
    spotifyPane.add(scrollSpotifyPane, "spanx 3, grow, wrap");

    lblTotalTracks = new JLabel("Total Canciones: 0");
    spotifyPane.add(lblTotalTracks, "spanx 3");

    tabbedPane.addTab("Spotify", spotifyPane);

    getContentPane().add(tabbedPane);
  }

  static void main() {
    EventQueue.invokeLater(() -> {
      try {
        JFrame.setDefaultLookAndFeelDecorated(true);
        UIManager.setLookAndFeel(new FlatLightLaf());

        WindowguiMigLayout window = new WindowguiMigLayout();
        window.setVisible(true);
      } catch (UnsupportedLookAndFeelException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
