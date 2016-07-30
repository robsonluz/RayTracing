package br.raytracing.animation;

import java.io.Serializable;
import java.util.List;

import br.raytracing.Point3D;
import br.raytracing.SceneLoader.Attribute;
import br.raytracing.entity.Camera;
import br.raytracing.entity.Entity;
import br.raytracing.entity.Light;
import br.raytracing.entity.LightDirected;
import br.raytracing.entity.Sphere;

public class Transform implements Serializable {
	private Point3D translate;
	private Point3D translateRelative;
	
	private int intervalIni;
	private int intervalEnd;
	private Animation animation;
	private String entityName;

	public void init(Animation animation) {
		this.animation = animation;
		translateRelative = new Point3D(translate);
		translateRelative.divideByScalar((double) ((intervalEnd - intervalIni)*animation.getFramesSecond()));
	}

	public final void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("translate".equals(attr.name)) translate = attr.getPoint3D();
			else if("name".equals(attr.name)) entityName = attr.getString();
			else if("interval".equals(attr.name)) {
				int a[] = attr.getIntArray();
				intervalIni = a[0];
				intervalEnd = a[1];
			}
		}
	}
	
	public void applyTransform(int frame, int currentSecond) {
		if(currentSecond>=intervalIni && currentSecond<=intervalEnd) {
			System.out.println("Second: "+currentSecond);
			if("camera".equals(entityName)){
				Camera c = animation.getScene().getCamera();
				c.getPosition().addVector(translateRelative);
				c.getLookAt().addVector(translateRelative);
			}else if("lights".equals(entityName)){
				for(Light light: animation.getScene().getLights()) {
					if(light instanceof LightDirected) {
						((LightDirected) light).direction.addVector(translateRelative);
					}
				}
			}else{
				Entity entity = animation.getScene().getMapEntitys().get(entityName);
				if(entity!=null) {
					if(entity instanceof Sphere) {
						((Sphere) entity).center.addVector(translateRelative);
					}
				}
			}
		}
	}
	
	public Point3D getTranslate() {
		return translate;
	}
	public void setTranslate(Point3D translate) {
		this.translate = translate;
	}
	public int getIntervalIni() {
		return intervalIni;
	}
	public void setIntervalIni(int intervalIni) {
		this.intervalIni = intervalIni;
	}
	public int getIntervalEnd() {
		return intervalEnd;
	}
	public void setIntervalEnd(int intervalEnd) {
		this.intervalEnd = intervalEnd;
	}
	
	
}
