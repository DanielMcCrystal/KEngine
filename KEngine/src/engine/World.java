package engine;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

public class World {

	public class Chunk {
		private boolean active;

		public void loadChunk() {
			if (!active) {
				ghostLoadChunk();
				for (Entity e : ghostEntities) {
					e.getChunk().ghostLoadChunk();
				}
			}
		}

		public void ghostLoadChunk() {
			active = true;
			activeChunks.add(this);
		}

		public void unloadChunk() {
			if (active) {
				ghostUnloadChunk();
				for (Entity e : ghostEntities) {
					e.getChunk().ghostUnloadChunk();
				}
			}
		}

		public void ghostUnloadChunk() {
			active = false;
			activeChunks.remove(this);
		}

		private final int posX, posY, size;

		public int getPosX() {
			return posX;
		}

		public int getPosY() {
			return posY;
		}

		private final int xIndex, yIndex;

		public int getXIndex() {
			return xIndex;
		}

		public int getYIndex() {
			return yIndex;
		}

		private LinkedList<Entity> collidables;

		public LinkedList<Entity> getCollidables() {
			return collidables;
		}

		private LinkedList<Entity> entities;

		public LinkedList<Entity> getEntities() {
			return entities;
		}

		private LinkedList<Entity> ghostEntities;

		public LinkedList<Entity> getGhostEntities() {
			return ghostEntities;
		}

		public void addGhostEntity(Entity e) {
			if (!ghostEntities.contains(e)) {
				ghostEntities.add(e);
			}
		}

		public void removeGhostEntity(Entity e) {
			ghostEntities.remove(e);
		}

		public Chunk(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
			xIndex = posX / chunkSize;
			yIndex = posY / chunkSize;
			this.size = chunkSize;
			entities = new LinkedList<Entity>();
			collidables = new LinkedList<Entity>();
			ghostEntities = new LinkedList<Entity>();
			garbageQueue = new ArrayList<Entity>();
			incomingQueue = new ArrayList<Entity>();

		}

		public void draw(Graphics page) {
			page.setColor(active ? Color.green : Color.red);
			int x = camera.getCameraAppliedXPoint(posX + 1, 1);
			int y = camera.getCameraAppliedYPoint(posY + 1, 1);
			int size = (int) ((this.size - 1) * camera.getZoom());
			page.drawRect(x, y, size, size);
		}

		public void addEntityToChunk(Entity e) {
			e.setChunk(this);
			if (e.hasCollision) {
				collidables.add(e);
			}
			entities.add(e);
			e.setChunk(this);
		}

		public ArrayList<Entity> garbageQueue;
		public ArrayList<Entity> incomingQueue;

		public void updateChunk() {
			for (Entity garbage : garbageQueue) {
				entities.remove(garbage);
				collidables.remove(garbage);
			}
			for (Entity income : incomingQueue) {
				addEntityToChunk(income);
			}

			garbageQueue.clear();
			incomingQueue.clear();
		}
	}

	public Chunk chunkContainingPoint(int x, int y) {
		int chunkX = 0, chunkY = 0, chunkXIndex, chunkYIndex;
		Chunk c;
		chunkX = x - (x % chunkSize);
		chunkY = y - (y % chunkSize);
		chunkXIndex = chunkX / chunkSize;
		chunkYIndex = chunkY / chunkSize;
		c = chunks[chunkXIndex][chunkYIndex];
		return c;
	}

	public Chunk chunkAtIndex(int xIndex, int yIndex) {
		return chunks[xIndex][yIndex];
	}

	private Chunk[][] chunks;
	private LinkedList<Chunk> activeChunks;

	public LinkedList<Chunk> getActiveChunks() {
		return activeChunks;
	}

	private final int chunkSize;

	public int getChunkSize() {
		return chunkSize;
	}

	private ArrayList<Entity> drawnEntities;

	public ArrayList<Entity> getDrawnEntities() {
		return drawnEntities;
	}

	private double gravity;

	public double getGravity() {
		return gravity;
	}

	private Camera camera;

	public Camera getCamera() {
		return camera;
	}

	public long getTickNumber() {
		return player.getTickNumber();
	}

