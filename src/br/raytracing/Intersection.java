package br.raytracing;

import br.raytracing.entity.Entity;

public class Intersection {
	public Ray ray;
	public Entity entity;
	public double distance;
	
	public Intersection(Ray ray, Entity entity, double distance) {
		super();
		this.ray = ray;
		this.entity = entity;
		this.distance = distance;
	}
	
	
	
	
}
