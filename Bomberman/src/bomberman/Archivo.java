/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import POJOMN.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Gerson
 */
public class Archivo {
	
	private int tipo; //tipo = 0 buscar usuario repetido, tipo == 1 obtener usuarios, tipo = 2 borrar/insertar, tipo = 3 buscar usuario y pass
	private static Archivo mnarchivo;
	File archivo = null;
	FileReader fr = null;
	BufferedReader br = null;
	FileWriter fichero = null;
	PrintWriter pw = null;
	private boolean encontrado;
	private String raiz = "C:////orga//"; //Agregar carpeta deseada
	private ArrayList<Usuario> lstUsuarios;
	
	public static Archivo getInstancia(){
		if(mnarchivo == null)
			mnarchivo = new Archivo();
		return mnarchivo;
	}
	
	public boolean buscar(String path){ 
		encontrado = false;
		leer(raiz +  path);
		return encontrado;
	}
	
	public void insertar(String path){
		escribir(raiz +  path, new Usuario());
	}
	
	public void quitar(String path){
		reescribir(raiz +  path, new Usuario());
	}
	
	public boolean buscarEnFila(String linea){ //recibe la linea del doc
		String[] arrOfStr = linea.split(","); 
		switch(getTipo()){
			case 0: //buscar nombre usuario
				if(arrOfStr[0].toUpperCase().equals(Usuario.getInstancia().getUsuario().toUpperCase())) return true;
			case 3: //buscar nombre usuario y pass
				if(arrOfStr[0].toUpperCase().equals(Usuario.getInstancia().getUsuario().toUpperCase()) && 
				arrOfStr[1].toUpperCase().equals(Usuario.getInstancia().getPass().toUpperCase())) return true;
		}
		return false;
	}
	
	public void obtenerInfoUsuario(String linea){
		String[] arrOfStr = linea.split(","); 
		Usuario usu= new Usuario();
		usu.setUsuario(arrOfStr[0]);
		usu.setPass(arrOfStr[1]);
		lstUsuarios.add(usu);
	}
	
	
	
	public void leer(String path){	
		try {
			archivo = new File (path);
			lstUsuarios = new ArrayList<>();
			if (archivo.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists!.");
				fr = new FileReader (archivo);
				br = new BufferedReader(fr);
				String linea;
				
				outer:
				while((linea=br.readLine())!=null){
					
					switch(getTipo()){
						case 0: //buscar nombre repetido
							encontrado = buscarEnFila(linea);
							if(encontrado)
								break outer; 
							else break;
						case 1://obtener nombres
							obtenerInfoUsuario(linea);
							break;
						case 3: //buscar nombre y pass
							encontrado = buscarEnFila(linea);
							if(encontrado)
								break outer; 
							else break;
					}
					
					System.out.println(linea);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		
			try{                    
			   if( null != fr ){   
				  fr.close();     
			   }                  
			}catch (Exception e2){ 
			   e2.printStackTrace();
			}
		}
	}
	
	public void escribir(String path, Object obj){
		try
		{
			fichero = new FileWriter(path, true); // ej, (ruta, boolean) so se desea modificar y agregar en la ultima linea
			pw = new PrintWriter(fichero);
			
			if(obj instanceof Usuario){
				pw.println(Usuario.getInstancia().getUsuario()+ "," +Usuario.getInstancia().getPass());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			// Nuevamente aprovechamos el finally para 
			// asegurarnos que se cierra el fichero.
			if (null != fichero)
			   fichero.close();
			} catch (Exception e2) {
			   e2.printStackTrace();
			}
		}
	}
	
	public void reescribir(String path, Object obj){
		try
		{
			fichero = new FileWriter(path); // ej, (ruta, boolean) so se desea modificar y agregar en la ultima linea
			pw = new PrintWriter(fichero);
			
			if(obj instanceof Usuario){
				
				for(Usuario usu : Archivo.getInstancia().getLstUsuarios()){
					pw.println(usu.getUsuario()+ "," +usu.getPass());
				}
				
				if( Archivo.getInstancia().getLstUsuarios().size() < 1){ //si no hay usuarios borrar archivo para evitar nulls
					File file = new File(path); 
					if(file.delete()) 
					{ 
						System.out.println("File deleted successfully"); 
					} 
					else
					{ 
						System.out.println("Failed to delete the file"); 
					} 
				}
					
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			// Nuevamente aprovechamos el finally para 
			// asegurarnos que se cierra el fichero.
			if (null != fichero)
			   fichero.close();
			} catch (Exception e2) {
			   e2.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
		/**
	 * @return the lstUsuarios
	 */
	public ArrayList<Usuario> getLstUsuarios() {
		return lstUsuarios;
	}

	/**
	 * @param lstUsuarios the lstUsuarios to set
	 */
	public void setLstUsuarios(ArrayList<Usuario> lstUsuarios) {
		this.lstUsuarios = lstUsuarios;
	}
}
