package client;

import java.awt.Color;
import java.awt.Graphics;

import engine.Entity;
import engine.Entity3D;
import engine.World;

public class Plane extends Entity3D {
	private int sizeZ;
	private Color color, color2;
	public Plane(int posX, int posY, int posZ, int sizeX, int sizeZ, Color color) {
		super(posX, posY, posZ, sizeX, 0);
		this.sizeZ = sizeZ;
		this.color = color;
		this.hasCollision = true;
		
		int red = color.getRed() - 50;
		int green = color.getGreen() - 50;
		int blue = color.getBlue() - 50;
		if (red < 0) {
			red = 0;
		}
		if (green < 0) {
			green = 0;
		}
		if (blue < 0) {
			blue = 0;
		}
		
		this.color2 = new Color(red, green, blue);
		this.drawEdges = false;
		this.drawVertices = false;
		this.drawFaces = true;
		this.setVertices();
	}

	@Override
	public void setVertices() {
		Vertex v1 = new Vertex(getPosX(), getPosY(), getPosZ());
		Vertex v2 = new Vertex(getPosX(), getPosY(), getPosZ() + sizeZ);
		Vertex v3 = new Vertex(getPosX() + getSizeX(), getPosY(), getPosZ());
		Vertex v4 = new Vertex(getPosX() + getSizeX(), getPosY(), getPosZ() + sizeZ);
		
		addEdge(v1, v2);
		addEdge(v1, v3);
		addFace(getEdge(0), getEdge(1), color);
		
		addEdge(v4, v2);
		addEdge(v4, v3);
		addFace(getEdge(2), getEdge(3), color);
		
	}
	
	@Override
	public void setWorld(World w) {
		super.setWorld(w);
		
		for(int i=0; i<5000; i++) {
			int x = (int) (Math.random() * getSizeX()) + getPosX();
			int z = (int) (Math.random() * sizeZ) + getPosZ();
			w.addEntity(new Dot(x, getPosY(), z, 5, 5));
		}
	}
	class Dot extends Entity {

		public Dot(int posX, int posY, int posZ, int sizeX, int sizeY) {
			super(posX, posY, posZ, sizeX, sizeY);
		}

		@Override
		public void addAllAnimations() {
			addAnimation("default", new Entity.Animation() {
				@Override
				public void addAllAnimationFrames() {
					addAnimationFrame(new AnimationFrame() {
						public void draw(Graphics page) {
							page.setColor(color2);
							page.fillRect(getCameraAdjustedX(), getCameraAdjustedY(),
									getCameraAdjustedSizeX(), getCameraAdjustedSizeY());
						}
					});
				}
			});
		}
	}
}
