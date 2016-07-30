package br.raytracing;

public interface CanvasPaint {
	public void paintPixel(int x, int y, int rgb);
	public boolean allPixelsRendered();
	public void clear();
}
