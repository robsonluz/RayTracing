package br.raytracing;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import br.raytracing.animation.Animation;
import br.raytracing.entity.Camera;
import br.raytracing.entity.Entity;
import br.raytracing.entity.Light;
import br.raytracing.entity.Scene;

/**
 * 
 * @author Robson J. P.
 * @since 1.0
 */
public class RayTracer {
	private Scene scene;
	private Camera camera;
	private Animation animation;
	
	public final int MAX_REFLECTION_RECURSION_DEPTH = 3;
	public static final double EPSILON = 0.00000001F;
	private BufferedImage renderImage;

	public RayTracer(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
	}
	
	public RayTracer() {
		camera = new Camera();
		scene = new Scene(camera);
	}

	private synchronized Color3D getColor(Ray ray, Entity entity, double distance, int recursionDepth, double a_RIndex) {

		if (recursionDepth > MAX_REFLECTION_RECURSION_DEPTH) 
			return new Color3D();
		
		Point3D pointOfIntersection = ray.getEndPoint();

		Color3D color = new Color3D();

		Color3D diffuse = entity.getColorAt(pointOfIntersection);

		ray.magnitude = ray.magnitude - 1;

		Point3D normal = entity.getNormal(pointOfIntersection);
		
		
		/*
		//Refraction
		if(entity.refraction> 0) {
	        boolean bInside = false;

	        if (normal.dot(ray.direction) > 0.0f) {
				normal.multiplyVectorByScalar(-1.0f);
				bInside = true;
			} else {
				bInside = false;
			}
			
			
			double fViewProjection = ray.direction.dot(normal);
			double fCosThetaI = Math.abs(fViewProjection);
			
			double fSinThetaI, fCosThetaT, fSinThetaT;
			
			double defaultRefractionCoef = 0.001;
			double entityDensity = 5;
			
			double fRefractionCoef = defaultRefractionCoef;
			
			
			double fDensity1 = defaultRefractionCoef;
			double fDensity2;
			
            if (bInside) {
				// We only consider the case where the ray is originating a
				// medium close to the void (or air)
				// In theory, we should first determine if the current object is
				// inside another one
				// but that's beyond the purpose of our code.
				fDensity2 = defaultRefractionCoef;
			} else {
				fDensity2 = entityDensity;
			}			

            if (fCosThetaI >= 0.999f) {
				// In this case the ray is coming parallel to the normal to the
				// surface
				// fReflectance = (fDensity1 - fDensity2) / (fDensity1 +
				// fDensity2);
				// fReflectance = fReflectance * fReflectance;
				fSinThetaI = 0.0f;
				fSinThetaT = 0.0f;
				fCosThetaT = 1.0f;
			} else {
				fSinThetaI = Math.sqrt(1 - fCosThetaI * fCosThetaI);
				fCosThetaT = 0.0;
				fSinThetaT = (fDensity1 / fDensity2) * fSinThetaI;
				if (fSinThetaT * fSinThetaT > 0.9999f) {
					// fReflectance = 1.0f ; // pure reflectance at grazing
					// angle
					fCosThetaT = 0.0f;
				} else {
					fCosThetaT = Math.sqrt(1 - fSinThetaT * fSinThetaT);
				}
			}

            
            double fOldRefractionCoef = fRefractionCoef;
            if (bInside) 
            {
            	fRefractionCoef = defaultRefractionCoef;
            }
            else
            {
            	fRefractionCoef = entityDensity;
            }            
            
            
            Point3D refractionDir = new Point3D(ray.direction);
			
			Point3D pn = new Point3D(normal);
			pn.multiplyVectorByScalar(fCosThetaI);
			refractionDir.addVector(pn);
			
			//viewRay.dir = (fOldRefractionCoef / fRefractionCoef) * viewRay.dir;
			refractionDir.multiplyVectorByScalar(fOldRefractionCoef / fRefractionCoef);
			
            pn = new Point3D(normal);
			pn.multiplyVectorByScalar(fCosThetaT);
			refractionDir.addVector(pn);
			
			//refractionDir.x *= -1;
			refractionDir.z *= -1;
			//refractionDir.negate();
			//refractionDir.multiplyVectorByScalar(0.5);
			
			Ray refractionRay = new Ray(pointOfIntersection, refractionDir, 1);
			
			Intersection refractionIntersection = findIntersection(refractionRay, null);
			if(refractionIntersection!=null) {
				refractionRay.magnitude = refractionIntersection.distance;
				//ray.magnitude = 10;
				Color3D reflectionColor = getColor(refractionRay, refractionIntersection.entity, refractionIntersection.distance , recursionDepth + 1);								
				//color = reflectionColor;
				color.addVectorAndMultiply(reflectionColor, entity.refraction);
			}			
		}
		*/
		
		
		
		

		for (Light light : scene.getLights()) {
			Point3D vectorToLight = light.getVectorToLight(pointOfIntersection);

			Ray rayToLight = new Ray(pointOfIntersection, vectorToLight, 1);
			rayToLight.normalize();

			double distanceToBlockingPrimitive = 0;
			
			// Light is visible if there's no intersection with an object at least epsilon away
			Intersection inter = findIntersection(rayToLight, null);
			
			if(inter!=null)
				distanceToBlockingPrimitive = inter.distance;
			//double distanceToLight = MathUtils.norm(MathUtils.calcPointsDiff(pointOfIntersection, light.getPosition()));
			double distanceToLight = pointOfIntersection.pointsDiff(light.position).length();
			
			boolean lightVisible = distanceToBlockingPrimitive <= EPSILON 
				|| distanceToBlockingPrimitive >= distanceToLight;
			
			if (lightVisible) {		
				Point3D amountOfLightAtIntersection = light
						.getAmountOfLight(pointOfIntersection);
	
				double visibleDiffuseLight = vectorToLight.dot(normal);
				if (visibleDiffuseLight > 0 ) {
					
					if(entity.refraction==0 || 0==0) {
						// Diffuse
						color.x += (diffuse.x * amountOfLightAtIntersection.x
								* visibleDiffuseLight) * (1d - entity.refraction);
						color.y += (diffuse.y * amountOfLightAtIntersection.y
								* visibleDiffuseLight) * (1d - entity.refraction);
						color.z += (diffuse.z * amountOfLightAtIntersection.z
								* visibleDiffuseLight) *  (1d - entity.refraction);
					}
	
						// Specular
						// Find the reflection around the normal
						Point3D reflectedVectorToLight = vectorToLight
								.reflectVector(normal);
						reflectedVectorToLight.normalize();
		
						double visibleSpecularLight = reflectedVectorToLight
								.dot(ray.direction);
		
						if (visibleSpecularLight < 0) {
							visibleSpecularLight = Math.pow(Math
									.abs(visibleSpecularLight), entity.shininess);
		
							color.x += entity.specular.x
									* amountOfLightAtIntersection.x
									* visibleSpecularLight;
							color.y += entity.specular.y
									* amountOfLightAtIntersection.y
									* visibleSpecularLight;
							color.z += entity.specular.z
									* amountOfLightAtIntersection.z
									* visibleSpecularLight;
						}
					
				}
			}
		}

		
		// Ambient		
		Point3D sceneAmbient = scene.getAmbientLight(); 
		Point3D surfaceAmbient = entity.ambient;
		
		color.x += sceneAmbient.x * surfaceAmbient.x; 		
		color.y += sceneAmbient.y * surfaceAmbient.y;
		color.z += sceneAmbient.z * surfaceAmbient.z;
		
		
		// Reflection Ray
		if(entity.reflectance> 0) {
			

//			Point3D p1 = new Point3D(normal);
//			p1.multiplyVectorByScalar(ray.direction.dot(normal));
//			p1.multiplyVectorByScalar(2.0f);
//			p1.negate();
//			
//			Point3D reflectionDirection = new Point3D(ray.direction);
//			reflectionDirection.addVector(p1);
//			
//			Point3D start = new Point3D(pointOfIntersection);
//			Point3D r1 = new Point3D(reflectionDirection);
//			r1.multiplyVectorByScalar(EPSILON);
//			start.addVector(r1);
			
			
			Point3D d = new Point3D(ray.direction);
			d.negate();
			Point3D reflectionDirection = d.reflectVector(normal);
			Point3D start = new Point3D(pointOfIntersection);
			Point3D r1 = new Point3D(reflectionDirection);
			r1.multiplyVectorByScalar(EPSILON);
			start.addVector(r1);
			
			
			Ray reflectionRay = new Ray(start, reflectionDirection, 1);
			reflectionRay.normalize();
	
			Intersection reflectionIntersection = findIntersection(reflectionRay, null);
			if(reflectionIntersection!=null) {
				ray.magnitude = reflectionIntersection.distance;
				Point3D reflectionColor = getColor(reflectionRay, reflectionIntersection.entity, reflectionIntersection.distance , recursionDepth + 1, a_RIndex);								
				
				color.addVectorAndMultiply(reflectionColor, entity.reflectance);
			}			
		}

		
		// calculate refraction
		double refr = entity.refraction;
		double refractionIndex = entity.refractionIndex;
		//double a_RIndex = 1;
		double result = 0;
		if ((refr > 0)){
			
	        if (normal.dot(ray.direction) > 0.0f) { //Inside
	        	normal.multiplyVectorByScalar(-1.0f);
				result = -1;
			}else{
				
				result = 1;
			}
			
			double rindex = refractionIndex;
			double n = a_RIndex / rindex;
			//vector3 N = prim->GetNormal( pi ) * (float)result;
			Point3D N = new Point3D(normal);
			N.multiplyVectorByScalar(result);
			//float cosI = -DOT( N, a_Ray.GetDirection() );
			double cosI = -N.dot(ray.direction);
			double cosT2 = 1.0f - n * n * (1.0f - cosI * cosI);
			if (cosT2 > 0.0f) {
				//vector3 T = (n * a_Ray.GetDirection()) + (n * cosI - sqrtf( cosT2 )) * N;
				
				//(n * a_Ray.GetDirection())
				Point3D temp = new Point3D(ray.direction);
				temp.multiplyVectorByScalar(n);
				
				
				temp.addScalar(n * cosI - Math.sqrt(cosT2));
				
				//n * cosI - sqrtf( cosT2 )) * N;
				Point3D T = new Point3D(N);
				T.multiplyByVector(temp);

				//T.multiplyVectorByScalar(n * cosI - Math.sqrt(cosT2));
				
				//T.addVector(temp);
				//T.normalize();
				
				
//				float dist;
//				Raytrace( Ray( pi + T * EPSILON, T ), rcol, a_Depth + 1, rindex, dist );
				
				Point3D rayOrigin = new Point3D(pointOfIntersection);
				
				//T * EPSILON
				Point3D T1 = new Point3D(T);
				T1.multiplyVectorByScalar(EPSILON);
				
				//pi + T * EPSILON
				rayOrigin.addVector(T1);
				//T.y *= -1;
				Ray refractionRay = new Ray(rayOrigin, T, 1);
				Intersection refractionIntersection = findIntersection(refractionRay, null);
				
				if(refractionIntersection!=null) {
					refractionRay.magnitude = refractionIntersection.distance;
					//ray.magnitude = 10;
					Color3D reflectionColor = getColor(refractionRay, refractionIntersection.entity, refractionIntersection.distance , recursionDepth + 1, rindex);								
					//color = reflectionColor;
					color.addVectorAndMultiply(reflectionColor, entity.refraction);
				}				
				
				
				
				
//				// apply Beer's law
//				Color absorbance = prim->GetMaterial()->GetColor() * 0.15f * -dist;
//				Color transparency = Color( expf( absorbance.r ), expf( absorbance.g ), expf( absorbance.b ) );
//				a_Acc += rcol * transparency;
			}
		}		
		
		
		
		
		//MathUtils.addVectorAndMultiply(color, reflectionColor, surface.getReflectance());
		
		//Apply Flog
		scene.getFog().applyFlog(color, distance);

		return color;
	}

