package br.raytracing;

import java.io.Serializable;
import java.util.List;

import br.raytracing.SceneLoader.Attribute;

public class VolumetricFog implements Serializable {
	private double zMin;
	private double zMax;
	private double density;
	private Color3D color;
	
	public VolumetricFog() {
		zMin = Double.MAX_VALUE;
		zMax = 15;
		density = 0.1;
		color = new Color3D(0.5, 0.5, 0.5);
	}
	
	public void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("zMin".equals(attr.name)) zMin = attr.getDouble();
			if("zMax".equals(attr.name)) zMax = attr.getDouble();
			if("density".equals(attr.name)) density = attr.getDouble();
			if("color".equals(attr.name)) color = attr.getColor3D();
		}
	}
	
	public void applyFlogToBackground(Color3D bgColor) {
		if(zMin < Double.MAX_VALUE) {
			double i = density * (zMax - zMin);
			bgColor.x = i * color.x + (1 - i);
			bgColor.y = i * color.y + (1 - i);
			bgColor.z = i * color.z + (1 - i);
		}
	}
	
	public void applyFlog(Color3D pointColor, double pointDistance) {
		if(pointDistance > zMin) {
			double i = density * (pointDistance - zMin);
			pointColor.x *= i * color.x + (1 - i);
			pointColor.y *= i * color.y + (1 - i);
			pointColor.z *= i * color.z + (1 - i);
		}
	}

	public double getZMin() {
		return zMin;
	}

	public void setZMin(double min) {
		zMin = min;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public Color3D getColor() {
		return color;
	}

	public void setColor(Color3D color) {
		this.color = color;
	}

	public double getZMax() {
		return zMax;
	}

	public void setZMax(double max) {
		zMax = max;
	}
	
//	private void applyFlog(Color3D pointColor, int i) {
//		pointColor.x *= i * color.x + (1 - i);
//		pointColor.y *= i * color.y + (1 - i);
//		pointColor.z *= i * color.z + (1 - i);	
//	}
	
}
