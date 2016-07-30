package br.raytracing;

import java.io.Serializable;


/**
 * 
 * @author Robson J. P.
 * @since 1.0
 */
public class Point3D implements Serializable {
	public double x;
	public double y;
	public double z;
	
	public Point3D() {
		x = y = z = 0;
	}
	
	public Point3D(Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public Point3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double length() {
		double length;
		length = x * x + y * y + z * z;
		length = Math.sqrt(length);
		return length;
	}
	


	public void normalize() {
		double length = length();
		if(length==0)
			return;
		x /= length;
		y /= length;
		z /= length;
	}
	
	public double angle(Point3D other) {
		double dot = dot(other);
		dot /= length();
		dot /= other.length();
		return Math.acos(dot);
	}

	public double dot(Point3D other) {
		double dot = this.x * other.x;
		dot += this.y * other.y;
		dot += this.z * other.z;
		return dot;
	}

	public Point3D cross(Point3D other) {
		Point3D newV = new Point3D();
		newV.x = (this.y * other.z - this.z * other.y);
		newV.y = (this.z * other.x - this.x * other.z);
		newV.z = (this.x * other.y - this.y * other.x);
		return newV;
	}
	
	public Point3D pointsDiff(Point3D other) {
		Point3D newV = new Point3D();
		newV.x = (other.x - this.x);
		newV.y = (other.y - this.y);
		newV.z = (other.z - this.z);
		return newV;
	}
	
	/**
	 * Add two points in 3D
	 * @param a
	 * @param b
	 * @return
	 */
	public Point3D addPoints(Point3D b) {
		return new Point3D( this.x + b.x , this.y + b.y , this.z + b.z );
	}
	
	public void negate() {
		x *= -1;
		y *= -1;
		z *= -1;
	}

	public void scaleAdd(double scl, Point3D other) {
		x = scl * x + other.x;
		y = scl * y + other.y;
		z = scl * z + other.z;
	}
	
	public void addVectorAndMultiply(Point3D addition, double scalar) {
		x += addition.x * scalar; 
		y += addition.y * scalar;
		z += addition.z * scalar;
	}	
	
	// Multiplies vec by a scalar
	public void multiplyVectorByScalar(double scalar) {
		x *= scalar; 
		y *= scalar;
		z *= scalar;
	}
	
	public void addScalar(double scalar) {
		x += scalar; 
		y += scalar;
		z += scalar;
	}
	
	// Reflects a vector around a normal vector. both vectors are assumed to have the same shift from the origin
	public Point3D reflectVector(Point3D normal) {
		double dotProduct = this.dot(normal);
		
		return new Point3D( -this.x + 2 * normal.x * dotProduct,
						    -this.y + 2 * normal.y * dotProduct,
  						    -this.z + 2 * normal.z * dotProduct );
						
	}
	
	// Vector addition, adds addition to vec
	public void addVector(Point3D addition) {
		x += addition.x; 
		y += addition.y;
		z += addition.z;
	}
	
	public void multiplyByVector(Point3D mult) {
		x *= mult.x; 
		y *= mult.y;
		z /= mult.z;	}
		
	
	public String toString() {
		return "["+x+", "+y+", "+z+"]";
	}

	public void divideByScalar(double scalar) {
		x /= scalar; 
		y /= scalar;
		z /= scalar;
	}
}
