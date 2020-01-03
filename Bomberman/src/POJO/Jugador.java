/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJO;

/**
 *
 * @author Gerson
 */
public class Jugador {

	/**
	 * @return the tiempo
	 */
	public double getTiempo() {
		return tiempo;
	}

	/**
	 * @param tiempo the tiempo to set
	 */
	public void setTiempo(double tiempo) {
		this.tiempo = tiempo;
	}

	/**
	 * @return the gano
	 */
	public boolean isGano() {
		return gano;
	}

	/**
	 * @param gano the gano to set
	 */
	public void setGano(boolean gano) {
		this.gano = gano;
	}

	/**
	 * @return the enemigos
	 */
	public int getEnemigos() {
		return enemigos;
	}

	/**
	 * @param enemigos the enemigos to set
	 */
	public void setEnemigos(int enemigos) {
		this.enemigos = enemigos;
	}

	/**
	 * @return the pared
	 */
	public int getPared() {
		return pared;
	}

	/**
	 * @param pared the pared to set
	 */
	public void setPared(int pared) {
		this.pared = pared;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the bonus
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the puntos
	 */
	public int getPuntos() {
		return puntos;
	}

	/**
	 * @param puntos the puntos to set
	 */
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	/**
	 * @return the vida
	 */
	public int getVida() {
		return vida;
	}

	/**
	 * @param vida the vida to set
	 */
	public void setVida(int vida) {
		this.vida = vida;
	}
	private String usuario;
	private int puntos;
	private int vida;
	private int bonus;
	private int x;
	private int y;
	private int enemigos;
	private int pared;
	private boolean gano;
	private double tiempo;
}
