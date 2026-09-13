package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public abstract class CalculadoraTarifas {
	static double IMPUESTO = 0.28;

	public CalculadoraTarifas() {
	}

	protected abstract int calculadoraCostoBase(Vuelo vuelo, Cliente cliente);
	protected abstract double calcularPorcentajeDescuento​(Cliente cliente);
	

	protected double calcularDistanciaVuelo(Ruta ruta) {
		Aeropuerto aeropuertoa = ruta.getDestino();
		Aeropuerto aeropuertob = ruta.getOrigen();

		int distancia = Aeropuerto.calcularDistancia(aeropuertoa, aeropuertob);
		return distancia;
	}

	public int calcularTarifa​(Vuelo vuelo, Cliente cliente) {
		double costoBase = this.calculadoraCostoBase(vuelo, cliente);
		double descuento = costoBase + this.calcularPorcentajeDescuento(cliente);
		double precio = costoBase - descuento;

		int impuesto = calcularValorImpuestos((int) precio);
		double resultado = precio + impuesto;

		return (int) resultado;
	}

	protected int calcularValorImpuestos(int costoBase) {
		return (int) (costoBase * IMPUESTO);
	}

}
