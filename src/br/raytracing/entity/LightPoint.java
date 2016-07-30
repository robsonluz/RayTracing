package br.raytracing.entity;

import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.SceneLoader.Attribute;

public class LightPoint extends Light {
	private Point3D attenuation = new Point3D(1, 0, 0);

	public LightPoint() {
	}
	
	// This constructor is called by LightArea
	public LightPoint(Point3D pos, Point3D attenuation, Point3D color) {
		this.position = pos;
		this.attenuation = attenuation;
		this.color = color;
	}	
	
	@Override
	public Point3D getAmountOfLight(Point3D point) {
		double d = position.pointsDiff(point).length();		
		double totalAttenuation = 1 / (attenuation.z * d * d + attenuation.y * d + attenuation.x);
		return new Point3D( color.x * totalAttenuation, color.y * totalAttenuation, color.z * totalAttenuation );  
	}

	@Override
	public Point3D getVectorToLight(Point3D pointOfIntersection) {
		Point3D vec = pointOfIntersection.pointsDiff(position);
		vec.normalize();
		return vec;
	}
	
	public void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("pos".equals(attr.name)) position = attr.getPoint3D();
			if("attenuation".equals(attr.name)) attenuation = attr.getPoint3D();
			if("color".equals(attr.name)) color = attr.getPoint3D();
		}
	}

}
