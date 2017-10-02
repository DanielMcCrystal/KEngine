package client;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import engine.Controls;
import engine.Play;
import engine.TextBox;
import engine.World;

public class Client extends Play {
	
	private static final long serialVersionUID = 1L;
	
	Character c;
	
	public void init() {
		super.init();
		c = new Character(1350, 50, 60, 85);
		getActiveWorld().addEntity(c);
		getActiveWorld().setCentralEntity(c);
		getActiveWorld().getCamera().lockOnEntity(c, true, true);
		
		getActiveWorld().addEntity(new Platform(1000, 0, 15, dimensionY));
		getActiveWorld().addEntity(new Platform(1000, dimensionY - 15, dimensionX + 100, 15));
		getActiveWorld().addEntity(new Platform(1000 + dimensionX - 15, 0, 15, dimensionY));
		getActiveWorld().addEntity(new Platform(1000, 0, dimensionX, 15));
		
		getActiveWorld().addEntity(new Platform(1000, 300, 500, 15));
		getActiveWorld().addEntity(new Platform(1200, 200, 100, 105));
		
		getActiveWorld().addEntity(new Box(1450, 200, 50, 50, Color.red, Color.yellow));
		getActiveWorld().addEntity(new Box(2400, 700, 60, 40, Color.blue, Color.green));
		getActiveWorld().addEntity(new Box(1550, 500, 35, 60, Color.magenta, Color.white));
		
		for (int i = 1; i <= 12; i+=2) {
			getActiveWorld().addEntity(
					new Platform(1500 + 50 * i + 15 * i, 300 + 30 * i + 15 * i, 50, 15));
		}
		
		
		
		for (int i = 0; i < 5000; i++) {
			getActiveWorld().addEntity(new Particle(1000, 0, 2, 2));
		}
		for(int i=0; i<250; i++) {
			int x = (int)(Math.random() * (1680 * 3));
			int z = (int)(Math.random() * -2000 - 1);
			int sizeY = (int)(Math.random() * 300 + 200);
			getActiveWorld().addEntity(new Tree(x, 885 - sizeY, z, sizeY / 2, sizeY));
		}
		
		getActiveWorld().addEntity(new Plane(0, 885, -2000, 3000, 6000, Color.white));
		getActiveWorld().addEntity(new Plane(3000, 885, -2000, 11000, 6000, Color.white));
		
		/*
		getActiveWorld().addEntity(new TextBox("Lorem ipsum dolor sit amet, usu aeterno percipit ad. "
				+ "No nibh idque putent nec, eos ex adhuc utamur quaestio. Esse bonorum voluptatum vim an, "
				+ "dolor consulatu laboramus ea eos. In lorem doctus vis, eum no integre eruditi, cu stet nemore "
				+ "corpora cum. Nam ut legere detracto scripserit, assum aeque meliore cum et, ex est tale homero "
				+ "inermis. Feugiat vulputate appellantur vix ut. Ea eam propriae aliquando tincidunt, "
				+ "tacimates mediocritatem ex has, mea ex sumo erat. Nisl ipsum tibique ea vel, an vim putent "
				+ "nominati forensibus. Vix prompta copiosae persequeris an, sea id nostrum iracundia.",
				new Font("Arial Narrow", Font.PLAIN, 24)));
		*/
		getActiveWorld().addEntity(new Cube(1150, 785, 50, 100));
		for (int i = -3; i <= 3; i++) {
			getActiveWorld().addEntity(
					new Circle(1350, 785, 15 * i, (int) (Math.sin(Math
							.acos(0.25 * i)) * 60), 0));
			getActiveWorld().addEntity(
					new Circle(1350, 785 + 15 * i, 0, (int) (Math.sin(Math
							.acos(0.25 * i)) * 60), 1));
			getActiveWorld().addEntity(
					new Circle(1350 + 15 * i, 785, 0, (int) (Math.sin(Math
							.acos(0.25 * i)) * 60), 2));
		}
		
		this.startGame();
	}

	boolean test;

	public void runGlobalControls() {
		World world = getActiveWorld();
		if (Controls.keyDown(KeyEvent.VK_C)) {
			if (!test) {
				test = true;
				if (getActiveWorld().getCamera().getLockedY()) {
					getActiveWorld().getCamera().lockOnEntity(c, true, false);
					getActiveWorld().getCamera().setPosY(dimensionY / 2);
				} else {
					getActiveWorld().getCamera().lockOnEntity(c, true, true);
				}
			}
		} else {
			test = false;
		}
		
		if (Controls.keyDown(KeyEvent.VK_UP)) {
			world.getCamera().zoomIn();
		} else if (Controls.keyDown(KeyEvent.VK_DOWN)) {
			world.getCamera().zoomOut();
		}

		if (Controls.keyDown(KeyEvent.VK_RIGHT)) {
			world.getCamera().fovIn();
		} else if (Controls.keyDown(KeyEvent.VK_LEFT)) {
			world.getCamera().fovOut();
		}

		else if (Controls.keyDown(KeyEvent.VK_R)) {
			world.getCamera().resetFieldOfView();
			world.getCamera().resetZoom();
		}

	}

}

