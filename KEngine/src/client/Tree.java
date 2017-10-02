package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import engine.Entity;
import engine.World;

public class Tree extends Entity {
	Color darkGreen1, darkGreen2, darkGreen3, brown;
	Polygon top, mid, bot;
	
	public Tree(int posX, int posY, int posZ, int sizeX, int sizeY) {
		super(posX, posY, posZ, sizeX, sizeY);
		darkGreen1 = new Color(25, 120 + (int) (100 * (posZ / 2000.0)), 25);
		darkGreen2 = new Color(25, darkGreen1.getGreen() - 4, 25);
		darkGreen3 = new Color(25, darkGreen2.getGreen() - 3, 25);
		brown = new Color(120 + (int) (120 * (posZ / 2000.0)),
				75 + (int) (75 * (posZ / 2000.0)),
				25 + (int) (25 * (posZ / 2000.0)));
		top = new Polygon();
		mid = new Polygon();
		bot = new Polygon();
	}
	
	private int lastX, lastY;
	@Override
	public void adjustForCamera() {
		super.adjustForCamera();
		int deltaX = getCameraAdjustedX() - lastX;
		int deltaY = getCameraAdjustedY() - lastY;
		lastX = getCameraAdjustedX();
		lastY = getCameraAdjustedY();
		top.translate(deltaX, deltaY);
		mid.translate(deltaX, deltaY);
		bot.translate(deltaX, deltaY);
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						int x = getCameraAdjustedX();
						int y = getCameraAdjustedY();
						int sizeX = getCameraAdjustedSizeX();
						int sizeY = getCameraAdjustedSizeY();
						
						page.setColor(brown);
						page.fillRect(x + (2 * sizeX / 5), y, sizeX / 5, sizeY);
						
						page.setColor(darkGreen3);
						page.fillPolygon(bot);
						
						page.setColor(darkGreen2);
						page.fillPolygon(mid);
						
						page.setColor(darkGreen1);
						page.fillPolygon(top);
					}
				});
			}
		});
	}
	
	@Override
	public void setLayer() {
		super.setLayer();
		setPolygons();
	}
	
	@Override
	public void setWorld(World w) {
		super.setWorld(w);
		setPolygons();
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
		
		top.reset();
		top.addPoint(x + sizeX / 2, y - sizeY / 5);
		top.addPoint(x + sizeX / 4, y + sizeY / 5);
		top.addPoint(x + 3 * sizeX / 4, y + sizeY / 5);
		
		mid.reset();
		mid.addPoint(x + sizeX / 2, y - sizeY / 5);
		mid.addPoint(x + sizeX / 5, y + sizeY / 2);
		mid.addPoint(x + 4 * sizeX / 5, y + sizeY / 2);
		
		bot.reset();
		bot.addPoint(x + sizeX / 2, y - sizeY / 5);
		bot.addPoint(x + sizeX / 6, y + sizeY - sizeY / 5);
		bot.addPoint(x + 5 * sizeX / 6, y + sizeY - sizeY / 5);
		
	}
	
}
