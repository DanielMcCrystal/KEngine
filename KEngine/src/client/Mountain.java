package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import engine.Entity;
import engine.World;

public class Mountain extends Entity {
	Polygon base, cap;
	int lastX, lastY;
	public Mountain(int posX, int posY, int posZ, int sizeX, int sizeY) {
		super(posX, posY, posZ, sizeX, sizeY);
		base = new Polygon();
		cap = new Polygon();
	}
	
	@Override
	public void setWorld(World w) {
		super.setWorld(w);
		setPolygons();
	}
	
	@Override
	public void setLayer() {
		super.setLayer();
		setPolygons();
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						page.setColor(Color.gray);
						page.fillPolygon(base);
						
						page.setColor(Color.white);
						page.fillPolygon(cap);
					}
				});
			}
		});
	}
	
	public void setPolygons() {
		setCameraAdjustedX();
		setCameraAdjustedY();
		setCameraAdjustedSizeX();
		setCameraAdjustedSizeY();
		int x = getCameraAdjustedX();
		int y = getCameraAdjustedY();
		lastX = x;
		lastY = y;
		int sizeX = getCameraAdjustedSizeX();
		int sizeY = getCameraAdjustedSizeY();
		
		base.reset();
		base.addPoint(x, y + sizeY);
		base.addPoint(x + sizeX, y + sizeY);
		base.addPoint(x + sizeX / 2, y);
		
		/*
		cap.reset();
		cap.addPoint(x + sizeX / 2, y);
		cap.addPoint(, y + sizeY / 5);
		cap.addPoint(x + 3 * sizeX / 4, y + sizeY / 5);
		*/
	}
	
	@Override
	public void updatePosition() {
		super.updatePosition();
		
		int deltaX = getCameraAdjustedX() - lastX;
		int deltaY = getCameraAdjustedY() - lastY;
		lastX = getCameraAdjustedX();
		lastY = getCameraAdjustedY();
		base.translate(deltaX, deltaY);
		cap.translate(deltaX, deltaY);
	}
	
}
