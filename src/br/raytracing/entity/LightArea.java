package br.raytracing.entity;

import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.SceneLoader.Attribute;


// Note: LightArea instances are only alive during parse-time. As soon as a LightArea has been fully parsed,
// it replaces itself with a grid of point-lights and ceases to exist.
public class LightArea extends Light {
	private Point3D p0, p1, p2;	
	private Point3D attenuation = new Point3D(1, 0, 0);
	private int gridWidth = 1;
	
	public void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
		if ("p0".equals(attr.name)) p0 = attr.getPoint3D();	
			if ("p1".equals(attr.name)) p1 = attr.getPoint3D();
			if ("p2".equals(attr.name)) p2 = attr.getPoint3D();
			if ("grid-width".equals(attr.name)) gridWidth = attr.getInt();		
			if ("attenuation".equals(attr.name)) attenuation = attr.getPoint3D();
			if ("color".equals(attr.name)) color = attr.getPoint3D();
		}
	}
	
	// This method will be never called (see class notes).
	public Point3D getAmountOfLight(Point3D point) {
		return null;
	}

	public void update(Scene scene) {
		// Replace the LightArea with a grid of point-light objects.
		Point3D effectiveAttenuation = new Point3D(attenuation);
		effectiveAttenuation.multiplyVectorByScalar(gridWidth * gridWidth);
		
		Point3D p1Offset = p0.pointsDiff(p1);
		Point3D p2Offset = p0.pointsDiff(p2);
		
		p1Offset.multiplyVectorByScalar(1F / gridWidth);
		p2Offset.multiplyVectorByScalar(1F / gridWidth);
		
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridWidth; j++) {
				
				Point3D pos = new Point3D( p0.x + i * p1Offset.x + j * p2Offset.x,
								p0.y + i * p1Offset.y + j * p2Offset.y,
								p0.z + i * p1Offset.z + j * p2Offset.z );
								
				LightPoint lightPoint = new LightPoint(pos, effectiveAttenuation, color);
				scene.getLights().add(lightPoint);
			}
		}
				
		
//		// Look for our instance in the list and remove it
//		for (int i = 0; i < entities.size(); i++) {
//			if (entities.get(i) == this) {
//				entities.remove(i);
//			}
//		}
		
	}

	@Override
	public Point3D getVectorToLight(Point3D pointOfIntersection) {
		return null;
	}

	public Point3D getP0() {
		return p0;
	}

	public void setP0(Point3D p0) {
		this.p0 = p0;
	}

	public Point3D getP1() {
		return p1;
	}

	public void setP1(Point3D p1) {
		this.p1 = p1;
	}

	public Point3D getP2() {
		return p2;
	}

	public void setP2(Point3D p2) {
		this.p2 = p2;
	}

	public Point3D getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Point3D attenuation) {
		this.attenuation = attenuation;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}
	
}
