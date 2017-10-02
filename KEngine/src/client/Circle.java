package client;

import engine.Entity3D;

class Circle extends Entity3D {

	private int orientation;
	private int radius;

	public Circle(int posX, int posY, int posZ, int radius, int orientation) {
		super(posX, posY, posZ, 0, 0);
		this.orientation = orientation;
		this.radius = radius;
		this.hasCollision = false;
		this.drawVertices = false;
		setVertices();
	}

	@Override
	public void setVertices() {
		if (orientation == 1) {
			Vertex[] circle3 = new Vertex[24];
			for (int i = 0; i < circle3.length; i++) {
				double x = Math.cos(Math.PI * i / (circle3.length / 2))
						* radius;
				double z = Math.sin(Math.PI * i / (circle3.length / 2))
						* radius;
				circle3[i] = new Vertex(getPosX() + (int) x, getPosY(),
						getPosZ() - (int) z);
				if (i > 0) {
					addEdge(circle3[i], circle3[i - 1]);
				}
			}
			addEdge(circle3[0], circle3[circle3.length - 1]);
		} else if (orientation == 0) {
			Vertex[] circle1 = new Vertex[24];
			for (int i = 0; i < circle1.length; i++) {
				double x = Math.cos(Math.PI * i / (circle1.length / 2))
						* radius;
				double y = Math.sin(Math.PI * i / (circle1.length / 2))
						* radius;
				circle1[i] = new Vertex(getPosX() + (int) x, getPosY()
						- (int) y, getPosZ());
				if (i > 0) {
					addEdge(circle1[i], circle1[i - 1]);
				}
			}
			addEdge(circle1[0], circle1[circle1.length - 1]);
		} else {
			Vertex[] circle2 = new Vertex[12];
			for (int i = 0; i < circle2.length; i++) {
				double z = Math.cos(Math.PI * i / 6) * radius;
				double y = Math.sin(Math.PI * i / 6) * radius;
				circle2[i] = new Vertex(getPosX(), getPosY() - (int) y,
						getPosZ() + (int) z);
				if (i > 0) {
					addEdge(circle2[i], circle2[i - 1]);
				}
			}
			addEdge(circle2[0], circle2[circle2.length - 1]);
		}
	}

}

