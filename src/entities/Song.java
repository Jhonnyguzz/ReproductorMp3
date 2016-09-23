package entities;

import java.io.File;
import java.io.Serializable;

/**
*The class Song has atributes
*such as title song, author song,
*duration song, date about song, album song
*and source file
@author Jhonatan Guzmán
*/
public class Song implements Serializable,Comparable<Song>
{
	private static final long serialVersionUID = 1L;
	private File selectedSong;
	private String title;
	private String author;
	private String time;
	private String album;
	private String year;
	/**
	 * Constructs a new instance
	 * of Song with File as parameter  
	 * @param selectedSong File with mp3 song
	 */
	public Song(File selectedSong)
	{
		this.selectedSong=selectedSong;
	}	
	/**
	 * Initialize a new instance
	 * of Song class empty
	 */
	public Song(){}
	/**
	 * Setter method of File selectedSong
	 * @param selectedSong File source
	 */
	public void setSelectedSong(File selectedSong)
	{
		this.selectedSong=selectedSong;
	}
	/**
	 * Getter method of File selectedSong
	 * @return selectedSong
	 */
	public File getSelectedSong()
	{
		return selectedSong;
	}
	/**
	 * Getter method of String title
	 * @return title
	 */
	public String getTitle() 
	{
		return title;
	}
	/**
	 * Setter method of String title
	 * @param title String
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}
	/**
	 * Getter method of String author
	 * @return author
	 */
	public String getAuthor() 
	{
		return author;
	}
	/**
	 * Setter method of String author
	 * @param author String
	 */
	public void setAuthor(String author) 
	{
		this.author = author;
	}
	/**
	 * Getter method of String time
	 * @return time
	 */
	public String getTime() 
	{
		return time;
	}
	/**
	 * Setter method of String time
	 * @param time String
	 */
	public void setTime(String time) 
	{
		this.time = time;
	}
	/**
	 * Getter method of String album
	 * @return album
	 */
	public String getAlbum() 
	{
		return album;
	}
	/**
	 * Setter method of String album
	 * @param album String
	 */
	public void setAlbum(String album) 
	{
		this.album = album;
	}
	/**
	 * Getter method of String year
	 * @return year
	 */
	public String getYear() 
	{
		return year;
	}
	/**
	 * Setter method of String year
	 * @param year String
	 */
	public void setYear(String year) 
	{
		this.year = year;
	}
	/**
	 * Override method from Object class
	 */
	@Override
    public String toString()
    {
    	return "Suena: "+ selectedSong;
    }
	/**
	 * Override method for implement Comparable interface 
	 * for compare two Songs to Ignore case 
	 * @param other other Song's instance
	 * @return A Integer 1 or -1 for compare two Songs   
	 */
	@Override
	public int compareTo(Song other) 
	{
		return this.selectedSong.getName().compareToIgnoreCase(other.selectedSong.getName());
	}
}