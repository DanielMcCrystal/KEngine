package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import engine.World.Chunk;

public abstract class Entity {

	// <PROPERTIES>
	protected boolean affectedByGravity;
	protected boolean moveable;
	protected boolean hasCollision;

	protected boolean onGround;

	protected World world;

	protected double mass;
	private double layer;

	public void setLayer() {
		layer = Math.pow(world.getCamera().getFieldOfView(), (posZ - world
				.getCamera().getPosZ()));
		setCameraAdjustedSizeX();
		setCameraAdjustedSizeY();

	}

	public double getLayer() {
		return layer;
	}

	public double getXMomentum() {
		return mass * xVelocity;
	}

	public void setWorld(World w) {
		world = w;
		setLayer();
		addAllAnimations();
		currentAnimation = animations.get("default");
		animationKey = "defualt";
		calculateGhostChunks();
	}

	protected double posX, posY, posZ;
	protected int sizeX, sizeY;

	public int getPosX() {
		return (int) posX;
	}

	public int getPosY() {
		return (int) posY;
	}

	public int getPosZ() {
		return (int) posZ;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	protected double xVelocity, yVelocity;

	public double getXVelocity() {
		return xVelocity;
	}

	public double getYVelocity() {
		return yVelocity;
	}

	// </PROPERTIES>

	public Entity(int posX, int posY, int posZ, int sizeX, int sizeY) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		xVelocity = 0;
		yVelocity = 0;
		ghostChunks = new LinkedList<Chunk>();
		animations = new HashMap<String, Animation>();
		animations.put("default", new Animation() {
			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						page.setColor(Color.red);
						page.fillRect(cameraAdjustedX, cameraAdjustedY, 10, 10);
					}
				});
			}
		});

	}

	// <COLLISION>
	private Chunk chunk;
	private LinkedList<Chunk> ghostChunks;

	public Chunk getChunk() {
		return chunk;
	}

	public LinkedList<Chunk> getGhostChunks() {
		return ghostChunks;
	}

	public void setChunk(Chunk c) {
		chunk = c;
	}

	public void transferToChunk(Chunk c) {
		chunk.garbageQueue.add(this);
		c.incomingQueue.add(this);
		chunk = c;
		calculateGhostChunks();
	}

	public void calculateGhostChunks() {
		if (hasCollision) {
			for (Chunk gc : ghostChunks) {
				gc.removeGhostEntity(this);
			}
			ghostChunks.clear();

			Chunk endChunk = world.chunkContainingPoint((int) posX + sizeX,
					(int) posY);
			for (int x = chunk.getXIndex() + 2; x <= endChunk.getXIndex(); x++) {
				Chunk gc = world.chunkAtIndex(x, chunk.getYIndex());
				gc.addGhostEntity(this);
				ghostChunks.add(gc);
			}
			endChunk = world.chunkContainingPoint((int) posX, (int) posY
					+ sizeY);
			for (int y = chunk.getYIndex() + 2; y <= endChunk.getYIndex(); y++) {
				Chunk gc = world.chunkAtIndex(chunk.getXIndex(), y);
				gc.addGhostEntity(this);
				ghostChunks.add(gc);
			}
		}
	}

	public void adjustForCamera() {
		setCameraAdjustedX();
		setCameraAdjustedY();
		setCameraAdjustedSizeX();
		setCameraAdjustedSizeY();
		testWithinView();
	}

	public void updatePosition() {
		currentAnimation.updateAnimation();

		if (moveable) {
			if (affectedByGravity) {
				yVelocity += world.getGravity();
			}
			if (hasCollision) {
				onGround = testIfOnGround();
				testIfXCollision();
				if (onGround) {
					if (xVelocity > 0) {
						xVelocity -= 1;
						if (xVelocity < 0) {
							xVelocity = 0;
						}
					} else if (xVelocity < 0) {
						xVelocity += 1;
						if (xVelocity > 0) {
							xVelocity = 0;
						}
					}
					yVelocity = 0;
				}
			}

			posX += xVelocity;
			posY += yVelocity;

			if (posX - (posX % world.getChunkSize()) != chunk.getPosX()
					|| posY - (posY % world.getChunkSize()) != chunk.getPosY()) {
				Chunk chunkImIn = world.chunkContainingPoint((int) (posX),
						(int) (posY));
				if (chunkImIn != chunk) {
					transferToChunk(chunkImIn);
				}
			}

		}
	}

	private void testIfXCollision() {
		for (World.Chunk c : world.getActiveChunks()) {
			for (Entity e : c.getCollidables()) {
				if (e != this) {
					boolean inLine = rangesOverlap(getPosY(),
							getPosY() + sizeY, e.getPosY(), e.getPosY()
									+ e.sizeY);
					if (inLine) {
						if (xVelocity > 0) {
							if (getPosX() + sizeX >= e.getPosX()
									&& getPosX() + sizeX < e.getPosX()
											+ e.sizeX) {
								posX = e.getPosX() - sizeX;
								if (e.moveable) {
									if (mass >= e.mass) {
										// TODO fix bad code
										e.xVelocity = xVelocity * 0.8;
										e.testIfXCollision();
										xVelocity = e.xVelocity;

									} else {
										xVelocity = 0;
										e.xVelocity = xVelocity;
									}
								} else {
									xVelocity = 0;
								}
							}
						} else if (xVelocity < 0) {
							if (getPosX() <= e.getPosX() + e.sizeX
									&& getPosX() > e.getPosX()) {
								posX = e.getPosX() + e.sizeX;
								if (e.moveable) {
									if (mass >= e.mass) {
										// TODO fix bad code
										e.xVelocity = xVelocity * 0.8;
										e.testIfXCollision();
										xVelocity = e.xVelocity;

									} else {
										xVelocity = 0;
										e.xVelocity = xVelocity;
									}
								} else {
									xVelocity = 0;

								}
							}
						}
					}
				}
			}
		}
	}

	private boolean testIfOnGround() {
		for (World.Chunk c : world.getActiveChunks()) {
			for (Entity e : c.getCollidables()) {
				if (e != this) {
					boolean inLine = rangesOverlap(getPosX(),
							getPosX() + sizeX, e.getPosX(), e.getPosX()
									+ e.sizeX);
					if (inLine) {

						if (yVelocity >= 0) {
							if (getPosY() + sizeY >= e.getPosY()
									&& getPosY() + sizeY <= e.getPosY()
											+ yVelocity) {
								posY = e.getPosY() - sizeY;
								yVelocity = 0;
								return true;
							}
						} else if (yVelocity < 0) {
							if (getPosY() <= e.getPosY() + e.sizeY
									&& getPosY() >= e.getPosY() + e.sizeY
											+ yVelocity) {
								posY = e.getPosY() + e.sizeY;
								yVelocity = 0;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean rangesOverlap(double a1, double a2, double b1,
			double b2) {
		if (a1 >= b1) {
			return a1 < b2;
		} else {
			return a2 > b1;
		}
	}

	// </COLLISION>

	// <ANIMATION>
	private HashMap<String, Animation> animations;
	private Animation currentAnimation;
	private String animationKey;

	public String getAnimationKey() {
		return animationKey;
	}

	public void switchAnimation(String key) {
		currentAnimation.resetAnimation();
		currentAnimation = animations.get(key);
		animationKey = key;
	}

	public final void draw(Graphics page) {
		currentAnimation.draw(page);

		/*
		 * page.setColor(Color.blue); page.drawLine(getCameraAdjustedX(),
		 * getCameraAdjustedY(), world
		 * .getCamera().getCameraAppliedXPoint(chunk.getPosX(), 1), world
		 * .getCamera().getCameraAppliedYPoint(chunk.getPosY(), 1));
		 * page.setColor(Color.cyan); for (Chunk gc : ghostChunks) {
		 * page.drawLine(getCameraAdjustedX(), getCameraAdjustedY(), world
		 * .getCamera().getCameraAppliedXPoint(gc.getPosX(), 1), world
		 * .getCamera().getCameraAppliedYPoint(gc.getPosY(), 1));
		 * 
		 * }
		 */
	}

	public abstract void addAllAnimations();

	public void addAnimation(String key, Animation e) {
		animations.put(key, e);
	}

	protected boolean withinView;

	public boolean getWithinView() {
		return withinView;
	}

	public void testWithinView() {
		withinView = (cameraAdjustedX + cameraAdjustedSizeX > 0)
				&& (cameraAdjustedX < Play.dimensionX)
				&& (cameraAdjustedY + cameraAdjustedSizeY > 0)
				&& (cameraAdjustedY < Play.dimensionY);
	}

	private int cameraAdjustedX, cameraAdjustedY, cameraAdjustedSizeX,
			cameraAdjustedSizeY;

	public void setCameraAdjustedX() {
		cameraAdjustedX = world.getCamera().getCameraAppliedXPoint((int) posX,
				layer);
	}

	public int getCameraAdjustedX() {
		return cameraAdjustedX;
	}

	public void setCameraAdjustedY() {
		cameraAdjustedY = world.getCamera().getCameraAppliedYPoint((int) posY,
				layer);
	}

	public int getCameraAdjustedY() {
		return cameraAdjustedY;
	}

	public void setCameraAdjustedSizeX() {
		cameraAdjustedSizeX = (int) (sizeX * layer * world.getCamera()
				.getZoom());
	}

	public void setCameraAdjustedSizeY() {
		cameraAdjustedSizeY = (int) (sizeY * layer * world.getCamera()
				.getZoom());
	}

	public int getCameraAdjustedSizeX() {
		return cameraAdjustedSizeX;
	}

	public int getCameraAdjustedSizeY() {
		return cameraAdjustedSizeY;
	}

	public abstract class Animation {
		private ArrayList<AnimationFrame> animationFrames;
		private AnimationFrame currentAnimationFrame;
		private int animationIndex, frameCount;

		public Animation() {
			animationFrames = new ArrayList<AnimationFrame>();
			animationIndex = 0;
			frameCount = 0;
			addAllAnimationFrames();
			currentAnimationFrame = animationFrames.get(0);
		}

		public void updateAnimation() {
			if (animationFrames.size() > 1) {
				if (frameCount < currentAnimationFrame.getDuration()) {
					frameCount++;
				} else {
					frameCount = 0;
					cycleAnimationState();
				}
			}
		}

		public void cycleAnimationState() {
			if (animationIndex < animationFrames.size() - 1) {
				animationIndex++;
			} else {
				animationIndex = 0;
			}
			currentAnimationFrame = animationFrames.get(animationIndex);
		}

		public void resetAnimation() {
			animationIndex = 0;
			currentAnimationFrame = animationFrames.get(0);
		}

		public void draw(Graphics page) {
			animationFrames.get(animationIndex).draw(page);
		}

		public abstract void addAllAnimationFrames();

		public void addAnimationFrame(AnimationFrame af) {
			animationFrames.add(af);
		}

		public AnimationFrame getAnimationFrame(int index) {
			return animationFrames.get(index);
		}

		public int numberOfFrames() {
			return animationFrames.size();
		}
	}

	public abstract class AnimationFrame {
		private int duration;

		public int getDuration() {
			return duration;
		}

		public AnimationFrame() {
			this(1);
		}

		public AnimationFrame(int duration) {
			this.duration = duration;
		}

		public abstract void draw(Graphics page);
	}

	public class ImageAnimationFrame extends AnimationFrame {
		private BufferedImage image;

		public Graphics getImageCanvas() {
			return image.getGraphics();
		}

		public int getHeight() {
			return image.getHeight();
		}

		public int getWidth() {
			return image.getWidth();
		}

		private int offsetX;
		private int offsetY;

		public ImageAnimationFrame(BufferedImage image) {
			this(image, 1, 0, 0);
		}

		public ImageAnimationFrame(BufferedImage image, int duration) {
			this(image, duration, 0, 0);
		}

		public ImageAnimationFrame(BufferedImage image, int offsetX, int offsetY) {
			this(image, 1, offsetX, offsetY);
		}

		public ImageAnimationFrame(BufferedImage image, int duration,
				int offsetX, int offsetY) {
			super(duration);
			this.image = image;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		@Override
		public void draw(Graphics page) {
			page.drawImage(image, (int) ((cameraAdjustedX - offsetX)),
					(int) ((cameraAdjustedY - offsetY)), null);
		}
	}

	// </ANIMATION>
}
