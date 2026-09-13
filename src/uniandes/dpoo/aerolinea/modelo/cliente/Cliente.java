package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.*;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {
	private List<Tiquete> tiquetesSinUsar;
	private List<Tiquete> tiquetesUsados;

	public Cliente() {
		super();
		this.tiquetesSinUsar = new ArrayList<>();
		this.tiquetesUsados = new ArrayList<>();
	}

	public void agregarTiquete​(Tiquete tiquete) {
		this.tiquetesSinUsar.add(tiquete);
	}

	public int calcularValorTotalTiquetes() {
		int valorTotal = 0;

		for (Tiquete tiqueteSU : this.tiquetesSinUsar) {
			valorTotal += tiqueteSU.getTarifa();
		}
		for (Tiquete tiqueteU : this.tiquetesUsados) {
			valorTotal += tiqueteU.getTarifa();
		}
		return valorTotal;
	}
	
	public int calcularValorTotalTiquetesSinUsar() {
		int valorTotal = 0;

		for (Tiquete tiqueteSU : this.tiquetesSinUsar) {
			valorTotal += tiqueteSU.getTarifa();
		}
		return valorTotal;
	}
	

	public abstract String getIdentificador();

	public abstract String getTipoCliente();

	public void usarTiquetes​(Vuelo vuelo) {
		List<Tiquete> paraEliminar = new ArrayList<>();

		for (Tiquete tiquete : this.tiquetesSinUsar) {
			if (tiquete.getVuelo().equals(vuelo)) {
				tiquete.marcarComoUsado();
				this.tiquetesUsados.add(tiquete);
				paraEliminar.add(tiquete);
			}

			for (Tiquete tiqueteE : paraEliminar) {
				this.tiquetesSinUsar.remove(tiqueteE);
			}
		}

	}
}
