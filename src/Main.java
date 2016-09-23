import entities.Playlist;
import gui.Windowgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceMistAquaLookAndFeel;

import control.Controller;

/**
 * Principal class
 * @author Jhonatan Guzmán
 */
public class Main 
{
	/**
	 * Principal method for run applet
	 * @param args String array
	 */
	public static void main(String[] args) 
	{       
        EventQueue.invokeLater(new Runnable()
        {
        	public void run()
        	{
        		try
        		{
        			JFrame.setDefaultLookAndFeelDecorated(true);
        			UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
        		}
        		catch(Exception e)
        		{
        			e.printStackTrace();
        		}
        		try
        		{
        	    	Windowgui view = new Windowgui();
        	    	Playlist listmusic = new Playlist();
        	        Controller control = new Controller(view,listmusic);
        			view.setVisible(true);
        		}
        		catch(Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        });
	}
}