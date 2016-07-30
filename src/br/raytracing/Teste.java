package br.raytracing;

import br.raytracing.entity.Box;
import br.raytracing.entity.Camera;
import br.raytracing.entity.Cylinder;
import br.raytracing.entity.LightArea;
import br.raytracing.entity.LightDirected;
import br.raytracing.entity.Rectangle;
import br.raytracing.entity.Scene;
import br.raytracing.entity.Sphere;
import br.raytracing.entity.Torus;

public class Teste {
	private static void scene1() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(0, 1, 3));
		camera.setLookAt(new Point3D(0, 0, 0));
		camera.setUpDirection(new Point3D(0, 1, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);
		
		//scene.ambient = new Color3D(1.1,1.1,1.1);

		
		Sphere sph = new Sphere(new Point3D(), 0.5, new Color3D(1, 0.5, 0));
		sph.shininess = 5;
		sph.reflectance = 0.5;
		sph.specular = new Point3D(0.1, 0.5, 0.3);
		
		sph.material = new TextureMaterial("images/jupiter-1k.png");
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(-0.4, -0.2, 0.9), 0.3, new Color3D(0, 0.5, 0));
		sph.shininess = 20;
		sph.specular = new Point3D(0.3, 0.2, 0.3);
		sph.material = new TextureMaterial("images/neptune-1k.png");
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(0.5, 0, 0.5), 0.3, new Color3D(0, 0.5, 0.5));
		sph.shininess = 30;
		sph.specular = new Point3D(0.1, 0.1, 0.3);
		sph.material = new TextureMaterial("images/EarthTM0360.jpg");
		//sph.reflectance = 0.3;		
		scene.addEntity(sph);		

		
		LightDirected lightDirected = new LightDirected(new Point3D(0.3, -0.7, -1), new Color3D(1, 1, 1));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		
		//lightDirected = new LightDirected(new Point3D(0.5, -1.9, -1.5), new Color3D(0.1, 0.1, 0.1));
		lightDirected = new LightDirected(new Point3D(10.3, -10.9, 9.5), new Color3D(0.5, 0.5, 0.5));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		
		
		rayTracer.render();
	}
	
	private static void scene2() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(0, 0.3, 2));
		camera.setDirection(new Point3D(0, 0, -1));
		camera.setUpDirection(new Point3D(0, 1, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);

		Rectangle rec = new Rectangle(new Point3D(-1, 0, -1),
									  new Point3D(-1, 0, 1),
									  new Point3D(1, 0, -1),
									  new Color3D(0.4,0.4,0.4));
		rec.reflectance = 0.8;
		//scene.addEntity(rec);

		
		Sphere sph = new Sphere(new Point3D(0, 0.5, 0), 0.2, new Color3D(1, 0.5, 0));
		sph.shininess = 5;
		sph.specular = new Point3D(0.1, 0.5, 0.3);
		sph.reflectance = 0.1;
		sph.material = new TextureMaterial("images/hearth_heightmap.gif");
		sph.bumpMapping = new TextureMaterial("images/hearth_heightmap.gif");
		scene.addEntity(sph);
		
		LightDirected lightDirected = new LightDirected(new Point3D(0, -1, -1), new Color3D(1, 1, 1));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		
		
		rayTracer.render();
	}	
	
	private static void scene3() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(-1, 1.7, 1));
		camera.setLookAt(new Point3D(0, 0.8, 0));
		camera.setUpDirection(new Point3D(0, 1, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);
		
		Box box = new Box(new Point3D(0, 0, 0),
						  new Point3D(1, 0, 0),
						  new Point3D(0, 1, 0),
						  new Point3D(0, 0, -1),
						  new Color3D(0.1,0.8,0.4));
		box.material = new CheckersMaterial();
		//box.material = new TextureMaterial("images/foto1.jpg");
		box.specular = new Point3D(0.7, 0.7, 0.7);
		box.shininess = 40;
		//box.reflectance = 0.3;
		
		Sphere sph = new Sphere(new Point3D(1.1, 0.8, 0.2), 0.2, new Color3D(1, 0.5, 0));
		sph.shininess = 5;
		sph.specular = new Point3D(0.1, 0.4, 0.4);
		sph.material = new TextureMaterial("images/neptune-1k.png");
		sph.reflectance = 0.8;
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(-0.3, 0.8, -0.4), 0.2, new Color3D(1, 0.5, 0));
		sph.shininess = 5;
		sph.specular = new Point3D(0.1, 0.4, 0.3);
		sph.material = new TextureMaterial("images/verde1.png");
		//sph.reflectance = 0.2;
		sph.refraction = 1;
		scene.addEntity(sph);
		
		
		
		scene.addEntity(box);
		
		LightDirected lightDirected = new LightDirected(new Point3D(0.3, -0.7, -1), new Color3D(1, 1, 1));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		
		
		rayTracer.render();
	}
	
	private static void scene4() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		//scene.setBackGroundColor(new Color3D(0.3,0.5,0.8));
		scene.setBackGroundColor(new Color3D(0.0,0.0,0.0));
		scene.getFog().setZMin(2);
		scene.getFog().setZMax(10);
		scene.getFog().setDensity(0.6);
		scene.getFog().setColor(new Color3D(0.8, 0.8, 0.8));
