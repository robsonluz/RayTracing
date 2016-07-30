package br.raytracing.remote;

import java.io.ObjectInputStream;

import br.raytracing.CanvasPaint;

public class RayClientWatch extends Thread {
	private ObjectInputStream in;
	private CanvasPaint canvas;

	public RayClientWatch(ObjectInputStream in, CanvasPaint canvas) {
		super();
		this.in = in;
	}

	@Override
	public void run() {
		try{
			while(true) {
				RemoteRenderedLine line = (RemoteRenderedLine) in.readObject();
				for(int i=0;i<line.getX().length;i++) {
					canvas.paintPixel(i, line.getY(), line.getX()[i]);
				}
			}
		}catch(Exception e) {
		}
	}
	
	
}