	public Intersection findIntersection(Ray ray, Entity ignorePrimitive) {
		double distance = scene.intersect(ray);
		if (scene.getIntersectedEntity() == null) { // Render the
			return null;
		} else {
			return new Intersection(ray, scene.getIntersectedEntity(), distance);
		}
	}

	public void render(final CanvasPaint canvas, final int yStart, final int yEnd, final boolean inverse) {
//		camera.reset();
//		scene.init();

//		new Thread() {
//			@Override
//			public void run() {
				
				int end = yEnd;
				if(yEnd==-1) {
					end = camera.getResolution().height;
				}
				
				render(0, yStart, camera.getResolution().width, end, canvas, inverse);
//			}
//		}.start();	
	}
	
	public void render() {
		/*camera.reset();
		scene.init();

		renderImage = new BufferedImage(camera.getResolution().width, camera.getResolution().height, BufferedImage.TYPE_INT_RGB);
		
		JFrame frame = new JFrame();
		final PanelCanvas canvas = new PanelCanvas(renderImage);
		Panel pnl = new Panel();
		pnl.setLayout(new BorderLayout());
		JButton cmdSalvar = new JButton("Salvar");
		cmdSalvar.setEnabled(false);
		cmdSalvar.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					JFileChooser file = new JFileChooser();
					int ret = file.showSaveDialog(canvas);
					if(ret == JFileChooser.APPROVE_OPTION) {
						ImageIO.write(renderImage, "PNG", file.getSelectedFile());
					}
				}catch(Exception ex) {
				}

			}
		
		});
		pnl.add(cmdSalvar, BorderLayout.PAGE_START);
		pnl.add(canvas, BorderLayout.CENTER);
		frame.setSize(camera.getResolution());
		frame.setContentPane(pnl);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		new Thread() {
			@Override
			public void run() {
				render(0, 0, camera.getResolution().width, camera.getResolution().height, canvas);
			}
		}.start();		
		
//		new Thread() {
//			@Override
//			public void run() {
//				render(0, 0, camera.getResolution().width, camera.getResolution().height/2, canvas);
//			}
//		}.start();
//		new Thread() {
//			@Override
//			public void run() {
//				render(0, (camera.getResolution().height/2)+1, camera.getResolution().width, camera.getResolution().height, canvas);
//			}
//		}.start();	


		//System.out.println("Pontos: " + hint);
		System.out.println("Raios: "+Ray.count);
		System.out.println("Fim");
		cmdSalvar.setEnabled(true);
		*/
	}
	
	
	public IntersectionIntern intersect(Ray ray) {
		double distance = Double.POSITIVE_INFINITY;
		Entity intersectedEntity = null;
		for(Entity entity: scene.getEntitys()) {
			double entityDistance = entity.intersect(ray);
			if(entityDistance < distance && entityDistance > RayTracer.EPSILON) {
				distance = entityDistance;
				intersectedEntity = entity;
			}
		}
		return new IntersectionIntern(intersectedEntity, distance);
	}	
	
