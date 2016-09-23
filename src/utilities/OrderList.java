package utilities;

import java.util.Comparator;

import entities.Song;
/**
 * Class that implements Comparator interface to sort 
 * high to low a ArrayList of Playlist class
 * @author Jhonatan Guzmán
 */
public class OrderList implements Comparator<Song>
{
	/**
	 * Override method for implement Comparator interface
	 * for sort ArrayList high to low
	 * @param arg0 Song instance
	 * @param arg1 Song instance
	 * @return A integer to sort ArrayList 1 or -1
	 */
	@Override
	public int compare(Song arg0, Song arg1) 
	{
		return -(arg0.getSelectedSong().getName().compareToIgnoreCase(arg1.getSelectedSong().getName()));
	}
}
