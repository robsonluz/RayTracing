package br.raytracing.entity;

import java.io.Serializable;

import br.raytracing.Point3D;

public abstract class Light implements Serializable {
	public Point3D position;
	public Point3D color = new Point3D(1,1,1);
	
	public abstract Point3D getVectorToLight(Point3D pointOfIntersection);
	
	public abstract Point3D getAmountOfLight(Point3D point);

	public void update() {}
}