	private static class IntersectionIntern {
		Entity entity;
		double distance;
		public IntersectionIntern(Entity entity, double distance) {
			super();
			this.entity = entity;
			this.distance = distance;
		}
		
	}
	
	private void render(int x, int y, CanvasPaint canvas) {
		int hits = 0;
		Color3D color = new Color3D();
		
		for (int k = 0; k < camera.superSampleWidth; k++) {															
			for (int l = 0; l < camera.superSampleWidth; l++) {	
				Ray ray = camera.getRay(x, y, k, l);
				if (ray != null) {
					//double distance = scene.intersect(ray);
					//if (scene.getIntersectedEntity() != null) {
					
					//Point3D ptAimed = start + myScene.persp.clearPoint * dir;
//                    Point3D ptAimed = new Point3D(ray.position);
//                    ptAimed.multiplyByVector(ray.direction);	
//                    ptAimed.addScalar(scene.getClearPoint());

					IntersectionIntern inter = intersect(ray);
					if(inter.entity!=null){
						ray.magnitude = inter.distance;

						Color3D sampleColor = getColor(ray, inter.entity, inter.distance, 1, 1);
						color.addVector(sampleColor);
						hits++;
						
						if (scene.getDispersion() != 0.0 && 0==1) {
							if(inter.distance < 1.7 || inter.distance > 4) {
	                       
	                            for(int i=0;i<10;i++) {
		                            Point3D vDisturbance = new Point3D();                        
		                            vDisturbance.x = (0.0039 * Math.random());
		                            vDisturbance.y = (0.0039 * Math.random());
		                            vDisturbance.z = 0.0f;
		                            ray.position.addVector(vDisturbance);
									inter = intersect(ray);
									if(inter.entity!=null){
										ray.magnitude = inter.distance;
				
										sampleColor = getColor(ray, inter.entity, inter.distance, 1, 1);
										color.addVector(sampleColor);
										hits++;
									}
		                        }
                            }
						}
					}
					

				}
			}
		}
		
		if (hits == 0) {
			color = scene.getBackGroundColor();			
			scene.getFog().applyFlogToBackground(color);
		}	
		else
		{
			// Average the cumulative color values
			color.multiplyVectorByScalar(1D / hits);
			//MathUtils.multiplyVectorByScalar(color, 1F / hits);
		}				
		
		//System.out.println(x);
		if(x<camera.getResolution().width && y<camera.getResolution().height) {
			//image[x][y] = color;
			canvas.paintPixel(x, y, color.getColor().getRGB());
			//renderImage.setRGB(x, y, color.getColor().getRGB());
			//canvas.repaint(x, y, 1, 1);
		}
		//list.add(new Pixel(x, y, color));
	}
	
