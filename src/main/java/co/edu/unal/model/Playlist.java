package co.edu.unal.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import co.edu.unal.controller.Action;

/**
 * Playlist class contains a ArrayList with instance of Song class
 * for simulates a Playlist of songs and different options to apply
 * @author Jhonatan Guzmán
 */
public class Playlist extends Action implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<Song> fileSong = new ArrayList<>();
	private int currentIndexSong;
	private int tam;
	private int progressSong;
	private int option;
	private boolean running = false;
	private double bytesLength;
	private double volume = 1.0;
	
	/**
	 * Constructs a new instance of Playlist class
	 * initializing a ArrayList of Song's instance  
	 * @param fileSong ArrayList of Song's instance
	 */
	public Playlist(ArrayList<Song> fileSong)
	{
		super();
		this.fileSong = fileSong;
	}

	/**
	 * Initialize an empty instance of Playlist class
	 */
	public Playlist(){}

	/**
	 * Setter method of ArrayList<Song> fileSong
	 * @param fileSong ArrayList of Song's instance
	 */
	public void setFileSong(ArrayList<Song> fileSong)
	{
		this.fileSong = fileSong;
	}

	/**
	 * Getter method of ArrayList<Song> fileSong
	 * @return fileSong
	 */
	public ArrayList<Song> getFileSong()
	{
		return fileSong;
	}

	/**
	 * Setter method of double bytesLength
	 * <strong>Procure not use!</strong>
	 * @param bytesLength Length of bytes per Song
	 */
	public void setBytesLength(double bytesLength)
	{
		this.bytesLength = bytesLength;
	}

	/**
	 * Getter method of double bytesLength
	 * @return bytesLength
	 */
	public double getBytesLength()
	{
		return bytesLength;
	}

	/**
	 * Return bytesLength with cast to Integer
	 * @return (int) bytesLenght
	 */
	public int getBytesLengthInt()
	{
		return (int) bytesLength;
	}

	/**
	 * Getter method of int progressSong
	 * @return progressSong
	 */
    public int getProgressSong() 
    {
		return progressSong;
	}

    /**
     * Setter method of int progressSong
     * <strong> Procure not use!</strong>
     * @param progressSong Progress of the Song while this sounds
     */
	public void setProgressSong(int progressSong) 
	{
		this.progressSong = progressSong;
	}

	/**
	 * Getter method of int Tam
	 * @return Tam
	 */
	public int getTam() 
	{
		return tam;
	}

	/**
	 * Setter method of int Tam
	 * <strong> Procure not use!</strong>
	 * @param tam Size of ArrayList chosen with JFileChooser DIRECTORIES_ONLY 
	 */
	public void setTam(int tam) 
	{
		this.tam = tam;
	}

	/**
	 * Getter method of int k
	 * @return k
	 */
	public int getCurrentIndexSong()
	{
		return currentIndexSong;
	}

	/**
	 * Setter method of int k
	 * <strong>only use if you want listen 
	 * a Song in ArrayList with this index!</strong>
	 * @param currentIndexSong Index of Song to play in ArrayList
	 */
	public void setCurrentIndexSong(int currentIndexSong)
	{
		this.currentIndexSong = currentIndexSong;
	}

	/**
	 * Getter method of int option
	 * @return option
	 */
	public int getOption() 
	{
		return option;
	}

	/**
	 * Setter method of int option
	 * @param option 0.Normal  1.Loop Playlist  2.Loop Song 3.Random 4.Just Once
	 */
	public void setOption(int option) 
	{
		this.option = option;
	}

	/**
	 * Getter method of boolean running
	 * @return running
	 */
	public boolean getRunning() 
	{
		return running;
	}

	/**
	 * Setter method of boolean running
	 * <strong>Only use for GUI Buttons</strong>
	 * @param running true for Song sounds, false for Song stopped
	 */
	public void setRunning(boolean running) 
	{
		this.running = running;
	}

	/**
	 * Getter method of volume value
	 * @return volume
	 */
	public double getVolume()
	{
		return volume;
	}

	/**
	 * Setter method of volume value
	 * @param volume double
	 */
	public void setVolume(double volume) 
	{
		this.volume = volume;
	}

	/**
	 * This method add to ArrayList an instance of Song Class,
	 * to create a List of Songs
	 * @param fileSelected An instance of Song class
	 */
	public void addSongList(Song fileSelected)
	{	
		fileSong.add(fileSelected);
	}

	/**
	 * This method add to ArrayList of Playlist class 
	 * another ArrayList of Song class, to create 
	 * a List of Songs
	 * @param files ArrayList of Song's instances
	 */
	public void addSongDir(List<Song> files)
	{
		fileSong.addAll(files);
	}

	/**
	 * This method open Song with index k+1 in ArrayList, 
	 * using method open from BasicPlayer 3.0 class 
	 * @throws BasicPlayerException
	 */
    public void nextSong()
    {
    	currentIndexSong = currentIndexSong +1 >= fileSong.size() ? 0 : currentIndexSong + 1;
    	try 
    	{
			getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
		} 
    	catch (BasicPlayerException e) 
    	{
			e.printStackTrace();
		}
    }

	/**
	 * This method open Song with index k-1 in ArrayList, 
	 * using method open from BasicPlayer 3.0 class 
	 * @throws BasicPlayerException
	 */
    public void prevSong()
    {
    	currentIndexSong = Math.max(currentIndexSong - 1, 0);
    	try 
    	{
			getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
    }

	/**
	 * This method open Song with index 0 in ArrayList, 
	 * using method open from BasicPlayer 3.0 class 
	 * @throws BasicPlayerException
	 */
    public void putInMemoryFirst()
    {
    	currentIndexSong = 0;
		try 
    	{
			getPlayer().open(fileSong.get(currentIndexSong).getSelectedSong());
		} catch (BasicPlayerException e) {
			e.printStackTrace();
			System.err.println("No se pudo poner en memoria");
			JOptionPane.showMessageDialog(null, "No se pudo cargar la canci\u00F3n", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }

    public void putInMemory(int index)
    {
		try 
    	{
			getPlayer().open(fileSong.get(index).getSelectedSong());
		} 
    	catch (BasicPlayerException e) 
    	{
			e.printStackTrace();
			System.err.println("No se pudo poner en memoria");
			JOptionPane.showMessageDialog(null, "No se pudo cargar la canci\u00F3n", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }

    /**
	 * Remove element i of ArrayList of Playlist class
	 * @param i Index in ArrayList to remove
	 */
	public void delete(int i)
	{
		//TODO Don't stop the song if the delete file is not in open method
		fileSong.remove(i);
		currentIndexSong = i;
		try 
		{
			getPlayer().open(this.fileSong.get(i).getSelectedSong());
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		this.Play();
	}

	/**
	 * Remove all elements in ArrayList of Playlist class
	 * and open a File null using method open from
	 * BasicPlayer 3.0 class
	 * @throws BasicPlayerException
	 */
	public void deleteAll()
	{
		File arch = new File("");
		try 
		{
			getPlayer().open(arch);
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		fileSong.clear();
	}

	/**
	 * Override method from Object class
	 */
    @Override
    public String toString()
    {
    	return "Suena: "+ fileSong.get(currentIndexSong).getSelectedSong().getName();
    }
}