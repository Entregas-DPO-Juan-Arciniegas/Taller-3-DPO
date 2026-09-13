package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {

	protected int COSTO_POR_KM_CORPORATIVO = 900;
	protected int COSTO_POR_KM_NATURAL = 600;
	protected double DESCUENTO_GRANDES = 0.2;
	protected double DESCUENTO_MEDIANAS = 0.1;
	protected double DESCUENTO_PEQ = 0.02;

	public CalculadoraTarifasTemporadaBaja() {
	}

	@Override
	protected int calculadoraCostoBase(Vuelo vuelo, Cliente cliente) {
		if (cliente.getTipoCliente() == "CORPORATIVO") {
			return (int) (COSTO_POR_KM_CORPORATIVO * calcularDistanciaVuelo(vuelo.getRuta()));
		} else {
			return (int) (COSTO_POR_KM_NATURAL * calcularDistanciaVuelo(vuelo.getRuta()));

		}
	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		if (cliente.getTipoCliente() == "CORPORATIVO") {
			return DESCUENTO_GRANDES;
		} else {
			return DESCUENTO_MEDIANAS;
		}
	}

}
