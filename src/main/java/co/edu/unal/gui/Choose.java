package co.edu.unal.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import co.edu.unal.util.SerializeList;
import co.edu.unal.model.Playlist;
import co.edu.unal.model.Song;
import io.reactivex.rxjava3.core.Observable;

/**
 * This class use JFileChooser for select Files to add a Song class 
 * and ArrayList of PlayList class
 * @author Jhonatan Guzmán
 */
public class Choose 
{
	private static JFileChooser chooseSong;
	private static JFileChooser chooseDir;
	private static JFileChooser openList;
	private static JFileChooser saveList;
	private static File selectedSong;
	private static String readArchive;
	private static String fileName;
	/**
	 * This method use JFileChooser instance for select a mp3 File
	 * @return songSelected
	 */
	public static Song getChoose()
	{
		chooseSong = new JFileChooser();
		chooseSong.setFileFilter(new FileNameExtensionFilter("Archivos mp3","mp3"));
	    int a = chooseSong.showOpenDialog(null);
	    Song songSelected;
 	    if(a==JFileChooser.APPROVE_OPTION)
	    {
 	    	selectedSong=chooseSong.getSelectedFile();
	    }
 	    songSelected = new Song(selectedSong);
 	    return songSelected;
	}
	/**
	 * This method use JFileChooser instance for select a array of mp3 Files
	 * <strong>Directories only!</strong>
	 * @return myArr
	 */
    public static List<Song> getDir() throws IOException {
        chooseDir = new JFileChooser();
        chooseDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int a = chooseDir.showOpenDialog(null);

        if(a==JFileChooser.APPROVE_OPTION) 
        {
            Path folder = chooseDir.getSelectedFile().toPath();
			return StreamSupport.stream(Observable.fromStream(Files.list(folder).parallel())
					.filter(path -> path.toString().endsWith(".mp3"))
					.map(path -> new Song(path.toFile())).blockingIterable().spliterator(), true)
					.collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    /**
     * JFileChooser for save a Playlist in disk 
     * with Serializar method
     * @param ob Object of Playlist to serialize
     */
	public static void getSaveList(Playlist ob)
	{
		saveList = new JFileChooser();
		saveList.setFileFilter(new FileNameExtensionFilter("Listas de reproducci\u00F3n POO","lrp"));
	    int a = saveList.showSaveDialog(null);	
 	    if(a==JFileChooser.APPROVE_OPTION)
	    {
 	    	fileName=saveList.getSelectedFile().getAbsolutePath();
 	    	
            if(!(fileName.endsWith(".lrp")))
            {
            	fileName=fileName+".lrp";
            }
 	    	
 	    	SerializeList.Serializar(fileName, ob);
	    }
	}
	/**
	 * JFileChooser for deserialize a Object of Playlist class
	 * and return the ArrayList of this
	 * @return fileSong
	 */
	public static ArrayList<Song> getOpenList()
	{
		openList = new JFileChooser();
		openList.setFileFilter(new FileNameExtensionFilter("Listas de reproducci\u00F3n POO","lrp"));
	    int a = openList.showOpenDialog(null);
 	    if(a==JFileChooser.APPROVE_OPTION)
	    {
 	    	readArchive=openList.getSelectedFile().getAbsolutePath();
	    }
 	    return SerializeList.Desserializar(readArchive);
	}
}