//		scene.getFocalBlur().setBlurLength(4);
//		scene.getFocalBlur().setIntensity(0.4);
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(-0.6, 1.5, 2.0));
		camera.setLookAt(new Point3D(0, 0.3, 0));
		camera.setUpDirection(new Point3D(0, 1, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);

		LightDirected lightDirected = new LightDirected(new Point3D(1, -3, -2), new Color3D(0.5, 0.5, 0.5));
		lightDirected.update();
		scene.getLights().add(lightDirected);
//		
		lightDirected = new LightDirected(new Point3D(5, -2, 0), new Color3D(0.7, 0.7, 0.7));
		lightDirected.update();
		scene.getLights().add(lightDirected);		
		
		
		lightDirected = new LightDirected(new Point3D(-1, -3, -2), new Color3D(0.3, 0.3, 0.3));
		lightDirected.update();
		scene.getLights().add(lightDirected);		

		
		
//		lightDirected = new LightDirected(new Point3D(-1.2, -3, 0.1), new Color3D(0.1, 0.1, 0.1));
//		lightDirected.update();
//		scene.getLights().add(lightDirected);
//		
		LightArea light = new LightArea();
		light.setP0(new Point3D(0, 4, -2));
		light.setP1(new Point3D(0, 4.5, -2));
		light.setP2(new Point3D(0, 4, -2.5));
		light.color = new Point3D(0.9, 0.9, 0.9);
		light.setGridWidth(1);
		light.update(scene);
		//scene.getLights().add(light);
		


		Rectangle rec = new Rectangle(new Point3D(-1.5, 0, 1.5),
				  new Point3D(7, 0, 1.5),
				  new Point3D(-1.5, 0, -7),
				  new Color3D(0.4,0.4,0.4));
		
//		rec.material = new TextureMaterial("images/water.jpg");
//		rec.materialRepeat = true;
//		rec.setMaterialWidth(2.5);
//		rec.setMaterialHeight(2.5);
		//rec.material = new CheckersMaterial();
		rec.material = new TextureMaterial("images/WP_SLI_Badge_CurvedGreenBar_SLI_1600x1200.jpg");
		//rec.bumpMapping = new TextureMaterial("images/travesseiro6.jpg");
		rec.specular = new Point3D();
		//rec.reflectance = 0.3;
		//rec.refraction = 0.5;
		scene.addEntity(rec);
		
		
		Sphere sph = new Sphere(new Point3D(0,0.5,0), 0.3, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.001;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		sph.refraction = 0.6;
		//sph.reflectance = 0.9;
		
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/jupiter-1k.png");
		sph.bumpMapping = new TextureMaterial("images/the_parched_earth_by_autolevels2.jpg");
		sph.bumpIntensity = 0.1;
		sph.bumpIndexTransparency = 0.01;
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(-0.5, 0.5, 0.9), 0.15, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.1;
		sph.refraction = 0.5;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/EarthTM0360.jpg");
		sph.bumpMapping = new TextureMaterial("images/the_parched_earth_by_autolevels2.jpg");
		sph.bumpIndexTransparency = 0.2;
		scene.addEntity(sph);
		
		
		Cylinder cyl = new Cylinder(new Point3D(-0.6, 0, -0.9), new Point3D(0, 1, 0), 0.6, 0.2);
		cyl.shininess = 50;
		cyl.reflectance = 0.6;
		cyl.specular = new Point3D(0.7, 0.7, 0.7);
		//cyl.material = new CheckersMaterial();
		//cyl.refraction = 0.5;
		cyl.material = new TextureMaterial("images/natureza51.jpg");
		//cyl.bumpMapping = new TextureMaterial("images/TOPOMAP1.gif");
		scene.addEntity(cyl);		
		
		
		sph = new Sphere(new Point3D(0.3, 0.8, 0.9), 0.13, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.001;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/verde1.png");
		sph.bumpMapping = new TextureMaterial("images/here-comes-the-skylight-heightmap.png");
		sph.bumpIndexTransparency = 0;
		sph.refraction = 0.5;
		//sph.refraction = 0.5;
		//sph.material = new TextureMaterial("images/verde1.png");
		scene.addEntity(sph);
		
		
		sph = new Sphere(new Point3D(1.1, 0.5, -1), 0.23, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 15;
		//sph.reflectance = 0.1;
		sph.specular = new Point3D(0.8, 0.8, 0.8);
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/fantasy_11.jpg");
		sph.bumpMapping = new TextureMaterial("images/the_parched_earth_by_autolevels2.jpg");
		sph.bumpIndexTransparency = 0.1;
		//sph.bumpIntensity = 0.2;
		sph.refraction = 0.5;
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(-0.3, 0.5, -2.5), 0.23, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.1;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/Egyptwall.jpg");
		sph.bumpMapping = new TextureMaterial("images/egyptwall_bump.jpg");
		//sph.bumpIndexTransparency = 0.1;
		sph.bumpIntensity = 0.2;
		//sph.refraction = 0.5;
		//scene.addEntity(sph);		
		
//		Box box = new Box(new Point3D(0.2, 0, -0.2),
//				  new Point3D(0.2, 0, -0.5),
//				  new Point3D(0.5, 0, -0.2),
//				  new Point3D(0.2, 0.9, -0.2),
//				  new Color3D(0.1,0.3,0.6));
//
//		box.specular = new Point3D(0.9, 0.9, 0.9);
//		box.shininess = 20;
//		box.reflectance = 0.9;
//		//box.materialRepeat = true;
//		box.setMaterial(new TextureMaterial("images/kenny.png"));
//		//box.setMaterial(new CheckersMaterial());
//		box.setMaterialWidth(0.01);
//		box.setMaterialHeight(0.1);		
//		scene.addEntity(box);	
		
		Torus tor = new Torus(new Point3D(0, 0.1, -0.4), 0.2, 0.1, new Point3D(0, 1, 1));
		tor.shininess = 50;
		tor.color = new Color3D(0.0, 0.7, 0.3);
		tor.specular = new Point3D(0.7, 0.7, 0.7);
		tor.material = new TextureMaterial("images/fantasy_11.jpg");
		//tor.bumpMapping = new TextureMaterial("images/the_parched_earth_by_autolevels2.jpg");
		tor.refraction = 1;
		//tor.material = new CheckersMaterial();
		scene.addEntity(tor);
		
		rayTracer.render();
	}
	
	private static void scene7() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		//scene.setBackGroundColor(new Color3D(0.3,0.5,0.8));
		scene.setBackGroundColor(new Color3D(0.0,0.0,0.0));
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(0, 0, 5.5));
		camera.setLookAt(new Point3D(0, 0, 0));
		camera.setUpDirection(new Point3D(-1, 0, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);
		
		LightDirected lightDirected = new LightDirected(new Point3D(0, -1, -1), new Color3D(1, 1, 1));
		lightDirected.update();
		scene.getLights().add(lightDirected);		
		
		Torus tor = new Torus(new Point3D(0, 0, 0), 1, 0.5, new Point3D(0, 1, 1));
		tor.shininess = 50;
		tor.color = new Color3D(0.0, 0.7, 0.3);
		tor.specular = new Point3D(0.7, 0.7, 0.7);
		tor.material = new TextureMaterial("images/fantasy_11.jpg");
		tor.material = new CheckersMaterial();
		scene.addEntity(tor);
		
		rayTracer.render();
	}
	
	private static void scene6() {
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		//scene.setBackGroundColor(new Color3D(0.3,0.5,0.8));
		scene.setBackGroundColor(new Color3D(0.0,0.0,0.0));
		scene.getFog().setZMin(2);
		scene.getFog().setZMax(10);
		scene.getFog().setDensity(0.6);
		scene.getFog().setColor(new Color3D(0.8, 0.8, 0.8));
		//scene.getFocalBlur().setBlurLength(4);
		//scene.getFocalBlur().setIntensity(0.4);
		
		Camera camera = rayTracer.getCamera();
		camera.setResolution(Camera.MED_RESOLUTION);
		camera.setEye(new Point3D(-0.6, 1.1, 2.0));
		camera.setLookAt(new Point3D(0, 0.3, 0));
		camera.setUpDirection(new Point3D(0, 1, 0));
		camera.setScreenWidth(1);
		camera.setScreenDist(1);

		LightDirected lightDirected = new LightDirected(new Point3D(1, -3, -2), new Color3D(0.3, 0.3, 0.3));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		
//		lightDirected = new LightDirected(new Point3D(5, -2, 0), new Color3D(0.3, 0.3, 0.3));
//		lightDirected.update();
//		scene.getLights().add(lightDirected);		
		
		
		lightDirected = new LightDirected(new Point3D(-1, -3, -2), new Color3D(0.3, 0.3, 0.3));
		lightDirected.update();
		scene.getLights().add(lightDirected);		

		/*
		
		lightDirected = new LightDirected(new Point3D(-1.2, -3, 0.1), new Color3D(0.1, 0.1, 0.4));
		lightDirected.update();
		scene.getLights().add(lightDirected);
		*/
		LightArea light = new LightArea();
		light.setP0(new Point3D(0, 4, -2));
		light.setP1(new Point3D(0, 4.5, -2));
		light.setP2(new Point3D(0, 4, -2.5));
		light.color = new Point3D(0.9, 0.9, 0.9);
		light.setGridWidth(2);
		light.update(scene);
		


		Rectangle rec = new Rectangle(new Point3D(-1.5, 0, 1.5),
				  new Point3D(7, 0, 1.5),
				  new Point3D(-1.5, 0, -7),
				  new Color3D(0.4,0.4,0.4));
		
//		rec.material = new TextureMaterial("images/water.jpg");
//		rec.materialRepeat = true;
//		rec.setMaterialWidth(2.5);
//		rec.setMaterialHeight(2.5);
		rec.material = new CheckersMaterial();
		//rec.material = new TextureMaterial("images/natureza51.jpg");
		//rec.bumpMapping = new TextureMaterial("images/travesseiro6.jpg");
		rec.specular = new Point3D();
		rec.reflectance = 0.3;
		//rec.refraction = 0.5;
		scene.addEntity(rec);
		
		
		Sphere sph = new Sphere(new Point3D(0,0.5,0), 0.4, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.001;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		//sph.refraction = 0.6;
		//sph.reflectance = 0.1;
		
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/jupiter-1k.png");
		sph.bumpMapping = new TextureMaterial("images/travesseiro6.jpg");
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(-0.5, 0.5, 0.4), 0.15, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.1;
		//sph.refraction = 0.9;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/EarthTM0360.jpg");
		//sph.bumpMapping = new TextureMaterial("images/hearth_heightmap.gif");
		scene.addEntity(sph);
		
		sph = new Sphere(new Point3D(0.3, 0.8, 0.6), 0.13, new Color3D(0.1, 0.6, 0.3));
		sph.shininess = 50;
		//sph.reflectance = 0.001;
		sph.specular = new Point3D(0.7, 0.7, 0.7);
		//sph.material = new CheckersMaterial();
		sph.material = new TextureMaterial("images/verde1.png");
		//sph.bumpMapping = new TextureMaterial("images/hearth_heightmap.gif");
		//sph.refraction = 0.5;
		//sph.refraction = 1;
		//sph.material = new TextureMaterial("images/verde1.png");
		
		scene.addEntity(sph);	
		
		Cylinder cyl = new Cylinder(new Point3D(-0.6, 0, -0.9), new Point3D(0, 1, 0), 0.6, 0.2);
		cyl.shininess = 50;
		//cyl.reflectance = 0.1;
		cyl.specular = new Point3D(0.7, 0.7, 0.7);
		//cyl.material = new CheckersMaterial();
		//cyl.refraction = 0.1;
		cyl.material = new TextureMaterial("images/texture1.jpg");	
		scene.addEntity(cyl);	
		
		rayTracer.render();
	}	
	
	private static void scene5() {
		RayTracer tracer = SceneLoader.load("./scenes/columns_7_multi_reflect.txt");
		tracer.render();
	}
	
	public static void main(String args[]) {
		scene3();
	}
}
