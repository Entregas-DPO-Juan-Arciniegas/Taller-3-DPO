package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	private Avion avion;
	private String fecha;
	private Ruta ruta;
	private Map<String, Tiquete> tiquetes;

	public Vuelo​(Ruta ruta, String fecha, Avion avion) {
		super();
		this.ruta = ruta;
		this.fecha = fecha;
		this.avion = avion;

	}

	public boolean equals(Object obj) {
		return this.equals(obj);
	}

	public Avion getAvion() {
		return this.avion;

	}

	public String getFecha() {
		return this.fecha;
	}

	public Ruta getRuta() {
		return this.ruta;
	}

	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes.values();
	}

	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad)
			throws VueloSobrevendidoException {

		if (this.avion.getCapacidad() < cantidad + this.getTiquetes().size()) {
			throw new VueloSobrevendidoException(this );
		}

		int tarifa = calculadora.calcularTarifa(this, cliente);

		for (int i = 0; i < cantidad; i++) {
			Tiquete tiquete = GeneradorTiquetes.generarTiquete(this, cliente, tarifa);
			this.tiquetes.put(tiquete.getCodigo(), tiquete);

		}
		return (int) (tarifa * cantidad);
	}
}
