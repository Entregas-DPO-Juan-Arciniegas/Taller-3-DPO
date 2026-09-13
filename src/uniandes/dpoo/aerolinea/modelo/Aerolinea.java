package uniandes.dpoo.aerolinea.modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaAlta;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaBaja;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaAerolinea;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaTiquetes;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

/**
 * En esta clase se organizan todos los aspectos relacionados con una Aerolínea.
 * 
 * Por un lado, esta clase cumple un rol central como estructurador para todo el
 * resto de elementos: directa o indirectamente, todos están contenidos y se
 * pueden acceder a través de la clase Aerolínea.
 * 
 * Por otro lado, esta clase implementa algunas funcionalidades adicionales a su
 * rol como estructurador, para lo cual se apoya en las otras clases que hacen
 * parte del proyecto.
 */
public class Aerolinea {
	private static CalculadoraTarifas CalculadoraTarifas = null;

	/**
	 * Una lista con los aviones de los que dispone la aerolínea
	 */
	private List<Avion> aviones;

	/**
	 * Un mapa con las rutas que cubre la aerolínea.
	 * 
	 * Las llaves del mapa son el código de la ruta, mientras que los valores son
	 * las rutas
	 */
	private Map<String, Ruta> rutas;

	/**
	 * Una lista de los vuelos programados por la aerolínea
	 */
	private List<Vuelo> vuelos;

	/**
	 * Un mapa con los clientes de la aerolínea.
	 * 
	 * Las llaves del mapa son los identificadores de los clientes, mientras que los
	 * valores son los clientes
	 */
	private Map<String, Cliente> clientes;

	/**
	 * Construye una nueva aerolínea con un nombre e inicializa todas las
	 * contenedoras con estructuras vacías
	 */
	public Aerolinea() {
		aviones = new LinkedList<Avion>();
		rutas = new HashMap<String, Ruta>();
		vuelos = new LinkedList<Vuelo>();
		clientes = new HashMap<String, Cliente>();
	}

	// ************************************************************************************
	//
	// Estos son los métodos que están relacionados con la manipulación básica de
	// los atributos
	// de la aerolínea (consultar, agregar)
	//
	// ************************************************************************************

	/**
	 * Agrega una nueva ruta a la aerolínea
	 * 
	 * @param ruta
	 */

	public void agregarVuelo(Vuelo vuelo) {
		this.vuelos.add(vuelo);
	}

	public void agregarRuta(Ruta ruta) {
		this.rutas.put(ruta.getCodigoRuta(), ruta);
	}

	/**
	 * Agrega un nuevo avión a la aerolínea
	 * 
	 * @param avion
	 */
	public void agregarAvion(Avion avion) {
		this.aviones.add(avion);
	}

	/**
	 * Agrega un nuevo cliente a la aerolínea
	 * 
	 * @param cliente
	 */
	public void agregarCliente(Cliente cliente) {
		this.clientes.put(cliente.getIdentificador(), cliente);
	}

	/**
	 * Verifica si ya existe un cliente con el identificador dado
	 * 
	 * @param identificadorCliente
	 * @return Retorna true si ya existía un cliente con el identificador,
	 *         independientemente de su tipo
	 */
	public boolean existeCliente(String identificadorCliente) {
		return this.clientes.containsKey(identificadorCliente);
	}

	/**
	 * Busca el cliente con el identificador dado
	 * 
	 * @param identificadorCliente
	 * @return Retorna el cliente con el identificador, o null si no existía
	 */
	public Cliente getCliente(String identificadorCliente) {
		return this.clientes.get(identificadorCliente);
	}

	/**
	 * Retorna todos los aviones de la aerolínea
	 * 
	 * @return
	 */
	public Collection<Avion> getAviones() {
		return aviones;
	}

	/**
	 * Retorna todas las rutas disponibles para la aerolínea
	 * 
	 * @return
	 */
	public Collection<Ruta> getRutas() {
		return rutas.values();
	}

	/**
	 * Retorna la ruta de la aerolínea que tiene el código dado
	 * 
	 * @param codigoRuta El código de la ruta buscada
	 * @return La ruta con el código, o null si no existe una ruta con ese código
	 */
	public Ruta getRuta(String codigoRuta) {
		return rutas.get(codigoRuta);
	}

	/**
	 * Retorna todos los vuelos de la aerolínea
	 * 
	 * @return
	 */
	public Collection<Vuelo> getVuelos() {
		return vuelos;
	}

	/**
	 * Busca un vuelo dado el código de la ruta y la fecha del vuelo.
	 * 
	 * @param codigoRuta
	 * @param fechaVuelo
	 * @return Retorna el vuelo que coincide con los parámetros dados. Si no lo
	 *         encuentra, retorna null.
	 */
	public Vuelo getVuelo(String codigoRuta, String fechaVuelo) {
		for (Vuelo vuelo : this.vuelos) {
			if (vuelo.getFecha().equals(fechaVuelo) && vuelo.getRuta().getCodigoRuta().equals(codigoRuta)) {
				return vuelo;
			}
		}
		return null;
	}

