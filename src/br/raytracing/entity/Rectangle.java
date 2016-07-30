package br.raytracing.entity;



import br.raytracing.Color3D;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.SceneLoader.Attribute;

public class Rectangle extends Entity {
	private Point3D p0, p1, p2, p3;
	private Point3D normal = null;
	private Point3D intersectionPoint = null;
	private double d;
	private Point3D AB, AC;
	private double ABdotAB, ACdotAC;
	private double ABnorm;
	private double ACnorm;

	public Rectangle(){
	}
	
	/**
	 * Constructor.  Used only for defining rectangular components of a box. 
	 * @param p0
	 * @param p1
	 * @param p2
	 * @throws ParseException
	 */
	public Rectangle(Point3D p0, Point3D p1, Point3D p2, Color3D color){
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
	}
	
	@Override
	public void setParameter(Attribute attr) {
		if("p0".equals(attr.name)) p0 = attr.getPoint3D();
		else if("p1".equals(attr.name)) p1 = attr.getPoint3D();
		else if("p2".equals(attr.name)) p2 = attr.getPoint3D();
	}	
	
	public void init() {
		
		p3 = calcFourthPoint(p0, p1, p2);
		
		//normal = MathUtils.crossProduct(MathUtils.calcPointsDiff(p0, p1), MathUtils.calcPointsDiff(p0, p2));
		normal = p0.pointsDiff(p1).cross(p0.pointsDiff(p2));
		normal.normalize();
		
		d = -normal.dot(p0); 	
		
		// Preprocess some calculations to reduce load during rendering
		AB = p0.pointsDiff(p1);
		ABdotAB = AB.dot(AB);
		AC = p0.pointsDiff(p2);
		ACdotAC = AC.dot(AC);
		ABnorm = AB.length();
		ACnorm = AC.length();		
	}
	
	
	@Override
	public double intersect(Ray ray) {
		double distance = intersectWithPlane(ray);
		
		if(distance != Double.POSITIVE_INFINITY  &&  distance != Double.NEGATIVE_INFINITY)
		{
			return intersectBarycentric(ray, distance);			
		}
		
		// TODO: check end cases when ray is exactly on the disc's plane \ even inside the disc
		return Double.POSITIVE_INFINITY;
	}	
	
	/**
	 * Check if the given ray intersects with the 3D plane containing the rectangle and returns 
	 * the intersection distance.
	 * @param ray
	 * @return
	 */
	private double intersectWithPlane(Ray ray) {
		
		// raySouce is called p0 in the lecture notes, it was rename to avoid conflicting names
		Point3D raySource = ray.position;
		Point3D V = ray.direction;
		double distance = 0;

		if(V.dot(normal) != 0)
		{
			// TODO: can optimize the numerator - should be preprocessed because it always calculates the same value 
			distance = (-(raySource.dot(normal) + d)) / V.dot(normal);
		}
		
		if(distance <= 0)
			return Double.POSITIVE_INFINITY;
		return distance;
	}
	
	/**
	 * Calculate the intersection distance of this rectangle with the given ray.
	 * Calculations are done with barycentric: two of the rectangle's edges are taken as 
	 * spanning vectors of the plane and then the intersection point with the plane is located 
	 * by a linear combination of these vectors.  If the coordinates of both spanning vectors are 
	 * scalars x,y such that 0 < x,y < 1 , then the intersecting point is within the rectangle.
	 * between the rectangle vertices.
	 * 
	 * @param ray
	 * @param distance
	 * @return
	 */
	private double intersectBarycentric(Ray ray, double distance) {
		
		Point3D v0, v1, v2;
		double dot00, dot01, dot02, dot11, dot12;
		double denominator, u, v;
				
		// Get the intersection point with the rectangle's plane
		ray.magnitude = distance;
		intersectionPoint = ray.getEndPoint();

		// Compute vectors        
		v0 = p0.pointsDiff(p2);
		v1 = p0.pointsDiff(p1);
		v2 = p0.pointsDiff(intersectionPoint);

		// Compute dot products
		dot00 = v0.dot(v0);
		dot01 = v0.dot(v1);
		dot02 = v0.dot(v2);
		dot11 = v1.dot(v1);
		dot12 = v1.dot(v2);

		// Compute barycentric coordinates
		denominator = 1 / (dot00 * dot11 - dot01 * dot01);
		u = (dot11 * dot02 - dot01 * dot12) * denominator;
		v = (dot00 * dot12 - dot01 * dot02) * denominator;

		// Check if point is in rectangle
		if ((u > 0) && (v > 0) && (u < 1) && (v < 1)) {
			return distance;
		}
		
		return Double.POSITIVE_INFINITY;
	}	
	
//	@Override
//	public Color3D getColorAt(Point3D pointOfIntersection) {
//		double[] coords =  getTextureCoords(pointOfIntersection);
//		Color3D c = getCheckersColor(coords);
//		return c!=null? c : color;
//		//return color;
//	}
	
	
//	// Returns the checkers color for a given 2D point in [0, 1] coordinates
//	public Color3D getCheckersColor(double[] point2D) {
//		 double checkersX = Math.abs(Math.floor(point2D[0] / checkersSize) % 2);
//		 double checkersY = Math.abs(Math.floor(point2D[1] / checkersSize) % 2);
//		 
//		 if (checkersX == 0 && checkersY == 0) return checkersDiffuse2;
//		 if (checkersX == 0 && checkersY == 1) return checkersDiffuse1;
//		 if (checkersX == 1 && checkersY == 0) return checkersDiffuse1;
//		 if (checkersX == 1 && checkersY == 1) return checkersDiffuse2;
//		 
//		 return null;
//	}
//	double checkersSize = 0.1F;
//	Color3D checkersDiffuse1 = new Color3D( 1.0F, 1.0F, 1.0F );
//	Color3D checkersDiffuse2 = new Color3D( 0.1F, 0.1F, 0.1F );			
	
	public double[] getTextureCoords(Point3D point) {
		
		
				
		// Calculate the projection of the intersection point onto the rectangle vectors
		Point3D AP = p0.pointsDiff(point);
		double q = 1 / p0.pointsDiff(p1).length();
				
//		double u = AB.dot(AP) / ABdotAB;
//		double v = AC.dot(AP) / ACdotAC;
		
		double u = AB.dot(AP);
		double v = AC.dot(AP);
		
		if(!materialRepeat) {
			u /= ABdotAB;
			v /= ACdotAC;
		}else{
			u /= getMaterialWidth();
			v /= getMaterialHeight();
		}
		//System.out.println(ACdotAC);
		
		u /= ABnorm * q;
		v /= ACnorm * q;
		
		return new double[] { u, v };
	}

	@Override
	protected Point3D getInternalNormal(Point3D pointOfIntersection) {
		return normal;
	}

	/**
	 * Calculates the fourth vertex of the rectangle
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Point3D calcFourthPoint(Point3D p0, Point3D p1, Point3D p2) {
		return new Point3D (p1.x + (p2.x - p0.x) , p1.y + (p2.y - p0.y) , p1.z + (p2.z - p0.z) );
	}
	
}
