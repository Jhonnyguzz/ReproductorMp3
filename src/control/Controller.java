package control;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.imageio.ImageIO;
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

import entities.Playlist;
import gui.Choose;
import gui.Windowgui;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import utilities.OrderList;

/**
 * This class is the controller of pattern MVC,
 * implements all Listeners that need 
 * @author Jhonatan Guzmán
 */
public class Controller implements ActionListener,ChangeListener,BasicPlayerListener, MouseListener, MouseMotionListener, KeyListener
{
	private Playlist listmusic = new Playlist();
	private Windowgui view = new Windowgui();
	/**
	 * Constructs a instance of Controller class with 
	 * instance of Windowgui and Playlist as parameters
	 * for add Listeners of these
	 * @param view Instance of Windowgui class
	 * @param listmusic Instance of Playlist class
	 */
	public Controller(Windowgui view, Playlist listmusic)
	{
		this.listmusic=listmusic;
		this.view=view;
		
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
		this.view.getRepPopmenu().addActionListener(this);
		this.view.getEdtPopmenu().addActionListener(this);
		this.view.getQuitPopmenu().addActionListener(this);
		
		this.view.getSliderVol().addChangeListener(this);	
		
		this.view.getSliderRep().addMouseMotionListener(this);
		
		this.view.getTableListSong().addMouseListener(this);
		this.view.getPopmenu().addMouseListener(this);
		
		this.view.getTextFieldSearch().addKeyListener(this);
		
		this.listmusic.getPlayer().addBasicPlayerListener(this);
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

		Object rowData[] = new Object[1];
		
		for(int i=0;i<this.listmusic.getFileSong().size();i++)
		{
			rowData[0]=null;
			rowData[0]=this.listmusic.getFileSong().get(i).getSelectedSong().getName(); 
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
		
		for(int i=0;i<this.listmusic.getFileSong().size();i++)
		{
			rowData1[0]=this.listmusic.getFileSong().get(i).getTitle();
			rowData1[1]=this.listmusic.getFileSong().get(i).getAuthor(); 
			rowData1[2]=this.listmusic.getFileSong().get(i).getAlbum(); 
			rowData1[3]=this.listmusic.getFileSong().get(i).getYear(); 
			rowData1[4]=this.listmusic.getFileSong().get(i).getGenre();
			
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
			listmusic.getPlayer().setGain(listmusic.getVolume());
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
		Object pushBotton = e.getSource();
		
		if(pushBotton==view.getBtnPlay())
		{
		    this.playOrPause();	
		}
		if(pushBotton==view.getBtnStop())
		{
			this.stopAllSong();
		}
		if(pushBotton==view.getBtnPrev())
		{
			this.thePrevSong();
		}
		if(pushBotton==view.getBtnNext())
		{
			this.theNextSong();
		}
		if(pushBotton==view.getMntmOpen())
		{
			listmusic.addSongList(Choose.getChoose());
			if(listmusic.getFileSong().size()==1)
			{
				listmusic.putInMemoryFirst();
				view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
				listmusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listmusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushBotton==view.getMntmOpenDir())
		{
			listmusic.addSongDir(Choose.getDir());
			if(listmusic.getFileSong().size()==listmusic.getTam())
			{
				listmusic.putInMemoryFirst();
				view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
				listmusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listmusic.setRunning(true);
			}
			else
			{
				listmusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listmusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushBotton==view.getMntmOpenList())
		{
			listmusic.addSongDir(Choose.getOpenList());
			if(listmusic.getFileSong().size()==listmusic.getTam())
			{
				listmusic.putInMemoryFirst();
				view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
				listmusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listmusic.setRunning(true);
			}
			else
			{
				listmusic.Play();
				this.view.getBtnPlay().setText("||");
				this.listmusic.setRunning(true);
			}
			this.printTable();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(pushBotton==view.getMntmSaveList())
		{
			Choose.getSaveList(listmusic);
		}
		if(pushBotton==view.getMntmExit())
		{
			System.exit(0);
		}
		if(pushBotton==view.getMntmOrderAz())
		{
			Collections.sort(listmusic.getFileSong());
			this.printTable();
		}
		if(pushBotton==view.getMntmOrderZa())
		{
			OrderList c = new OrderList();
			Collections.sort(listmusic.getFileSong(), c);
			this.printTable();
		}
		if(pushBotton==view.getMntmRemoveList())
		{
			listmusic.deleteAll();
			view.getNameSongs().setText("Bienvenido");
			this.printTable();
		}
		if(pushBotton==view.getMntmAbout())
		{
			JOptionPane.showMessageDialog(null, "Desarrollado por: Jhonatan Guzmán\nPara Programación Orientada a Objetos 2013 - I", "Acerca de", 1);
		}
		if(pushBotton==view.getBtnDel())
		{
			//TODO Don't stop the song if the delete file is not in open method
			//it is in delete method from model
			this.listmusic.delete(this.view.getTableListSong().getSelectedRow());
			this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.printTable();
            this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
		}
		if(pushBotton==view.getBtnInfo())
		{	
			JOptionPane.showMessageDialog(null, 				
					"Título: "+
			        this.listmusic.getFileSong().get(this.view.getTableListSong().getSelectedRow()).getTitle()+"\n"+
					"Autor: "+		
			        this.listmusic.getFileSong().get(this.view.getTableListSong().getSelectedRow()).getAuthor()+"\n"+
			        "Álbum: "+
			        this.listmusic.getFileSong().get(this.view.getTableListSong().getSelectedRow()).getAlbum()+"\n"+
			        "Duración: "+
			        this.listmusic.getFileSong().get(this.view.getTableListSong().getSelectedRow()).getTime()+"\n"+
			        "Año: "+
			        this.listmusic.getFileSong().get(this.view.getTableListSong().getSelectedRow()).getYear(), "Información", 1);
		}
		if(pushBotton==view.getRdbtnNormal())
		{
			this.listmusic.setOption(0);
		}
		if(pushBotton==view.getRdbtnLoopList())
		{
			this.listmusic.setOption(1);
		}
		if(pushBotton==view.getRdbtnLoopSong())
		{
			this.listmusic.setOption(2);
		}
		if(pushBotton==view.getRdbtnRandom())
		{
			this.listmusic.setOption(3);
		}
		if(pushBotton==view.getRepPopmenu())
		{
			this.playSinceTable();
		}
		if(pushBotton==view.getEdtPopmenu())
		{
			//TODO
		}
		if(pushBotton==view.getQuitPopmenu())
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
			listmusic.setVolume((double)view.getSliderVol().getValue()/100);
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
		this.listmusic.setBytesLength(0);
		
		if (properties.containsKey("audio.length.bytes")) 
		{
			this.listmusic.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
		}
		
		String album="Información desconocida";
		String title="Información desconocida";
		String author="Información desconocida";
		String time="Información desconocida";
		String year="Información desconocida";
		
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
		float progressUpdate = (float) (bytesread * 1.0f / this.listmusic.getBytesLength() * 1.0f);
		this.listmusic.setProgressSong((int)(this.listmusic.getBytesLength() * progressUpdate));
		
		this.view.getSliderRep().setMaximum(this.listmusic.getBytesLengthInt());
		this.view.getSliderRep().setValue(this.listmusic.getProgressSong());
		
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
		if(arg0.getCode()==8 && this.listmusic.getOption()==0)
		{
			if(this.listmusic.getK()==this.listmusic.getFileSong().size()-1)
			{
			    this.listmusic.Stop();
			    this.view.getBtnPlay().setText(">");
			    this.listmusic.setRunning(false);
			    this.principalVolume();
			}
			else
			{
			    this.listmusic.Stop();
			    this.listmusic.nextSong();
			    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
                this.listmusic.Play();
                this.view.getBtnPlay().setText("||");
                this.listmusic.setRunning(true);
                this.principalVolume();
                this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
			}
		}
		if(arg0.getCode()==8 && this.listmusic.getOption()==1)
		{
			this.listmusic.Stop();
			this.listmusic.nextSong();
		    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
            this.listmusic.Play();
            this.view.getBtnPlay().setText("||");
            this.listmusic.setRunning(true);
            this.principalVolume();
            this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
            
		}
		if(arg0.getCode()==8 && this.listmusic.getOption()==2)
		{
			this.listmusic.Stop();
		    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
		}
		if(arg0.getCode()==8 && this.listmusic.getOption()==3)
		{

			int max=this.listmusic.getFileSong().size()-1;
			this.listmusic.setK(this.getRandomNumber(0, max));

			
			this.listmusic.Stop();
			this.listmusic.putInMemory(this.listmusic.getK());
		    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
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
	        this.playSinceTable();
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
		    listmusic.getPlayer().seek(this.view.getSliderRep().getValue());
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
		
		TableRowSorter<TableModel> trs2 = new TableRowSorter<>(this.view.getTableListSong().getModel());
		trs2.setRowFilter(RowFilter.regexFilter(regExp, 0));
		
		this.view.getTableListSong().setRowSorter(trs2);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	private void theNextSong()
	{
		if(this.listmusic.getOption()==3)
		{
			int max=this.listmusic.getFileSong().size()-1;
			this.listmusic.setK(this.getRandomNumber(0, max));

			
			this.listmusic.Stop();
			this.listmusic.putInMemory(this.listmusic.getK());
		    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
		}
		else
		{
			listmusic.nextSong();
			view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
		}
	}
	
	private void thePrevSong() 
	{
		//Random song if I press prev button
		if(this.listmusic.getOption()==3)
		{
			int max=this.listmusic.getFileSong().size()-1;
			this.listmusic.setK(this.getRandomNumber(0, max));

			
			this.listmusic.Stop();
			this.listmusic.putInMemory(this.listmusic.getK());
		    this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();	
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
		}
		else
		{
			listmusic.prevSong();
			view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
			this.view.getTableListSong().getSelectionModel().setSelectionInterval(listmusic.getK(), listmusic.getK());
		}
	}
	
	private void playOrPause() 
	{
		switch(this.view.getBtnPlay().getText()) 
	    {
            case ">":
                if(this.listmusic.getRunning()) 
                {
                	listmusic.Continue();
                	this.view.getBtnPlay().setText("||");
                }
                if(this.listmusic.getRunning()==false) 
                {
                	listmusic.Play();
                    this.view.getBtnPlay().setText("||");
                    this.listmusic.setRunning(true);
                }
            break;
            
            case "||":
                    listmusic.Pause();
                    this.view.getBtnPlay().setText(">");
            break;
        }
	}
	
	private void stopAllSong() 
	{
		listmusic.Stop();
		this.view.getBtnPlay().setText(">");
		this.listmusic.setRunning(false);
	}
	
	private void playSinceTable()
	{
		int file=this.view.getTableListSong().getSelectedRow();
		//necessary for filter the table
		file = this.view.getTableListSong().convertRowIndexToModel(file);
		this.listmusic.setK(file);
		
        try 
        {
			this.listmusic.getPlayer().open(this.listmusic.getFileSong().get(file).getSelectedSong());
		} 
        catch (BasicPlayerException e1) 
        {
			e1.printStackTrace();
			System.err.println("Error al intentar reproducir");
		}
        this.view.getNameSongs().setText(this.listmusic.getFileSong().get(file).getSelectedSong().getName());
        this.listmusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listmusic.setRunning(true);
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
			if((int)aux[i]>=65 && (int)aux[i]<=90)
			{
				tmp = "("+aux[i]+"|"+(char)(aux[i]+32)+")";
				sb.append(tmp);
			}
			else if((int)aux[i]>=97 && (int)aux[i]<=122)
			{
				tmp = "("+(char)(aux[i]-32)+"|"+aux[i]+")";
				sb.append(tmp);
			}
			else if(aux[i]=='Ñ' || (int)aux[i]=='ñ')
			{
				tmp = "(Ñ|ñ)";
				sb.append(tmp);
			}
			else
			{
				sb.append(aux[i]);
			}
		}
		return sb.toString();
	}
	
	public void loadImgSong()
	{
		Mp3File mp3file = null;
		try {
			mp3file = new Mp3File(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getAbsolutePath());
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
				File newFile= new File("temp.jpg");
				BufferedImage imag = null;
				try {
					imag = ImageIO.read(new ByteArrayInputStream(albumImageData));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					ImageIO.write(imag, "jpg", newFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				/* A continuación otro método para recuperar la imagen
				
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
				System.out.println("No hay imagen disponible para la canción "+this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
				
			}
		}
	}
}