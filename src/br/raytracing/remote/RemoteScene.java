package br.raytracing.remote;

import java.io.Serializable;

import br.raytracing.entity.Scene;

public class RemoteScene implements Serializable {
	private Scene scene;
	private int yStart;
	private boolean inverse;
	
	public RemoteScene(Scene scene, int start, boolean inverse) {
		super();
		this.scene = scene;
		yStart = start;
		this.inverse = inverse;
	}
	
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public int getYStart() {
		return yStart;
	}
	public void setYStart(int start) {
		yStart = start;
	}
	public boolean isInverse() {
		return inverse;
	}
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
	
}
