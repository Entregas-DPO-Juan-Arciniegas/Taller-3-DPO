package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public class CalculadoraTarifasTemporadaAlta extends CalculadoraTarifas {

	protected int COSTO_POR_KM = 1000;

	public CalculadoraTarifasTemporadaAlta() {
	}

	@Override
	protected int calculadoraCostoBase(Vuelo vuelo, Cliente cliente) {
		return (int) (COSTO_POR_KM * calcularDistanciaVuelo(vuelo.getRuta()));

	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		return 0;
	}

}
