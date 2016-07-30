package br.raytracing.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.raytracing.SceneLoader.Attribute;
import br.raytracing.entity.Scene;

public class Animation implements Serializable {
	private int framesSecond;
	private int time;
	
	private int framesToRender;
	private int currentFrame=1;
	
	private List<Transform> transforms = new ArrayList<Transform>();
	private Scene scene;

	public void init() {
		framesToRender = framesSecond*time;
		currentFrame = 1;
		for(Transform tr: transforms) {
			tr.init(this);
		}
	}
	
	public final void parserAttributes(List<Attribute> attrs){
		for(Attribute attr: attrs) {
			if("frames-per-second".equals(attr.name)) framesSecond = attr.getInt();
			else if("time".equals(attr.name)) time = attr.getInt();
		}
	}
	
	public boolean hasMoreFrames() {
		return currentFrame<=framesToRender;
	}
	
	public void applyTransformations(Scene scene) {
		if(hasMoreFrames()) {
			for(Transform tr: transforms) {
				tr.applyTransform(currentFrame, currentFrame/framesSecond);
			}
			currentFrame++;
		}
	}

	public List<Transform> getTransforms() {
		return transforms;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public int getFramesToRender() {
		return framesToRender;
	}

	public int getFramesSecond() {
		return framesSecond;
	}
	
}
