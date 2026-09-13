package uniandes.dpoo.aerolinea.tiquetes;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public class Tiquete {
	private Cliente	cliente;
	private String	codigo;
	private int	tarifa;
	private boolean	usado;
	private Vuelo vuelo;



	public Tiquete​(String codigo, Vuelo vuelo, Cliente clienteComprador, int tarifa){
		super();
		this.codigo = codigo;
		this.vuelo = vuelo;
		this.cliente = clienteComprador;
		this.tarifa = tarifa;
		this.usado = false;
		
		clienteComprador.agregarTiquete(this);
	}
	
	public boolean esUsado() {
		return this.usado;
	}
	
	public Cliente getCliente() {
		return this.cliente;
	}
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public int getTarifa() {
		return this.tarifa;
	}
	
	public Vuelo getVuelo() {
		return this.vuelo;
	}
	
	public void marcarComoUsado() {
		this.usado = true;
	}
	
}
