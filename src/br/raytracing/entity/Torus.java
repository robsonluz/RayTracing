package br.raytracing.entity;

import Jama.Matrix;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.RootFinder;


public class Torus extends Entity {

	private Point3D center;
	private double centralRadius;
	private double tubeRadius;
	private Point3D normal;
	
	private double centralRadiusSquare;
	private double tubeRadiusSquare;
	
	// quatric polynomial coefficients
	private double a4, a3, a2, a1, a0;
	private double alpha, beta, gamma;

	public Torus() {
	}
	
	public Torus(Point3D center, double centralRadius, double tubeRadius, Point3D normal) {
		this.center = center;
		this.centralRadius = centralRadius;
		this.tubeRadius = tubeRadius;
		this.normal = normal;
	}
	
	
	@Override
	public void init() {
		normal.normalize();
		centralRadiusSquare = (centralRadius * centralRadius);
		tubeRadiusSquare = (tubeRadius * tubeRadius);
	}
	
	
	@Override
	public double intersect(Ray ray) {
		// Convert the ray position and direction to matrix style
		Matrix rayPosition = new Matrix(4,1);
		Matrix rayDirection = new Matrix(4,1);
		rayPosition.set(0, 0, ray.getPosition().x);
		rayPosition.set(1, 0, ray.getPosition().y);
		rayPosition.set(2, 0, ray.getPosition().z);
		rayPosition.set(3, 0, 1);
		rayDirection.set(0, 0, ray.getDirection().x);
		rayDirection.set(1, 0, ray.getDirection().y);
		rayDirection.set(2, 0, ray.getDirection().z);
		rayDirection.set(3, 0, 1);
		
		// Create the translation matrix
		Matrix M = Matrix.identity(4, 4);
		M.set(0, 3, -center.x);
		M.set(1, 3, -center.y);
		M.set(2, 3, -center.z);

		// Translate the position and direction vectors
		Matrix MPosition = M.times(rayPosition);
		Matrix MDirection = M.times(rayDirection);
		
		// Extract them from the matrix form
		Point3D translatedPosition = new Point3D( MPosition.get(0, 0) , MPosition.get(1, 0) , MPosition.get(2, 0) );
		Point3D translatedDirection = new Point3D( MDirection.get(0, 0) , MDirection.get(1, 0) , MDirection.get(2, 0) );
		
		translatedDirection.normalize();
		
		// Reconstruct the ray after translation
		//ray.setPosition(translatedPosition);
		//ray.setDirection(translatedDirection);
		
		// Prepare parameters to work with for solving the polynomial
		Point3D p = translatedPosition;
		Point3D d = translatedDirection;
		alpha = d.dot(d);
		beta = 2 * p.dot(d);
		gamma = p.dot(p) - tubeRadiusSquare - centralRadiusSquare;
		
		// Quatric polynomial coefficients
		a4 = (alpha * alpha);
		a3 = 2 * alpha * beta;
		a2 = ((beta * beta))  +  (2 * alpha * gamma)  +  (4 * centralRadiusSquare * (d.z * d.z));
		a1 = (2 * beta * gamma)  +  (8 * centralRadiusSquare * p.z * d.z);
		a0 = (gamma * gamma)  +  (4 * centralRadiusSquare * (p.z * p.z))  -  (4 * centralRadiusSquare * tubeRadiusSquare);
		
		// Solve quatric
		double[] coefficients = {a0, a1, a2, a3, a4};
		double[] roots = RootFinder.SolveQuartic(coefficients);
		
		if (roots == null || roots.length == 0)  return Double.POSITIVE_INFINITY;
		
		// Find the closest intersecting point
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < roots.length; i++) {
			if(roots[i] < min)
			{
				min = roots[i];
			}
		}
		return (min == Double.POSITIVE_INFINITY) ? Double.POSITIVE_INFINITY : min;
	}

	
	
	@Override
	protected Point3D getInternalNormal(Point3D point) {
		Point3D normal = new Point3D( 0, 0, 0);
		double innerComponent = (point.x * point.x)  +  
							   (point.y * point.y)  +  
							   (point.z * point.z)  - tubeRadiusSquare - centralRadiusSquare;
		
		normal.x = 4 * point.x * innerComponent;
		normal.y = 4 * point.y * innerComponent;
		normal.z = 4 * point.z * innerComponent + (8 * centralRadiusSquare * (point.z * point.z));
		
		// Create the normal in matrix form
		Matrix normalMatrix = new Matrix(4,1);
		normalMatrix.set(0, 0, normal.x);
		normalMatrix.set(1, 0, normal.y);
		normalMatrix.set(2, 0, normal.z);
		normalMatrix.set(3, 0, 1);
		
		// Create the translation matrix
		Matrix M = Matrix.identity(4, 4);
		M.set(0, 3, center.x);
		M.set(1, 3, center.y);
		M.set(2, 3, center.z);

		// Translate the normal
		Matrix Mnormal = M.times(normalMatrix);

		// Extract it from the matrix form
		Point3D translatedNormal = new Point3D( Mnormal.get(0, 0) , Mnormal.get(1, 0) , Mnormal.get(2, 0) );
		
		translatedNormal.normalize();
		return translatedNormal;
	}
	@Override
	public double[] getTextureCoords(Point3D point) {
		Point3D referenceVector = new Point3D( 1, 0, 0 );
		Point3D pointOnRing = new Point3D(point);
		Point3D opositeNormal = new Point3D(normal);
		opositeNormal.negate();
		pointOnRing.addVectorAndMultiply(opositeNormal, tubeRadius);
		//MathUtils.addVectorAndMultiply(pointOnRing, MathUtils.oppositeVector(normal), tubeRadius);
		Point3D vectorToRing = center.pointsDiff(pointOnRing);
		vectorToRing.normalize();
		
		double u = Math.acos(referenceVector.dot(vectorToRing));
	    if(referenceVector.cross(vectorToRing).dot(normal) < 0)
	    {
	    	u = 2 * Math.PI - u;
	    }
	    
	    u /= (2 * Math.PI);
		
	    Point3D fromRingToPoint = pointOnRing.pointsDiff(point);
	    fromRingToPoint.normalize();

	    double v = Math.acos(referenceVector.dot(fromRingToPoint));
//	    if(MathUtils.dotProduct(MathUtils.crossProduct(referenceVector, fromRingToPoint), normal) < 0)
//	    {
//	    	v = 2 * Math.PI - v;
//	    }
	    v /= (2 * Math.PI);

			
		return new double[] { u, v };
	}


	public Point3D getCenter() {
		return center;
	}


	public void setCenter(Point3D center) {
		this.center = center;
	}


	public double getCentralRadius() {
		return centralRadius;
	}


	public void setCentralRadius(double centralRadius) {
		this.centralRadius = centralRadius;
	}


	public double getTubeRadius() {
		return tubeRadius;
	}


	public void setTubeRadius(double tubeRadius) {
		this.tubeRadius = tubeRadius;
	}


	public Point3D getNormal() {
		return normal;
	}


	public void setNormal(Point3D normal) {
		this.normal = normal;
	}


}
