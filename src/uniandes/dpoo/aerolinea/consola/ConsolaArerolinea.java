package uniandes.dpoo.aerolinea.consola;

import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;

public class ConsolaArerolinea extends ConsolaBasica {
	private Aerolinea unaAerolinea;

	/**
	 * Es un método que corre la aplicación y realmente no hace nada interesante:
	 * sólo muestra cómo se podría utilizar la clase Aerolínea para hacer pruebas.
	 */
	public void correrAplicacion() {
		try {
			unaAerolinea = new Aerolinea();
			// String archivo = this.pedirCadenaAlUsuario( "Digite el nombre del archivo
			// json con la información de una aerolinea" );
			String archivo1 = "aerolinea.json";
			String archivo2 = "tiquetes.json";
			unaAerolinea.cargarAerolinea("./datos/" + archivo1, CentralPersistencia.JSON);
			unaAerolinea.cargarTiquetes("./datos/" + archivo2, CentralPersistencia.JSON);
		} catch (TipoInvalidoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InformacionInconsistenteException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		try {
			ConsolaArerolinea ca = new ConsolaArerolinea();
			ca.correrAplicacion();
			
			//prueba
			
			Avion avion1 = new Avion("FELIPE", 4);
			ca.unaAerolinea.agregarAvion(avion1);

			Aeropuerto aeropuerto1 = new Aeropuerto("casaGrande", "234", "santoDomingo", 20.4, 23.0);
			Aeropuerto aeropuerto2 = new Aeropuerto("CasaPequeña", "235", "santoDomingo", 20.4, 23.0);

			Ruta ruta = new Ruta(aeropuerto1, aeropuerto2, "0224", "0324", "5069");
			ca.unaAerolinea.agregarRuta(ruta);

			String fecha = "2024-11-05";
			String codigoRuta = "5069";
			String nombreAvion = "FELIPE";

			ca.unaAerolinea.programarVuelo(fecha, codigoRuta, nombreAvion);
			
			//print de prueba
			Vuelo vuelocargado = ca.unaAerolinea.getVuelo(codigoRuta, fecha);
			System.out.println("fecha: " + vuelocargado.getFecha());
			System.out.println("Avion: " + vuelocargado.getAvion().getNombre());
			System.out.println("Codigo Ruta: " + vuelocargado.getRuta().getCodigoRuta());
			
			//guardado prueba
			
			ca.unaAerolinea.salvarAerolinea("./datos/aerolinea.json", CentralPersistencia.JSON);

			ca.unaAerolinea.salvarTiquetes("./datos/tiquetes.json", CentralPersistencia.JSON);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		

	}
}
