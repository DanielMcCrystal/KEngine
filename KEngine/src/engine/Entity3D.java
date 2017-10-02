package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public abstract class Entity3D extends Entity {
	protected boolean drawVertices, drawEdges, drawFaces;

	private ArrayList<Face> faces;
	private ArrayList<Edge> edges;

	public Edge getEdge(int index) {
		return edges.get(index);
	}

	private ArrayList<Vertex> vertices;

	public Entity3D(int posX, int posY, int posZ, int sizeX, int sizeY) {
		super(posX, posY, posZ, sizeX, sizeY);
		vertices = new ArrayList<Vertex>();
		faces = new ArrayList<Face>();
		edges = new ArrayList<Edge>();
		drawVertices = false;
		drawEdges = true;
		drawFaces = true;
	}
	
	public void setLayer() {
		super.setLayer();
		for(Vertex v: vertices) {
			v.setLayer();
		}
	}
	

	public void updatePosition() {
		super.updatePosition();
		boolean any = false;
		for (Vertex v : vertices) {
			v.updatePosition();
			if (v.getCameraAdjustedX() > 0
					&& v.getCameraAdjustedX() < Play.dimensionX
					&& v.getCameraAdjustedY() > 0
					&& v.getCameraAdjustedY() < Play.dimensionY) {
				any = true;
			}
		}
		withinView = any;
	}

	@Override
	public void addAllAnimations() {
		addAnimation("default", new Entity.Animation() {
			public void addAllAnimationFrames() {
				addAnimationFrame(new AnimationFrame() {
					public void draw(Graphics page) {
						if (drawFaces) {
							for (Face f : faces) {
								f.draw(page);
							}
						}
						if (drawEdges) {
							for (Edge e : edges) {
								e.draw(page);
							}
						}
						if (drawVertices) {
							for (Vertex v : vertices) {
								v.draw(page);
							}
						}
					}
				});
			}
		});
	}
	

	@Override
	public void setWorld(World w) {
		for (Vertex v : vertices) {
			v.setWorld(w);
			w.addEntity(v);
		}
		super.setWorld(w);
	}

	public abstract void setVertices();

	public void addFace(Edge e1, Edge e2, Color color) {
		faces.add(new Face(e1, e2, color));
	}

	public void addEdge(Vertex v1, Vertex v2) {
		if(v1 == null || v2 == null) {
			System.out.println(v1 + " " + v2);
		}
		
		boolean flag1 = false, flag2 = false;
		for (Vertex v : vertices) {
			if (!flag1 && v == v1) {
				flag1 = true;
			} else if (!flag2 && v == v2) {
				flag2 = true;
			}
		}
		if (!flag1) {
			vertices.add(v1);
		}
		if (!flag2) {
			vertices.add(v2);
		}
		edges.add(new Edge(v1, v2));
	}

	class Face {
		Vertex v1, v2, v3;
		Edge e1, e2;
		Color color;
		Polygon poly;

		public Face(Edge e1, Edge e2, Color color) {
			this.e1 = e1;
			this.e2 = e2;
			this.color = color;
			if (e1.v1 == e2.v1) {
				this.v1 = e1.v1;
				this.v2 = e1.v2;
				this.v3 = e2.v2;
			} else if (e1.v1 == e2.v2) {
				this.v1 = e1.v1;
				this.v2 = e1.v2;
				this.v3 = e2.v1;
			} else if (e1.v2 == e2.v1) {
				this.v1 = e1.v2;
				this.v2 = e1.v1;
				this.v3 = e2.v2;
			} else if (e1.v2 == e2.v2) {
				this.v1 = e1.v2;
				this.v2 = e1.v1;
				this.v3 = e2.v1;
			} else {
				System.out.println("Invalid plane!!");
			}
			
			poly = new Polygon();
			
			poly.addPoint(v1.getCameraAdjustedX(), v1.getCameraAdjustedY());
			poly.addPoint(v2.getCameraAdjustedX(), v2.getCameraAdjustedY());
			poly.addPoint(v3.getCameraAdjustedX(), v3.getCameraAdjustedY());
		}

		public void draw(Graphics page) {
			page.setColor(color);
			System.out.println();
			poly.xpoints[0] = v1.getCameraAdjustedX();
			poly.ypoints[0] = v1.getCameraAdjustedY();
			
			poly.xpoints[1] = v2.getCameraAdjustedX();
			poly.ypoints[1] = v2.getCameraAdjustedY();
			
			poly.xpoints[2] = v3.getCameraAdjustedX();
			poly.ypoints[2] = v3.getCameraAdjustedY();
			
			page.fillPolygon(poly);
		}
	}

	private class Edge {
		Vertex v1, v2;

		public Edge(Vertex v1, Vertex v2) {
			this.v1 = v1;
			this.v2 = v2;
		}

		public void draw(Graphics page) {
			page.setColor(Color.red);
			page.drawLine(v1.getCameraAdjustedX(), v1.getCameraAdjustedY(),
					v2.getCameraAdjustedX(), v2.getCameraAdjustedY());
		}
	}

	public class Vertex extends Entity {

		public Vertex(int posX, int posY, int posZ) {
			super(posX, posY, posZ, 0, 0);
		}
		
		@Override
		public int getCameraAdjustedSizeX() {
			return 0;
		}

		@Override
		public int getCameraAdjustedSizeY() {
			return 0;
		}

		@Override
		public void addAllAnimations() {
			addAnimation("default", new Entity.Animation() {
				public void addAllAnimationFrames() {
					addAnimationFrame(new AnimationFrame() {
						public void draw(Graphics page) {
							page.setColor(Color.cyan);
							page.fillRect(getCameraAdjustedX() - 2, getCameraAdjustedY() - 2,
									4, 4);
						}
					});
				}
			});
		}
	}
}
