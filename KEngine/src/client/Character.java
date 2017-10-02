package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.Controls;
import engine.Entity;

public class Character extends Entity {
	private boolean facingRight;

	public Character(int posX, int posY, int sizeX, int sizeY) {
		super(posX, posY, 0, sizeX, sizeY);
		this.hasCollision = true;
		this.moveable = true;
		this.affectedByGravity = true;
		facingRight = true;
	}
 
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					@Override
					public void draw(Graphics page) {
						page.setColor(Color.BLUE);
						page.fillRect(getCameraAdjustedX(), getCameraAdjustedY(),
								getCameraAdjustedSizeX(), getCameraAdjustedSizeY());
					}
					
				});
			}
		});
	}
	/*
	@Override
	public void addAllAnimations() {
		addAnimation("idleRight", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/IdleR.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		addAnimation("idleLeft", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/IdleL.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		addAnimation("walkRight", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/1R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 2, 18, 15));
					image = ImageIO.read(new File("animation/3R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/4R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/5R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/6R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/7R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		addAnimation("walkLeft", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/1L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 2, 18, 15));
					image = ImageIO.read(new File("animation/3L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/4L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/5L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/6L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
					image = ImageIO.read(new File("animation/7L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		addAnimation("jumpRight", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/3R.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		addAnimation("jumpLeft", new Entity.Animation() {
			@Override
			public void addAllAnimationFrames() {
				BufferedImage image;
				try {
					image = ImageIO.read(new File("animation/3L.png"));
					addAnimationFrame(new ImageAnimationFrame(image, 18, 15));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	public void updatePosition() {
		super.updatePosition();
		if (Controls.keyDown(KeyEvent.VK_D)) {
			facingRight = true;
			if (!getAnimationKey().equals("walkRight")) {
				//switchAnimation("walkRight");
			}
			if (xVelocity < 5) {
				xVelocity += (onGround) ? 2 : 1;
			}
		} else if (Controls.keyDown(KeyEvent.VK_A)) {
			facingRight = false;
			if (!getAnimationKey().equals("walkLeft")) {
				//switchAnimation("walkLeft");
			}
			if (xVelocity > -5) {
				xVelocity -= (onGround) ? 2 : 1;
			}
		} else {
			if (!getAnimationKey().equals("default")) {
				if (facingRight)
					;//switchAnimation("idleRight");
				else
					;//switchAnimation("idleLeft");
			}
		}
		if (onGround) {
			if (Controls.keyDown(KeyEvent.VK_SPACE)) {
				yVelocity = -10;
			}
		} else {
			if (facingRight)
				;//switchAnimation("jumpRight");
			else
				;//switchAnimation("jumpLeft");
		}
		
		
	}
	
}