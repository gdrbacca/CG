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

public class Main implements KeyListener,
							MouseListener, MouseMotionListener, GLEventListener  {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

//	private ObjetoGrafico objeto = new ObjetoGrafico();
	//private ArrayList<ObjetoGrafico> objs = new ArrayList<ObjetoGrafico>();
	
//	private ObjetoGrafico[] objetos = { 
//			new ObjetoGrafico(),
//			new ObjetoGrafico() };
	
	double x1 = 0, y1 = 400, x2 = 378,  y2 = 0;
	int contZoomIn, contZoomOut = 0;
	private int indiceObj = -1;
	private boolean criaObj = false;
	private boolean selectVertice = false;
	int antigoX, antigoY;
	Ponto4D pSelecionado = new Ponto4D();
	private Mundo mundo = new Mundo();
	private Camera camera = new Camera(0.0, 400.0, 378.0, 0.0);
	// "render" feito logo apos a inicializacao do contexto OpenGL.


	// metodo definido na interface GLEventListener.
	// "render" feito pelo cliente OpenGL.


	
	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(camera.getX1(), camera.getY1(), camera.getX2(), camera.getY2());


		gl.glLineWidth(1.0f);
		gl.glPointSize(1.0f);
		desenhaSRU();
		mundo.desenha();

		gl.glFlush();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {		
	}
	

	@Override
	public void reshape(GLAutoDrawable arg0, int x, int y, int width, int height) {
		gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
		
	}
	
	public void keyPressed(KeyEvent e) {

		if(mundo.getObjeto().size() > 0){
			switch (e.getKeyCode()) {
			case KeyEvent.VK_P:
				mundo.getObjeto().get(indiceObj).exibeVertices();
				break;
			case KeyEvent.VK_M:
				mundo.getObjeto().get(indiceObj).exibeMatriz();
				break;
	
			case KeyEvent.VK_R:
				mundo.getObjeto().get(indiceObj).atribuirIdentidade();
				break;
	
			case KeyEvent.VK_Q:
				selectVertice = !selectVertice;
				break;	
				
			case KeyEvent.VK_D:
				if(!selectVertice){
					mundo.getObjeto().remove(indiceObj);
					indiceObj = mundo.getObjeto().size()-1;
					mundo.atribuiSelecionado(indiceObj);
				} else{
					mundo.deletaPonto(indiceObj);
				}
				break;
				
			case KeyEvent.VK_A:
				mundo.getObjeto().get(indiceObj).trocarPrimitiva();;
				break;	
				
			case KeyEvent.VK_RIGHT:
				mundo.getObjeto().get(indiceObj).translacaoXYZ(10.0,0.0,0.0);
				break;
				
			case KeyEvent.VK_LEFT:
				mundo.getObjeto().get(indiceObj).translacaoXYZ(-10.0,0.0,0.0);
				break;
				
			case KeyEvent.VK_UP:
				mundo.getObjeto().get(indiceObj).translacaoXYZ(0.0,-10.0,0.0);
				break;
				
			case KeyEvent.VK_DOWN:
				mundo.getObjeto().get(indiceObj).translacaoXYZ(0.0,10.0,0.0);
				break;
	
			case KeyEvent.VK_PAGE_UP:
				mundo.getObjeto().get(indiceObj).escalaXYZ(1.5,1.5);
				break;
				
			case KeyEvent.VK_PAGE_DOWN:
				mundo.getObjeto().get(indiceObj).escalaXYZ(0.5,0.5);
				break;
	
			case KeyEvent.VK_HOME:
	//			objetos[0].RoracaoZ();
				break;
	
			case KeyEvent.VK_1:
				mundo.getObjeto().get(indiceObj).escalaXYZPtoFixo(0.5, new Ponto4D(-200.0,-200.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_2:
				mundo.getObjeto().get(indiceObj).escalaXYZPtoFixo(1.5, new Ponto4D(-200.0,-200.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_3:
				mundo.getObjeto().get(indiceObj).rotacaoZPtoFixo(5.0, new Ponto4D(0.0,0.0,0.0,0.0));
				break;
			
			case KeyEvent.VK_4:
				indiceObj++;
				if(indiceObj > mundo.getObjeto().size() - 1)
					indiceObj = 0;
				mundo.atribuiSelecionado(indiceObj);
				break;
				
			case KeyEvent.VK_5:
				mundo.getObjeto().get(indiceObj).trocaCor();
				break;
				
			case KeyEvent.VK_SPACE:
				if(criaObj){
					criaObj = false;
					mundo.atribuirBbox();
					mundo.atribuiSelecionado(indiceObj);
				}
				break;
			
			case KeyEvent.VK_T:  //zoom out
				if(contZoomOut < 10){
					x1 -= 10;
					y1 += 10;
					x2 += 10;
					y2 -= 10;
					camera.setX1(x1);
					camera.setY1(y1);
					camera.setX2(x2);
					camera.setY2(y2);
					contZoomOut+=1;
					contZoomIn-=1;
				}
				break;
			
			case KeyEvent.VK_H:  //zoom in
				if(contZoomIn < 10){
					x1 += 10;
					y1 -= 10;
					x2 -= 10;
					y2 += 10;
					camera.setX1(x1);
		            camera.setY1(y1);
		            camera.setX2(x2);
		            camera.setY2(y2);
		            contZoomIn+=1;
		            contZoomOut-=1;
				}
				break;
				
			case KeyEvent.VK_Y:  //cima
				if(camera.getY2() < 200){
					x2 += 10;
					y2 += 10;
					camera.setX2(x2);
					camera.setY2(y2);
				}
				break;
				
			case KeyEvent.VK_N:  //baixo
				if(camera.getY2() > -200){
					x2 -= 10;
					y2 -= 10;
					camera.setX2(x2);
					camera.setY2(y2);
				}
				break;
				
			case KeyEvent.VK_G:  //esquerda
				if(y1 < 600){
					x1 += 10;
					y1 += 10;
					camera.setX1(x1);
					camera.setY1(y1);
				}
				break;
				
			case KeyEvent.VK_J:  //direita
				if(y1 > 200){
					x1 -= 10;
					y1 -= 10;
					camera.setX1(x1);
					camera.setY1(y1);
				}
				break;
				
			case KeyEvent.VK_U:  //centraliza
				x1 = 0;
				y1 = 400;
				x2 = 378;
				y2 = 0;
				camera.setX1(x1);
				camera.setX2(x2);
				camera.setY1(y1);
				camera.setY2(y2);
				break;
			}
		}

		glDrawable.display();
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
		if(selectVertice){
			int movtoX = arg0.getX() - antigoX;
			int movtoY = arg0.getY() - antigoY;
			mundo.movePonto(movtoX, movtoY);
			
			antigoX = arg0.getX();
			antigoY = arg0.getY();
			glDrawable.display();
		}
	}

	public void mouseMoved(MouseEvent arg0) {
		if(mundo.getObjeto().size() > 0 && criaObj){
			mundo.deslizaPontoObj(arg0.getX(), arg0.getY());
			glDrawable.display();
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		System.out.println((arg0.getX())+" "+(arg0.getY()));
		if(arg0.getButton() == MouseEvent.BUTTON1){
			if(!selectVertice){
				indiceObj = mundo.criaObj(criaObj, arg0.getX(), arg0.getY(), gl, indiceObj);
				if(!criaObj){
					criaObj = true;
				}
			}
			else{
				mundo.selecionaPonto(indiceObj, arg0.getX(), arg0.getY());
			}
		}
		else if(arg0.getButton() == MouseEvent.BUTTON3){
			int indice = mundo.selecionaObjetoMouse(arg0.getX(), arg0.getY());
			if(indice != -1)
				indiceObj = indice;
			System.out.println("indice selected: "+indice);
		}
		
		glDrawable.display();
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {		
	}

	public void mousePressed(MouseEvent arg0) {
		if(selectVertice){
			antigoX = arg0.getX();
			antigoY = arg0.getY();
		}
	}

	public void mouseReleased(MouseEvent arg0) {		
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

}