	private void render(int xini, int yini, int xend, int yend, CanvasPaint canvas, boolean inverse) {
		//Color3D image[][] = new Color3D[xend-xini][yend-yini];
		int passos = 1;
		if(inverse) {
			for (int y = yend; y >= yini; y--) {
				for (int x = xini; x <= xend; x++) {
					render(x, y, canvas);
				}
				
				if(canvas.allPixelsRendered())
					break;
			}
		}else{
			for(int p=0; p < passos; p++) {
				for (int y = yini; y <= yend; y+=1) {
					for (int x = xini + p; x <= xend; x+=passos) {
						render(x, y, canvas);
					}
					if(canvas.allPixelsRendered())
						break;
				}
			}
		}
		
		//scene.getFocalBlur().applyBlur(xini, yini, xend, yend, image, renderImage, canvas);
		System.out.println("Raios: "+Ray.count);
		System.out.println("Fim.");
	}

	public Scene getScene() {
		return scene;
	}

	public Camera getCamera() {
		return camera;
	}

	public static class PanelCanvas extends Panel {
		private BufferedImage renderImage;

		public PanelCanvas(BufferedImage renderImage) {
			super();
			//this.list = list;
			this.renderImage = renderImage;
			//setBackground(Color.blue);
			
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(renderImage, 0, 0, null);
			//
			/*
			Graphics2D g2 = (Graphics2D) g;
			for (Pixel pixel : list) {
				g.setColor(pixel.color.getColor());
				
				//g2.draw(new Point2D.Float(pixel.x, pixel.y));
				g.drawLine(pixel.x, pixel.y, pixel.x, pixel.y);
			}
			*/
		}

	}

//	private class Pixel {
//		public int x;
//		public int y;
//		public Color3D color;
//
//		public Pixel(int x, int y, Color3D color) {
//			super();
//			this.x = x;
//			this.y = y;
//			this.color = color;
//		}
//
//	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
}
