import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener,
							MouseListener, MouseMotionListener  {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

//	private ObjetoGrafico objeto = new ObjetoGrafico();
	private ArrayList<ObjetoGrafico> objs = new ArrayList<ObjetoGrafico>();
	
//	private ObjetoGrafico[] objetos = { 
//			new ObjetoGrafico(),
//			new ObjetoGrafico() };
	
	
	private int indiceObj = -1;
	private boolean criaObj = false;
	// "render" feito logo apos a inicializacao do contexto OpenGL.
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

//		for (byte i=0; i < objetos.length; i++) {
//			objetos[i].atribuirGL(gl);
//		}
		//objs.add(objetos[0]);
		//objs.add(objetos[1]);
//		objeto.atribuirGL(gl);
	}

	// metodo definido na interface GLEventListener.
	// "render" feito pelo cliente OpenGL.
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glu.gluOrtho2D(0.0f, 400.0f, 378.0f, 0.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glLineWidth(1.0f);
		gl.glPointSize(1.0f);
		desenhaSRU();
		for (byte i=0; i < objs.size(); i++) {
			objs.get(i).desenha();
		}
		//System.out.println("guiguigu");

//		objeto.desenha();

		gl.glFlush();
	}

	public void desenhaSRU() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(100.0f, 200.0f);
			gl.glVertex2f(300.0f, 200.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(200.0f, 100.0f);
			gl.glVertex2f(200.0f, 300.0f);
		gl.glEnd();
	}
	
	public void keyPressed(KeyEvent e) {

		if(objs.size() > 0){
			switch (e.getKeyCode()) {
			case KeyEvent.VK_P:
				objs.get(indiceObj).exibeVertices();
				break;
			case KeyEvent.VK_M:
				objs.get(indiceObj).exibeMatriz();
				break;
	
			case KeyEvent.VK_R:
				objs.get(indiceObj).atribuirIdentidade();
				break;
	
			case KeyEvent.VK_RIGHT:
				objs.get(indiceObj).translacaoXYZ(10.0,0.0,0.0);
				break;
			case KeyEvent.VK_LEFT:
				objs.get(indiceObj).translacaoXYZ(-10.0,0.0,0.0);
				break;
			case KeyEvent.VK_UP:
				objs.get(indiceObj).translacaoXYZ(0.0,-10.0,0.0);
				break;
			case KeyEvent.VK_DOWN:
				objs.get(indiceObj).translacaoXYZ(0.0,10.0,0.0);
				break;
	
			case KeyEvent.VK_PAGE_UP:
				objs.get(indiceObj).escalaXYZ(2.0,2.0);
				break;
			case KeyEvent.VK_PAGE_DOWN:
				objs.get(indiceObj).escalaXYZ(0.5,0.5);
				break;
	
			case KeyEvent.VK_HOME:
	//			objetos[0].RoracaoZ();
				break;
	
			case KeyEvent.VK_1:
				objs.get(indiceObj).escalaXYZPtoFixo(0.5, new Ponto4D(-200.0,-200.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_2:
				objs.get(indiceObj).escalaXYZPtoFixo(2.0, new Ponto4D(-200.0,-200.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_3:
				objs.get(indiceObj).rotacaoZPtoFixo(10.0, new Ponto4D(-200.0,-189.0,0.0,0.0));
				break;
			
			case KeyEvent.VK_4:
				indiceObj++;
				if(indiceObj > objs.size() - 1)
					indiceObj = 0;
				break;
				
			case KeyEvent.VK_5:
				objs.get(indiceObj).trocaCor();
				break;
				
			case KeyEvent.VK_SPACE:
				//System.out.println("espaço");
				criaObj = false;
				break;
			}
		}

		glDrawable.display();
	}

	// metodo definido na interface GLEventListener.
	// "render" feito depois que a janela foi redimensionada.
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
		// System.out.println(" --- reshape ---");
	}

	// metodo definido na interface GLEventListener.
	// "render" feito quando o modo ou dispositivo de exibicao associado foi
	// alterado.
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// System.out.println(" --- displayChanged ---");
	}

	public void keyReleased(KeyEvent arg0) {
		// System.out.println(" --- keyReleased ---");
	}

	public void keyTyped(KeyEvent arg0) {
		// System.out.println(" --- keyTyped ---");
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(objs.size() > 0 && criaObj){
			objs.get(objs.size()-1).deslizaPonto(arg0.getX(), arg0.getY());
			glDrawable.display();
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println((arg0.getX())+" "+(arg0.getY()));
		if(arg0.getButton() == MouseEvent.BUTTON1){
			//System.out.println("btn3");
			Ponto4D pto = new Ponto4D(arg0.getX(), arg0.getY(), 0, 1);
			if(!criaObj){
				criaObj = true;
				ObjetoGrafico obj = new ObjetoGrafico();
				obj.atribuirGL(gl);
				obj.addPonto(pto);
				objs.add(obj);
				indiceObj = objs.size()-1;
			}
			else{
				objs.get(objs.size()-1).addPonto(pto);
			}
		}
		else if(arg0.getButton() == MouseEvent.BUTTON3)
			System.out.println("btn1");
		
		glDrawable.display();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
