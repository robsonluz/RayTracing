package br.raytracing.remote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import br.raytracing.CanvasPaint;
import br.raytracing.entity.Scene;

public class RayClient {
	private String serverHost;
	private int serverPort;
	private Boolean isToRun = true;
	
	public RayClient(String serverHost, int serverPort) {
		super();
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}
	
	public void renderOnServer(/*String sceneFileName,*/final Scene scene, final CanvasPaint canvasPaint, final int yStart, final boolean inverse) {

				try{
					Socket server = new Socket(serverHost, serverPort);
					ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
					out.writeObject(new RemoteScene(scene, yStart, inverse));
					out.flush();
					/*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
					
					writer.write(String.valueOf(yStart));//yStart
					writer.write("\r\n");
					writer.flush();
					
					writer.write(String.valueOf(inverse));//inverse
					writer.write("\r\n");
					writer.flush();
					
					
					Scanner sc = new Scanner(new File(sceneFileName));
					while(sc.hasNextLine()) {
						writer.write(sc.nextLine());
						writer.write("\r\n");
						writer.flush();
					}
					writer.write("__end_of_scene");
					writer.write("\r\n");
					writer.flush();
					*/
					
					ObjectInputStream in = new ObjectInputStream(server.getInputStream());
					while(true) {
						synchronized (isToRun) {
							if(isToRun) {
								RemoteRenderedLine line = (RemoteRenderedLine) in.readObject();
								for(int i=0;i<line.getX().length;i++) {
									canvasPaint.paintPixel(i, line.getY(), line.getX()[i]);
								}
							}else{
								break;
							}
						}
					}
					server.close();
					System.out.println("Fim server");
					
					//RayClientWatch watch = new RayClientWatch(new ObjectInputStream(server.getInputStream()), canvasPaint);
					//watch.start();
	
				}catch(Exception e) {
				}			
//			}
//		}.start();
	
	}
	
	public void stop() {
		this.isToRun = false;
	}
}
