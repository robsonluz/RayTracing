package br.raytracing.remote;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import br.raytracing.CanvasPaint;

public class RayServerCanvasPaint implements CanvasPaint {
	private ObjectOutputStream out;
	private Dimension dimension;
	private int currentY;
	private int currentLine[];
	
	public RayServerCanvasPaint(OutputStream out, Dimension dimension) throws IOException {
		this.out = new ObjectOutputStream(out);
		this.dimension = dimension;
		
		currentLine = new int[dimension.width];
	}

	@Override
	public void clear() {
		currentLine = new int[dimension.width];
	}

	@Override
	public void paintPixel(int x, int y, int rgb) {
		currentY = y;
		currentLine[x] = rgb;
		if(x >= dimension.width - 1){
			sendToClient();
			currentLine = new int[dimension.width];
		}
	}
	
	private void sendToClient() {
		try{
			out.writeObject(new RemoteRenderedLine(currentY, currentLine));
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean allPixelsRendered() {
		return false;
	}
	
}
