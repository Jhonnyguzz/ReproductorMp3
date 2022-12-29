package co.edu.unal.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import co.edu.unal.model.Playlist;
import co.edu.unal.model.Song;

/**
 * this class implements Serializable interface
 * to Serialize ListSong class and "Save" in 
 * disk a Playlist, and also open Playlist
 */
public class SerializeList implements Serializable
{	
	private static final long serialVersionUID = 1L;
	/**
	 * This method receives a String corresponding a 
	 * File to Serialize ListSong class, and receives 
	 * a instance of ListSong class to Serialize 
	 * @param fileName String File
	 * @param ob Playlist instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void Serializar(String fileName, Playlist ob)
	{
		try
		{
		    FileOutputStream output = new FileOutputStream(fileName);
		    ObjectOutputStream oboutput = new ObjectOutputStream(output); 
		    oboutput.writeObject(ob);
		    oboutput.close();
		    output.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}
	/**
	 * This method receives a String fileName to deserialize
	 * a instance of ListSong class and return only ArrayList of this 
	 * @param fileName String File
	 * @return fileSong
	 * @throws Exception
	 */
	public static ArrayList<Song> Desserializar(String fileName)
	{
		Playlist obret=null;
		try
		{	
		    FileInputStream input = new FileInputStream(fileName);
		    ObjectInputStream obinput = new ObjectInputStream(input); 
		    
		    obret = (Playlist) obinput.readObject();
		    obinput.close();
		    input.close();
		}
		catch(Exception e)
		{
			System.err.println("Could not load any playlist");
			return new ArrayList<>();
		}
		return obret.getFileSong();	
	}
}