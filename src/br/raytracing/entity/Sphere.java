package br.raytracing.entity;

import br.raytracing.Color3D;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.SceneLoader.Attribute;

public class Sphere extends Entity {
	public Point3D center;
	private double radius;

	public Sphere() {
		center = new Point3D(0, 0, 0);
		radius = 0.5;
		color = new Color3D();
	}
	
	public Sphere(Point3D center, double radius, Color3D color) {
		super();
		this.center = center;
		this.radius = radius;
		this.color = color;
	}

	@Override
	public double intersect(Ray ray) {
		// if(1==1)
		// return Double.POSITIVE_INFINITY;
		// Note that locals are named according to the equations in the lecture
		// notes.
		// return intersectAlgebraic(ray);
		double d = intersectGeometric(ray);
		if(bumpMapping!=null) {
			double coords[] = getTextureCoords(ray.getEndPoint());
			Color3D bumpColor = bumpMapping.getColor(coords);
			if(bumpColor.x <= bumpIndexTransparency) {
				return Double.POSITIVE_INFINITY;
			}
			/*if(bumpColor.x!=0 || bumpColor.y!=0 || bumpColor.z!=0) {
				d += bumpColor.x; 
				//System.out.println(bumpColor);	
			}*/
		}
		return d;

		// double inter;
		//
		// double b, c; // quadratic function (a=1)
		// double disc; // discriminant
		// double t;
		//
		// b = 2. * (ray.direction.x * (ray.position.x - center.x) +
		// ray.direction.y
		// * (ray.position.y - center.y) + ray.direction.z * (ray.position.z -
		// center.z));
		// c = Math.pow((ray.position.x - center.x), 2)
		// + Math.pow((ray.position.y - center.y), 2)
		// + Math.pow((ray.position.z - center.z), 2) - Math.pow(radius, 2);
		// disc = Math.pow(b, 2) - 4 * c;
		// if (disc < 0) {
		// inter = Double.POSITIVE_INFINITY;
		// } else {
		// t = (-b - Math.sqrt(disc));
		// if (t < 0) {
		// t = (-b + Math.sqrt(disc));
		// }
		// t = t / 2.;
		// if (t < 1e-2) {
		// inter = Double.POSITIVE_INFINITY;
		// } else {
		// inter = new Double(t);
		// }
		// }
		// return inter;
	}

	private double intersectGeometric(Ray ray) {
		Point3D L = ray.position.pointsDiff(center);
		Point3D V = new Point3D(ray.direction);
		// System.out.println(ray);
		// System.exit(0);

		double tCA = L.dot(V);

		if (tCA < 0) {
			// In this case the camera is inside the sphere or the sphere center
			// lies
			// behind the ray, which means we have no intersection
			return Double.POSITIVE_INFINITY;
		}

		double LSquare = L.dot(L);

		double dSquare = LSquare - (tCA * tCA);
		double radiusSquare = (radius * radius);

		if (dSquare > radiusSquare) {
			// In this case the ray misses the sphere
			return Double.POSITIVE_INFINITY;
		}

		double tHC = Math.sqrt(radiusSquare - dSquare);

		// We now check where the ray originated:
		// Gur: CHECK. LSquare == MathUtils.dotProduct(L, L), can't be smaller
		if (L.dot(L) < LSquare) {
			// The ray originated in the sphere - the intersection is with the
			// exit point
			return tCA + tHC;
		} else {
			// The ray originated ouside the sphere - the intersection is with
			// the entrance point
			return tCA - tHC;
		}
	}

	private double intersectAlgebraic(Ray ray) {

		// Quadratic equation coefficients
		double a, b, c;

		// Note that locals are named according to the equations in the lecture
		// notes.
		Point3D v = new Point3D(ray.direction);
		Point3D p0 = new Point3D(ray.position);
		Point3D O = new Point3D(center);
		Point3D p0_O = p0.pointsDiff(O);

		a = 1;
		b = 2 * v.dot(p0_O);
		c = p0_O.dot(p0_O) - (radius * radius);

		// Solve equation for at^2 + bt + c = 0
		double[] roots = solveQuadraticEquation(a, b, c);

		double distance;
		if (roots[0] > 0 && roots[1] > 0) {
			distance = Math.min(roots[0], roots[1]);
		} else if (roots[0] <= 0 && roots[1] <= 0) {
			distance = Double.POSITIVE_INFINITY;
		} else {
			distance = Math.max(roots[0], roots[1]);
		}
		return distance;
	}

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

	public Color3D getColor() {
		return color;
	}

	public void setColor(Color3D color) {
		this.color = color;
	}

//	@Override
//	public Color3D getColorAt(Point3D pointOfIntersection) {
//		
//		double[] coords =  getTextureCoords(pointOfIntersection);
//		Color3D c = getCheckersColor(coords);
//		return c!=null? c : color;
//		
//
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

	@Override
	protected Point3D getInternalNormal(Point3D pointOfIntersection) {
		Point3D normal = center.pointsDiff(pointOfIntersection);
		return normal;
	}
	
	
	
	public double[] getTextureCoords(Point3D point) {
		Point3D rp = center.pointsDiff(point);
		
        double v = rp.z / radius;
        
        if (Math.abs(v) > 1) v -= 1 * Math.signum(v);
        v = Math.acos(v);
        
        double u = rp.x / (radius * Math.sin(v));
        
        if (Math.abs(u) > 1) u = Math.signum(u);
        u = Math.acos(u);               
        
        if (rp.y < 0)
            u = -u;
        if (rp.z < 0)
            v = v + Math.PI;
        
        if (Double.isNaN(u)) {
        	int a = 0; a++;
        }
        
        u = (u / (2 * Math.PI));
        v = (v / Math.PI);
        
        if (u > 1) u -= 1;
        if (u < 0) u += 1;
        
        if (v > 1) v -= 1;
        if (v < 0) v += 1;
        
        return new double[] {u , v };						
	}



	@Override
	public void setParameter(Attribute attr) {
		if("center".equals(attr.name)) center = attr.getPoint3D();
		else if("radius".equals(attr.name)) radius = attr.getDouble();
	}	
}
