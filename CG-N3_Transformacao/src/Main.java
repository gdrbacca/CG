import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

//	private ObjetoGrafico objeto = new ObjetoGrafico();
	private ObjetoGrafico[] objetos = { 
			new ObjetoGrafico(),
			new ObjetoGrafico() };
	private int indiceObj = 0;
	// "render" feito logo apos a inicializacao do contexto OpenGL.
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		for (byte i=0; i < objetos.length; i++) {
			objetos[i].atribuirGL(gl);
		}
//		objeto.atribuirGL(gl);
	}

	// metodo definido na interface GLEventListener.
	// "render" feito pelo cliente OpenGL.
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glu.gluOrtho2D(-250.0f, 250.0f, -250.0f, 250.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glLineWidth(1.0f);
		gl.glPointSize(1.0f);

		desenhaSRU();
		for (byte i=0; i < objetos.length; i++) {
			objetos[i].desenha();
		}

//		objeto.desenha();

		gl.glFlush();
	}

	public void desenhaSRU() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(-200.0f, 0.0f);
			gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(0.0f, -200.0f);
			gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}
	
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_P:
			objetos[indiceObj].exibeVertices();
			break;
		case KeyEvent.VK_M:
			objetos[indiceObj].exibeMatriz();
			break;

		case KeyEvent.VK_R:
			objetos[indiceObj].atribuirIdentidade();
			break;

		case KeyEvent.VK_RIGHT:
			objetos[indiceObj].translacaoXYZ(10.0,0.0,0.0);
			break;
		case KeyEvent.VK_LEFT:
			objetos[indiceObj].translacaoXYZ(-10.0,0.0,0.0);
			break;
		case KeyEvent.VK_UP:
			objetos[indiceObj].translacaoXYZ(0.0,10.0,0.0);
			break;
		case KeyEvent.VK_DOWN:
			objetos[indiceObj].translacaoXYZ(0.0,-10.0,0.0);
			break;

		case KeyEvent.VK_PAGE_UP:
			objetos[indiceObj].escalaXYZ(2.0,2.0);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			objetos[indiceObj].escalaXYZ(0.5,0.5);
			break;

		case KeyEvent.VK_HOME:
//			objetos[0].RoracaoZ();
			break;

		case KeyEvent.VK_1:
			objetos[indiceObj].escalaXYZPtoFixo(0.5, new Ponto4D(-150.0,-150.0,0.0,0.0));
			break;
			
		case KeyEvent.VK_2:
			objetos[indiceObj].escalaXYZPtoFixo(2.0, new Ponto4D(-150.0,-150.0,0.0,0.0));
			break;
			
			case KeyEvent.VK_3:
				objetos[indiceObj].rotacaoZPtoFixo(10.0, new Ponto4D(-15.0,-15.0,0.0,0.0));
				break;
		
			case KeyEvent.VK_4:
				indiceObj++;
				if(indiceObj > objetos.length - 1)
					indiceObj = 0;
				break;
			case KeyEvent.VK_5:
				objetos[indiceObj].trocaCor();
				System.out.println("oiodais");
				break;
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

}
