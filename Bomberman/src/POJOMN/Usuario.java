/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJOMN;

/**
 *
 * @author Juan Carlos Aragón
 */
public class Usuario {

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	private static Usuario mnUsuario;
	
	public static Usuario getInstancia(){
		if(mnUsuario == null)
			mnUsuario = new Usuario();
		return mnUsuario;
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
	private String usuario;
	private String pass;
    
    
}
