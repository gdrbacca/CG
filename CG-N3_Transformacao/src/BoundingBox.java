import java.util.ArrayList;

import javax.media.opengl.GL;

public final class BoundingBox {
	private double menorX;
	private double menorY;
	private double menorZ;
	private double maiorX;
	private double maiorY;
	private double maiorZ;
	private Ponto4D centro = new Ponto4D();
//    private Color color;


	public BoundingBox() {
		this(0, 0, 0, 0, 0, 0);
	}
	
	public BoundingBox(double smallerX, double smallerY, double smallerZ, double greaterX, double greaterY, double greaterZ) {
		this.menorX = smallerX;
		this.menorY = smallerY;
		this.menorZ = smallerZ;
		this.maiorX = greaterX;
		this.maiorY = greaterY;
		this.maiorZ = greaterZ;
	}
	
	public void atribuirBoundingBox(double smallerX, double smallerY, double smallerZ, double greaterX, double greaterY, double greaterZ) {
		this.menorX = smallerX;
		this.menorY = smallerY;
		this.menorZ = smallerZ;
		this.maiorX = greaterX;
		this.maiorY = greaterY;
		this.maiorZ = greaterZ;
		processarCentroBBox();
	}
		
	public void atribuirBoundingBox(ArrayList<Ponto4D> arrayPtos){
		System.err.println(arrayPtos.size());
		this.menorX = arrayPtos.get(0).obterX();
		this.menorY = arrayPtos.get(0).obterY();
		this.maiorX = arrayPtos.get(0).obterX();
		this.maiorY = arrayPtos.get(0).obterY();
		for(Ponto4D ptos : arrayPtos){
			//System.out.println("size:");
			if(ptos.obterX() > this.maiorX)
				this.maiorX = ptos.obterX();
			else if(ptos.obterX() < this.menorX)
				this.menorX = ptos.obterX();
			
			if(ptos.obterY() > this.maiorY)
				this.maiorY = ptos.obterY();
			else if(ptos.obterY() < this.menorY)
				this.menorY = ptos.obterY();
			
			if(ptos.obterZ() > this.maiorZ)
				this.maiorZ = ptos.obterZ();
			else if(ptos.obterZ() < this.menorZ)
				this.menorZ = ptos.obterZ();
		}
		System.out.println("maior x: "+this.maiorX);
		System.out.println("menor x: "+this.menorX);
		System.out.println("maior y: "+this.maiorY);
		System.out.println("menor y: "+this.menorY);
		System.out.println("maior z: "+this.maiorZ);
		System.out.println("menor z: "+this.menorZ);
	}
	
	public void atualizarBBox(Ponto4D point) {
	    atualizarBBox(point.obterX(), point.obterY(), point.obterZ());
	}

	public void atualizarBBox(double x, double y, double z) {
	    if (x > maiorX)
	        maiorX = x;
	    else if (x < menorX) {
	        menorX = x;
	    }
	    if (y > maiorY)
	        maiorY = y;
	    else if (y < menorY) {
	        menorY = y;
	    }
	    if (z > maiorZ)
	        maiorZ = z;
	    else if (z < menorZ) {
	        menorZ = z;
	    }
	}
	
	public void processarCentroBBox() {
	    centro.atribuirX((maiorX + menorX)/2);
	    centro.atribuirY((maiorY + menorY)/2);
	    centro.atribuirZ((maiorZ + menorZ)/2);
	}

	public void desenharOpenGLBBox(GL gl) {
		gl.glColor3f(1.0f, 0.0f, 0.0f);

		gl.glBegin (GL.GL_LINE_LOOP);
			gl.glVertex3d (menorX, maiorY, menorZ);
			gl.glVertex3d (maiorX, maiorY, menorZ);
			gl.glVertex3d (maiorX, menorY, menorZ);
			gl.glVertex3d (menorX, menorY, menorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (menorX, menorY, menorZ);
	    	gl.glVertex3d (menorX, menorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, menorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (maiorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, maiorY, maiorZ);
	    	gl.glVertex3d (menorX, menorY, maiorZ);
	    	gl.glVertex3d (maiorX, menorY, maiorZ);
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3d (maiorX, menorY, menorZ);
	    	gl.glVertex3d (maiorX, maiorY, menorZ);
	    	gl.glVertex3d (maiorX, maiorY, maiorZ);
	    	gl.glVertex3d (maiorX, menorY, maiorZ);
    	gl.glEnd();
	}

	/// Obter menor valor X da BBox.
	public double obterMenorX() {
		return menorX;
	}

	/// Obter menor valor Y da BBox.
	public double obterMenorY() {
		return menorY;
	}

	/// Obter menor valor Z da BBox.
	public double obterMenorZ() {
		return menorZ;
	}

	/// Obter maior valor X da BBox.
	public double obterMaiorX() {
		return maiorX;
	}

	/// Obter maior valor Y da BBox.
	public double obterMaiorY() {
		return maiorY;
	}

	/// Obter maior valor Z da BBox.
	public double obterMaiorZ() {
		return maiorZ;
	}
	
	/// Obter ponto do centro da BBox.
	public Ponto4D obterCentro() {
		return centro;
	}

}

