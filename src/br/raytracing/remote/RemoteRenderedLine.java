package br.raytracing.remote;

import java.io.Serializable;

public class RemoteRenderedLine implements Serializable {
	private static final long serialVersionUID = 1L;
	private int y;
	private int x[];
	
	public RemoteRenderedLine(int y, int[] x) {
		super();
		this.y = y;
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int[] getX() {
		return x;
	}
	public void setX(int[] x) {
		this.x = x;
	}
	
}
