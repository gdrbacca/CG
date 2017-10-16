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
	private ObjetoGrafico objetoSelecionado;
	private int numObjetos = -1;
	
	
	public Mundo(){
		objs = new ArrayList<ObjetoGrafico>();
	}

	
	public ArrayList<ObjetoGrafico> getObjeto(){
		return this.objs;
	}
	
	public int remove(){
		for (int i = 0; i < objs.size(); i++) {
			if(objetoSelecionado == objs.get(i)){
				if(objetoSelecionado.getEhFilho()){
					int numPai = objetoSelecionado.getNumeroPai();
					for (int j = 0; j < objs.size(); j++) {
						if(objs.get(j).getNumeroObjeto() == numPai){
							//System.err.println("removeu do pai "+numPai);
							objs.get(j).getFilhos().remove(objetoSelecionado);
						}
					}
				}
				objs.remove(objetoSelecionado);
				if(objs.size() > 0){
					objetoSelecionado = objs.get(objs.size() - 1);
					return objs.size() - 1;
				}
			}
		}
		return -1;
	}
	
	public void desenha(){
		for (byte i=0; i < objs.size(); i++) {
			if(objs.get(i).getEhFilho())
				continue;
			objs.get(i).desenha();
		}
	}
	
	public int criaObj(boolean cria, int x, int y, GL gl, int indiceObj){
		Ponto4D pto = new Ponto4D(x, y, 0, 1);
		int indice = indiceObj;
		if(!cria){
			ObjetoGrafico objetoTemp = new ObjetoGrafico();
			objetoTemp.atribuirGL(gl);
			objetoTemp.addPonto(pto);
			objetoTemp.setNumeroObjeto(++numObjetos);
			objs.add(objetoTemp);
			indice = objs.size()-1;
			return indice;
		}
		else{
			objs.get(indice).addPonto(pto);
			return indice;
		}
	}

	public void atribuirBbox(){
		objs.get(objs.size()-1).atribuirBbox();
	}
	
	public void atribuiSelecionado(int indice, boolean filho){
		for (int i = 0; i < objs.size(); i++) {
			if(i == indice){
				objs.get(i).setSelecionado(true);
				if(filho && objetoSelecionado != null){
					objs.get(indice).ehFilho(true);
					objs.get(indice).setNumeroPai(objetoSelecionado.getNumeroObjeto());
					objetoSelecionado.adicionaFilho(objs.get(indice));
				}
				objetoSelecionado = objs.get(indice);
			}
			else
				objs.get(i).setSelecionado(false);
		}
	}
	
	
	public void deslizaPontoObj(int x, int y){
		objs.get(objs.size()-1).deslizaPonto(x, y);
	}
	
	public void selecionaPonto(int indice, int x, int y){
		objs.get(indice).desenharPonto(x, y);
	}
	
	public void movePonto(int x, int y){
		objetoSelecionado.movePonto(x, y);
	}
	
	public void deletaPonto(int indice){
		objs.get(indice).deletarPonto();
	}
	
	public int selecionaObjetoMouse(double x, double y){
		int i = 0;
		for(;i < objs.size(); i++){
			if(objs.get(i).estaDentroObj(x, y)){
				objetoSelecionado = objs.get(i);
				atribuiSelecionado(i, false);
				return i;
			}
		}
		return -1;
	}
	
}
