package br.raytracing;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import br.raytracing.RayTracer.PanelCanvas;
import br.raytracing.entity.Camera;

public class FocalBlur implements Serializable {
	private int blurLength = 0;
	private double intensity = 0.4;
	private int quantityBlur = 1;
	
	//Variáveis calculadas
	private int centerX;
	private int centerY;
	private int blurDistance;
	private int div;
	
	
	public FocalBlur() {
		super();
	}

	public void init(Camera camera) {
		if(blurLength > 0) {
			centerX = camera.getResolution().width / 2;
			centerY = camera.getResolution().height / 2;
			int maxDistanceToCenter = calcDistance(0, 0, centerX, centerY);
			
			blurDistance = (int) (((float)maxDistanceToCenter) * intensity);
			div = blurDistance / blurLength; 
			blurDistance = maxDistanceToCenter - blurDistance;
		}
	}
	
	private int getBlurIntensity(int x, int y) {
		int distance = calcDistance(x, y, centerX, centerY);
		
		if(distance > blurDistance) {
			int ret = ((distance - blurDistance) / div) + 1;
			return ret;
		}
		return 0;
	}
	
	private static int calcDistance(int x1, int y1, int x2, int y2) {
		return Math.abs((int) Math.sqrt( ((x2 - x1)*(x2 - x1)) + ((y2 - y1)*(y2 - y1))));
	}
	
	public void applyBlur(int xini, int yini, int xend, int yend, Color3D image[][], BufferedImage renderImage, PanelCanvas canvas) {
		if(blurLength > 0) {
			for(int q=0;q<quantityBlur;q++) {
				for (int y = yini+blurLength+1; y <= yend-blurLength-1; y++) {
					for (int x = xini+blurLength+1; x <= xend-blurLength-1; x++) {
						int intensity = getBlurIntensity(x, y);
						for(int a=1;a<=intensity;a++) {
							image[x][y].x = (image[x][y].x +
										 image[x+a][y].x +
										 image[x-a][y].x +
										 image[x][y+a].x +
										 image[x][y-a].x) / 5;
			
							image[x][y].y = (image[x][y].y +
									 image[x+a][y].y +
									 image[x-a][y].y +
									 image[x][y+a].y +
									 image[x][y-a].y) / 5;
							
							image[x][y].z = (image[x][y].z +
									 image[x+a][y].z +
									 image[x-a][y].z +
									 image[x][y+a].z +
									 image[x][y-a].z) / 5;
						}
						renderImage.setRGB(x, y, image[x][y].getColor().getRGB());
						canvas.repaint(x, y, 1, 1);
					}
				}
			}
		}
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		if(intensity > 1)
			intensity = 1;
		this.intensity = intensity;
	}

	public int getBlurLength() {
		return blurLength;
	}

	public void setBlurLength(int blurLength) {
		this.blurLength = blurLength;
	}

	public int getQuantityBlur() {
		return quantityBlur;
	}

	public void setQuantityBlur(int quantityBlur) {
		this.quantityBlur = quantityBlur;
	}
}
