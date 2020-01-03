/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;
import POJO.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author »Gersøn
 */
public class VwPrincipal extends JFrame implements ActionListener{
	private JPanel  pnlCelda;
	private JButton btnCrear;
	private JScrollPane scrollPane;
	public static int filas = 12, columnas = 12;
	public static Celda[][] celda, celdaData;
	public static Jugador jugadorPrincipal;
	private static int contFor;
	private static int contFor2;
	private static long inicio;
	private static long fin;
	private static int tipoJuego; //0 == entreno. 1 == carga
	private static String nombreArchivo;
	private static int pausa;
	//Carga de imagenes
	ImageIcon  ima_player = new ImageIcon(getClass().getResource("/Imagenes/jugador.gif"));
	ImageIcon  ima_enemy = new ImageIcon(getClass().getResource("/Imagenes/enemigo.gif"));
	ImageIcon ima_wall = new ImageIcon(getClass().getResource("/Imagenes/wall.png"));
	ImageIcon ima_bonus = new ImageIcon(getClass().getResource("/Imagenes/bonus.gif"));
	ImageIcon ima_puerta = new ImageIcon(getClass().getResource("/Imagenes/puerta.gif"));
	/**
	 * Instancia las variables creadas en el diseño
	 * y agrega los componentes a sus contenedores
	 */
	public VwPrincipal(int tipo, String archivo){
		tipoJuego = tipo;
		nombreArchivo = archivo;
		jugadorPrincipal = new Jugador();
		pnlCelda = new JPanel(null);
		scrollPane = new JScrollPane(pnlCelda, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		btnCrear = new JButton();
		btnCrear.addActionListener(this);
		btnCrear.doClick();
		this.setSize(1050, 1050);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	//Devuelve la matriz binaria
	public void MatrizBinaria(){
		Runnable runnable = new Runnable() {
			@Override
			public void run(){
				while(true){
					// Esto se ejecuta en segundo plano, solo una vez
					boolean[][] matriz = new boolean[filas][columnas];
					for(int fila = 1 ; fila < filas; fila++){ //Iniciamos con fila, luego con la columna
						for(int columna = 1 ; columna < columnas; columna++){
							if(celda[fila][columna] .getIcon() != null)
								matriz[fila][columna] = true;
						}
					}
					try {
						Thread.sleep(5);
						//ENVIAR MATRIZ A METODO DE COMUNICACION
					} catch (InterruptedException ex) {
						Logger.getLogger(VwPrincipal.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		};
		// Creamos un hilo y le pasamos el runnable
		Thread hilo = new Thread(runnable);
		hilo.start();
	}
	
	public static void Pausar(){
		 //JOptionPane.showMessageDialog(null, "Juego pausado, se ha guardado la info.");
		
	}
	
	public static void Reiniciar(){
		//celda = celdaData.clone();
	}
	
	//Verifica si existe objeto en el camino para avanzar
	public static void VerificarObjeto(int x, int y, int up, int down, int left, int right, int space, boolean bomba, boolean explosion, boolean limpiar){
		boolean encontroTope = false;
		boolean buscar = false;
		Celda objEncontrado = new Celda();
		int newX = x;
		int newY = y;
		
		if(space == 0 && !bomba){
		outer:
		for(int fila = 1; fila < filas+1; fila++){ //Iniciamos con fila, luego con la columna
			for(int columna = 1 ; columna < columnas+1; columna++){
				//Revisa si la siguiente posicion es tope del juego
				if(up > 0 && (x-up) == 0) encontroTope = true; 
				if(down > 0 && (x+down) == 13) encontroTope = true; 
				if(left > 0 && (y-left) == 0 ) encontroTope = true; 
				if(right > 0  && (y+right) == 13) encontroTope = true;
				//Cambia de posicion
				if(!encontroTope && up > 0 && (x-up) == fila && y == columna){ buscar = true; newX = (x-up); }
				if(!encontroTope && down > 0 && (x+down) == fila && y == columna){ buscar = true; newX = (x+down); }
				if(!encontroTope && left > 0 &&  (y-left) == columna && x == fila){ buscar = true; newY = (y-left); }
				if(!encontroTope && right > 0 && (y+right) ==  columna && x == fila){ buscar = true; newY = (y+right); }
				//Obtiene la informacion del objeto encontrado en la siguiente posicion
				if(buscar){
					//System.out.println("fila " + fila + " col " + columna);
					objEncontrado = celda[fila][columna];
					break outer; 
				}
			}
		}}
		
		if(celda[x][y].getObjeto() instanceof Jugador && celda[newX][newY].isPuerta()){
			GanoJugador();
			VwPrincipal.jugadorPrincipal.setGano(true);
		}
		if(!VwPrincipal.jugadorPrincipal.isGano()){
			boolean ejecutoAccion = false;
			//Si no hay objeto en la siguiente posicion puede avanzar
			if(!explosion && !bomba && !limpiar && buscar && objEncontrado.getObjeto() == null && !objEncontrado.isPared() && !objEncontrado.isPuerta()
					&& !objEncontrado.isBomba() && !objEncontrado.isBonus()){
				ejecutoAccion = true;
				Avanzar(x, y, newX, newY);
			}
			//Deja bomba con tiempo
			if(!explosion && !bomba && space == 1){
				ejecutoAccion = true;
				DejarBomba(x,y);
			}
			//Activar bomba con tiempo 1.5s
			if(bomba) 
				ActivarBomba(x, y);
			//Explosion de bomba
			if(buscar && explosion)
				ExplosionBomba(newX, newY, up, down, left, right);
			//Limpiar explosion bomba y contar puntos
			if(buscar && limpiar){
				ReiniciarPosicionExplotada(newX, newY, up, down, left, right);
				PonerJugadorExplotado(newX, newY);
			}
			if(!ejecutoAccion  && celda[x][y].getObjeto() instanceof Enemigo){
				celda[x][y].EmpezarEnemigo(x, y);
			}
		}
	}
	
	public static void LimpiarCentro(int x, int y){
		ImageIcon icon = null;
		if(celda[x][y].getObjeto() instanceof Jugador)  
			icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/jugador.gif"));
		if(celda[x][y].isBomba())  
			 icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));
		celda[x][y].setIcon(icon);
		celda[x][y].setExplosion(false);
		celda[x][y].setPerteneceJugador(false);
	}
	
	private static void PonerJugadorExplotado(int newX, int newY){
		ImageIcon icon = null;
		//celda[newX][newY].setExplosion(false);
		if(celda[newX][newY].getObjeto() instanceof Jugador) {
			icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/jugador.gif"));
			celda[newX][newY].setIcon(icon);
		}
		if(celda[newX][newY].getObjeto() instanceof Enemigo){
			icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/enemigo.gif"));
			celda[newX][newY].setIcon(icon);
		}
		
	}
	
	//Revisa en cual posicion exploto el objeto (ya no permite explotar mas)
	private static void VerificarPosicionExplotada(int x, int y, int up, int down, int left, int right){
		for(int cont = 0; cont < VwPrincipal.jugadorPrincipal.getBonus() + 1; cont++){
			if(up > 0 && x-cont > -1)  celda[x-cont][y].setUp_exploto(true);
			if(down > 0 && x+cont < 13)  celda[x+cont][y].setDown_exploto(true);
			if(left > 0 && y-cont > -1) celda[x][y-cont].setLeft_exploto(true);
			if(right > 0 && y+cont < 13)  celda[x][y+cont].setDer_exploto(true);
		}
	}
	
	//Revisa en cual posicion exploto el objeto
	private static void ReiniciarPosicionExplotada(int x, int y, int up, int down, int left, int right){
		ImageIcon icon = null;
		if(celda[x][y].isExplosion()){
			celda[x][y].setIcon(icon);
			celda[x][y].setExplosion(false);
		}else{
			if(up > 0 )  celda[x][y].setUp_exploto(false);
			if(down > 0)  celda[x][y].setDown_exploto(false);
			if(left > 0) celda[x][y].setLeft_exploto(false);
			if(right > 0) celda[x][y].setDer_exploto(false);
			if(celda[x][y].isPerteneceJugador()) celda[x][y].setPerteneceJugador(false);
		}
	}
	
	//Revisa en cual posicion exploto el objeto
	private static boolean sePuedeExplotar(int x, int y, int up, int down, int left, int right){
		if(up > 0 && celda[x][y].isUp_exploto()) return false;
		if(down > 0 && celda[x][y].isDown_exploto()) return false;
		if(left > 0 && celda[x][y].isLeft_exploto()) return false;
		if(right > 0 && celda[x][y].isDer_exploto()) return false;
		return true;
	}
	
	private static void ExplosionBomba(int newX, int newY, int up, int down, int left, int right){
		ImageIcon icon = null;
		if(up > 0)  icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));
		if(down > 0)  icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));
		if(left > 0)  icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));
		if(right > 0)  icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));
		boolean permitido = false;
		if(!celda[newX][newY].isPuerta()){
			permitido = sePuedeExplotar(newX, newY, up, down, left, right);
		}else if(celda[newX][newY].isPuerta()){
			VerificarPosicionExplotada(newX, newY, up, down, left, right); //Media vez se verifique es porque existe un objeto
		}
		
		if(permitido){
			celda[newX][newY].setExplosion(true);
			celda[newX][newY].setIcon(icon);
			
			if(up > 0 && celda[newX+1][newY].isPerteneceJugador()) celda[newX][newY].setPerteneceJugador(true);
			if(down > 0 && celda[newX-1][newY].isPerteneceJugador()) celda[newX][newY].setPerteneceJugador(true);
			if(left > 0 && celda[newX][newY+1].isPerteneceJugador()) celda[newX][newY].setPerteneceJugador(true);
			if(right > 0 && celda[newX][newY-1].isPerteneceJugador()) celda[newX][newY].setPerteneceJugador(true);
		}
		
		if(permitido && celda[newX][newY].isPared()){ //Exploto pared
			celda[newX][newY].setPared(false);
			if(celda[newX][newY].isPerteneceJugador()){
				jugadorPrincipal.setPuntos(jugadorPrincipal.getPuntos()+1); //PARED PUNTO = 1
				jugadorPrincipal.setPared(jugadorPrincipal.getPared()+1);
			}
			VerificarPosicionExplotada(newX, newY, up, down, left, right); //Media vez se verifique es porque existe un objeto
		}else if(permitido && celda[newX][newY].isBonus()){//Exploto bonus
			celda[newX][newY].setBonus(false);
			if(celda[newX][newY].isPerteneceJugador()){
				jugadorPrincipal.setBonus(jugadorPrincipal.getBonus()+1);
			}
			VerificarPosicionExplotada(newX, newY, up, down, left, right); //Media vez se verifique es porque existe un objeto
		}else if(permitido && celda[newX][newY].getObjeto() instanceof Jugador){ //Bajar vida Jugador sino se movio
			jugadorPrincipal.setVida(jugadorPrincipal.getVida()-1);
			RevisarVidaJugador();
			//VerificarPosicionExplotada(newX, newY, up, down, left, right);
		}else if(permitido && celda[newX][newY].getObjeto() instanceof Enemigo){ //Bajar vida Jugador sino se movio
			celda[newX][newY].setIcon(null);
			celda[newX][newY].setObjeto(null);
			if(celda[newX][newY].isPerteneceJugador()){
				jugadorPrincipal.setPuntos(jugadorPrincipal.getPuntos()+2); //ENEMIGO PUNTO = 2
				jugadorPrincipal.setEnemigos(jugadorPrincipal.getEnemigos()+1);
			}
			//VerificarPosicionExplotada(newX, newY, up, down, left, right);
		}
	}
	
	private static void ActivarBomba(int x, int y){
		if(celda[x][y].getObjeto() instanceof Jugador){ //Bajar vida Jugador sino se movio
			jugadorPrincipal.setVida(jugadorPrincipal.getVida()-1);
			RevisarVidaJugador();
		}else if(celda[x][y].getObjeto() instanceof Enemigo){ //Matar enemigo
			celda[x][y].setIcon(null);
			celda[x][y].setObjeto(null);
		}
		
		ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/fuego.gif"));		
		celda[x][y].setIcon(icon);
		celda[x][y].setBomba(false);
		celda[x][y].setExplosion(true);
		celda[x][y].CrearTiempoExplosion(x, y);
	}
	
	private static void RevisarVidaJugador(){
		celda[0][0].setIcon(null);
		 fin = System.currentTimeMillis();
		double tiempo = (double) ((fin - inicio)/1000);
		jugadorPrincipal.setTiempo(tiempo);
		if(jugadorPrincipal.getVida() == 0){
			JOptionPane.showMessageDialog(null, "Has Perdido \n usuario: x \n" + "vida:  "+ jugadorPrincipal.getVida() +
					"\n bonus: " + String.valueOf(jugadorPrincipal.getBonus())  +
					"\n **" +
					"\n **muertos (+2): " + String.valueOf(jugadorPrincipal.getEnemigos()) +
					"\n **paredes (+1): " + String.valueOf(jugadorPrincipal.getPared()) +
					"\n **puntos: " + String.valueOf(jugadorPrincipal.getPuntos()) +
					"\n **tiempo (s): " +  tiempo
					, "InfoBox: FIN" , JOptionPane.INFORMATION_MESSAGE);
			 ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/muerto.gif"));
			celda[0][0].setIcon(icon);
		}
		if(jugadorPrincipal.getVida() == 1){
			 ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/1.gif"));
			celda[0][0].setIcon(icon);
		}
		if(jugadorPrincipal.getVida() == 2){
			 ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/2.gif"));
			celda[0][0].setIcon(icon);
		}
	}
	
	private static void GanoJugador(){
		 fin = System.currentTimeMillis();
		double tiempo = (double) ((fin - inicio)/1000);
		jugadorPrincipal.setTiempo(tiempo);
		JOptionPane.showMessageDialog(null, "Has Ganado \n usuario: x \n" + "vida:  "+ jugadorPrincipal.getVida() +
				"\n bonus: " + String.valueOf(jugadorPrincipal.getBonus())  +
				"\n **" +
				"\n **muertos (+2): " + String.valueOf(jugadorPrincipal.getEnemigos()) +
				"\n **paredes (+1): " + String.valueOf(jugadorPrincipal.getPared()) +
				"\n **puntos: " + String.valueOf(jugadorPrincipal.getPuntos()) +
				"\n **tiempo (s): " +  tiempo
				, "InfoBox: FIN" , JOptionPane.INFORMATION_MESSAGE);
		ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/gano.gif"));
		celda[0][0].setIcon(icon);
	}
	
	private static void DejarBomba(int x, int y){
		ImageIcon icon = null;
		icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/bomb.gif"));
		celda[x][y].setIcon(icon);
		celda[x][y].setBomba(true);
		celda[x][y].CrearTiempoBomba(x, y);
		if(celda[x][y].getObjeto() instanceof Jugador)
			celda[x][y].setPerteneceJugador(true);
		 if(celda[x][y].getObjeto() instanceof Enemigo){
			celda[x][y].EmpezarEnemigo(x, y);
		}
	}
	
	private static void Avanzar(int x, int y, int newX, int newY){
		ImageIcon icon = null;
		
		if(celda[x][y].getObjeto() instanceof Jugador){
			icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/jugador.gif"));
			celda[newX][newY].setFocusable(true);
		}
		if(celda[x][y].getObjeto() instanceof Enemigo) 
			icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/enemigo.gif"));
		
		//Celda Nueva
		if(!celda[newX][newY].isExplosion()){
			celda[newX][newY].setIcon(icon);
		}
		celda[newX][newY].setObjeto(celda[x][y].getObjeto());
		
		//Celda Anterior
		if(!celda[x][y].isBomba() && !celda[x][y].isExplosion())  //Dejar la bomba o explosion si esta puesta
			celda[x][y].setIcon(null);
		
		if(celda[newX][newY].isExplosion() && celda[x][y].getObjeto() instanceof Jugador){ //Bajar vida Jugador sino se movio
			RevisarVidaJugador();
		}
		
		if(celda[newX][newY].isExplosion() && celda[x][y].getObjeto() instanceof Enemigo){ //Bajar vida Enemigo sino se movio
			celda[newX][newY].setIcon(null);
			celda[newX][newY].setObjeto(null);
		}else if(!celda[newX][newY].isExplosion() && celda[x][y].getObjeto() instanceof Enemigo){
			celda[newX][newY].EmpezarEnemigo(newX, newY);
		}
		
		if(celda[x][y].getObjeto() instanceof Jugador) {
			jugadorPrincipal.setX(newX);
			jugadorPrincipal.setY(newY);
		}
		
		celda[x][y].setObjeto(null);
		celda[x][y].setFocusable(false);
	}
	
	private void  obtenerData() throws FileNotFoundException, IOException{
		celdaData = new Celda[13][13]; //Celda con la informacion del archivo
		String cadena = "";
		try{
			String ruta = Paths.get(".").toAbsolutePath().normalize().toString();
			FileReader f;
			if(tipoJuego == 0)
				f = new FileReader(ruta+"/src/carga/campoEntrenamiento.bomb");
			else
				f = new FileReader(ruta+"/src/carga/" + nombreArchivo);
			
			BufferedReader b = new BufferedReader(f);
			
			int col = 1;
			int fil = 1;
			while((cadena = b.readLine())!=null) {
				System.out.println(cadena);
				char[] caracteres = cadena.toCharArray();
				col = 1;
				for (int i = 0; i < caracteres.length; i++) {
					celdaData[fil][col] = new Celda();
					celdaData[fil][col].setFila(fil);
					celdaData[fil][col].setColumna(col);
					switch((int)caracteres[i]){
						case (int)'X':
							celdaData[fil][col].setIcon(ima_wall);
							celdaData[fil][col].setPared(true);
							break;
						case (int)'x':
							celdaData[fil][col].setIcon(ima_wall);
							celdaData[fil][col].setPared(true);
							break;
						case (int)'J':
							celdaData[fil][col].setIcon(ima_player);
							Jugador jugador = new Jugador();
							jugadorPrincipal.setVida(3);
							
							celdaData[fil][col].setObjeto(jugador);
							celdaData[fil][col].setFocusable(true);
							break;
						case (int)'j':
							celdaData[fil][col].setIcon(ima_player);
							Jugador jugador2 = new Jugador();
							jugadorPrincipal.setVida(3);
							
							celdaData[fil][col].setObjeto(jugador2);
							celdaData[fil][col].setFocusable(true);
							break;
						case (int)'L':
							celdaData[fil][col].setIcon(ima_puerta);
							celdaData[fil][col].setPuerta(true);
							break;
						case (int)'l':
							celdaData[fil][col].setIcon(ima_puerta);
							celdaData[fil][col].setPuerta(true);
							break;
						case (int)'B':
							celdaData[fil][col].setIcon(ima_bonus);
							celdaData[fil][col].setBonus(true);
							break;
						case (int)'b':
							celdaData[fil][col].setIcon(ima_bonus);
							celdaData[fil][col].setBonus(true);
							break;
						case (int)'E':
							celdaData[fil][col].setIcon(ima_enemy);
							Enemigo enemigo = new Enemigo();	
							enemigo.setVida(1);
							celdaData[fil][col].setObjeto(enemigo);
							celdaData[fil][col].EmpezarEnemigo(fil, col);
							break;
						case (int)'e':
							celdaData[fil][col].setIcon(ima_enemy);
							Enemigo enemigo2 = new Enemigo();	
							enemigo2.setVida(1);
							celdaData[fil][col].setObjeto(enemigo2);
							celdaData[fil][col].EmpezarEnemigo(fil, col);
							break;
					}
					col++;
				}
				fil++;
			}
			b.close();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	/**
	 * Crea las celdas
	 */
	public void actionPerformed(ActionEvent e) {
		try{
			String row = "12";
			String column = "12";
			inicio = System.currentTimeMillis();
			if((Integer.parseInt(row) + 1) >= filas && (Integer.parseInt(column) + 1) >= columnas){
				String[] arrEncabezado = generarEncabezados(Integer.parseInt(column));
				filas = Integer.parseInt(row) + 1;
				columnas = Integer.parseInt(column) + 1;
				pnlCelda.setLayout(new GridLayout (filas,columnas));
				pnlCelda.removeAll(); //Limpiamos el panel.
				celda = new Celda[filas][columnas]; //Celda para el juego
				obtenerData();
				
				
				for(int fila = 0 ; fila < filas; fila++){ //Iniciamos con fila, luego con la columna
					for(int columna = 0 ; columna < columnas; columna++){
						celda[fila][columna] = new Celda();
						celda[fila][columna].setFila(fila);
						celda[fila][columna].setColumna(columna);
						if(celdaData[fila][columna] != null)
							celda[fila][columna] = celdaData[fila][columna]; //Ingresamos la informacion a nuestro juego
						
						if(columna == 0 && fila == 0){
							Vida(0, fila, columna, 3);
						}
						
						if(columna >= 1 && fila >= 1){
							celda[fila][columna].setNombre(fila, columna, arrEncabezado[columna - 1]);
						}

						if(columna >= 1 && fila == 0){
							celda[fila][columna].setEditar(false);
							celda[fila][columna].setEncabezado(arrEncabezado[columna - 1]);
							celda[fila][columna].centrarTexto();
						}

						if(columna == 0 && fila == 0){
							celda[fila][columna].setEditar(false);
						}

						if(columna == 0 && fila > 0){
						   celda[fila][columna].setEditar(false);
						   celda[fila][columna].setPosEnumeracion(String.valueOf(fila));
						   celda[fila][columna].centrarTexto();
						}
						pnlCelda.add(celda[fila][columna]);
					}
				}
				MatrizBinaria();
			}
		}catch(Exception ex){
		}
	}
	//Remover vida
	public void Vida(int remover, int fila, int columna, int vida){
		//celda[fila][columna].setEncabezado(String.valueOf(vida - remover));
		//celda[fila][columna].setBorder(new LineBorder(Color.LIGHT_GRAY, 26));
		ImageIcon icon = new ImageIcon(VwPrincipal.class.getClass().getResource("/Imagenes/3.gif"));
		celda[0][0].setIcon(icon);
		
	}
	
	/**
	 * Crea los titulos de los encabezados
	 */
	public String[] generarEncabezados(int numColumnas){
		String[] arrEncabezados = new String[numColumnas];
		String[] arrLetras = {"A","B","C","D","E","F","G","H","I","J","K","L",
						   "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int letra = 0;
		int contadorAlfabeto = 0;

		for(int cont = 0; cont <= numColumnas - 1; cont++){
			if(cont > 25){
				if(letra <= 25){
					arrEncabezados[cont] = arrLetras[letra] + arrLetras[contadorAlfabeto];
					contadorAlfabeto++;
					if(contadorAlfabeto >= 26){
						letra++;
						contadorAlfabeto = 0;
					}
				}
			}else{
				arrEncabezados[cont] = arrLetras[cont];
			}
		}
		return arrEncabezados;
	}
	
		/**
	 * @return the contFor
	 */
	public static int getContFor() {
		return contFor;
	}

	/**
	 * @param aContFor the contFor to set
	 */
	public static void setContFor(int aContFor) {
		contFor = aContFor;
	}
	
	/**
	 * @return the contFor2
	 */
	public static int getContFor2() {
		return contFor2;
	}

	/**
	 * @param aContFor2 the contFor2 to set
	 */
	public static void setContFor2(int aContFor2) {
		contFor2 = aContFor2;
	}
	
	public static int getRandom(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}

