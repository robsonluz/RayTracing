package br.raytracing.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.raytracing.Color3D;
import br.raytracing.FocalBlur;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.RayTracer;
import br.raytracing.VolumetricFog;
import br.raytracing.SceneLoader.Attribute;

public class Scene extends Entity {
	private List<Entity> entitys;
	private Map<String, Entity> mapEntitys;
	private List<Light> lights;
	private Point3D ambientLight = new Point3D(1,1,1);
	private Color3D backGroundColor = new Color3D(0,0,0);
	
	private Entity intersectedEntity;
	private VolumetricFog fog;
	private FocalBlur focalBlur;
	private Camera camera;
	
	private double dispersion = 10;
	private double clearPoint = 0.3;
	
	public Scene(Camera camera) {
		this.camera = camera;
		entitys = new LinkedList<Entity>();
		lights = new LinkedList<Light>();
		fog = new VolumetricFog();
		focalBlur = new FocalBlur();
	}
	
	@Override
	public double intersect(Ray ray) {
		double distance = Double.POSITIVE_INFINITY;
		intersectedEntity = null;
		for(Entity entity: entitys) {
			double entityDistance = entity.intersect(ray);
			if(entityDistance < distance && entityDistance > RayTracer.EPSILON) {
				distance = entityDistance;
				intersectedEntity = entity;
			}
		}
		return distance;
	}
	
	public void loadAllEntityTextures() {
		for(Entity entity: entitys) {
			entity.loadTextures();
		}
	}
	
	public void addEntity(Entity entity) {
		entitys.add(entity);
	}

	public Entity getIntersectedEntity() {
		return intersectedEntity;
	}

	public List<Light> getLights() {
		return lights;
	}

	@Override
	public void init() {
		mapEntitys = new HashMap<String, Entity>();
		focalBlur.init(camera);
		for(Entity entity: entitys) {
			entity.init();
			if(entity.getName()!=null) {
				mapEntitys.put(entity.getName(), entity);
			}
		}
		for(Light light: lights) {
			light.update();
		}
	}

	@Override
	public Color3D getColorAt(Point3D pointOfIntersection) {
		return null;
	}

	@Override
	protected Point3D getInternalNormal(Point3D pointOfIntersection) {
		return null;
	}

	public List<Entity> getEntitys() {
		return entitys;
	}

	public Point3D getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(Point3D ambientLight) {
		this.ambientLight = ambientLight;
	}

	public Color3D getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Color3D backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	@Override
	public double[] getTextureCoords(Point3D point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(Attribute attr) {
		if("background-col".equals(attr.name)) backGroundColor = attr.getColor3D();
		else if("ambient-light".equals(attr.name)) ambientLight = attr.getPoint3D();
		else if("super-samp-width".equals(attr.name)) camera.superSampleWidth = attr.getInt();//Camera
		else if("width".equals(attr.name)) camera.getResolution().width = attr.getInt();
		else if("height".equals(attr.name)) camera.getResolution().height = attr.getInt();
	}

	public VolumetricFog getFog() {
		return fog;
	}

	public void setFog(VolumetricFog fog) {
		this.fog = fog;
	}

	public FocalBlur getFocalBlur() {
		return focalBlur;
	}

	public void setFocalBlur(FocalBlur focalBlur) {
		this.focalBlur = focalBlur;
	}

	public double getDispersion() {
		return dispersion;
	}

	public void setDispersion(double dispersion) {
		this.dispersion = dispersion;
	}

	public double getClearPoint() {
		return clearPoint;
	}

	public void setClearPoint(double clearPoint) {
		this.clearPoint = clearPoint;
	}

	public Camera getCamera() {
		return camera;
	}

	public Map<String, Entity> getMapEntitys() {
		return mapEntitys;
	}
	
	
}
