package control;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;

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
public class ControllerForWindows implements ActionListener,ChangeListener,BasicPlayerListener, MouseListener, MouseMotionListener, HotkeyListener, IntellitypeListener
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
	public ControllerForWindows(Windowgui view, Playlist listmusic)
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
		
		this.view.getSliderVol().addChangeListener(this);	
		
		this.view.getSliderRep().addMouseMotionListener(this);
		
		this.view.getTableListSong().addMouseListener(this);
		
		this.listmusic.getPlayer().addBasicPlayerListener(this);
		
		//Hotkeys
		JIntellitype.getInstance().addHotKeyListener(this);		
        JIntellitype.getInstance().addIntellitypeListener(this);
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
			this.listmusic.delete(this.view.getTableListSong().getSelectedRow());
			this.view.getNameSongs().setText(this.listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			this.printTable();
		}
		if(pushBotton==view.getBtnInfo())
		{	
			JOptionPane.showMessageDialog(null, 				
					"Título: "+
			        this.listmusic.getFileSong().get(this.listmusic.getK()).getTitle()+"\n"+
					"Autor: "+		
			        this.listmusic.getFileSong().get(this.listmusic.getK()).getAuthor()+"\n"+
			        "Álbum: "+
			        this.listmusic.getFileSong().get(this.listmusic.getK()).getAlbum()+"\n"+
			        "Duración: "+
			        this.listmusic.getFileSong().get(this.listmusic.getK()).getTime()+"\n"+
			        "Año: "+
			        this.listmusic.getFileSong().get(this.listmusic.getK()).getYear(), "Información", 1);
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
			
		this.listmusic.getFileSong().get(this.listmusic.getK()).setAlbum(album);
		this.listmusic.getFileSong().get(this.listmusic.getK()).setTitle(title);
		this.listmusic.getFileSong().get(this.listmusic.getK()).setAuthor(author);
		this.listmusic.getFileSong().get(this.listmusic.getK()).setTime(time);
		this.listmusic.getFileSong().get(this.listmusic.getK()).setYear(year);
		
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
        int file=this.view.getTableListSong().getSelectedRow();
        this.listmusic.setK(file);
        try 
        {
			this.listmusic.getPlayer().open(this.listmusic.getFileSong().get(file).getSelectedSong());
		} 
        catch (BasicPlayerException e1) 
        {
			e1.printStackTrace();
		}
        this.view.getNameSongs().setText(this.listmusic.getFileSong().get(file).getSelectedSong().getName());
        this.listmusic.Play();
        this.view.getBtnPlay().setText("||");
        this.listmusic.setRunning(true);
        this.principalVolume();
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
	public void mouseReleased(MouseEvent arg0) {}
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
	
	/**
	 * Hotkeys Listener
	 */
	public void onHotKey(int aIdentifier) 
	{
		System.out.println("WM_HOTKEY message received " + Integer.toString(aIdentifier));
	}
	
	/**
	 * Hotkeys Listener
	 */
	public void onIntellitype(int aCommand) 
	{

		switch (aCommand) {
		case JIntellitype.APPCOMMAND_BROWSER_BACKWARD:
			System.out.println("BROWSER_BACKWARD message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_FAVOURITES:
			System.out.println("BROWSER_FAVOURITES message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_FORWARD:
			System.out.println("BROWSER_FORWARD message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_HOME:
			System.out.println("BROWSER_HOME message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_REFRESH:
			System.out.println("BROWSER_REFRESH message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_SEARCH:
			System.out.println("BROWSER_SEARCH message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_BROWSER_STOP:
			System.out.println("BROWSER_STOP message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_LAUNCH_APP1:
			System.out.println("LAUNCH_APP1 message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_LAUNCH_APP2:
			System.out.println("LAUNCH_APP2 message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_LAUNCH_MAIL:
			System.out.println("LAUNCH_MAIL message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
			System.out.println("MEDIA_NEXTTRACK message received " + Integer.toString(aCommand));
			this.theNextSong();
			break;
		case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
			System.out.println("MEDIA_PLAY_PAUSE message received " + Integer.toString(aCommand));
			this.playOrPause();
			break;
		case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
			System.out.println("MEDIA_PREVIOUSTRACK message received " + Integer.toString(aCommand));
			this.thePrevSong();
			break;
		case JIntellitype.APPCOMMAND_MEDIA_STOP:
			System.out.println("MEDIA_STOP message received " + Integer.toString(aCommand));
			this.stopAllSong();
			break;
		case JIntellitype.APPCOMMAND_VOLUME_DOWN:
			System.out.println("VOLUME_DOWN message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_VOLUME_UP:
			System.out.println("VOLUME_UP message received " + Integer.toString(aCommand));
			break;
		case JIntellitype.APPCOMMAND_VOLUME_MUTE:
			System.out.println("VOLUME_MUTE message received " + Integer.toString(aCommand));
			break;
		default:
			System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
			break;
		}
	}
	
	public void theNextSong()
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
		}
		else
		{
			listmusic.nextSong();
			view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
		}
	}
	
	public void thePrevSong() 
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
		}
		else
		{
			listmusic.prevSong();
			view.getNameSongs().setText(listmusic.getFileSong().get(listmusic.getK()).getSelectedSong().getName());
			listmusic.Play();
			this.view.getBtnPlay().setText("||");
			this.listmusic.setRunning(true);
			this.principalVolume();
		}
	}
	
	public void playOrPause() 
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
	
	public void stopAllSong() 
	{
		listmusic.Stop();
		this.view.getBtnPlay().setText(">");
		this.listmusic.setRunning(false);
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