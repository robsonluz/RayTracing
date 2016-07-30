package br.raytracing;

import java.io.Serializable;

public abstract class Material implements Serializable {
	public abstract Color3D getColor(double[] point2D);
}
