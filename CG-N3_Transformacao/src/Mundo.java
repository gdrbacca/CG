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


public class Mundo{
	
	
	
	private ArrayList<ObjetoGrafico> objs;

	
	public Mundo(){
		objs = new ArrayList<ObjetoGrafico>();
	}

	
	public ArrayList<ObjetoGrafico> getObjeto(){
		return this.objs;
	}
	
	public void desenha(){
		for (byte i=0; i < objs.size(); i++) {
			objs.get(i).desenha();
		}
	}
	
	public int criaObj(boolean cria, int x, int y, GL gl, int indiceObj){
		Ponto4D pto = new Ponto4D(x, y, 0, 1);
		int indice = indiceObj;
		if(!cria){
			ObjetoGrafico obj = new ObjetoGrafico();
			obj.atribuirGL(gl);
			obj.addPonto(pto);
			objs.add(obj);
			indice = objs.size()-1;
			return indice;
		}
		else{
			objs.get(objs.size()-1).addPonto(pto);
			return indice;
		}
	}

	
	public void deslizaPontoObj(int x, int y){
		objs.get(objs.size()-1).deslizaPonto(x, y);
	}
	
	


	
	
}
