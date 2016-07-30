package br.raytracing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.raytracing.animation.Animation;
import br.raytracing.animation.Transform;
import br.raytracing.entity.Box;
import br.raytracing.entity.Camera;
import br.raytracing.entity.Cylinder;
import br.raytracing.entity.Entity;
import br.raytracing.entity.LightArea;
import br.raytracing.entity.LightDirected;
import br.raytracing.entity.LightPoint;
import br.raytracing.entity.Rectangle;
import br.raytracing.entity.Scene;
import br.raytracing.entity.Sphere;

public class SceneLoader {
	public static RayTracer loadFromText(String text) {
		try{
			ByteArrayInputStream bin = new ByteArrayInputStream(text.getBytes());
			Scanner reader = new Scanner(bin);
			return load(reader);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static RayTracer load(String filename) {
		Scanner reader = null;
		try{
			reader = new Scanner(new File(filename));
			return load(reader);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static RayTracer load(Scanner reader) {

		
		List<Element> elements = new ArrayList<Element>();
		Element currentElement=null;
		while(reader.hasNextLine()) {
			String line = reader.nextLine().trim();
			if(line.length()==0 || line.startsWith("#"))
				continue;
			if(line.endsWith(":")) {
				if(currentElement!=null)
					elements.add(currentElement);
				currentElement = new Element();
				currentElement.type = line.substring(0, line.indexOf(":"));
			}else if(currentElement!=null){
				currentElement.attrs.add(new Attribute(line));
			}
		}
		if(currentElement!=null)
			elements.add(currentElement);
		
		RayTracer rayTracer = new RayTracer();
		Scene scene = rayTracer.getScene();
		Camera camera = rayTracer.getCamera();
		for(Element e: elements) {
			if("scene".equals(e.type)) {
				scene.parserAttributes(e.attrs);
			}else if("camera".equals(e.type)) {
				camera.parserAttributes(e.attrs);
			}else if("cylinder".equals(e.type)) {
				addEntity(scene, new Cylinder(), e);
			}else if("sphere".equals(e.type)) {
				addEntity(scene, new Sphere(), e);
			}else if("box".equals(e.type)) {
				addEntity(scene, new Box(), e);
			}else if("rectangle".equals(e.type)) {
				addEntity(scene, new Rectangle(), e);
			}else if("light-directed".equals(e.type)) {
				LightDirected lightDirected = new LightDirected();
				lightDirected.parserAttributes(e.attrs);
				lightDirected.update();
				scene.getLights().add(lightDirected);
			}else if("light-point".equals(e.type)) {
				LightPoint lightPoint = new LightPoint();
				lightPoint.parserAttributes(e.attrs);
				scene.getLights().add(lightPoint);		
			}else if("light-area".equals(e.type)) {
				LightArea lightArea = new LightArea();
				lightArea.parserAttributes(e.attrs);
				lightArea.update(scene);
			}else if("fog".equals(e.type)) {
				scene.getFog().parserAttributes(e.attrs);
			}else if("animation".equals(e.type)) {
				Animation anim = new Animation();
				anim.setScene(scene);
				anim.parserAttributes(e.attrs);
				rayTracer.setAnimation(anim);
			}else if("transform".equals(e.type)) {
				Transform tr = new Transform();
				tr.parserAttributes(e.attrs);
				rayTracer.getAnimation().getTransforms().add(tr);
			}else{
				//throw new RuntimeException("Unknow type: "+e.type);
			}
		}
		
		return rayTracer;
	}
	private static void addEntity(Scene scene, Entity entity, Element e) {
		entity.parserAttributes(e.attrs);
		scene.addEntity(entity);
	}
	
	private static class Element {
		String type;
		List<Attribute> attrs = new ArrayList<Attribute>();
	}
	
	public static class Attribute {
		public String name;
		public String value;
		public Attribute(String line) {
			int i = line.indexOf("=");
			name = line.substring(0, i).trim();
			value = line.substring(i+1, line.length()).trim();
			/*
			System.out.println("-------");
			System.out.println(name);
			System.out.println(value);
			System.out.println("-------\r\n");
			*/
		}
		
		public int[] getIntArray() {
			String v[] = value.split(" ");
			int[] ret = new int[v.length];
			for(int i=0;i<v.length;i++) {
				ret[i] = new Integer(v[i]);
			}
			return ret;
		}
		
		public Color3D getColor3D() {
			String v[] = value.split(" ");
			if(v.length==3) {
				return new Color3D(new Double(v[0]), new Double(v[1]), new Double(v[2]));
			}
			throw new RuntimeException("Wrong format for attribute: "+name+", value: "+value);
		}
		
		public Point3D getPoint3D() {
			String v[] = value.split(" ");
			if(v.length==3) {
				return new Point3D(new Double(v[0]), new Double(v[1]), new Double(v[2]));
			}
			throw new RuntimeException("Wrong format for attribute: "+name+", value: "+value);
		}
		
		public double getDouble() {
			try{
				return new Double(value);
			}catch(Exception e){
				throw new RuntimeException("Wrong format for attribute: "+name+", value: "+value);
			}
		}
		
		public String getString() {
			return value;
		}

		public int getInt() {
			try{
				return new Integer(value);
			}catch(Exception e){
				throw new RuntimeException("Wrong format for attribute: "+name+", value: "+value);
			}
		}

		public boolean getBoolean() {
			try{
				return new Boolean(value);
			}catch(Exception e){
				throw new RuntimeException("Wrong format for attribute: "+name+", value: "+value);
			}
		}
	}
}
