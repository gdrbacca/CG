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
	private Ponto4D pontoSelecionado = null;
	private ArrayList<ObjetoGrafico> objetos = new ArrayList();
	
	/// Matrizes temporarias que sempre sao inicializadas com matriz Identidade entao podem ser "static".
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();		
//	private static Transformacao4D matrizTmpRotacaoZ = new Transformacao4D();
	private static Transformacao4D matrizGlobal = new Transformacao4D();
//	private double anguloGlobal = 0.0;
	
	private int cor = 0;
	private float[][] matCores = new float[4][4];
	private boolean selecionado = false;
	private boolean desenhaPonto = false;
	private boolean ehFilho = false;
	private int numeroObjeto = -1;
	private int numeroPai = -1;
	
	public ObjetoGrafico() {
		initCores();
	}

	public void atribuirGL(GL gl) {
		this.gl = gl;
	}

	public double obterTamanho() {
		return tamanho;
	}
	
	/**
	 * Colocar o poligono em modo selecionado.
	 * @param selecionado true se é selecionado, false se não.
	 */	
	public void setSelecionado(boolean selecionado){
		this.selecionado = selecionado;
	}
	
	/**
	 * Adicionar poligono filho ao poligono selecionado.
	 * @param filho poligono a ser adicionado ao pai.
	 */	
	public void adicionaFilho(ObjetoGrafico filho){
		this.objetos.add(filho);
	}
	
	/**
	 * Alterna o poligono entre poligono fechado e poligono aberto.
	 */	
	public void trocarPrimitiva(){
		if(primitiva == GL.GL_LINE_LOOP)
			primitiva = GL.GL_LINE_STRIP;
		else
			primitiva = GL.GL_LINE_LOOP;
	}
	
	public double obterPrimitava() {
		return primitiva;
	}
	
	/**
	 * Adiciona um novo vertice ao poligono.
	 * @param pto vertice a ser adicionado.
	 */	
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
	
	/**
	 * Desenha o vertice antes da confirmação, para poder mover o mouse enquanto desenha.
	 * @param x coordenada x.
	 * @param y coordenada y.
	 */	
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
	
	/**
	 * Verifica se o ponto de clique está dentro do poligono.
	 * @param x coordenada x.
	 * @param y coordenada y.
	 * @return true se está dentro false se não está. 
	 */
	public boolean estaDentroObj(double x, double y){
		Ponto4D p1 = null;
		Ponto4D p2 = null;
		int nInt = 0;
		if(x <= bbox.obterMaiorX() && x >= bbox.obterMenorX()){
			if(y <= bbox.obterMaiorY() && y >= bbox.obterMenorY()){  //esta dentro bbox
				System.err.println("esta dentro bbox");
				for (int i = 0; i < vertices.size(); i++) {
					if(i+1 < vertices.size()){
						p1 = vertices.get(i);
						p2 = vertices.get(i+1);
						
					}else{
						p1 = vertices.get(i);
						p2 = vertices.get(0);
					}
					if(p1.obterY() != p2.obterY()){
						double ti = temInterseccao(y, p1.obterY(), p2.obterY());
						if(ti >= 0 && ti <= 1){
							double xi = p1.obterX() + (p2.obterX() - p1.obterX()) * ti;
							if(xi > x){
								nInt++;
							}
						}
					}
				}
				
			}
		}
		if(nInt % 2 != 0){
			System.out.println("esta dentro");
				return true;
		}
			else
				return false;
	}
	
	private double temInterseccao(double yi, double y1, double y2){
		return (yi - y1) / (y2 - y1);
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
		this.matCores[3][0] = 1.0f;
		this.matCores[3][1] = 0.0f;
		this.matCores[3][2] = 0.5f;
	}
	
	/**
	 * Alternar as cores do poligono entre as cores disponíveis.
	 */
	public void trocaCor(){
		this.cor++;
		if(this.cor > 3)
			this.cor = 0;
	}
	
	/**
	 * Seleciona um vertice do poligono.
	 * @param x coordenada x.
	 * @param y coordenada y.
	 */
	public void selecionaPonto(int x, int y){
		double maiorD = Double.MAX_VALUE;
		Ponto4D pontoTransform = null;
		pontoSelecionado = new Ponto4D();
		for(Ponto4D ptos : vertices){
			pontoTransform = matrizObjeto.transformPoint(ptos);
			double p = Math.pow((pontoTransform.obterX() - x), 2);
			double p2 = Math.pow((pontoTransform.obterY() - y), 2);
			double d = Math.sqrt(p + p2);
			if(d < maiorD){
				maiorD = d;
				pontoSelecionado = ptos;
				//pontoSelecionado.atribuirX(ptos.obterX());
				//pontoSelecionado.atribuirY(ptos.obterY());
				//pontoSelecionado.atribuirZ(0);
			}
		}
	}
	
	public void desenharPonto(int x, int y){
		this.desenhaPonto = true;
		selecionaPonto(x, y);
	}
	
	/**
	 * Remove o ponto selecionado.
	 */
	public void deletarPonto(){
		if(pontoSelecionado != null){
			//System.out.println("ponto removido: "+pontoSelecionado.obterX()+" "+
			//pontoSelecionado.obterY()+" "+vertices.remove(pontoSelecionado));
			if(vertices.size() > 3){
				vertices.remove(pontoSelecionado);
				bbox.atribuirBoundingBox(vertices);
				pontoSelecionado = null;
			}
			else
				desenhaPonto = true;
		}
	}
	
	/**
	 * Move o vertice selecionado.
	 * @param x coordenada x.
	 * @param y coordenada y.
	 */
	public void movePonto(int x, int y){
		if(pontoSelecionado != null){
			pontoSelecionado.atribuirX(pontoSelecionado.obterX() + x);
			pontoSelecionado.atribuirY(pontoSelecionado.obterY() + y);
			bbox.atribuirBoundingBox(vertices);
			desenhaPonto = true;
		}
	}
	
	/**
	 * Desenha o poligono e seus respectivos filhos.
	 */
	public void desenha() {
		
		gl.glColor3f(matCores[cor][0], matCores[cor][1], matCores[cor][2]);
		gl.glLineWidth(tamanho);
		gl.glPointSize(tamanho);

		//gl.glLoadIdentity();
		gl.glPushMatrix();
			gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
			gl.glBegin(primitiva);
				for (int i=0; i < vertices.size(); i++) {
					gl.glVertex2d(vertices.get(i).obterX(), vertices.get(i).obterY());
				}
			gl.glEnd();
			if(selecionado){
				bbox.desenharOpenGLBBox(gl);
			}
			if(desenhaPonto && pontoSelecionado != null){
				gl.glPointSize(10.0f);
				gl.glBegin(GL.GL_POINTS);
					gl.glVertex2d(pontoSelecionado.obterX(), pontoSelecionado.obterY());
				gl.glEnd();
				
				//pontoSelecionado = null;
				desenhaPonto = false;
			}
			//////////// ATENCAO: chamar desenho dos filhos... 
			for(int i = 0; i < objetos.size(); i++){
				objetos.get(i).desenha();
			}
			
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

	/**
	 * Aumenta e diminui a escala do poligono em relação ao centro de sua Bounding Box.
	 * @param escala A escala de aumento ou diminuição.
	 * @param ptoFixo O ponto que será usado para basear a escala.
	 */
	public void escalaXYZPtoFixo(double escala, Ponto4D ptoFixo) {
		bbox.processarCentroBBox();
		ptoFixo = bbox.obterCentro();
		ptoFixo.atribuirX(ptoFixo.obterX() *-1);
		ptoFixo.atribuirY(ptoFixo.obterY() *-1);
		
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
	
	/**
	 * Rotaciona o poligono em relação ao centro de sua Bounding Box.
	 * @param angulo O angulo de rotação.
	 * @param ptoFixo O ponto que será usado para basear a rotação.
	 */
	public void rotacaoZPtoFixo(double angulo, Ponto4D ptoFixo) {
		bbox.processarCentroBBox();
		ptoFixo = bbox.obterCentro();
		ptoFixo.atribuirX(ptoFixo.obterX() *-1);
		ptoFixo.atribuirY(ptoFixo.obterY() *-1);
		
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
//		System.out.println("anguloGlobal:" + anguloGlobal);
	}
	
	public ArrayList<ObjetoGrafico> getFilhos(){
		return this.objetos;
	}

	public void ehFilho(boolean ehFilho){
		this.ehFilho = ehFilho;
	}
	
	public boolean getEhFilho(){
		return this.ehFilho;
	}
	
	public void setNumeroObjeto(int num){
		this.numeroObjeto = num;
	}
	
	public int getNumeroObjeto(){
		return this.numeroObjeto;
	}
	
	public void setNumeroPai(int num){
		this.numeroPai = num;
	}
	
	public int getNumeroPai(){
		return this.numeroPai;
	}
}

