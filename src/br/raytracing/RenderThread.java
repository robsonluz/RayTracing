package br.raytracing;

public class RenderThread implements Runnable {
	private RayTracer tracer;
	private CanvasPaint canvasPaint;
	private RenderDistribution renderDistribution;
	
	public RenderThread(RayTracer tracer, CanvasPaint canvasPaint, RenderDistribution renderDistribution) {
		super();
		this.tracer = tracer;
		this.canvasPaint = canvasPaint;
		this.renderDistribution = renderDistribution;
	}


	@Override
	public void run() {
		while(true) {
			int y  = renderDistribution.getNextLineToRender();
			if(y==-1)
				break;
			//System.out.println(y);
			tracer.render(canvasPaint, y, y, false);
			
		}
		
	}
	
}
