package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea {
	private String AEROPUERTOS = "aeropuertos";
	private String AVIONES = "aviones";
	private String RUTAS = "rutas";
	private String VUELOS = "vuelos";

	@Override
	public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException {
		String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
		JSONObject raiz = new JSONObject(jsonCompleto);

		Map<String, Aeropuerto> aeropuertosCargados = cargarAeropuertos(aerolinea, raiz.getJSONArray(AEROPUERTOS));
		Map<String, Avion> avionesCargados = cargarAviones(aerolinea, raiz.getJSONArray(AVIONES));
		Map<String, Ruta> rutasCargadas = cargarRutas(aerolinea, raiz.getJSONArray(RUTAS), aeropuertosCargados);
		cargarVuelos(aerolinea, raiz.getJSONArray(VUELOS), avionesCargados, rutasCargadas);

	}

	private Map<String, Aeropuerto> cargarAeropuertos(Aerolinea aerolinea, JSONArray jAeropuertos) throws InformacionInconsistenteException {
		Map<String, Aeropuerto> resultado = new HashMap<>();

		for (int i = 0; i < jAeropuertos.length(); i++) {
			JSONObject jAeropuerto = jAeropuertos.getJSONObject(i);

			String nombre = jAeropuerto.getString("nombre");
			String codigo = jAeropuerto.getString("codigo");
			String nombreCiudad = jAeropuerto.getString("nombreCiudad");
			double latitud = jAeropuerto.getDouble("latitud");
			double longitud = jAeropuerto.getDouble("longitud");

			try {
				Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo, nombreCiudad, latitud, longitud);
				resultado.put(codigo, aeropuerto);
			} catch (Exception e) {
				throw new InformacionInconsistenteException(e.getMessage());
			}
		}

		return resultado;

	}

	private Map<String, Avion> cargarAviones(Aerolinea aerolinea, JSONArray jAviones) {
		Map<String, Avion> resultado = new HashMap<>();

		for (int i = 0; i < jAviones.length(); i++) {
			JSONObject jAvion = jAviones.getJSONObject(i);
			String nombre = jAvion.getString("nombre");
			int capacidad = jAvion.getInt("capacidad");

			Avion avion = new Avion(nombre, capacidad);
			resultado.put(nombre, avion);
			aerolinea.agregarAvion(avion);
		}
		return resultado;

	}

	private Map<String, Ruta> cargarRutas(Aerolinea aerolinea, JSONArray jRutas, Map<String, Aeropuerto> aeropuertosCargados) {
		Map<String, Ruta> resultado = new HashMap<>();

		for (int i = 0; i < jRutas.length(); i++) {
			JSONObject jRuta = jRutas.getJSONObject(i);

			Aeropuerto origen = aeropuertosCargados.get(jRuta.getString("origen"));
			Aeropuerto destino = aeropuertosCargados.get(jRuta.getString("destino"));
			String horaSalida = jRuta.getString("horaSalida");
			String horaLlegada = jRuta.getString("horaLlegada");
			String codigoRuta = jRuta.getString("codigoRuta");

			Ruta ruta = new Ruta(origen, destino, horaSalida, horaLlegada, codigoRuta);
			resultado.put(codigoRuta, ruta);
			aerolinea.agregarRuta(ruta);
		}
		return resultado;

	}

	private void cargarVuelos(Aerolinea aerolinea, JSONArray jVuelos, Map<String, Avion> avionesCargados, Map<String, Ruta> rutasCargadas) {
		for (int i = 0; i < jVuelos.length(); i++) {
			JSONObject jVuelo = jVuelos.getJSONObject(i);

			String fecha = jVuelo.getString("fecha");
			Ruta ruta = rutasCargadas.get(jVuelo.getString("codigoRuta"));
			Avion nombreAvion = avionesCargados.get(jVuelo.getString("nombreAvion"));

			Vuelo vuelo = new Vuelo(ruta, fecha, nombreAvion);
			aerolinea.agregarVuelo(vuelo);

		}
	}

	@Override
	public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {
		JSONObject jobject = new JSONObject();

		salvarAeropuertos(aerolinea, jobject);
		salvarVuelos(aerolinea, jobject);
		salvarRutas(aerolinea, jobject);
		salvarAviones(aerolinea, jobject);

		PrintWriter pw = new PrintWriter(archivo);
		jobject.write(pw, 2, 0);
		pw.close();
	}

	private void salvarAeropuertos(Aerolinea aerolinea, JSONObject jobject) {
		JSONArray jAeropuertos = new JSONArray();
		Map<String, Aeropuerto> aeropuertosMapa = new HashMap<>();

		for (Ruta ruta : aerolinea.getRutas()) {

			String codigoOrigen = ruta.getOrigen().getCodigo();
			String codigoDestino = ruta.getDestino().getCodigo();

			if (!aeropuertosMapa.containsKey(codigoOrigen)) {
				aeropuertosMapa.put(codigoOrigen, ruta.getOrigen());
			}
			if (!aeropuertosMapa.containsKey(codigoDestino)) {
				aeropuertosMapa.put(codigoDestino, ruta.getDestino());
			}
		}

		for (Aeropuerto aeropuerto : aeropuertosMapa.values()) {
			JSONObject jAeropuerto = new JSONObject();
			jAeropuerto.put("nombre", aeropuerto.getNombre());
			jAeropuerto.put("codigo", aeropuerto.getCodigo());
			jAeropuerto.put("nombreCiudad", aeropuerto.getNombreCiudad());
			jAeropuerto.put("latitud", aeropuerto.getLatitud());
			jAeropuerto.put("longitud", aeropuerto.getLongitud());

			jAeropuertos.put(jAeropuerto);
		}

		jobject.put(AEROPUERTOS, jAeropuertos);
	}

	private void salvarAviones(Aerolinea aerolinea, JSONObject jobject) {
		JSONArray jAviones = new JSONArray();
		for (Avion avion : aerolinea.getAviones()) {
			JSONObject jAvion = new JSONObject();
			jAvion.put("nombre", avion.getNombre());
			jAvion.put("capacidad", avion.getCapacidad());

			jAviones.put(jAvion);
		}
		jobject.put(AVIONES, jAviones);
	}

	private void salvarRutas(Aerolinea aerolinea, JSONObject jobject) {
		JSONArray jRutas = new JSONArray();
		for (Ruta ruta : aerolinea.getRutas()) {
			JSONObject jRuta = new JSONObject();
			jRuta.put("codigoRuta", ruta.getCodigoRuta());
			jRuta.put("origen", ruta.getOrigen().getCodigo());
			jRuta.put("destino", ruta.getDestino().getCodigo());
			jRuta.put("horaSalida", ruta.getHoraSalida());
			jRuta.put("horaLlegada", ruta.getHoraLlegada());

			jRutas.put(jRuta);
		}
		jobject.put(RUTAS, jRutas);
	}

	private void salvarVuelos(Aerolinea aerolinea, JSONObject jobject) {
		JSONArray jVuelos = new JSONArray();
		for (Vuelo vuelo : aerolinea.getVuelos()) {
			JSONObject jVuelo = new JSONObject();
			jVuelo.put("fecha", vuelo.getFecha());
			jVuelo.put("codigoRuta", vuelo.getRuta().getCodigoRuta());
			jVuelo.put("nombreAvion", vuelo.getAvion().getNombre());
			
			jVuelos.put(jVuelo);
		}
		jobject.put(VUELOS, jVuelos);
	}

}
