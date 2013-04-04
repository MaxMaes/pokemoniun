package org.pokenet.client.ui.twl;

import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.pokenet.client.backend.TWLInputAdapter;

import de.matthiasmann.twl.GUI;
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
        root.showLoginScreen();
	}
	
	/* Nice, but will probably not be used because we dont use java ImageIO.
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
    }*/

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
