package br.raytracing;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.MediaLocator;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import br.raytracing.entity.Camera;
import br.raytracing.entity.Scene;
import br.raytracing.remote.RayClient;
import br.raytracing.util.ImagesToMovie;


public class Main {
	public static void renderScene(String txtScene, boolean stereo) {
		
	
		
		final RayTracer tracer = SceneLoader.load(txtScene);
		
		
		
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(out);
			long ini = System.currentTimeMillis();
			o.writeObject(tracer.getScene());
			System.out.println(System.currentTimeMillis()-ini);
			//tracer.getScene()
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame();
		final PanelCanvasPaint canvas = new PanelCanvasPaint(tracer.getCamera());
		final BufferedImage renderImage = canvas.getRenderImage();
		Panel pnl = new Panel();
		pnl.setLayout(new BorderLayout());
		JButton cmdSalvar = new JButton("Salvar");
		cmdSalvar.setEnabled(true);
		cmdSalvar.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					JFileChooser file = new JFileChooser();
					int ret = file.showSaveDialog(canvas);
					if(ret == JFileChooser.APPROVE_OPTION) {
						ImageIO.write(renderImage, "PNG", file.getSelectedFile());
					}
				}catch(Exception ex) {
				}

			}
		
		});
		pnl.add(cmdSalvar, BorderLayout.PAGE_START);
		pnl.add(canvas, BorderLayout.CENTER);
		frame.setSize(tracer.getCamera().getResolution().width+100, tracer.getCamera().getResolution().height+150);
		frame.setContentPane(pnl);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

		RenderDistribution renderDistribution = new RenderDistribution(tracer.getCamera().getResolution().height);
		
		if(tracer.getAnimation()!=null) {
			tracer.getAnimation().init();
			int frm = 1;
			Vector<String> inputFiles = new Vector<String>();
			while(tracer.getAnimation().hasMoreFrames()) {
				System.out.println("Frame: "+(frm++));
				canvas.clear();
				
				tracer.getCamera().reset();
				tracer.getScene().init();
				
				renderOnServer(tracer.getScene(), canvas);
				tracer.render(canvas, 0, -1, false);

				
				
				if(stereo) {
					BufferedImage imgStereo = new BufferedImage(tracer.getCamera().getResolution().width, tracer.getCamera().getResolution().height * 2, BufferedImage.TYPE_INT_RGB);					
					imgStereo.getGraphics().drawImage(renderImage, 0, 0, null);
					
					//Muda a posição do olho
					tracer.getCamera().getEye().x += 0.2;
					canvas.clear();
					tracer.getCamera().reset();
					tracer.getScene().init();
					tracer.render(canvas, 0, -1, false);
					
					imgStereo.getGraphics().drawImage(renderImage, 0, tracer.getCamera().getResolution().height, null);
					
					tracer.getCamera().getEye().x -= 0.2;

					try{
						String f = "./temp/a_"+frm+".jpg";
						ImageIO.write(imgStereo, "JPG", new File(f));
						inputFiles.add(f);
					}catch(Exception e){
						e.printStackTrace();
					}

				}else{
				
					try{
						String f = "./temp/a_"+frm+".jpg";
						ImageIO.write(renderImage, "JPG", new File(f));
						inputFiles.add(f);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				tracer.getAnimation().applyTransformations(tracer.getScene());				
			}
			ImagesToMovie imageToMovie = new ImagesToMovie();
			MediaLocator oml = new MediaLocator("file:./temp/movie.mov");			
			imageToMovie.doIt(tracer.getCamera().getResolution().width, tracer.getCamera().getResolution().height*2, tracer.getAnimation().getFramesSecond(), inputFiles, oml);
			
			//Remove os arquivos
			for(String f: inputFiles) {
				File ff = new File(f);
				if(ff.isFile() && ff.exists()) {
					ff.delete();
				}
			}
		}else{
			
			try{
				
				tracer.getCamera().reset();
				tracer.getScene().init();				
				
				//Modelo sem thread
//				renderOnServer(tracer.getScene(), canvas);
//				tracer.render(canvas, 0, -1, false);
				
				//Modelo com threads
				int numThreads = 10;
				tracer.getCamera().reset();
				tracer.getScene().init();
				for(int i=0;i<numThreads;i++) {
					new Thread(new RenderThread(tracer, canvas, renderDistribution)).start();
				}
				
				
				if(stereo) {
					BufferedImage imgStereo = new BufferedImage(tracer.getCamera().getResolution().width, tracer.getCamera().getResolution().height * 2, BufferedImage.TYPE_INT_RGB);
					imgStereo.getGraphics().drawImage(renderImage, 0, 0, null);
					
					//Muda a posição do olho
					tracer.getCamera().getEye().x += 0.2;
					canvas.clear();
					tracer.getCamera().reset();
					tracer.getScene().init();
					tracer.render(canvas, 0, -1, false);
					
					imgStereo.getGraphics().drawImage(renderImage, 0, tracer.getCamera().getResolution().height, null);
					
					ImageIO.write(imgStereo, "JPG", new File("./temp/img_l.jpg"));
				}
				//tracer.render(canvas, 0, -1, false);
			}catch(Exception e) {
			}
			//tracer.render(canvas, 0, -1, false);
		}
		
		
	}
	
	private static void renderOnServer(final Scene scene, final PanelCanvasPaint canvas) {
		final RayClient rayClient = new RayClient("192.168.1.103", 6543);
		//final RayClient rayClient = new RayClient("localhost", 6543);
		canvas.setCanvasPaintListener(new PanelCanvasPaint.CanvasPaintListener() {
			@Override
			public void onStop() {
				rayClient.stop();
			}
		});	
		new Thread() {
			public void run() {
				rayClient.renderOnServer(scene, canvas, 0, true);
			}
		}.start();
	}
	
	public static void main(String args[]) {
		
		renderScene("./scenes/teste_bump_transp.txt", false);
		//renderScene("./scenes/teste_movie.txt", false);
		
		//renderScene("./scenes/spheres_attenuation_1.txt", false);
		
	}
}
