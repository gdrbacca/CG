import java.util.ArrayList;

import javax.media.opengl.GL;
public final class ObjetoGrafico {
	GL gl;
	private float tamanho = 2.0f;

	private int primitiva = GL.GL_LINE_LOOP;
	private ArrayList<Ponto4D> vertices = new ArrayList<Ponto4D>();
//	private Ponto4D[] vertices = { 	
//			new Ponto4D(100.0, 100.0, 0.0, 1.0),
//			new Ponto4D(200.0, 100.0, 0.0, 1.0), 
//			new Ponto4D(200.0, 200.0, 0.0, 1.0),
//			new Ponto4D(100.0, 200.0, 0.0, 1.0) };

//	private int primitiva = GL.GL_POINTS;
//	private Ponto4D[] vertices = { new Ponto4D(10.0, 10.0, 0.0, 1.0) };	

	private Transformacao4D matrizObjeto = new Transformacao4D();
	private BoundingBox bbox = null;
	
	/// Matrizes temporarias que sempre sao inicializadas com matriz Identidade entao podem ser "static".
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();		
//	private static Transformacao4D matrizTmpRotacaoZ = new Transformacao4D();
	private static Transformacao4D matrizGlobal = new Transformacao4D();
//	private double anguloGlobal = 0.0;
	
	private int cor = 0;
	private float[][] matCores = new float[3][3];
	private boolean selecionado = false;
	
	public ObjetoGrafico() {
		initCores();
	}

	public void atribuirGL(GL gl) {
		this.gl = gl;
	}

	public double obterTamanho() {
		return tamanho;
	}

	public void setSelecionado(boolean selecionado){
		this.selecionado = selecionado;
	}
	
	public void trocarPrimitiva(){
		if(primitiva == GL.GL_LINE_LOOP)
			primitiva = GL.GL_LINE_STRIP;
		else
			primitiva = GL.GL_LINE_LOOP;
	}
	
	public double obterPrimitava() {
		return primitiva;
	}
	
	public void addPonto(Ponto4D pto){
		if(vertices.size()<=1){
			System.out.println("adddou primeiro");
			this.vertices.add(pto);
			this.vertices.add(new Ponto4D(pto.obterX(), pto.obterY(), 0, 1));
		}
		else{
			this.vertices.set(vertices.size()-1, pto);
			this.vertices.add(new Ponto4D(pto.obterX(), pto.obterY(), 0, 1));
		}
	}
	
	public void deslizaPonto(double x, double y){
		this.vertices.get(vertices.size()-1).atribuirX(x);
		this.vertices.get(vertices.size()-1).atribuirY(y);
//		System.out.println(vertices.get(vertices.size()-1).obterX());
//		System.out.println(vertices.get(vertices.size()-1).obterY());
	}
	
	public void atribuirBbox(){
		this.bbox = new BoundingBox();
		this.bbox.atribuirBoundingBox(vertices);
	}
	
	private void initCores(){
		this.matCores[0][0] = 0.0f;
		this.matCores[0][1] = 0.0f;
		this.matCores[0][2] = 0.0f;
		this.matCores[1][0] = 1.0f;
		this.matCores[1][1] = 1.0f;
		this.matCores[1][2] = 0.0f;
		this.matCores[2][0] = 0.0f;
		this.matCores[2][1] = 1.0f;
		this.matCores[2][2] = 1.0f;
	}
	
	public void trocaCor(){
		this.cor++;
		if(this.cor > 2)
			this.cor = 0;
	}
	
	public void desenha() {
		
		gl.glColor3f(matCores[cor][0], matCores[cor][1], matCores[cor][2]);
		gl.glLineWidth(tamanho);
		gl.glPointSize(tamanho);

		gl.glPushMatrix();
			gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
			gl.glBegin(primitiva);
//			System.out.println(vertices.size());
//			System.out.println(vertices.get(0).obterX()+" "+vertices.get(0).obterY());
//			System.out.println(vertices.get(1).obterX()+" "+vertices.get(1).obterY());
				for (int i=0; i < vertices.size(); i++) {
					gl.glVertex2d(vertices.get(i).obterX(), vertices.get(i).obterY());
				}
			gl.glEnd();
			if(selecionado){
				bbox.desenharOpenGLBBox(gl);
			}

			//////////// ATENCAO: chamar desenho dos filhos... 

		gl.glPopMatrix();
	}

	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx,ty,tz);
		matrizObjeto = matrizTranslate.transformMatrix(matrizObjeto);		
	}

	public void escalaXYZ(double Sx,double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();		
		matrizScale.atribuirEscala(Sx,Sy,1.0);
		matrizObjeto = matrizScale.transformMatrix(matrizObjeto);
	}

	///TODO: erro na rotacao
	public void rotacaoZ(double angulo) {
//		anguloGlobal += 10.0; // rotacao em 10 graus
//		Transformacao4D matrizRotacaoZ = new Transformacao4D();		
//		matrizRotacaoZ.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
//		matrizObjeto = matrizRotacaoZ.transformMatrix(matrizObjeto);
	}
	
	public void atribuirIdentidade() {
//		anguloGlobal = 0.0;
		matrizObjeto.atribuirIdentidade();
	}

	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.obterX(),ptoFixo.obterY(),ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirEscala(escala, escala, 1.0);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.obterX(),ptoFixo.obterY(),ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
	}
	
	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.obterX(),ptoFixo.obterY(),ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.obterX(),ptoFixo.obterY(),ptoFixo.obterZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
	}

	public void exibeMatriz() {
		matrizObjeto.exibeMatriz();
	}

	public void exibeVertices() {
		for (int i = 0; i < vertices.size(); i++) {
			System.out.println("P0[" + vertices.get(i).obterX() + "," + vertices.get(i).obterY()+ "]");
		}
//		
//		System.out.println("anguloGlobal:" + anguloGlobal);
	}

	
}

