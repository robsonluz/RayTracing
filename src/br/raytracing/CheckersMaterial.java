package br.raytracing;

public class CheckersMaterial extends Material {

	private double checkersSize = 0.1F;
	private Color3D checkersDiffuse1 = new Color3D( 1.0F, 1.0F, 1.0F );
	private Color3D checkersDiffuse2 = new Color3D( 0.1F, 0.1F, 0.1F );		
	
	@Override
	public Color3D getColor(double[] point2D) {
		 double checkersX = Math.abs(Math.floor(point2D[0] / checkersSize) % 2);
		 double checkersY = Math.abs(Math.floor(point2D[1] / checkersSize) % 2);
		 
		 if (checkersX == 0 && checkersY == 0) return checkersDiffuse2;
		 if (checkersX == 0 && checkersY == 1) return checkersDiffuse1;
		 if (checkersX == 1 && checkersY == 0) return checkersDiffuse1;
		 if (checkersX == 1 && checkersY == 1) return checkersDiffuse2;
		 
		 return null;
	}

	public double getCheckersSize() {
		return checkersSize;
	}

	public void setCheckersSize(double checkersSize) {
		this.checkersSize = checkersSize;
	}

	public Color3D getCheckersDiffuse1() {
		return checkersDiffuse1;
	}

	public void setCheckersDiffuse1(Color3D checkersDiffuse1) {
		this.checkersDiffuse1 = checkersDiffuse1;
	}

	public Color3D getCheckersDiffuse2() {
		return checkersDiffuse2;
	}

	public void setCheckersDiffuse2(Color3D checkersDiffuse2) {
		this.checkersDiffuse2 = checkersDiffuse2;
	}
	
}
