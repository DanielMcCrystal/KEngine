package engine;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Play extends Applet {

	public static int dimensionX = 1680, dimensionY = 900;

	private static final long serialVersionUID = 1L;
	private Controls controls;
	private Engine engine;

	private BufferedImage canvas;

	private World activeWorld;

	public World getActiveWorld() {
		return activeWorld;
	}

	public void setActiveWorld(World world) {
		this.activeWorld = world;
	}

	public void init() {
		activeWorld = new World(300, 150, this);
		controls = new Controls();
		this.setSize(dimensionX, dimensionY);
		canvas = new BufferedImage(dimensionX, dimensionY,
				BufferedImage.TYPE_3BYTE_BGR);
	}

	public void startGame() {
		this.addKeyListener(controls);
		frameNumber = 0;
		lastFrame = 0;
		engine = new Engine(this, 60);
	}

	public long getTickNumber() {
		return engine.getTickNumber();
	}
	
	
	private long frameNumber, lastFrame;
	public long getFrameNumber() {
		return frameNumber;
	}
	public long getDeltaFrames() {
		long temp = lastFrame;
		lastFrame = frameNumber;
		return frameNumber - temp;
		
	}
	public void paint(Graphics page) {
		if(frameNumber < Long.MAX_VALUE) {
			frameNumber++;
		} else {
			frameNumber = 0;
		}
		activeWorld.drawWorld(canvas.getGraphics());
		page.drawImage(canvas, 0, 0, null);
	}

	public void runGlobalControls() {

	}
}
