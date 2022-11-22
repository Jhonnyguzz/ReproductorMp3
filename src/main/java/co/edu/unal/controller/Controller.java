package co.edu.unal.controller;

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
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

/**
 * This class is the controller of pattern MVC,
 * implements all Listeners that need
 * @author Jhonatan Guzmán
 */
public class Controller implements ActionListener, ChangeListener, BasicPlayerListener, MouseListener, MouseMotionListener, KeyListener
{
	private Playlist listMusic;
	private Windowgui view;
	/**
	 * Constructs a instance of Controller class with
	 * instance of Windowgui and Playlist as parameters
	 * for add Listeners of these
	 * @param view Instance of Windowgui class
	 * @param listMusic Instance of Playlist class
	 */
	public Controller(Windowgui view, Playlist listMusic)
	{
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
		this.view.getPopmenu().addMouseListener(this);

		this.view.getTextFieldSearch().addKeyListener(this);

		this.listMusic.getPlayer().addBasicPlayerListener(this);
	}
	/**
	 * This method is called when you need refresh JTable
	 * with name of Songs in ArrayList
	 */
	public void printTable()
	{
		String columnas[] = new String[]{"Lista de Reproducci\u00F3n"};
		Object filas[][] = new Object[][] {};
		DefaultTableModel modelTable = new DefaultTableModel(filas,columnas)
		{
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {false};
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		};
		this.view.getTableListSong().setModel(modelTable);
		this.view.getTableListSong().getColumnModel().getColumn(0).setResizable(false);
		this.view.getTableListSong().getColumnModel().getColumn(0).setPreferredWidth(227);

		this.view.getTableListSong().setRowSorter(new TableRowSorter<>(this.view.getTableListSong().getModel()));

		Object rowData[] = new Object[1];

		for(int i = 0; i<this.listMusic.getFileSong().size(); i++)
		{
			rowData[0]=null;
			rowData[0]=this.listMusic.getFileSong().get(i).getSelectedSong().getName();
			modelTable.addRow(rowData);
		}

		String columnas1[] = new String[] {"Nombre", "Artista", "\u00C1lbum", "A\u00F1o", "G\u00E9nero"};
		Object filas1[][] = new Object[][] {};
		DefaultTableModel modelTable1 = new DefaultTableModel(filas1,columnas1)
		{
			private static final long serialVersionUID = -5989295416281562571L;
			boolean[] columnEditables = new boolean[] {true,true,true,true,true};
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		};
		this.view.getTable().setModel(modelTable1);

		Object rowData1[] = new Object[5];

		for(int i = 0; i<this.listMusic.getFileSong().size(); i++)
		{
			rowData1[0]=this.listMusic.getFileSong().get(i).getTitle();
			rowData1[1]=this.listMusic.getFileSong().get(i).getAuthor();
			rowData1[2]=this.listMusic.getFileSong().get(i).getAlbum();
			rowData1[3]=this.listMusic.getFileSong().get(i).getYear();
			rowData1[4]=this.listMusic.getFileSong().get(i).getGenre();

			modelTable1.addRow(rowData1);
		}
	}
	/**
	 * This method generate a random integer
	 * for select a random song when option
	 * random is active
	 * @param min int
	 * @param max int
	 * @return A value between min and max
	 */
	public int getRandomNumber(int min, int max)
	{
		return min + (int)(Math.random() * (max - min + 1 ));
	}
	/**
	 * This method get a volume balance
	 * in each song
	 */
	public void principalVolume()
	{
		try
		{
			listMusic.getPlayer().setGain(listMusic.getVolume());
		}
		catch (BasicPlayerException e1)
		{
			e1.printStackTrace();
		}
	}
	/**
	 * Override method for implement ActionListener interface, this method
	 * listen any event produce by a JButton in GUI
	 * @param e ActionEvent of any JButton in GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object pushButton = e.getSource();

		if(pushButton==view.getBtnPlay())
		{
			this.playOrPause();
		}
		if(pushButton==view.getBtnStop())
		{
			this.stopAllSong();
		}
		if(pushButton==view.getBtnPrev())
		{
			this.thePrevSong();
		}
		if(pushButton==view.getBtnNext())
		{
			this.theNextSong();
		}
		if(pushButton==view.getMntmOpen())
		{
			listMusic.addSongList(Choose.getChoose());
			if(listMusic.getFileSong().size()==1)
			{
				try {
					listMusic.putInMemoryFirst();
					view.getNameSongs().setText(listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
				}
				catch(IndexOutOfBoundsException t) {
					System.err.println("Empty List");
				}
				listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushButton==view.getMntmOpenDir())
		{
			try {
				listMusic.addSongDir(Choose.getDir());
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			if(listMusic.getFileSong().size()== listMusic.getTam())
			{
				try {
					listMusic.putInMemoryFirst();
					view.getNameSongs().setText(listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
				}
				catch(IndexOutOfBoundsException t) {
					System.err.println("Empty List");
				}
				listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
			}
			else
			{
				listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushButton==view.getMntmOpenList())
		{
			listMusic.addSongDir(Choose.getOpenList());
			if(listMusic.getFileSong().size()== listMusic.getTam())
			{
				try {
					listMusic.putInMemoryFirst();
					view.getNameSongs().setText(listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
				}catch(IndexOutOfBoundsException t) {
					System.err.println("Empty List");
				}
				listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
			}
			else
			{
				listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushButton==view.getMntmSaveList())
		{
			Choose.getSaveList(listMusic);
		}
		if(pushButton==view.getMntmExit())
		{
			System.exit(0);
		}
		if(pushButton==view.getMntmOrderAz())
		{
			//TODO Deprecated with RowSorter
			//Collections.sort(listmusic.getFileSong());
			//this.printTable();
		}
		if(pushButton==view.getMntmOrderZa())
		{
			//TODO Deprecated with RowSorter
			//OrderList c = new OrderList();
			//Collections.sort(listmusic.getFileSong(), c);
			//this.printTable();
		}
		if(pushButton==view.getMntmRemoveList())
		{
			listMusic.deleteAll();
			view.getNameSongs().setText("Bienvenido");
			this.printTable();
		}
		if(pushButton==view.getMntmAbout())
		{
			JOptionPane.showMessageDialog(null, "Desarrollado por: Jhonatan Guzm\u00E1n\nPara Programaci\u00F3n Orientada a Objetos 2013 - I", "Acerca de", 1);
		}
		if(pushButton==view.getBtnDel())
		{
			//TODO Don't stop the song if the delete file is not in open method
			//it is in delete method from model
			this.listMusic.delete(this.view.getTableListSong().getSelectedRow());
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
		if(pushButton==view.getBtnInfo())
		{
			int file=this.view.getTableListSong().getSelectedRow();
			//necessary for filter the table
			file = this.view.getTableListSong().convertRowIndexToModel(file);

			JOptionPane.showMessageDialog(null,
					"T\u00EDtulo: "+
							this.listMusic.getFileSong().get(file).getTitle()+"\n"+
							"Autor: "+
							this.listMusic.getFileSong().get(file).getAuthor()+"\n"+
							"\u00E1lbum: "+
							this.listMusic.getFileSong().get(file).getAlbum()+"\n"+
							"Duraci\u00F3n: "+
							this.listMusic.getFileSong().get(file).getTime()+"\n"+
							"A\u00F1o: "+
							this.listMusic.getFileSong().get(file).getYear(), "Informaci\u00F3n", 1);
		}

		if(pushButton==view.getRdbtnNormal())
		{
			this.listMusic.setOption(0);
		}
		if(pushButton==view.getRdbtnLoopList())
		{
			this.listMusic.setOption(1);
		}
		if(pushButton==view.getRdbtnLoopSong())
		{
			this.listMusic.setOption(2);
		}
		if(pushButton==view.getRdbtnRandom())
		{
			this.listMusic.setOption(3);
		}
		if(pushButton==view.getRdbtnJustOnce())
		{
			this.listMusic.setOption(4);
		}

		if(pushButton==view.getRepPopmenu())
		{
			this.playFromTable();
		}
		if(pushButton==view.getEdtPopmenu())
		{
			//TODO
		}
		if(pushButton==view.getQuitPopmenu())
		{
			//TODO
		}
	}
	/**
	 * Override method for implement ChangeListener interface, this method
	 * listen any change produce by a JSlider vol in GUI to control volume
	 * @param e ChangeEvent of JSlider vol in GUI
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object moveSlider = e.getSource();

		if(moveSlider==view.getSliderVol())
		{
			listMusic.setVolume((double)view.getSliderVol().getValue()/100);
			this.principalVolume();
			view.getLblVol().setText(view.getSliderVol().getValue()+"%");
		}
	}
	/**
	 * Override method for implement BasicPlayerListener interface, this method
	 * run when a mp3 File is opened with open method of BasicPlayer class,
	 * get BytesLength of that song, for more information, view BasicPlayer 3.0 Javadoc
	 * @param arg0 Object
	 * @param properties Properties of song
	 */
	@Override
	public void opened(Object arg0, Map properties)
	{
		this.listMusic.setBytesLength(0);

		if (properties.containsKey("audio.length.bytes"))
		{
			this.listMusic.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
		}

		String album="Informaci\u00F3n desconocida";
		String title="Informaci\u00F3n desconocida";
		String author="Informaci\u00F3n desconocida";
		String time="Informaci\u00F3n desconocida";
		String year="Informaci\u00F3n desconocida";

		if(properties.containsKey("album"))
		{
			album=properties.get("album").toString();
		}
		if(properties.containsKey("title"))
		{
			title=properties.get("title").toString();
		}
		if(properties.containsKey("author"))
		{
			author=properties.get("author").toString();
		}
		if(properties.containsKey("duration"))
		{
			long microseconds = (long)properties.get("duration");
			int mili = (int) (microseconds / 1000);
			int sec = (mili / 1000) % 60;
			int min = (mili / 1000) / 60;
			time = String.valueOf(min)+":"+String.valueOf(sec);
			if(sec>=0 && sec<=9)
			{
				time = String.valueOf(min)+":"+"0"+String.valueOf(sec);
			}
		}
		if(properties.containsKey("date"))
		{
			year=properties.get("date").toString();
		}

		//TODO in process to delete this listener
		//this.listmusic.getFileSong().get(this.listmusic.getK()).setAlbum(album);
		//this.listmusic.getFileSong().get(this.listmusic.getK()).setTitle(title);
		//this.listmusic.getFileSong().get(this.listmusic.getK()).setAuthor(author);
		//this.listmusic.getFileSong().get(this.listmusic.getK()).setTime(time);
		//this.listmusic.getFileSong().get(this.listmusic.getK()).setYear(year);

		System.out.println("Cargando imagen");
		loadImgSong();

	}
	/**
	 * Override method for implement BasicPlayerListener interface, this method run
	 * many times per second to get information of Song while this sounds, for more
	 * information, view BasicPlayer 3.0 Javadoc
	 * @param bytesread bytes read per second - int
	 * @param microseconds microseconds in Song - long
	 * @param pcmdata byte
	 * @param properties Properties of song - Map
	 */
	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
	{
		float progressUpdate = (float) (bytesread * 1.0f / this.listMusic.getBytesLength() * 1.0f);
		this.listMusic.setProgressSong((int)(this.listMusic.getBytesLength() * progressUpdate));

		this.view.getSliderRep().setMaximum(this.listMusic.getBytesLengthInt());
		this.view.getSliderRep().setValue(this.listMusic.getProgressSong());

		int mili = (int) (microseconds / 1000);
		int sec = (mili / 1000) % 60;
		int min = (mili / 1000) / 60;
		String time = String.valueOf(min)+":"+String.valueOf(sec);
		if(sec>=0 && sec<=9)
		{
			time = String.valueOf(min)+":"+"0"+String.valueOf(sec);
		}
		this.view.getLblTime().setText(time);
	}
	/**
	 * Override method for implement BasicPlayerListener interface, this method run
	 * when a song end, then, sounds next song in ArrayList, for more
	 * information, view BasicPlayer 3.0 Javadoc
	 * @param arg0 BasicPlayerEvent
	 */
	@Override
	public void stateUpdated(BasicPlayerEvent arg0)
	{
		if(arg0.getCode()==8 && this.listMusic.getOption()==0)
		{
			if(this.listMusic.getCurrentIndexSong()==this.listMusic.getFileSong().size()-1)
			{
				this.listMusic.Stop();
				this.view.getBtnPlay().setText(">");
				this.listMusic.setRunning(false);
				this.principalVolume();
			}
			else
			{
				this.listMusic.Stop();
				this.listMusic.nextSong();
				this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
				this.listMusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listMusic.setRunning(true);
				this.principalVolume();
				this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
			}
		}
		if(arg0.getCode()==8 && this.listMusic.getOption()==1)
		{
			this.listMusic.Stop();
			this.listMusic.nextSong();
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());

		}
		if(arg0.getCode()==8 && this.listMusic.getOption()==2)
		{
			this.listMusic.Stop();
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
		}
		if(arg0.getCode()==8 && this.listMusic.getOption()==3)
		{

			int max=this.listMusic.getFileSong().size()-1;
			this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));


			this.listMusic.Stop();
			this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
		if(arg0.getCode()==8 && this.listMusic.getOption()==4)
		{
			this.listMusic.Stop();
			this.view.getBtnPlay().setText(">");
			this.listMusic.setRunning(false);
			this.principalVolume();
		}
	}
	/**
	 * Override method for implement BasicPlayerListener interface,
	 * this method is not used, for more
	 * information, view BasicPlayer 3.0 Javadoc
	 */
	@Override
	public void setController(BasicController arg0) {}
	/**
	 * Override method for implement MouseListener interface,
	 * when you click in a row of JTable, select a index and
	 * play the song with that index in ArrayList
	 * @param e MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e))
		{
			this.playFromTable();
		}
		if(e.getClickCount()==1 && SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger())
		{
			Point p = e.getPoint();
			int rowNumber = this.view.getTableListSong().rowAtPoint( p );
			ListSelectionModel modelo = this.view.getTableListSong().getSelectionModel();
			modelo.setSelectionInterval( rowNumber, rowNumber );
			this.view.getPopmenu().show(e.getComponent(), e.getX(), e.getY());
		}
	}
	/**
	 * Override method for implement MouseListener interface,
	 * this method is not used
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	/**
	 * Override method for implement MouseListener interface,
	 * this method is not used
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}
	/**
	 * Override method for implement MouseListener interface,
	 * this method is not used
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {}
	/**
	 * Override method for implement MouseListener interface,
	 * this method is not used
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.getClickCount()==1 && SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger())
		{
			Point p = e.getPoint();
			int rowNumber = this.view.getTableListSong().rowAtPoint( p );
			ListSelectionModel modelo = this.view.getTableListSong().getSelectionModel();
			modelo.setSelectionInterval( rowNumber, rowNumber );
			this.view.getPopmenu().show(e.getComponent(), e.getX(), e.getY());
		}
	}
	/**
	 * Override method for implement MouseMotionListener interface,
	 * this method get the movement of JSlider rep to forward or backward
	 * the Song
	 * @param arg0 MouseEvent
	 */
	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		try
		{
			listMusic.getPlayer().seek(this.view.getSliderRep().getValue());
		}
		catch (BasicPlayerException e1)
		{
			e1.printStackTrace();
		}
	}
	/**
	 * Override method for implement MouseMotionListener interface,
	 * this method is not used
	 */
	@Override
	public void mouseMoved(MouseEvent arg0){}

	@Override
	public void keyReleased(KeyEvent e)
	{
		String regExp = this.view.getTextFieldSearch().getText();

		regExp = toRegExpWithLowerAndUpper(regExp);
		regExp = ".*" + regExp + ".*";
		//Looking for accent vowels
		//Cuidado! Se est� usando unicode mas no ascii, revisar opciones
//		char char1[] = new char[10];
//		char1[0] = '�';
//		char1[1] = '�';
//		char1[2] = '�';
//		char1[3] = '�';
//		char1[4] = '�';
//		char1[5] = '�';
//		char1[6] = '�';
//		char1[7] = '�';
//		char1[8] = '�';
//		char1[9] = '�';
//
//		for (int i = 0; i < char1.length; i++) {
//			System.out.println((int)char1[i]);
//		}

//		System.out.println(regExp);

		TableRowSorter<TableModel> trs2 = new TableRowSorter<>(this.view.getTableListSong().getModel());
		trs2.setRowFilter(RowFilter.regexFilter(regExp, 0));

		this.view.getTableListSong().setRowSorter(trs2);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	protected void theNextSong()
	{
		if(this.listMusic.getOption()==3)
		{
			int max=this.listMusic.getFileSong().size()-1;
			this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));


			this.listMusic.Stop();
			this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
		else
		{
			listMusic.nextSong();
			view.getNameSongs().setText(listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
	}

	protected void thePrevSong()
	{
		//Random song if I press prev button
		if(this.listMusic.getOption()==3)
		{
			int max=this.listMusic.getFileSong().size()-1;
			this.listMusic.setCurrentIndexSong(this.getRandomNumber(0, max));


			this.listMusic.Stop();
			this.listMusic.putInMemory(this.listMusic.getCurrentIndexSong());
			this.view.getNameSongs().setText(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			this.listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
		else
		{
			listMusic.prevSong();
			view.getNameSongs().setText(listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());
			listMusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listMusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listMusic.getCurrentIndexSong(), listMusic.getCurrentIndexSong());
		}
	}

	protected void playOrPause()
	{
		switch(this.view.getBtnPlay().getText())
		{
			case ">":
				if(this.listMusic.getRunning())
				{
					listMusic.Continue();
					this.view.getBtnPlay().setText("||");
				}
				if(!this.listMusic.getRunning())
				{
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

	protected void stopAllSong()
	{
		listMusic.Stop();
		this.view.getBtnPlay().setText(">");
		this.listMusic.setRunning(false);
	}

	private void playFromTable()
	{
		int file=this.view.getTableListSong().getSelectedRow();
		//necessary for filter the table
		file = this.view.getTableListSong().convertRowIndexToModel(file);
		this.listMusic.setCurrentIndexSong(file);

		try
		{
			this.listMusic.getPlayer().open(this.listMusic.getFileSong().get(file).getSelectedSong());
		}
		catch (BasicPlayerException e1)
		{
			e1.printStackTrace();
			System.err.println("Error al intentar reproducir");
		}
		this.view.getNameSongs().setText(this.listMusic.getFileSong().get(file).getSelectedSong().getName());
		this.listMusic.Play();
		this.view.getBtnPlay().setText("||");
		this.listMusic.setRunning(true);
		this.principalVolume();
	}

	private String toRegExpWithLowerAndUpper(String regExp)
	{
		char aux[] = regExp.toCharArray();
		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for (int i = 0; i < regExp.length(); i++)
		{
			tmp = "";
			if(aux[i]=='A' || aux[i]=='a' || (int)aux[i]==193 || (int)aux[i]==225)
			{
				tmp = "("+"A"+"|"+(char)193+"|a"+"|"+(char)225+")";
				sb.append(tmp);
			}
			else if(aux[i]=='E' || aux[i]=='e' || (int)aux[i]==201 || (int)aux[i]==233)
			{
				tmp = "("+"E"+"|"+(char)201+"|e"+"|"+(char)233+")";
				sb.append(tmp);
			}
			else if(aux[i]=='I' || aux[i]=='i' || (int)aux[i]==205 || (int)aux[i]==237)
			{
				tmp = "("+"I"+"|"+(char)205+"|i"+"|"+(char)237+")";
				sb.append(tmp);
			}
			else if(aux[i]=='O' || aux[i]=='o' || (int)aux[i]==211 || (int)aux[i]==243)
			{
				tmp = "("+"O"+"|"+(char)211+"|o"+"|"+(char)243+")";
				sb.append(tmp);
			}
			else if(aux[i]=='U' || aux[i]=='u' || (int)aux[i]==218 || (int)aux[i]==250)
			{
				tmp = "("+"U"+"|"+(char)218+"|u"+"|"+(char)250+")";
				sb.append(tmp);
			}
			else if((int)aux[i]>=65 && (int)aux[i]<=90)
			{
				tmp = "("+aux[i]+"|"+(char)(aux[i]+32)+")";
				sb.append(tmp);
			}
			else if((int)aux[i]>=97 && (int)aux[i]<=122)
			{
				tmp = "("+(char)(aux[i]-32)+"|"+aux[i]+")";
				sb.append(tmp);
			}
			else if(aux[i]=='\u00F1' || aux[i]=='\u00D1')
			{
				tmp = "(\u00F1|\u00D1)";
				sb.append(tmp);
			}
			else
			{
				sb.append(aux[i]);
			}
		}
		return sb.toString();
	}

	private void loadImgSong()
	{
		Mp3File mp3file = null;
		try {
			mp3file = new Mp3File(this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getAbsolutePath());
		} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
			e1.printStackTrace();
		}

		if (mp3file.hasId3v2Tag())
		{
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();

			byte[] albumImageData = id3v2Tag.getAlbumImage();
			if (albumImageData != null)
			{
				//TODO Fix a issue with Denied access exception by administrator permission
				//Not write the image in separated file
				/*
				File newFile= new File("temp.jpg");
				BufferedImage imag = null;

				try {
					imag = ImageIO.read(new ByteArrayInputStream(albumImageData));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if(!(imag==null)) {
					try {
						ImageIO.write(imag, "jpg", newFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else
				{
					System.out.println("Etiqueta encontrada pero sin imagen");
				}*/

				/* A continuacion otro metodo para recuperar la imagen

				String mimeType = id3v2Tag.getAlbumImageMimeType();
				// Write image to file - can determine appropriate file extension from the mime type
				RandomAccessFile file = null;
				try {
					file = new RandomAccessFile("album-artwork.jpg", "rw");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				try {
					file.write(albumImageData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				} */

				ImageIcon icon = new ImageIcon(albumImageData);
				Icon icono = new ImageIcon(icon.getImage().getScaledInstance(this.view.getBtnImgSong().getWidth(), this.view.getBtnImgSong().getHeight(), Image.SCALE_DEFAULT));
				this.view.getBtnImgSong().setIcon(icono);
			}
			else
			{
				ImageIcon icon = new ImageIcon(getClass().getResource("/note.png"));
				Icon icono = new ImageIcon(icon.getImage().getScaledInstance(this.view.getBtnImgSong().getWidth(), this.view.getBtnImgSong().getHeight(), Image.SCALE_DEFAULT));
				this.view.getBtnImgSong().setIcon(icono);
				System.out.println("No hay imagen disponible para la canci\u00F3n "+this.listMusic.getFileSong().get(listMusic.getCurrentIndexSong()).getSelectedSong().getName());

			}
		}
	}
}