	/**
	 * Retorna todos los clientes de la aerolínea
	 * 
	 * @return
	 */
	public Collection<Cliente> getClientes() {
		return clientes.values();
	}

	/**
	 * Retorna todos los tiquetes de la aerolínea, los cuales se recolectan vuelo
	 * por vuelo
	 * 
	 * @return
	 */
	public Collection<Tiquete> getTiquetes() {
		List<Tiquete> tiquetes = new ArrayList<>();
		for (Vuelo vuelo : this.vuelos) {
			for (Tiquete tiquete : vuelo.getTiquetes()) {
				tiquetes.add(tiquete);
			}
		}
		return tiquetes;

	}

	// ************************************************************************************
	//
	// Estos son los métodos que están relacionados con la persistencia de la
	// aerolínea
	//
	// ************************************************************************************

	/**
	 * Carga toda la información de la aerolínea a partir de un archivo
	 * 
	 * @param archivo     El nombre del archivo.
	 * @param tipoArchivo El tipo del archivo. Puede ser CentralPersistencia.JSON o
	 *                    CentralPersistencia.PLAIN.
	 * @throws TipoInvalidoException             Se lanza esta excepción si se
	 *                                           indica un tipo de archivo inválido
	 * @throws IOException                       Lanza esta excepción si hay
	 *                                           problemas leyendo el archivo
	 * @throws InformacionInconsistenteException Lanza esta excepción si durante la
	 *                                           carga del archivo se encuentra
	 *                                           información que no es consistente
	 */
	public void cargarAerolinea(String archivo, String tipoArchivo)
			throws TipoInvalidoException, IOException, InformacionInconsistenteException {
		IPersistenciaAerolinea cargador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
		cargador.cargarAerolinea(archivo, this);
	}

