package br.raytracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderDistribution {
	private int numLines;
	private boolean reverse;
	private int current;
	
	private List<Integer> lines;
	
	
	public RenderDistribution(int numLines) {
		super();
		current = 0;
		this.numLines = numLines;
		lines = new ArrayList<Integer>(numLines);
		for(int i=0;i<numLines;i+=4) {
			System.out.println(i);
			lines.add(i);
		}
		for(int i=1;i<numLines;i+=4) {
			lines.add(i);
		}
		for(int i=2;i<numLines;i+=4) {
			lines.add(i);
		}
		for(int i=3;i<numLines;i+=4) {
			lines.add(i);
		}	
		//System.exit(0);
		for(int i=0;i<numLines;i++) {
			System.out.println(lines.get(i));
		}
		//System.exit(0);
		//Collections.shuffle(lines);
	}



	public synchronized int getNextLineToRender() {
		return current==numLines ? -1 : lines.get(current++);
		//return current==numLines ? -1 : current++;
	}
}
