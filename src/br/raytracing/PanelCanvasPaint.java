package br.raytracing;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import br.raytracing.entity.Camera;

public class PanelCanvasPaint extends Panel implements CanvasPaint {
	private static final long serialVersionUID = 1L;
	private BufferedImage renderImage;

	private boolean lines[];
	private int renderedLines;
	private int maxX;
	private int maxY;
	
	private Camera camera;
	
	private CanvasPaintListener canvasPaintListener;
	private boolean allPixelsRendered = false;
	
	
	private long timeIni = System.currentTimeMillis();
	
//	private int pixels[][];
	
	
	public PanelCanvasPaint(Camera camera) {
		super();
		this.camera = camera;
		renderImage = new BufferedImage(camera.getResolution().width, camera.getResolution().height, BufferedImage.TYPE_INT_RGB);
		lines = new boolean[camera.getResolution().height];
		this.maxX = camera.getResolution().width - 1;
		this.maxY = camera.getResolution().height;
		
//		pixels = new int[camera.getResolution().width][camera.getResolution().height];
	}
	
	@Override
	public void paintPixel(int x, int y, int rgb) {
		//renderImage.set
		
		//pixels[x][y] = rgb;
		renderImage.setRGB(x, y, rgb);
		
		
		//repaint(x, y, 1, 1);

		if(x==camera.getResolution().width-1) {
			repaint(0, y, camera.getResolution().width, 1);
		}
		
		if(x==maxX && !lines[y]) {//Marca a linha renderizada
			renderedLines++;
			lines[y] = true;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(renderImage, 0, 0, null);
	}

	public BufferedImage getRenderImage() {
		return renderImage;
	}

	@Override
	public boolean allPixelsRendered() {
		if(allPixelsRendered)
			return true;
		boolean b = renderedLines >= maxY;
		
		if(b) {
			if(canvasPaintListener!=null) //Dispara evento de Stop
				canvasPaintListener.onStop();
			allPixelsRendered = b;
			
			long end = System.currentTimeMillis() - timeIni;
			System.out.println("Tempo de render: "+end+" ms.");
			repaint();
		}
		return b;
	}
	
	@Override
	public void clear() {
		allPixelsRendered = false;
		lines = new boolean[camera.getResolution().height];
		renderedLines = 0;
	}

	public static interface CanvasPaintListener {
		public void onStop();
	}

	public CanvasPaintListener getCanvasPaintListener() {
		return canvasPaintListener;
	}

	public void setCanvasPaintListener(CanvasPaintListener canvasPaintListener) {
		this.canvasPaintListener = canvasPaintListener;
	}
	
}
