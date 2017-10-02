package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class TextBox extends Entity {

	private String text;
	private ArrayList<String> textLines;
	private Font font;
	private int lineSpace;
	private int height;

	public TextBox(String text, Font font) {
		super(20, Play.dimensionY - 220, Integer.MAX_VALUE,
				Play.dimensionX - 700, 200);
		textLines = new ArrayList<String>();
		this.font = font;
		this.text = text;

	}

	public void setWorld(World w) {
		super.setWorld(w);
		
		Graphics g = world.player.getGraphics();
		FontMetrics fm = g.getFontMetrics(font);
		lineSpace = fm.getLeading();
		height = fm.getHeight();

		int lastIndex = 0;
		String currentLine = "";
		String token = "";
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ' || i == text.length() - 1) {
				token = text.substring(lastIndex, i + 1);
				lastIndex = i+1;

				if (fm.stringWidth(currentLine + token) < sizeX - 10) {
					currentLine += token;
				} else {
					textLines.add(currentLine);
					currentLine = token;
				}
			}
		}
		textLines.add(currentLine);
	}
	
	@Override
	public void testWithinView() {
		withinView = true;
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Animation() {

			@Override
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						page.setColor(Color.black);
						page.fillRect(getPosX() - 5, getPosY() - 5, sizeX + 10,
								sizeY + 10);
						page.setColor(Color.white);
						page.fillRect(getPosX(), getPosY(), sizeX, sizeY);

						page.setFont(font);
						page.setColor(Color.blue);
						for (int i = 0; i < textLines.size(); i++) {
							String str = textLines.get(i);
							page.drawString(str, getPosX() + 10, getPosY()
									+ (i * (lineSpace + height)) + height);
						}
					}
				});
			}

		});
	}

}
