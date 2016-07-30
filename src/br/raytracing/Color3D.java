package br.raytracing;

import java.awt.Color;

public class Color3D extends Point3D {
	public Color3D() {
		x = y = z = 0;
	}
	
	public Color3D(int rgb) {
		int a = (rgb >> 24) & 0xff;   
        int r = (rgb >> 16) & 0xff;   
        int g = (rgb >>  8) & 0xff;   
        int b = (rgb >>  0) & 0xff;
        
        x = r / 255F;
        y = g / 255F;
        z = b / 255F;
	}
	
	public Color3D(Color3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public Color3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private void normColors() {
		if(x>1) x=1;
		if(y>1) y=1;
		if(z>1) z=1;
		
		if(x<0) x=0;
		if(y<0) y=0;
		if(z<0) z=0;		
	}
	
	public Color getColor() {
		normColors();
		int r = (int) (x * 255);
		int g = (int) (y * 255);
		int b = (int) (z * 255);
		return new Color(r, g, b);
	}
}
