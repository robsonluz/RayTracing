package br.raytracing.entity;

import br.raytracing.Color3D;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.SceneLoader.Attribute;

public class Box extends Entity {
	private Point3D d0;
	private Point3D d1;
	
	private Point3D p0, p1, p2, p3;
	private Rectangle[] rectangles = new Rectangle[6];
	private Rectangle currentIntersectingRectangle = null;
	
	public Box(){
	}
	
	public Box(Point3D p0, Point3D p1, Point3D p2, Point3D p3, Color3D color) {
		super();
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.color = color;
	}

	@Override
	public void setParameter(Attribute attr) {
		if("p0".equals(attr.name)) p0 = attr.getPoint3D();
		else if("p1".equals(attr.name)) p1 = attr.getPoint3D();
		else if("p2".equals(attr.name)) p2 = attr.getPoint3D();
		else if("p3".equals(attr.name)) p3 = attr.getPoint3D();
		else if("d0".equals(attr.name)) d0 = attr.getPoint3D();
		else if("d1".equals(attr.name)) d1 = attr.getPoint3D();
	}

	public void init() {
		if(p0==null && d0!=null && d1!=null) {
			p0 = new Point3D(d0);
			p1 = new Point3D(d0.x+d1.x, d0.y, d0.z);
			p2 = new Point3D(d0.x, d1.y, d0.z);
			p3 = new Point3D(d1);
		}
		
		
		Point3D p0_p1 = p0.pointsDiff(p1);
		Point3D p0_p3 = p0.pointsDiff(p3);
		
		// Assume this is a cube just for the documentation
		rectangles[0] = new Rectangle(p0, p1, p2, color);		// Front facing rectangle
		rectangles[1] = new Rectangle(p0, p2, p3, color);		// Left facing rectangle
		rectangles[2] = new Rectangle(p0, p3, p1, color);		// Bottom facing rectangle
		
		rectangles[0].init();
		rectangles[1].init();
		rectangles[2].init();

		rectangles[3] = new Rectangle(p1, p3.addPoints(p0_p1), p2.addPoints(p0_p1), color);		// Right facing rectangle
		rectangles[4] = new Rectangle(p2, p2.addPoints(p0_p1), p2.addPoints(p0_p3), color);		// Top facing rectangle
		rectangles[5] = new Rectangle(p3, p2.addPoints(p0_p3), p3.addPoints(p0_p1), color);		// Back facing rectangle
		
		rectangles[3].init();
		rectangles[4].init();
		rectangles[5].init();
		
		for(Rectangle rec: rectangles) {
			rec.setMaterial(material);	
			rec.setMaterialWidth(getMaterialWidth());
			rec.setMaterialHeight(getMaterialHeight());
		}
	}
	
	@Override
	public double intersect(Ray ray) {
		// Start off with infinite distance and no intersecting primitive
		double minDistance = Double.POSITIVE_INFINITY;

		for (int i = 0; i < rectangles.length; i++) {
			double t = rectangles[i].intersect(ray);

			// If we found a closer intersecting rectangle, keep a reference to
			// and it
			if (t < minDistance) {
				minDistance = t;
				currentIntersectingRectangle = rectangles[i];
			}
		}

		return minDistance;
	}	


	@Override
	protected Point3D getInternalNormal(Point3D pointOfIntersection) {
		return currentIntersectingRectangle.getNormal(pointOfIntersection);
	}

	@Override
	public double[] getTextureCoords(Point3D point) {
		return currentIntersectingRectangle.getTextureCoords(point);
	}

	
	
}
