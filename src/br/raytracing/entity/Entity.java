package br.raytracing.entity;

import java.io.Serializable;
import java.util.List;

import br.raytracing.CheckersMaterial;
import br.raytracing.Color3D;
import br.raytracing.Material;
import br.raytracing.Perlim;
import br.raytracing.Point3D;
import br.raytracing.Ray;
import br.raytracing.TextureMaterial;
import br.raytracing.SceneLoader.Attribute;


/**
 * 
 * @author Robson J. P.
 * @since 1.0
 */
public abstract class Entity implements Serializable {
	public double shininess = 0;
	public Point3D specular = new Point3D(1.0F, 1.0F, 1.0F);
	public double reflectance = 0f;
	public double refraction = 0f;
	public double refractionIndex = 10;
	public Point3D ambient = new Point3D(0.1F, 0.1F, 0.1F);
	public Material material;
	public boolean materialRepeat;
	private double materialWidth = 1;
	private double materialHeight = 1;
	public Color3D color = new Color3D(0.8F, 0.8F, 0.8F);
	
	public TextureMaterial bumpMapping;
	public double bumpIntensity = 0.1;
	public double bumpIndexTransparency = -1;
	private String name;
	

	public final void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("name".equals(attr.name)) name = attr.getString();
			else if("mtl-diffuse".equals(attr.name)) color = attr.getColor3D();
			else if("mtl-specular".equals(attr.name)) specular = attr.getColor3D();
			else if("mtl-ambient".equals(attr.name)) ambient = attr.getColor3D();
			else if("mtl-shininess".equals(attr.name)) shininess = attr.getDouble();
			else if("mtl-reflectance".equals(attr.name)) reflectance = attr.getDouble();
			else if("mtl-refraction".equals(attr.name)) refraction = attr.getDouble();
			else if("mtl-refractionIndex".equals(attr.name)) refractionIndex = attr.getDouble();
			else if("mtl-materialWidth".equals(attr.name)) materialWidth = attr.getDouble();
			else if("mtl-materialHeight".equals(attr.name)) materialHeight = attr.getDouble();
			else if("mtl-repeat".equals(attr.name)) materialRepeat = attr.getBoolean();
			else if("mtl-bumpMapping".equals(attr.name)) bumpMapping = new TextureMaterial(attr.getString());
			else if("mtl-bumpIntensity".equals(attr.name)) bumpIntensity = attr.getDouble();
			else if("mtl-bumpIndexTransparency".equals(attr.name)) bumpIndexTransparency = attr.getDouble();
			
			else if("mtl-type".equals(attr.name)){
				if("checkers".equals(attr.getString())) {
					setMaterial(new CheckersMaterial());
				}
			}else if("mtl-texture".equals(attr.name)) 
				setMaterial(new TextureMaterial(attr.getString()));
			else setParameter(attr);
		}
	}
	
	public void init(){
	}
	
	public void loadTextures() {
		if(bumpMapping!=null) {
			bumpMapping.loadTexture();
		}
		if(material instanceof TextureMaterial) {
			((TextureMaterial) material).loadTexture();
		}
	}
	
	public abstract double intersect(Ray ray);
	
	public Color3D getColorAt(Point3D pointOfIntersection) {
		Color3D c = material!=null? material.getColor(getTextureCoords(pointOfIntersection)) : null;
		return c!=null? c : color;
	}
	public abstract double[] getTextureCoords(Point3D point);
	
	public final Point3D getNormal(Point3D pointOfIntersection) {
		Point3D normal = getInternalNormal(pointOfIntersection);
		if(bumpMapping!=null && bumpIntensity > 0) {
			double coords[] = getTextureCoords(pointOfIntersection);
			Color3D bumpColor = bumpMapping.getColor(coords);
			//if(bumpColor.x!=0 || bumpColor.y!=0 || bumpColor.z!=0) {
				//double d = bumpColor.x;
				/*normal.addScalar(d);*/
				
				
				double intensity = bumpIntensity;
				double d = bumpColor.x;
				
				double noiseCoefx = Perlim.getInstance().noise(intensity * pointOfIntersection.x, intensity * pointOfIntersection.y, intensity * pointOfIntersection.z);
				double noiseCoefy = Perlim.getInstance().noise(intensity * pointOfIntersection.y, intensity * pointOfIntersection.z, intensity * pointOfIntersection.x);
				double noiseCoefz = Perlim.getInstance().noise(intensity * pointOfIntersection.z, intensity * pointOfIntersection.x, intensity * pointOfIntersection.y);
				
				normal.x = (1.0f - d ) * normal.x + d * noiseCoefx;				
				normal.y = (1.0f - d ) * normal.y + d * noiseCoefy;
				normal.z = (1.0f - d ) * normal.z + d * noiseCoefz;
				
				
				double temp = normal.dot(normal);
				if(temp!=0) {
					temp = 1.0f / Math.sqrt(temp);
					normal.multiplyVectorByScalar(temp);
				}
				
				
				//normal.addScalar(bumpColor.x);
				
			//}
		}		
		normal.normalize();
		return normal;
	}
	
	protected abstract Point3D getInternalNormal(Point3D pointOfIntersection);
	
	
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public double getMaterialWidth() {
		return materialWidth;
	}
	public void setMaterialWidth(double materialWidth) {
		if(materialWidth > 0)
			this.materialWidth = materialWidth;
	}
	public double getMaterialHeight() {
		return materialHeight;
	}
	public void setMaterialHeight(double materialHeight) {
		if(materialHeight > 0)
			this.materialHeight = materialHeight;
	}

	public void setParameter(Attribute attr){}

	public TextureMaterial getBumpMapping() {
		return bumpMapping;
	}

	public void setBumpMapping(TextureMaterial bumpMapping) {
		this.bumpMapping = bumpMapping;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

