package client;

import java.awt.Color;

import engine.Entity3D;

public class RectangularPrism extends Entity3D {

	private int sizeZ;

	public int getSizeZ() {
		return sizeZ;
	}

	public RectangularPrism(int posX, int posY, int posZ, int sizeX, int sizeY,
			int sizeZ) {
		super(posX, posY, posZ, sizeX, sizeY);
		this.sizeZ = sizeZ;
		this.hasCollision = true;
		setVertices();
	}

	public void setVertices() {
		Vertex[] vertices = new Vertex[8];
		vertices[0] = new Vertex(getPosX(), getPosY(), getPosZ());
		vertices[1] = new Vertex(getPosX() + getSizeX(), getPosY(), getPosZ());
		vertices[2] = new Vertex(getPosX() + getSizeX(),
				getPosY() + getSizeY(), getPosZ());
		vertices[3] = new Vertex(getPosX(), getPosY() + getSizeY(), getPosZ());
		vertices[4] = new Vertex(getPosX(), getPosY(), getPosZ() - sizeZ);
		vertices[5] = new Vertex(getPosX() + getSizeX(), getPosY(), getPosZ()
				- sizeZ);
		vertices[6] = new Vertex(getPosX() + getSizeX(),
				getPosY() + getSizeY(), getPosZ() - sizeZ);
		vertices[7] = new Vertex(getPosX(), getPosY() + getSizeY(), getPosZ()
				- sizeZ);

		addEdge(vertices[0], vertices[1]);
		addEdge(vertices[1], vertices[2]);
		addEdge(vertices[2], vertices[3]);
		addEdge(vertices[3], vertices[0]);

		addEdge(vertices[4], vertices[5]);
		addEdge(vertices[5], vertices[6]);
		addEdge(vertices[6], vertices[7]);
		addEdge(vertices[7], vertices[4]);

		addEdge(vertices[0], vertices[4]);
		addEdge(vertices[1], vertices[5]);
		addEdge(vertices[2], vertices[6]);
		addEdge(vertices[3], vertices[7]);

		addFace(getEdge(0), getEdge(1), Color.blue);
		addFace(getEdge(3), getEdge(2), Color.blue);

		addFace(getEdge(0), getEdge(8), Color.red);
		addFace(getEdge(4), getEdge(9), Color.red);

		addFace(getEdge(1), getEdge(9), Color.green);
		addFace(getEdge(5), getEdge(10), Color.green);
	}

}

class Cube extends RectangularPrism {

	public Cube(int posX, int posY, int posZ, int sideLength) {
		super(posX, posY, posZ, sideLength, sideLength, sideLength);
		setVertices();
		this.drawFaces = false;
	}
	
	@Override
	public void setVertices() {
		super.setVertices();

	}

}