	/**
	 * Salva la información de la aerlínea en un archivo
	 * 
	 * @param archivo     El nombre del archivo.
	 * @param tipoArchivo El tipo del archivo. Puede ser CentralPersistencia.JSON o
	 *                    CentralPersistencia.PLAIN.
	 * @throws TipoInvalidoException Se lanza esta excepción si se indica un tipo de
	 *                               archivo inválido
	 * @throws IOException           Lanza esta excepción si hay problemas
	 *                               escribiendo en el archivo
	 */
	public void salvarAerolinea(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException {
		IPersistenciaAerolinea cargador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
		cargador.salvarAerolinea(archivo, this);
	}

	/**
	 * Carga toda la información de sobre los clientes y tiquetes de una aerolínea a
	 * partir de un archivo
	 * 
	 * @param archivo     El nombre del archivo.
	 * @param tipoArchivo El tipo del archivo. Puede ser CentralPersistencia.JSON o
	 *                    CentralPersistencia.PLAIN.
	 * @throws TipoInvalidoException             Se lanza esta excepción si se
	 *                                           indica un tipo de archivo inválido
	 * @throws IOException                       Lanza esta excepción si hay
	 *                                           problemas leyendo el archivo
	 * @throws InformacionInconsistenteException Lanza esta excepción si durante la
	 *                                           carga del archivo se encuentra
	 *                                           información que no es consistente
	 *                                           con la información de la aerolínea
	 */
	public void cargarTiquetes(String archivo, String tipoArchivo)
			throws TipoInvalidoException, IOException, InformacionInconsistenteException {
		IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes(tipoArchivo);
		cargador.cargarTiquetes(archivo, this);
	}

	/**
	 * Salva la información de la aerlínea en un archivo
	 * 
	 * @param archivo     El nombre del archivo.
	 * @param tipoArchivo El tipo del archivo. Puede ser CentralPersistencia.JSON o
	 *                    CentralPersistencia.PLAIN.
	 * @throws TipoInvalidoException Se lanza esta excepción si se indica un tipo de
	 *                               archivo inválido
	 * @throws IOException           Lanza esta excepción si hay problemas
	 *                               escribiendo en el archivo
	 */
	public void salvarTiquetes(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException {
		IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes(tipoArchivo);
		cargador.salvarTiquetes(archivo, this);
	}

	// ************************************************************************************
	//
	// Estos son los métodos que están relacionados con funcionalidades interesantes
	// de la aerolínea
	//
	// ************************************************************************************

	/**
	 * Agrega un nuevo vuelo a la aerolínea, para que se realice en una cierta
	 * fecha, en una cierta ruta y con un cierto avión.
	 * 
	 * Este método debe verificar que el avión seleccionado no esté ya ocupado para
	 * otro vuelo en el mismo intervalo de tiempo del nuevo vuelo. No es necesario
	 * verificar que se encuentre en el lugar correcto (origen del vuelo).
	 * 
	 * @param fecha       La fecha en la que se realizará el vuelo
	 * @param codigoRuta  La ruta que cubirá el vuelo
	 * @param nombreAvion El nombre del avión que realizará el vuelo
	 * @throws Exception Lanza esta excepción si hay algún problema con los datos
	 *                   suministrados
	 */
	public void programarVuelo(String fecha, String codigoRuta, String nombreAvion) throws Exception {
		Avion avion1 = null;
		for (Avion avion : this.getAviones()) {
			if (avion.getNombre().equals(nombreAvion)) {
				avion1 = avion;
			}
		}
		if (avion1 == null) {
			throw new IllegalArgumentException("El avion " + nombreAvion + " no existe.");
		}
		
		
		

		Ruta ruta = this.getRuta(codigoRuta);

		String horaInicio1 = ruta.getHoraSalida();
		String horaFin1 = ruta.getHoraLlegada();

		
		
		for (Vuelo vuelo2 : this.getVuelos()) {
			String horaInicio2 = vuelo2.getRuta().getHoraSalida();
			String horaFin2 = vuelo2.getRuta().getHoraLlegada();

			boolean mismafecha = vuelo2.getFecha().equals(fecha);
			boolean mismoAvion = nombreAvion.equals(vuelo2.getAvion().getNombre());
			boolean despues = horaFin1.compareTo(horaInicio2) < 0;
			boolean antes = horaInicio1.compareTo(horaFin2) > 0;

			if (mismafecha && mismoAvion && (despues || antes)) {
				throw new IllegalArgumentException("El avion " + nombreAvion + " ya esta ocupado en otro vuelo.");
			} 
			
		}

		Vuelo vuelo = new Vuelo(ruta, fecha, avion1);
		this.agregarVuelo(vuelo);
	}

	/**
	 * Vende una cierta cantidad de tiquetes para un vuelo, verificando que la
	 * información sea correcta.
	 * 
	 * Los tiquetes deben quedar asociados al vuelo y al cliente.
	 * 
	 * Según la fecha del vuelo, se deben usar las tarifas de temporada baja (enero
	 * a mayo y septiembre a noviembre) o las de temporada alta (el resto del año).
	 * 
	 * @param identificadorCliente El identificador del cliente al cual se le venden
	 *                             los tiquetes
	 * @param fecha                La fecha en la que se realiza el vuelo para el
	 *                             que se van a vender los tiquetes
	 * @param codigoRuta           El código de la ruta para el que se van a vender
	 *                             los tiquetes
	 * @param cantidad             La cantidad de tiquetes que se quieren comprar
	 * @return El valor total de los tiquetes vendidos
	 * @throws VueloSobrevendidoException Se lanza esta excepción si no hay
	 *                                    suficiente espacio en el vuelo para todos
	 *                                    los pasajeros
	 * @throws Exception                  Se lanza esta excepción para indicar que
	 *                                    no se pudieron vender los tiquetes por
	 *                                    algún otro motivo
	 */
	public int venderTiquetes(String identificadorCliente, String fecha, String codigoRuta, int cantidad)
			throws VueloSobrevendidoException, Exception {

		int mes = Integer.parseInt(fecha.split("-")[1]);

		if ((mes >= 1 && mes <= 5) || (mes >= 9 && mes <= 11)) {
			CalculadoraTarifas = new CalculadoraTarifasTemporadaBaja();
		} else {
			CalculadoraTarifas = new CalculadoraTarifasTemporadaAlta();
		}

		Cliente cliente = this.getCliente(identificadorCliente);
		Vuelo vuelo = this.getVuelo(codigoRuta, fecha);

		int total = vuelo.venderTiquetes(cliente, CalculadoraTarifas, cantidad);
		return total;
	}

	/**
	 * Registra que un cierto vuelo fue realizado
	 * 
	 * @param fecha      La fecha del vuelo
	 * @param codigoRuta El código de la ruta que recorrió el vuelo
	 */
	public void registrarVueloRealizado(String fecha, String codigoRuta) {
		Vuelo vuelo = this.getVuelo(codigoRuta, fecha);
		Collection<Tiquete> tiquetes = vuelo.getTiquetes();
		for (Tiquete tiquete : tiquetes) {
			tiquete.getCliente().usarTiquetes(vuelo);
		}
	}

	/**
	 * Calcula cuánto valen los tiquetes que ya compró un cliente dado y que todavía
	 * no ha utilizado
	 * 
	 * @param identificadorCliente El identificador del cliente
	 * @return La suma de lo que pagó el cliente por los tiquetes sin usar
	 */
	public String consultarSaldoPendienteCliente(String identificadorCliente) {
		int valor = this.getCliente(identificadorCliente).calcularValorTotalTiquetesSinUsar(); // esta es una nueva
																								// funcion que cree
																								// porque la otra
																								// calcula el valor
																								// total de todos los
																								// tiquetes comprados
																								// incluyendo los usados
		return Integer.toString(valor);
	}

}
