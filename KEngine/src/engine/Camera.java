package engine;

public class Camera {
	
	private int posX, posY, posZ;
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public int getPosZ() {
		return posZ;
	}
	private double fieldOfView = 1.0012;
	public double getFieldOfView() {
		return fieldOfView;
	}
	public void resetFieldOfView() {
		fieldOfView = 1.0012;
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	public void fovIn() {
		fieldOfView *= 1.000007;
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	public void fovOut() {
		if(fieldOfView > 1) {
			fieldOfView /= 1.000007;
		}
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	
	private double zoom = 1;
	public double getZoom() {
		return zoom;
	}
	public void resetZoom() {
		zoom = 1;
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	public void zoomIn() {
		zoom *= 1.01;
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	public void zoomOut() {
		zoom /= 1.01;
		for(Entity e: world.getDrawnEntities()) {
			e.setLayer();
		}
	}
	
	public int getCameraAppliedXPoint(int x, double layer) {
		return (int) ((x - posX + (Play.dimensionX / 2 / layer / zoom)) * layer * zoom);
	}
	
	public int getCameraAppliedYPoint(int y, double layer) {
		return (int) ((y - posY + (Play.dimensionY / 2 / layer / zoom)) * layer * zoom);
	}
	
	
	private Entity locked;
	private boolean lockX, lockY;
	public boolean getLockedX() {
		return lockX;
	}
	public boolean getLockedY() {
		return lockY;
	}
	
	public void setPosX(int x) {
		posX = x;
	}
	public void setPosY(int y) {
		posY = y;
	}
	
	private World world;
	public World getWorld() {
		return world;
	}
	
	public Camera(World world, int posX, int posY) {
		this.world = world;
		this.posX = posX;
		this.posY = posY;
		
	}
	public Camera(World world) {
		this.world = world;
		this.posX = Play.dimensionX / 2;
		this.posY = Play.dimensionY / 2;
	}
	
	public void lockOnEntity(Entity e, boolean x, boolean y) {
		locked = e;
		lockX = x;
		lockY = y;
	}
	
	public void update() {
		if(locked != null) {
			if(lockX) {
				posX = locked.getPosX() + locked.getSizeX() / 2;
			}
			if(lockY) {
				posY = locked.getPosY() + locked.getSizeY() / 2 - (int)(50 / zoom);
			}
		}
	}
	
}
