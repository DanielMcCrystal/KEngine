package engine;
import java.util.Timer;
import java.util.TimerTask;

public class Engine {
	
	private Play player;
	private Timer timer;
	
	private int tickRate;
	private int desiredFPS;
	
	private long tickNumber;
	public static int actualFPS;
	
	public Engine(Play player, int tickRate) {
		this.player = player;
		this.tickRate = tickRate;
		desiredFPS = 60;
		tickNumber = 0;
		timer = new Timer();
		timer.schedule(new Tick(), perSecond(tickRate));
		timer.schedule(new Paint(), perSecond(desiredFPS));
		timer.schedule(new Second(), 1000);
	}
	
	public long getTickNumber() {
		return tickNumber;
	}
	
	class Second extends TimerTask {
		public void run() {
			timer.schedule(new Second(), 1000);
			actualFPS = (int) player.getDeltaFrames();
		}
	}
	
	class Tick extends TimerTask {
		public void run() {
			if(tickNumber < Long.MAX_VALUE) {
				tickNumber++;
			} else {
				tickNumber = 0;
			}
			
			timer.schedule(new Tick(), perSecond(tickRate));
			
			player.getActiveWorld().updateWorld();
			player.runGlobalControls();
				
		}
	}
	
	class Paint extends TimerTask {
		public void run() {
			
			player.repaint();
			timer.schedule(new Paint(), perSecond(desiredFPS));
			
		}
	}
	
	private int perSecond(int x) {
		return 1000 / x;
	}
}
