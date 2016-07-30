package br.raytracing;

import java.util.Vector;

import javax.media.MediaLocator;

import br.raytracing.util.ImagesToMovie;

public class StereoVideoGenerate {
	public static void main(String args[]) {
		Vector<String> inputFiles = new Vector<String>();
		for(int i=0;i<100;i++) {
			inputFiles.add("./temp/stereo_2.jpg");
		}
		
		ImagesToMovie imageToMovie = new ImagesToMovie();
		MediaLocator oml = new MediaLocator("file:./temp/movie_stereo2.mpeg");			
		imageToMovie.doIt(800, 1200, 10, inputFiles, oml);
	}
}