	public Play player;

	public World(int numChunksX, int numChunksY, Play player) {
		this(numChunksX, numChunksY, 0.4, 1500, player);
	}

	public World(int numChunksX, int numChunksY, int chunkSize, Play player) {
		this(numChunksX, numChunksY, 0.4, chunkSize, player);
	}

	public World(int numChunksX, int numChunksY, double gravity, int chunkSize,
			Play player) {
		this.player = player;
		camera = new Camera(this);
		drawnEntities = new ArrayList<Entity>();

		this.gravity = gravity;

		this.chunkSize = chunkSize;
		activeChunks = new LinkedList<Chunk>();
		chunks = new Chunk[numChunksX][];
		for (int x = 0; x < numChunksX; x++) {
			chunks[x] = new Chunk[numChunksY];
			for (int y = 0; y < numChunksY; y++) {
				chunks[x][y] = new Chunk(x * chunkSize, y * chunkSize);
			}
		}

		gp = new GradientPaint(Play.dimensionX / 2, 0, darkBlue,
				Play.dimensionX / 2, (int) (Play.dimensionY / 1.15), lightBlue);
		rect = new Rectangle(0, 0, Play.dimensionX, Play.dimensionY);
	}

	public void addEntity(Entity e) {
		chunkContainingPoint((int) e.posX, (int) e.posY).addEntityToChunk(e);
		e.setWorld(this);
		int i;
		for (i = 0; i < drawnEntities.size(); i++) {
			if (drawnEntities.get(i).getLayer() > e.getLayer()) {
				drawnEntities.add(i, e);
				i--;
				break;
			}
		}
		if (i == drawnEntities.size()) {
			drawnEntities.add(e);
		}

	}

	private Entity centralEntity;

	public void setCentralEntity(Entity e) {
		centralEntity = e;
	}

	public void updateWorld() {
		
		for (int i = 0; i < activeChunks.size();) {
			Chunk c = activeChunks.get(i);
			c.unloadChunk();
		}
		Chunk centralChunk;
		if (centralEntity == null) {
			centralChunk = chunkContainingPoint(camera.getPosX(),
					camera.getPosY());
		} else {
			centralChunk = chunkContainingPoint(centralEntity.getPosX(),
					centralEntity.getPosY());
		}
		centralChunk.loadChunk();
		
		int x = centralChunk.getXIndex();
		int y = centralChunk.getYIndex();
		if (x > 0) {
			chunks[x - 1][y].loadChunk();
			if (y > 0) {
				chunks[x][y - 1].loadChunk();
				chunks[x - 1][y - 1].loadChunk();
			}
			if (y < chunks[x].length) {
				chunks[x - 1][y + 1].loadChunk();
				chunks[x][y + 1].loadChunk();
			}
		}
		if (x < chunks.length) {
			chunks[x + 1][y].loadChunk();
			if (y > 0) {
				chunks[x + 1][y - 1].loadChunk();
			}
			if (y < chunks[x].length) {
				chunks[x + 1][y + 1].loadChunk();
			}
		}
		
		/*
		for (Chunk c : activeChunks) {
			for (Entity e : c.entities) {
				e.updatePosition();
			}
			c.updateChunk();
		}
		*/
		for(int i=0; i<chunks.length; i++) {
			for(int j=0; j<chunks[i].length; j++) {
				Chunk c = chunks[i][j];
				for(Entity e: c.entities) {
					e.updatePosition();
				}
				c.updateChunk();
			}
		}
		for (Entity e : drawnEntities) {
			e.adjustForCamera();
		}
		camera.update();
	}

	private Color darkBlue = new Color(0, 0, 40);
	private Color lightBlue = new Color(160, 200, 240);
	private GradientPaint gp;
	private Rectangle rect;

	public void drawWorld(Graphics page) {
		Graphics2D g2d = (Graphics2D) page;
		g2d.setPaint(gp);
		g2d.fill(rect);

		for (Entity e : drawnEntities) {
			if (e.getWithinView()) {
				e.draw(page);
			}
		}

		page.setColor(Color.yellow);
		page.drawString(Engine.actualFPS + " fps", 1560, 50);

	}

}
