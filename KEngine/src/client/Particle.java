package client;

import java.awt.Color;
import java.awt.Graphics;

import engine.Entity;
import engine.World;

public class Particle extends Entity {

	public Particle(int posX, int posY, int sizeX, int sizeY) {
		super(posX, posY, 0, sizeX, sizeY);
		this.moveable = true;
		this.onGround = true;
		this.posX = (int) (Math.random() * 3200);
		this.posY = -10;
		int range = 2000;
		this.posZ = Math.random() * range - (range / 2);
		xVelocity = Math.random();
	}

	@Override
	public void setWorld(World w) {
		super.setWorld(w);
		yVelocity = Math.random() * (world.getGravity() * 2);
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						page.setColor(Color.white);
						page.fillRect(getCameraAdjustedX(), getCameraAdjustedY(),
								getCameraAdjustedSizeX(), getCameraAdjustedSizeY());
					}
				});
			}
		});
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		if (getPosY() > 1000) {
			posX = (int) (Math.random() * 3200);
			posY = -10;
			xVelocity = Math.random();
			yVelocity = Math.random() * (world.getGravity() * 2);
		}
	}
}
