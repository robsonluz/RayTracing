package br.raytracing.entity;

import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.SceneLoader.Attribute;

public class Cylinder extends Entity {
	private Point3D start = null;
	private Point3D direction = null;
	private double length;
	private double radius;
	
	// Fields for calculation optimizations
	private Point3D end = null;
	private double radiusSquare;
	private Point3D AB;
	private double ABdotAB;
	private Point3D referenceVector;
	private Point3D pivotVector;
	
	public Cylinder() {
	}
	
	public Cylinder(Point3D start, Point3D direction, double length, double radius) {
		super();
		this.start = start;
		this.direction = direction;
		this.length = length;
		this.radius = radius;
	}

	public void init() {
		// Choose an arbitrary vector as a reference vector - say the x basis vector
		referenceVector = new Point3D ( 1F, 0F, 0F );
		
		if (referenceVector.dot(direction) > 0.9)
			referenceVector = new Point3D (  0F, 1F, 0F );
			
		referenceVector.normalize();
		direction.normalize();
		
		pivotVector = direction.cross(referenceVector);
		pivotVector.normalize();
		
		// The new end point determines the new direction, we just have to normalize it
		end = new Point3D();
		end.x = start.x + (direction.x * length);
		end.y = start.y + (direction.y * length);
		end.z = start.z + (direction.z * length);
		

		// Optimization:  Perform calculations for later use
		radiusSquare = radius * radius;
		AB = start.pointsDiff(end);
		ABdotAB = AB.dot(AB);	
	}
	
	@Override
	public double intersect(Ray ray) {
		Point3D AO, AOxAB, VxAB; // Vectors to work with
		double a, b, c; // Quadratic equation coefficients

		AO = start.pointsDiff(ray.position);
		AOxAB = AO.cross(direction);
		VxAB = ray.direction.cross(direction);

		a = VxAB.dot(VxAB);
		b = 2 * VxAB.dot(AOxAB);
		c = AOxAB.dot(AOxAB) - radiusSquare;

		// Solve equation for at^2 + bt + c = 0
		double[] roots = solveQuadraticEquation(a, b, c);
		double distance;

		if (roots[0] == Double.POSITIVE_INFINITY) {
			distance = Double.POSITIVE_INFINITY;
		} else if (roots[0] <= 0 && roots[1] <= 0) {
			distance = Double.POSITIVE_INFINITY;
		}
		// We need to choose the closest intersection point which is within the
		// cylinder length
		else if (roots[0] >= 0 && roots[1] >= 0) {
			if (isPointOnCylinder(roots[0], ray)) {
				if (isPointOnCylinder(roots[1], ray)) {
					distance = Math.min(roots[0], roots[1]);
				} else {
					distance = roots[0];
				}
			} else if (isPointOnCylinder(roots[1], ray)) {
				distance = roots[1];
			} else {
				distance = Double.POSITIVE_INFINITY;
			}
		} else {
			distance = Math.max(roots[0], roots[1]);
		}

		return distance;
	}	
	
	private boolean isPointOnCylinder(double root, Ray ray) {

		// Formulas according to http://answers.yahoo.com/question/index?qid=20080218071458AAYz1s1
		Point3D AP;
//		double[] intersectPoint = ray.getPosition();
//		MathUtils.addVectorAndMultiply(intersectPoint, ray.getDirection(), root);
		
		double tmpMagnitude = ray.magnitude; 
		
		ray.magnitude = root;
		
		// Calculate the projection of the intersection point onto the direction vector of the cylinder
		AP = start.pointsDiff(ray.getEndPoint());
		double t = direction.dot(AP);
		
		if(t > length || t < 0)
			return false;
		
		//ray.magnitude = 1;
		ray.magnitude = tmpMagnitude;
		return true;
	}	
	
	@Override
	protected Point3D getInternalNormal(Point3D pointOfIntersection) {
		// Formulas according to http://answers.yahoo.com/question/index?qid=20080218071458AAYz1s1
		Point3D AP, center;
		
		// Calculate the projection of the intersection point onto the direction vector of the cylinder
		AP = start.pointsDiff(pointOfIntersection);
		double t = AB.dot(AP) / ABdotAB;
		center = new Point3D(start);
		center.addVectorAndMultiply(AB, t);

		// Calculate the vector from the intersection point to its projection onto the direction of the cylinder.
		Point3D normal = center.pointsDiff(pointOfIntersection);
		//normal.normalize();
		
		return normal;
	}
	
	@Override
	public double[] getTextureCoords(Point3D point) {
		try {			
			
			double pointStartDiff = point.pointsDiff(start).length(); 
			double dist = Math.sqrt(Math.abs((pointStartDiff * pointStartDiff) - (radius * radius)));
	
			Ray startToCenter = new Ray(start, direction, dist);
			Point3D pointToCenter = point.pointsDiff(startToCenter.getEndPoint());
			pointToCenter.normalize();
	
			double u = dist / length;
			double q = pointToCenter.dot(referenceVector);
			if (Math.abs(q) > 1) q = 1 * Math.signum(q);
			
			double v = Math.acos(q);
			Point3D orthoToPointToCenter = pointToCenter.cross(referenceVector);
			orthoToPointToCenter.normalize();
			
			if (orthoToPointToCenter.dot(direction) < 0) {
				v = (2 * Math.PI) - v;
			}
			
			v = v / (2 * Math.PI);
			
			return new double[] { u, v };		
		}
		catch (Exception e) {
			e.printStackTrace();
			return new double[] { 0, 0 };
		}	}
	
	private double[] solveQuadraticEquation(double a, double b, double c) {

		double[] roots = new double[2];
		if (a == 0) {
			roots[0] = -c / b;
		} else {
			double discriminant = (b * b) - 4 * a * c;

			if (discriminant < 0) {
				roots[0] = Double.POSITIVE_INFINITY;
			} else if (discriminant == 0) {
				roots[0] = (-b) / (2 * a);
			} else {
				discriminant = Math.sqrt(discriminant);
				double denominator = 2 * a;
				roots[0] = (-b + discriminant) / (denominator);
				roots[1] = (-b - discriminant) / (denominator);

				// Return the closest intersecting point
				// The primitives are convex so the closer point necessarily
				// occludes the the farther point
				// if(root1 > 0 && root2 > 0) return Math.min(root1, root2);

				// The camera is positioned inside the geometric primitive or
				// the primitive is completely behind us
				// return Math.max(root1, root2);

				// TODO: need to deal with negative roots?
			}
		}
		return roots;
	}

	public Point3D getStart() {
		return start;
	}

	public void setStart(Point3D start) {
		this.start = start;
	}

	public Point3D getDirection() {
		return direction;
	}

	public void setDirection(Point3D direction) {
		this.direction = direction;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public void setParameter(Attribute attr) {
		if("start".equals(attr.name)) start = attr.getPoint3D();
		else if("direction".equals(attr.name)) direction = attr.getPoint3D();
		else if("length".equals(attr.name)) length = attr.getDouble();
		else if("radius".equals(attr.name)) radius = attr.getDouble();
	}	

	
	
}
