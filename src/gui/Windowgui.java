package gui;  

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
/**
 * Class that contains GUI
 * @author Jhonatan Guzm�n
 */
public class Windowgui extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
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
	private JMenuBar menuBar;
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
	private JButton btnInfo;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel btnImgSong;
	private ImageIcon fondo = new ImageIcon(getClass().getResource("/note.png"));
	/**
	 * Constructs a instance of windowgui, also add properties of 
	 * a window as title, bounds, JPanel, buttons...
	 */
	public Windowgui()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logoun.png")));
		this.setTitle("Reproductor MP3");
		this.setResizable(false);        
		this.setBounds(350,150,665,536); 
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane); 
		contentPane.setLayout(null);
		
		btnPlay = new JButton(">");
		btnPlay.setBounds(178, 390, 52, 23);
		btnPlay.setOpaque(false);
		contentPane.add(btnPlay);

		btnStop = new JButton("\u25A0");
		btnStop.setBounds(238, 390, 46, 23);
		btnStop.setOpaque(false);
		contentPane.add(btnStop);
		
		sliderRep = new JSlider();
		sliderRep.setValue(0);
		sliderRep.setOpaque(false);
		sliderRep.setBounds(33, 356, 386, 23);
		contentPane.add(sliderRep);
		
		sliderVol = new JSlider();
		sliderVol.setOrientation(SwingConstants.VERTICAL);
		sliderVol.setValue(100);
		sliderVol.setBounds(530, 269, 52, 101);
		sliderVol.setOpaque(false);
		contentPane.add(sliderVol);
		
		lblVol = new JLabel("100%");
		lblVol.setBounds(488, 269, 40, 14);
		contentPane.add(lblVol);
		
		btnPrev = new JButton("<<");
		btnPrev.setBounds(92, 390, 52, 23);
		btnPrev.setOpaque(false);
		contentPane.add(btnPrev);
		
		btnNext = new JButton(">>");
		btnNext.setBounds(317, 390, 52, 23);
		btnNext.setOpaque(false);
		contentPane.add(btnNext);
		
		tableListSong = new JTable(new DefaultTableModel(null, new String[]{"Lista de Reproducci\u00F3n"}));
		scroll = new JScrollPane(tableListSong);
		scroll.setBounds(33, 44, 445, 267);
		contentPane.add(scroll);

		rdbtnLoopList = new JRadioButton("Repetir lista");
		buttonGroup.add(rdbtnLoopList);
		rdbtnLoopList.setBounds(488, 77, 109, 23);
		rdbtnLoopList.setOpaque(false);
		contentPane.add(rdbtnLoopList);
		
		rdbtnLoopSong = new JRadioButton("Repetir canci\u00F3n");
		buttonGroup.add(rdbtnLoopSong);
		rdbtnLoopSong.setBounds(488, 109, 138, 23);
		rdbtnLoopSong.setOpaque(false);
		contentPane.add(rdbtnLoopSong);
		
		rdbtnNormal = new JRadioButton("Normal");
		rdbtnNormal.setSelected(true);
		buttonGroup.add(rdbtnNormal);
		rdbtnNormal.setBounds(488, 47, 94, 23);
		rdbtnNormal.setOpaque(false);
		contentPane.add(rdbtnNormal);
		
		rdbtnRandom = new JRadioButton("Aleatorio");
		buttonGroup.add(rdbtnRandom);
		rdbtnRandom.setBounds(488, 140, 109, 23);
		rdbtnRandom.setOpaque(false);
		contentPane.add(rdbtnRandom);
		
		lblTime = new JLabel("0:00");
		lblTime.setBounds(432, 356, 46, 14);
		contentPane.add(lblTime);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 765, 21);
		contentPane.add(menuBar);
		
		mnFile = new JMenu("Archivo");
		menuBar.add(mnFile);
		
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
		menuBar.add(mnEdit);
		
		mnOrder = new JMenu("Ordenar");
		mnEdit.add(mnOrder);
		
		mntmOrderAz = new JMenuItem("Ordenar A-Z");
		mnOrder.add(mntmOrderAz);
		
		mntmOrderZa = new JMenuItem("Ordenar Z-A");
		mnOrder.add(mntmOrderZa);
		
		mntmRemoveList = new JMenuItem("Borrar Lista");
		mnEdit.add(mntmRemoveList);
		
		mnHelp = new JMenu("Ayuda");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("Acerca de...");
		mnHelp.add(mntmAbout);
		
		nameSongs = new JLabel("Bienvenido");
		nameSongs.setBounds(33, 322, 445, 23);
		contentPane.add(nameSongs);
		
		btnDel = new JButton("Del");
		btnDel.setBounds(488, 235, 66, 23);
		btnDel.setOpaque(false);
		contentPane.add(btnDel);
		
		btnInfo = new JButton("Info");
		btnInfo.setBounds(488, 201, 66, 23);
		btnInfo.setOpaque(false);
		contentPane.add(btnInfo);
		
		btnImgSong = new JLabel("");
		btnImgSong.setBounds(488, 367, 161, 129);
		btnImgSong.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(btnImgSong.getWidth(), btnImgSong.getHeight(), Image.SCALE_DEFAULT)));
		contentPane.add(btnImgSong);
	}
	/**
	 * Getter method of JButton btnPlay
	 * @return btnPlay
	 */
    public JButton getBtnPlay()
    {
    	return btnPlay;
    }
	/**
	 * Getter method of JButton btnStop
	 * @return btnStop
	 */
    public JButton getBtnStop()
    {
    	return btnStop;
    }
	/**
	 * Getter method of JButton btnPrev
	 * @return btnPrev
	 */
	public JButton getBtnPrev()
	{
		return btnPrev;
	}
	/**
	 * Getter method of JButton btnNext
	 * @return btnNext
	 */
	public JButton getBtnNext()
	{
		return btnNext;
	}
	/**
	 * Getter method of JMenuItem mntmOpen
	 * @return mntmOpen
	 */
	public JMenuItem getMntmOpen()
	{
		return mntmOpen;
	}
	/**
	 * Getter method of JMenuItem mntmOpenDir
	 * @return mntmOpenDir
	 */
	public JMenuItem getMntmOpenDir()
	{
		return mntmOpenDir;
	}
	/**
	 * Getter method of JMenuItem mntmOpenList
	 * @return mntmOpenList
	 */
	public JMenuItem getMntmOpenList()
	{
		return mntmOpenList;
	}
	/**
	 * Getter method of JMenuItem mntmSaveList
	 * @return mntmSaveList
	 */
	public JMenuItem getMntmSaveList()
	{
		return mntmSaveList;
	}
	/**
	 * Getter method of JMenuItem mntmExit
	 * @return mntmExit
	 */
	public JMenuItem getMntmExit()
	{
		return mntmExit;
	}
	/**
	 * Getter method of JMenuItem mntmOrderAz
	 * @return mntmOrderAz
	 */
	public JMenuItem getMntmOrderAz()
	{
		return mntmOrderAz;
	}
	/**
	 * Getter method of JMenuItem mntmOrderZa
	 * @return mntmOrderZa
	 */
	public JMenuItem getMntmOrderZa()
	{
		return mntmOrderZa;
	}
	/**
	 * Getter method of JMenuItem mntmRemoveList
	 * @return mntmRemoveList
	 */
	public JMenuItem getMntmRemoveList()
	{
		return mntmRemoveList;
	}
	/**
	 * Getter method of JMenuItem mntmAbout
	 * @return mntmAbout
	 */
	public JMenuItem getMntmAbout() 
	{
		return mntmAbout;
	}
	/**
	 * Getter method of JSlider sliderRep
	 * @return sliderRep
	 */
	public JSlider getSliderRep()
	{
		return sliderRep;
	}
	/**
	 * Getter method of JSlider sliderVol
	 * @return sliderVol
	 */
	public JSlider getSliderVol()
	{
		return sliderVol;
	}
	/**
	 * Getter method of JLabel lblVol
	 * @return lblVol
	 */
	public JLabel getLblVol()
	{
		return lblVol;
	}	
	/**
	 * Getter method of JLabel nameSongs
	 * @return nameSongs
	 */
	public JLabel getNameSongs()
	{
		return nameSongs;
	}
	/**
	 * Getter method of JTable tableListSong
	 * @return tableListSong
	 */
	public JTable getTableListSong()
	{
		return tableListSong;
	}
	/**
	 * Getter method of JButton btnDel
	 * @return btnDel
	 */
	public JButton getBtnDel() 
	{
		return btnDel;
	}
	/**
	 * Getter method of JButton btnInfo
	 * @return btnInfo
	 */
	public JButton getBtnInfo() 
	{
		return btnInfo;
	}
	/**
	 * Getter method of JLabel lblTime
	 * @return lblTime
	 */
	public JLabel getLblTime() 
	{
		return lblTime;
	}
	/**
	 * Getter method of JRadioButton rdbtnLoopList
	 * @return rdbtnLoopList
	 */
	public JRadioButton getRdbtnLoopList() 
	{
		return rdbtnLoopList;
	}
	/**
	 * Getter method of JRadioButton rdbtnLoopSong
	 * @return rdbtnLoopSong
	 */
	public JRadioButton getRdbtnLoopSong() 
	{
		return rdbtnLoopSong;
	}
	/**
	 * Getter method of JRadioButton rdbtnNormal
	 * @return rdbtnNormal
	 */
	public JRadioButton getRdbtnNormal() 
	{
		return rdbtnNormal;
	}
	public JRadioButton getRdbtnRandom() 
	{
		return rdbtnRandom;
	}
	public JLabel getBtnImgSong() {
		return btnImgSong;
	}
}