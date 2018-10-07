package co.edu.unal;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.melloware.jintellitype.JIntellitype;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;

import co.edu.unal.controller.Controller;
import co.edu.unal.controller.ControllerForWindows;
import co.edu.unal.model.Playlist;
import co.edu.unal.gui.Windowgui;

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
        			/**
        			 * Only if you have Windows 64bits - check for you OS and JIntellitype.dll (64bits)
        			 */
        			if (JIntellitype.isJIntellitypeSupported()) {
        				System.out.println("Using hotkeys for windows (Only 64bits)");
        				ControllerForWindows control = new ControllerForWindows(view,listmusic);
        			}
        			else
        			{
						System.out.println("JIntellitype.dll was not detected on System32 (Only Windows 64bits)");
        				Controller control = new Controller(view,listmusic);
        			}
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