package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import entities.Playlist;
import entities.Song;
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
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
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
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException c)
		{
			c.printStackTrace();
			return null;
		} 
		catch (ClassNotFoundException d) 
		{
			d.printStackTrace();
			return null;
		}
		return obret.getFileSong();	
	}
}