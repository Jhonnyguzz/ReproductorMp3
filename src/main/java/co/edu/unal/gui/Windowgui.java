package co.edu.unal.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import lombok.Getter;

/**
 * Class that contains GUI
 *
 * @author Jhonatan Guzmán
 */
@Getter
public class Windowgui extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel repPane;
  private JPanel infoPane;
  private JButton btnPlay;
  private JButton btnStop;
  private JButton btnPrev;
  private JButton btnNext;
  private JButton btnDel;
  private JSlider sliderRep;
  private JSlider sliderVol;
  private JLabel lblTime;
  private JLabel lblVol;
  private JTable tableListSong;
  private JScrollPane scroll;
  private JRadioButton rdbtnLoopList;
  private JRadioButton rdbtnLoopSong;
  private JRadioButton rdbtnNormal;
  private JRadioButton rdbtnRandom;
  private JRadioButton rdbtnJustOnce;
  private JMenuBar jMenuBar;
  private JMenu mnFile;
  private JMenuItem mntmOpen;
  private JMenuItem mntmOpenDir;
  private JMenuItem mntmOpenList;
  private JMenuItem mntmSaveList;
  private JMenuItem mntmExit;
  private JMenu mnEdit;
  private JMenu mnOrder;
  private JMenuItem mntmOrderAz;
  private JMenuItem mntmOrderZa;
  private JMenuItem mntmRemoveList;
  private JMenu mnHelp;
  private JMenuItem mntmAbout;
  private JLabel nameSongs;
  private JMenuItem btnInfo;
  private ButtonGroup buttonGroup = new ButtonGroup();
  private JLabel btnImgSong;
  private ImageIcon fondo = new ImageIcon(
      Objects.requireNonNull(getClass().getResource("/note.png")));
  private JTabbedPane tabbedPane;
  private JPopupMenu popmenu;
  private JMenuItem repPopmenu;
  private JMenuItem edtPopmenu;
  private JMenuItem quitPopmenu;
  private JScrollPane scrollPane;
  private JTable table;
  private JTextField textFieldSearch;

  /**
   * Constructs an instance of windowgui, also add properties of a window as title, bounds, JPanel,
   * buttons...
   */
  public Windowgui() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logoun.png")));
    this.setTitle("Reproductor MP3");
    this.setResizable(false);
    this.setBounds(350, 150, 724, 539);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    tabbedPane = new JTabbedPane();

    repPane = new JPanel();
    repPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    //Menu bar

    jMenuBar = new JMenuBar();
    jMenuBar.setBounds(0, 0, 1018, 21);
    setJMenuBar(jMenuBar);

    mnFile = new JMenu("Archivo");
    jMenuBar.add(mnFile);

    mntmOpen = new JMenuItem("Abrir canci\u00F3n");
    mnFile.add(mntmOpen);

    mntmOpenDir = new JMenuItem("Abrir directorio");
    mnFile.add(mntmOpenDir);

    mntmOpenList = new JMenuItem("Abrir lista");
    mnFile.add(mntmOpenList);

    mntmSaveList = new JMenuItem("Guardar lista");
    mnFile.add(mntmSaveList);

    mntmExit = new JMenuItem("Salir");
    mnFile.add(mntmExit);

    mnEdit = new JMenu("Edici\u00F3n");
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
    getContentPane().setLayout(new CardLayout(0, 0));
    repPane.setLayout(null);

    //Finish menu bar

    btnPlay = new JButton(">");
    btnPlay.setBounds(148, 404, 51, 23);
    btnPlay.setOpaque(false);
    repPane.add(btnPlay);

    btnStop = new JButton("\u25A0");
    btnStop.setBounds(209, 404, 51, 23);
    btnStop.setOpaque(false);
    repPane.add(btnStop);

    sliderRep = new JSlider();
    sliderRep.setValue(0);
    sliderRep.setOpaque(false);
    sliderRep.setBounds(10, 367, 397, 26);
    repPane.add(sliderRep);

    sliderVol = new JSlider();
    sliderVol.setValue(100);
    sliderVol.setBounds(308, 401, 99, 26);
    sliderVol.setOpaque(false);
    repPane.add(sliderVol);

    lblVol = new JLabel("100%");
    lblVol.setBounds(417, 408, 29, 14);
    repPane.add(lblVol);

    btnPrev = new JButton("<<");
    btnPrev.setBounds(10, 404, 59, 23);
    btnPrev.setOpaque(false);
    repPane.add(btnPrev);

    btnNext = new JButton(">>");
    btnNext.setBounds(79, 404, 59, 23);
    btnNext.setOpaque(false);
    repPane.add(btnNext);

    tableListSong = new JTable(
        new DefaultTableModel(null, new String[]{"Lista de Reproducci\u00F3n"}));
    //tableListSong.setRowSorter(new TableRowSorter<>(tableListSong.getModel()));
    scroll = new JScrollPane(tableListSong);
    scroll.setBounds(491, 42, 202, 281);
    repPane.add(scroll);

    rdbtnLoopList = new JRadioButton("Repetir lista");
    buttonGroup.add(rdbtnLoopList);
    rdbtnLoopList.setBounds(576, 343, 117, 23);
    rdbtnLoopList.setOpaque(false);
    repPane.add(rdbtnLoopList);

    rdbtnLoopSong = new JRadioButton("Repetir canci\u00F3n");
    buttonGroup.add(rdbtnLoopSong);
    rdbtnLoopSong.setBounds(576, 370, 117, 23);
    rdbtnLoopSong.setOpaque(false);
    repPane.add(rdbtnLoopSong);

    rdbtnNormal = new JRadioButton("Normal");
    rdbtnNormal.setSelected(true);
    buttonGroup.add(rdbtnNormal);
    rdbtnNormal.setBounds(491, 343, 69, 23);
    rdbtnNormal.setOpaque(false);
    repPane.add(rdbtnNormal);

    rdbtnRandom = new JRadioButton("Aleatorio");
    buttonGroup.add(rdbtnRandom);
    rdbtnRandom.setBounds(491, 370, 83, 23);
    rdbtnRandom.setOpaque(false);
    repPane.add(rdbtnRandom);

    rdbtnJustOnce = new JRadioButton("Una Vez");
    buttonGroup.add(rdbtnJustOnce);
    rdbtnJustOnce.setBounds(491, 404, 83, 23);
    rdbtnJustOnce.setOpaque(false);
    repPane.add(rdbtnJustOnce);

    lblTime = new JLabel("0:00");
    lblTime.setBounds(417, 367, 22, 14);
    repPane.add(lblTime);

    nameSongs = new JLabel("Bienvenido");
    nameSongs.setBounds(10, 334, 436, 14);
    repPane.add(nameSongs);

    btnDel = new JButton("Del");
    btnDel.setBounds(594, 404, 69, 23);
    btnDel.setOpaque(false);
    repPane.add(btnDel);

    btnImgSong = new JLabel("");
    btnImgSong.setBounds(10, 11, 445, 312);
    btnImgSong.setIcon(new ImageIcon(fondo.getImage()
        .getScaledInstance(btnImgSong.getWidth(), btnImgSong.getHeight(), Image.SCALE_DEFAULT)));
    repPane.add(btnImgSong);

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

    textFieldSearch = new JTextField();
    textFieldSearch.setBounds(561, 11, 132, 20);
    repPane.add(textFieldSearch);
    textFieldSearch.setColumns(10);

    JLabel lblBuscar = new JLabel("Buscar");
    lblBuscar.setBounds(491, 17, 46, 14);
    repPane.add(lblBuscar);

    infoPane = new JPanel();
    infoPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    infoPane.setLayout(new BorderLayout(0, 0));

    table = new JTable(new DefaultTableModel(
        new Object[][]{
        },
        new String[]{
            "Nombre", "Artista", "\u00C1lbum", "A\u00F1o", "G\u00E9nero"
        }
    ));
    scrollPane = new JScrollPane(table);
    infoPane.add(scrollPane, BorderLayout.CENTER);

    tabbedPane.addTab("Detalles", infoPane);

    getContentPane().add(tabbedPane, "name_430147323327930");
  }
}