package client;

import java.awt.Color;
import java.awt.Graphics;

import engine.Entity;

public class Box extends Entity {

	private Color color1, color2, color1Dark;

	public Box(int posX, int posY, int sizeX, int sizeY, Color color1,
			Color color2) {
		super(posX, posY, 0, sizeX, sizeY);
		this.moveable = true;
		this.affectedByGravity = true;
		this.hasCollision = true;

		this.color1 = color1;
		this.color2 = color2;

		int red = color1.getRed() - 125;
		int green = color1.getGreen() - 125;
		int blue = color1.getBlue() - 125;
		if (red < 0) {
			red = 0;
		}
		if (green < 0) {
			green = 0;
		}
		if (blue < 0) {
			blue = 0;
		}
		this.color1Dark = new Color(red, green, blue);
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						int x = getCameraAdjustedX();
						int y = getCameraAdjustedY();
						int sizeX = getCameraAdjustedSizeX();
						int sizeY = getCameraAdjustedSizeY();

						page.setColor(color1);
						page.fillRect(x, y, sizeX, sizeY);

						page.setColor(color1Dark);
						page.fillRect(x, y, sizeX, 5);
						page.fillRect(x, y, 5, sizeY);
						page.fillRect(x, y + sizeY - 5, sizeX, 5);
						page.fillRect(x + sizeX - 5, y, 5, sizeY);

						page.setColor(color2);
						page.fillRect(x + (int) (2.0 * sizeX / 5), y,
								(int) (sizeX / 5.0), sizeY);
						page.fillRect(x, y + (int) (2.0 * sizeY / 5), sizeX,
								(int) (sizeY / 5.0));
					}
				});
			}
		});
	}
}