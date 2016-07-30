package br.raytracing.entity;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.SceneLoader.Attribute;

public class Camera implements Serializable {
	private Point3D eye;
	private Point3D lookAt;
	private Point3D direction;
	private Point3D upDirection;	
	private Point3D rightDirection;
	private Point3D viewplaneUp; 	
	
	private double screenDist = 1;
	private double screenWidth = 2;	
	
	private double pixelWidth;
	private double pixelHeight;
	
	
	private Point3D forward;
	private int focalLength;
	private Dimension resolution;
	
	//private Point currentPoint;
	//private Point3D rayOrigin;
	//private double mPerPix;
	
	//protected final static int LOWFILMSIZE = 35;
	
	/** Low resolution image=> 640x480 */
	public final static Dimension LOW_RESOLUTION = new Dimension(640, 480);
	/** Medium resolution image => 800x600 */
	public final static Dimension MED_RESOLUTION = new Dimension(800, 600);
	/** High resolution image => 1024x768 */
	public final static Dimension HIGH_RESOLUTION = new Dimension(1024, 768);
	
	public final static Dimension HIGH_WIDE_RESOLUTION = new Dimension(1440, 900);
	
	public int superSampleWidth = 5;
	
	
	public Camera() {
		resolution = LOW_RESOLUTION;
		
		eye = new Point3D(0, 0, 2);
		lookAt = new Point3D(0, 0, 0);
		upDirection = new Point3D(0, 1, 0);
		
		screenWidth = 1;
		screenDist = 1;
	}
	
	public void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("eye".equals(attr.name)) eye = attr.getPoint3D();
			if("look-at".equals(attr.name)) lookAt = attr.getPoint3D();
			if("screen-dist".equals(attr.name)) screenDist = attr.getDouble();
			if("screen-width".equals(attr.name)) screenWidth = attr.getDouble();
			if("up-direction".equals(attr.name)) upDirection = attr.getPoint3D();
		}
	}
	

	public Camera(Point3D eye, Point3D lookAt, Point3D upDirection, double screenDist, double screenWidth) {
		super();
		this.eye = eye;
		this.lookAt = lookAt;
		this.upDirection = upDirection;
		this.screenDist = screenDist;
		this.screenWidth = screenWidth;
	}



	public void reset() {
		if(direction==null) {
			direction = eye.pointsDiff(lookAt);
			direction.normalize();
		}
		
		// Compute a right direction and a viewplane up direction (perpendicular to the look-at vector)
		rightDirection = upDirection.cross(direction);
		rightDirection.normalize();
		rightDirection.multiplyVectorByScalar(-1);
		
		viewplaneUp = rightDirection.cross(direction);		
		viewplaneUp.normalize();			
		
		pixelWidth = screenWidth / resolution.width;
		pixelHeight = (resolution.width / resolution.height) * pixelWidth;
		
		System.out.println("viewplaneUp: " + viewplaneUp);
		System.out.println("direction: " + direction);
		System.out.println("rightDirection: " + rightDirection);
		System.out.println("position: " + eye);
		
		System.out.println();
		
		
		///////////////////////////////////////////
//		
//		mPerPix = (LOWFILMSIZE / 1000.) / LOW_RESOLUTION.height;
//		
//		rayOrigin = new Point3D(position.x - focalLength * forward.x / 1000., position.y
//				- focalLength * forward.y / 1000., position.z - focalLength
//				* forward.z / 1000.);

	}
	
	public Ray getRay(int x, int y, double sampleXOffset, double sampleYOffset) {
		//if(currentPoint.x <= resolution.width && currentPoint.y <= resolution.height) {
			
			//System.out.println(direction);
			//System.out.println(currentPoint.x+" | "+currentPoint.y);
			
			Ray ray = new Ray(new Point3D(this.eye), new Point3D(this.direction), this.screenDist);
			Point3D endPoint = ray.getEndPoint();		
			//System.out.println(viewplaneUp);
			
			
//			double upOffset = -1 * (y - (scene.getCanvasHeight() / 2) - (sampleYOffset / superSampleWidth)) * pixelHeight;
//			double rightOffset = (x - (scene.getCanvasWidth() / 2) + (sampleXOffset / superSampleWidth)) * pixelWidth;			
			
			double upOffset = -1 * (y - (resolution.height / 2) - (sampleYOffset / superSampleWidth))  * pixelHeight;
			double rightOffset = (x - (resolution.width / 2)+ (sampleXOffset / superSampleWidth)) * pixelWidth;
			
			
			
			/*MathUtils.addVectorAndMultiply(endPoint, viewplaneUp, upOffset);
			MathUtils.addVectorAndMultiply(endPoint, rightDirection, rightOffset);*/
			//System.out.println(rightDirection);
			endPoint.addVectorAndMultiply(viewplaneUp, upOffset);
			endPoint.addVectorAndMultiply(rightDirection, rightOffset);
			
			//System.out.println(rightDirection);
			
					
			//ray.setDirection(MathUtils.calcPointsDiff(eye, endPoint));						
			ray.direction = eye.pointsDiff(endPoint);
			ray.normalize();
			//System.out.println(ray);
		
			//Calcula próximo ponto
//			currentPoint.x++;
//			if (currentPoint.x > resolution.width - 1) {
//				currentPoint.x = 0;
//				currentPoint.y++;
//			}	
			//System.out.println(ray);
			return ray;			
			
			/*Ray ray = new Ray();
			
			//Calcula posição inicial do ray
			ray.position.x = position.x + (currentPoint.x - (resolution.width / 2.)) * mPerPix;
			ray.position.y = position.y - (currentPoint.y - (resolution.height / 2.)) * mPerPix;
			ray.position.z = position.z;
	

			ray.direction.x = ray.position.x - rayOrigin.x;
			ray.direction.y = ray.position.y - rayOrigin.y;
			ray.direction.z = ray.position.z - rayOrigin.z;
			ray.direction.normalize();

			
			//Calcula próximo ponto
			currentPoint.x++;
			if (currentPoint.x > resolution.width - 1) {
				currentPoint.x = 0;
				currentPoint.y++;
			}
			
			return ray;*/
		//}
		//return null;
	}
	
	public Point3D getPosition() {
		return eye;
	}

	public void setPosition(Point3D position) {
		this.eye = position;
	}

	public Point3D getForward() {
		return forward;
	}

	public void setForward(Point3D forward) {
		this.forward = forward;
	}

	public int getFocalLength() {
		return focalLength;
	}

	public void setFocalLength(int focalLength) {
		this.focalLength = focalLength;
	}

	public Dimension getResolution() {
		return resolution;
	}

	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}



	public Point3D getEye() {
		return eye;
	}



	public void setEye(Point3D eye) {
		this.eye = eye;
	}



	public Point3D getLookAt() {
		return lookAt;
	}



	public void setLookAt(Point3D lookAt) {
		this.lookAt = lookAt;
	}



	public Point3D getUpDirection() {
		return upDirection;
	}



	public void setUpDirection(Point3D upDirection) {
		this.upDirection = upDirection;
	}



	public double getScreenDist() {
		return screenDist;
	}



	public void setScreenDist(double screenDist) {
		this.screenDist = screenDist;
	}



	public double getScreenWidth() {
		return screenWidth;
	}



	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}



	public void setDirection(Point3D direction) {
		this.direction = direction;
	}

//	public Point getCurrentPoint() {
//		return currentPoint;
//	}
}
