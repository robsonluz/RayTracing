package br.raytracing;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;



public class TextureMaterial extends Material {

	private String textureFileName;
	private int textureWidth;
	private int textureHeight;
	private transient Color3D[][] texture;
	
	public TextureMaterial(String textureFileName) {
		this.textureFileName = textureFileName;
		loadTexture();
	}
	
	@Override
	public Color3D getColor(double[] point2D) {
		if(texture==null)
			return null;
		
		int textureX = Math.abs((int)Math.round(point2D[0] * textureWidth)) % textureWidth; 
		int textureY = Math.abs((int)Math.round(point2D[1] * textureHeight)) % textureHeight;
		
//		if(textureX >= textureWidth)
//			textureX = textureWidth - 1;
//		
//		if(textureY >= textureHeight)
//			textureY = textureHeight - 1;	
//		
//		if(textureX==463) {
//			System.out.println("l");
//		}
		
		return texture[textureX][textureY];		
	}
	
	
	private BufferedImage loadImage(String ref) {   
        BufferedImage bimg = null;   
        try {   
  
            bimg = ImageIO.read(new File(ref));   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        return bimg;   
    }  
	
	// Loads a texture into a matrix (3rd dimension values are in [0, 1])
	public void loadTexture() {	
		BufferedImage img = loadImage(textureFileName);
		if(img!=null) {
			textureWidth = img.getWidth();
			textureHeight = img.getHeight();
			texture = new Color3D[textureWidth][textureHeight];
			
			for(int i=0;i<textureWidth;i++) {
				for(int j=0;j<textureHeight;j++) {
					int rgb = img.getRGB(i, j);
					texture[i][j] = new Color3D(rgb);
				}
			}
			
		}
	}	
	
}
