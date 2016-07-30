package br.raytracing.entity;

import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.SceneLoader.Attribute;

public class LightDirected extends Light {
	public Point3D direction;		
	public Point3D oppositeDirection;
	
	public LightDirected(){
	}
			
	
	public LightDirected(Point3D direction, Point3D color) {
		this.direction = direction;
		this.color = color;
	}
	
	public void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("direction".equals(attr.name)) direction = attr.getPoint3D();
			if("color".equals(attr.name)) color = attr.getPoint3D();
		}
	}	
	
	
	public void update() {
		position = new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		direction.normalize();
		oppositeDirection = new Point3D(direction);
		oppositeDirection.negate();
	}
	
	@Override
	public Point3D getVectorToLight(Point3D pointOfIntersection) {
		return oppositeDirection;
	}

	@Override
	public Point3D getAmountOfLight(Point3D point) {
		return color;
	}
	
}
