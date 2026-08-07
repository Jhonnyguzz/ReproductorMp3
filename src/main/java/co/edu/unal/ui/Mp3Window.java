package co.edu.unal.ui;

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
import lombok.Getter;

@Getter
public abstract class Mp3Window extends JFrame {
  
  protected JPanel repPane;
  protected JPanel infoPane;
  protected JPanel spotifyPane;
  protected JButton btnPlay;
  protected JButton btnStop;
  protected JButton btnPrev;
  protected JButton btnNext;
  protected JButton btnDel;
  protected JSlider sliderRep;
  protected JSlider sliderVol;
  protected JLabel lblTime;
  protected JLabel lblVol;
  protected JTable tableListSong;
  protected JScrollPane scroll;
  protected JRadioButton rdbtnLoopList;
  protected JRadioButton rdbtnLoopSong;
  protected JRadioButton rdbtnNormal;
  protected JRadioButton rdbtnRandom;
  protected JRadioButton rdbtnJustOnce;
  protected JMenuBar jMenuBar;
  protected JMenu mnFile;
  protected JMenuItem mntmOpen;
  protected JMenuItem mntmOpenDir;
  protected JMenuItem mntmOpenList;
  protected JMenuItem mntmSaveList;
  protected JMenuItem mntmExit;
  protected JMenu mnEdit;
  protected JMenu mnOrder;
  protected JMenuItem mntmOrderAz;
  protected JMenuItem mntmOrderZa;
  protected JMenuItem mntmRemoveList;
  protected JMenu mnHelp;
  protected JMenuItem mntmAbout;
  protected JLabel nameSongs;
  protected JMenuItem btnInfo;
  protected ButtonGroup buttonGroup;
  protected JLabel btnImgSong;
  protected ImageIcon fondo;
  protected JTabbedPane tabbedPane;
  protected JPopupMenu popmenu;
  protected JMenuItem repPopmenu;
  protected JMenuItem edtPopmenu;
  protected JMenuItem quitPopmenu;
  protected JScrollPane scrollPane;
  protected JScrollPane scrollSpotifyPane;
  protected JTable table;
  protected JTable spotifyTable;
  protected JTextField textFieldSearch;
  protected JLabel lblSearchSpotify;
  protected JLabel lblTotalTracks;
  protected JTextField textFieldSpotify;
  protected JButton btnConnectSpotify;
  protected JButton btnLoadTracks;
  protected JButton btnRefreshToken;
  
}
