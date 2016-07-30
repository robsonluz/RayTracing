package br.raytracing.remote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import br.raytracing.RayTracer;
import br.raytracing.SceneLoader;

public class RayServerRender extends Thread {
	private Socket client;
	

	public RayServerRender(Socket client) {
		super();
		this.client = client;
		System.out.println("Client accept: "+client);
	}

	@Override
	public void run() {
		try{
			/*InputStream in = client.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			
			//Le o yStart;
			int yStartT = new Integer(bin.readLine());
			
			boolean inverse = new Boolean(bin.readLine());
			
			
			String sceneText = loadScene(bin);
			*/
			
			ObjectInputStream oin = new ObjectInputStream(client.getInputStream());
			RemoteScene remoteScene = (RemoteScene) oin.readObject();
			
			int yStartT = remoteScene.getYStart();
			boolean inverse = remoteScene.isInverse();
			
			remoteScene.getScene().loadAllEntityTextures();
			RayTracer rayTracer = new RayTracer(remoteScene.getScene(), remoteScene.getScene().getCamera());
			RayServerCanvasPaint canvas = new RayServerCanvasPaint(client.getOutputStream(), rayTracer.getCamera().getResolution());
			rayTracer.render(canvas, yStartT, -1, inverse);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String loadScene(BufferedReader bin) throws Exception{
		StringBuffer sb = new StringBuffer();
		String line;
		while(!"__end_of_scene".equals((line = bin.readLine()))) {
			System.out.println(line);
			sb.append(line);
			sb.append("\r\n");
		}
		return sb.toString();
		
	}
	
	
}
