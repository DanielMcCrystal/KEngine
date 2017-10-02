package client;

import java.awt.Color;
import java.awt.Graphics;

import engine.Entity;

public class Platform extends Entity {

	public Platform(int posX, int posY, int sizeX, int sizeY) {
		super(posX, posY, 0, sizeX, sizeY);
		this.hasCollision = true;
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					@Override
					public void draw(Graphics page) {
						int x = getCameraAdjustedX();
						int y = getCameraAdjustedY();
						int sizeX = getCameraAdjustedSizeX();
						int sizeY = getCameraAdjustedSizeY();
						
						page.setColor(Color.gray);
						page.fillRect(x, y, sizeX, sizeY);
						page.setColor(Color.white);
						page.fillRect(x - (int) (2 * world.getCamera().getZoom()), y
								- (int) (world.getCamera().getZoom()), sizeX
								+ (int) (4 * world.getCamera().getZoom()), (int) (2 * world
								.getCamera().getZoom()));
					}
				});
			}
		});
	}
}
