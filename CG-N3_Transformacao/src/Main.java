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
	
	
	private int indiceObj = -1;
	private boolean criaObj = false;
	private boolean selectVertice = false;
	int antigoX, antigoY;
	Ponto4D pSelecionado = new Ponto4D();
	private Mundo mundo = new Mundo();
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
		glu.gluOrtho2D(0.0f, 400.0f, 378.0f, 0.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glLineWidth(1.0f);
		gl.glPointSize(1.0f);
		//desenhaSRU();
		mundo.desenha();

//		objeto.desenha();

		gl.glFlush();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
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
				//System.out.println("espaço");
				if(criaObj){
					criaObj = false;
					mundo.atribuirBbox();
					mundo.atribuiSelecionado(indiceObj);
				}
				break;
			}
		}

		glDrawable.display();
	}

	// metodo definido na interface GLEventListener.
	// "render" feito depois que a janela foi redimensionada.


	// metodo definido na interface GLEventListener.
	// "render" feito quando o modo ou dispositivo de exibicao associado foi
	// alterado.


	public void keyReleased(KeyEvent arg0) {
		// System.out.println(" --- keyReleased ---");
	}

	public void keyTyped(KeyEvent arg0) {
		// System.out.println(" --- keyTyped ---");
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if(mundo.getObjeto().size() > 0 && criaObj){
			mundo.deslizaPontoObj(arg0.getX(), arg0.getY());
			glDrawable.display();
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(selectVertice){
			antigoX = arg0.getX();
			antigoY = arg0.getY();
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
