package org.pokenet.client.ui.twl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.pokenet.client.backend.TWLInputAdapter;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.DynamicImage.Format;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

/**
 * Test class for the new UI
 * @author Myth1c
 *
 */
public class TWLTest extends BasicGame {

	public TWLTest(String title) {
		super(title);
	}

	private static TWLTest m_instance;
	
	private LWJGLRenderer lwjglRenderer;
	private ThemeManager theme;
	private GUI gui;
	private GUIPane root;
	private TWLInputAdapter twlInputAdapter;
	
	public static void main(String[] args)
	{
		try
		{
			TWLTest test = TWLTest.getInstance();
			AppGameContainer gameContainer = new AppGameContainer(test, 800, 600, false);
			gameContainer.setTargetFrameRate(60);
			gameContainer.setAlwaysRender(true);
			gameContainer.setShowFPS(false);
			gameContainer.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		twlInputAdapter.render();		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// construct & configure root widget
        String m_filepath = System.getProperty("res.path");
		if(m_filepath == null)
			m_filepath = "";

        // save Slick's GL state while loading the theme
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        try {
            lwjglRenderer = new LWJGLRenderer();
            File f = new File(m_filepath + "res/themes/default/Default.xml");
            theme = ThemeManager.createThemeManager(f.getAbsoluteFile().toURI().toURL() , lwjglRenderer);
            root = new GUIPane();
            gui = new GUI(root, lwjglRenderer);
            loadBackground(m_filepath,gui);
            gui.applyTheme(theme);
        } catch (LWJGLException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            // restore Slick's GL state
            GL11.glPopAttrib();
        }

        // connect input
        twlInputAdapter = new TWLInputAdapter(gui, gc.getInput());
        gc.getInput().addPrimaryListener(twlInputAdapter); 	
        root.getLoginScreen().setServerRevision(2000);
	}
	
	private DynamicImage getDynamicImageFromBufferedImage(BufferedImage bufferedImage)
    {
		 ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

         for(int i=0; i<bufferedImage.getHeight(); i++) {
             for(int j=0; j<bufferedImage.getWidth(); j++) {
                 int argb  = bufferedImage.getRGB( j, i );
                 byte alpha = (byte) (argb >> 24);
                 byte red   = (byte) (argb >> 16);
                 byte green = (byte) (argb >> 8);
                 byte blue  = (byte) (argb);

                 byteBuffer.put(red);
                 byteBuffer.put(green);
                 byteBuffer.put(blue);
                 byteBuffer.put(alpha);
             }
         }
         byteBuffer.flip();

         Renderer renderer = TWLTest.getInstance().getRenderer();
	      DynamicImage dynImage= renderer.createDynamicImage(bufferedImage.getWidth(), bufferedImage.getHeight());
	      dynImage.update(byteBuffer, Format.RGBA);
	      return dynImage;
    }
	
	private void loadBackground(String respath, GUI gui) 
	{
		
		String backgroundPath = "";
		/* Load the background image, NOTE: Months start at 0, not 1 */
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.MONTH) == 1)
		{
			if(cal.get(Calendar.DAY_OF_MONTH) >= 7 && cal.get(Calendar.DAY_OF_MONTH) <= 14)
				backgroundPath = respath + "res/pokenet_valentines.png";
			else
				backgroundPath = respath + "res/pokenet_venonat.png";
		}
		else if(cal.get(Calendar.MONTH) == 2 && cal.get(Calendar.DAY_OF_MONTH) > 14)
			/* If second half of March, show Easter login */
			backgroundPath = respath + "res/pokenet_easter.png";
		else if(cal.get(Calendar.MONTH) == 3 && cal.get(Calendar.DAY_OF_MONTH) < 26)
			/* If before April 26, show Easter login */
			backgroundPath = respath + "res/pokenet_easter.png";
		else if(cal.get(Calendar.MONTH) == 9)
			/* Halloween */
			backgroundPath = respath + "res/pokenet_halloween.png";
		else if(cal.get(Calendar.MONTH) == 11)
			/* Christmas! */
			backgroundPath = respath + "res/pokenet_xmas.png";
		else if(cal.get(Calendar.MONTH) == 0)
			/* January - Venonat Time! */
			backgroundPath = respath + "res/pokenet_venonat.png";
		else if(cal.get(Calendar.MONTH) >= 5 && cal.get(Calendar.MONTH) <= 7)
			/* Summer login */
			backgroundPath = respath + "res/pokenet_summer.png";
		else
			/* Show normal login screen */
			backgroundPath = respath + "res/pokenet_normal.png";
		
		BufferedImage bufferedimg = null;
        try 
        {
			bufferedimg = ImageIO.read(new File(backgroundPath));
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		gui.setBackground(getDynamicImageFromBufferedImage(bufferedimg));
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		twlInputAdapter.update();	
	}
	
	public static TWLTest getInstance() {
		if(m_instance == null)
			m_instance = new TWLTest("TWL");
		return m_instance;
	}
	
	public LWJGLRenderer getRenderer() {
		return lwjglRenderer;
	}
	
	public ThemeManager getTheme() {
		return theme;
	}

}
