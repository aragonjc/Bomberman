/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import static bomberman.VwPrincipal.celda;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Gerson
 */
public class Celda extends JLabel implements KeyListener{
	
	private Object objeto;
	private int fila;
	private int columna;
	private boolean pared;
	private boolean bomba;
	private boolean puerta;
	private boolean explosion;
	private boolean bonus;
	private boolean left_exploto;
	private boolean der_exploto;
	private boolean up_exploto;
	private boolean down_exploto;
	private boolean perteneceJugador;
	
	Celda(){
		addKeyListener(this);
		setBorder(new LineBorder(Color.darkGray, 5));
		setFont(new Font("Arial",Font.PLAIN + Font.BOLD,18));
	}
	
	void EmpezarEnemigo(int fila_in, int columna_in) {
		class OneShotTask implements Runnable {
			int fila;
			int columna;
			OneShotTask(int f_in, int c_in) { fila = f_in; columna = c_in;}
			
			@Override
			public void run() {
				try {
					Thread.sleep(750);
				} catch (InterruptedException ex) {
					Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
				}
				switch(VwPrincipal.getRandom(1,5)){
					case 1://up
						VwPrincipal.VerificarObjeto(fila, columna, 1, 0, 0, 0, 0, false, false, false);
						break;
					case 2://down
						VwPrincipal.VerificarObjeto(fila, columna, 0, 1, 0, 0, 0, false, false, false);
						break;
					case 3://right
						VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 0, 1, 0, false, false, false);
						break;
					case 4://left
						VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 1, 0, 0, false, false, false);
						break;
					case 5://dejar bomba
						VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 0, 0, 1, false, false, false);
						break;
				}
			}
		}
		Thread t = new Thread(new OneShotTask(this.getFila(), this.getColumna()));
		t.start();
	}
	
	public void CrearTiempoExplosion(int fila, int columna){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				//Poner fuego bomba
				VwPrincipal.setContFor(0);
				int tam = 1;
			
				for(int cont = VwPrincipal.getContFor(); cont < tam; cont++){
					VwPrincipal.VerificarObjeto(fila, columna, 1 + cont, 0, 0, 0, 0, false, true, false); //up
					VwPrincipal.VerificarObjeto(fila, columna, 0, 1 + cont, 0, 0, 0, false, true, false); //down
					VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 1 + cont, 0, 0, false, true, false); //left
					VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 0, 1 + cont, 0, false, true, false); //right
					VwPrincipal.setContFor(VwPrincipal.getContFor()+1);
					if(celda[fila][columna].isPerteneceJugador()){
						tam = VwPrincipal.jugadorPrincipal.getBonus() + 1;
					}
				}
				try {
					Thread.sleep(350);
				} catch (InterruptedException ex) {
					Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
				}
					//Ejecutar accion despues de 1 seg
					VwPrincipal.LimpiarCentro(fila, columna); //Limpiar fuego
					VwPrincipal.setContFor2(0);
					for(int cont = VwPrincipal.getContFor2(); cont < tam; cont++){
						VwPrincipal.VerificarObjeto(fila, columna, 1 + cont, 0, 0, 0, 0, false, false, true); //up
						VwPrincipal.VerificarObjeto(fila, columna, 0, 1 + cont, 0, 0, 0, false, false, true); //down
						VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 1 + cont, 0, 0, false, false, true); //left
						VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 0, 1 + cont, 0, false, false, true); //right
						VwPrincipal.setContFor2(VwPrincipal.getContFor2()+1);
					}
			}
		};
		// Creamos un hilo y le pasamos el runnable
		Thread hilo = new Thread(runnable);
		hilo.start();
	}
	
	public void CrearTiempoBomba(int fila, int columna){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// Esto se ejecuta en segundo plano, solo una vez
				try {
					Thread.sleep(2500);
					//Ejecutar accion despues de 1.5 seg
					VwPrincipal.VerificarObjeto(fila, columna, 0, 0, 0, 0, 0, true, false, false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		// Creamos un hilo y le pasamos el runnable
		Thread hilo = new Thread(runnable);
		hilo.start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				VwPrincipal.VerificarObjeto(this.getFila(), this.getColumna(), 1, 0, 0, 0, 0, false, false, false);
				break;
			case KeyEvent.VK_DOWN:
				VwPrincipal.VerificarObjeto(this.getFila(), this.getColumna(), 0, 1, 0, 0, 0, false, false, false);
				break;
			case KeyEvent.VK_RIGHT:
				VwPrincipal.VerificarObjeto(this.getFila(), this.getColumna(), 0, 0, 0, 1, 0, false, false, false);
				break;
			case KeyEvent.VK_LEFT:
				VwPrincipal.VerificarObjeto(this.getFila(), this.getColumna(), 0, 0, 1, 0, 0, false, false, false);
				break;
			case KeyEvent.VK_SPACE:
				VwPrincipal.VerificarObjeto(this.getFila(), this.getColumna(), 0, 0, 0, 0, 1, false, false, false);
				break;
			case KeyEvent.VK_P:
				//VwPrincipal.Pausar();
				break;
			case KeyEvent.VK_R:
				//VwPrincipal.Reiniciar();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
	/**
	 * @return the explosion
	 */
	public boolean isExplosion() {
		return explosion;
	}

	/**
	 * @param explosion the explosion to set
	 */
	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}
	
	/**
	 * @return the objeto
	 */
	public Object getObjeto() {
		return objeto;
	}

	/**
	 * @param objeto the objeto to set
	 */
	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}
	
		/**
	 * @return the pared
	 */
	public boolean isPared() {
		return pared;
	}

	/**
	 * @param pared the pared to set
	 */
	public void setPared(boolean pared) {
		this.pared = pared;
	}

	/**
	 * @return the bomba
	 */
	public boolean isBomba() {
		return bomba;
	}

	/**
	 * @param bomba the bomba to set
	 */
	public void setBomba(boolean bomba) {
		this.bomba = bomba;
	}
	
	/**
	 * @return the fila
	 */
	public int getFila() {
		return fila;
	}

	/**
	 * @param fila the fila to set
	 */
	public void setFila(int fila) {
		this.fila = fila;
	}

	/**
	 * @return the columna
	 */
	public int getColumna() {
		return columna;
	}

	/**
	 * @param columna the columna to set
	 */
	public void setColumna(int columna) {
		this.columna = columna;
	}
	
	public void setPosEnumeracion(String pos){
		setText(pos);
	}

	public void setEncabezado(String enc){
		setText(enc);
	}
	public void setEditar(Boolean bln){
		setEnabled(bln);
	}

	
	public void centrarTexto(){
		setHorizontalAlignment(this.CENTER);
	}

	public void setNombre(int fila, int columna, String nombre){
		setName(nombre + fila);        
	}
	
		/**
	 * @return the bonus
	 */
	public boolean isBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	
	/**
	 * @return the left_exploto
	 */
	public boolean isLeft_exploto() {
		return left_exploto;
	}

	/**
	 * @param left_exploto the left_exploto to set
	 */
	public void setLeft_exploto(boolean left_exploto) {
		this.left_exploto = left_exploto;
	}

	/**
	 * @return the der_exploto
	 */
	public boolean isDer_exploto() {
		return der_exploto;
	}

	/**
	 * @param der_exploto the der_exploto to set
	 */
	public void setDer_exploto(boolean der_exploto) {
		this.der_exploto = der_exploto;
	}

	/**
	 * @return the up_exploto
	 */
	public boolean isUp_exploto() {
		return up_exploto;
	}

	/**
	 * @param up_exploto the up_exploto to set
	 */
	public void setUp_exploto(boolean up_exploto) {
		this.up_exploto = up_exploto;
	}

	/**
	 * @return the down_exploto
	 */
	public boolean isDown_exploto() {
		return down_exploto;
	}

	/**
	 * @param down_exploto the down_exploto to set
	 */
	public void setDown_exploto(boolean down_exploto) {
		this.down_exploto = down_exploto;
	}
	
		/**
	 * @return the perteneceJugador
	 */
	public boolean isPerteneceJugador() {
		return perteneceJugador;
	}

	/**
	 * @param perteneceJugador the perteneceJugador to set
	 */
	public void setPerteneceJugador(boolean perteneceJugador) {
		this.perteneceJugador = perteneceJugador;
	}
	
	/**
	 * @return the puerta
	 */
	public boolean isPuerta() {
		return puerta;
	}

	/**
	 * @param puerta the puerta to set
	 */
	public void setPuerta(boolean puerta) {
		this.puerta = puerta;
	}
}
