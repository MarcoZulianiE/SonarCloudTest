package co.edu.uniandes.dse.museoartemoderno.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaisDTO {
	private Long id;
	private String nombrePais;
	private String coordenadas;
	private String imagen;
}
