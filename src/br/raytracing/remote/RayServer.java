package br.raytracing.remote;

import java.net.ServerSocket;

public class RayServer {
	private int port;
	
	public RayServer(int port) {
		super();
		this.port = port;
	}
	
	public void start() {
		try{
			ServerSocket server = new ServerSocket(port);
			System.out.println("Escutando na porta: "+port);
			while(true) {
				new RayServerRender(server.accept()).start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new RayServer(6543).start();
	}
}
