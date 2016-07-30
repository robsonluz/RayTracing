package br.raytracing;



/**
 * 
 * @author Robson J. P.
 * @since 1.0
 */
public class Ray {
	public Point3D position;
	public Point3D direction;
	public double magnitude;
	public static int count = 0;
	
	public Ray() {
		position = new Point3D();
		direction = new Point3D();
		count++;
	}
	
	public Ray(Point3D position, Point3D direction, double magnitude) {
		super();
		this.position = position;
		this.direction = direction;
		this.magnitude = magnitude;
		count++;
	}


	public String toString() {
		return "pos["+position.x+","+position.y+","+position.z+"] | dir["+direction.x+","+direction.y+","+direction.z+"]";
	}
	
	// Normalizes the vector
	public void normalize() {
		direction.normalize();
		//magnitude = 1;	
	}	
	
	// Returns the end of the vector as a point in 3D space 
	public Point3D getEndPoint() {
		return new Point3D( position.x + magnitude * direction.x, 
							position.y + magnitude * direction.y, 
							position.z + magnitude * direction.z);
		
	}

	public Point3D getPosition() {
		return position;
	}

	public void setPosition(Point3D position) {
		this.position = position;
	}

	public Point3D getDirection() {
		return direction;
	}

	public void setDirection(Point3D direction) {
		this.direction = direction;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}	
}
