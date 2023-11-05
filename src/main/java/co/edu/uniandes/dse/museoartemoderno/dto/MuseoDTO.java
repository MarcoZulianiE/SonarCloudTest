package co.edu.uniandes.dse.museoartemoderno.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MuseoDTO {
	
	private Long id;

	private String nombre;

	private String img;
	
	private String direccion;
	
	private List<String> salasExposicion = new ArrayList<>();
	
	private Integer totalObrasExhibidas;
	
	private PaisDTO ubicacion;

}
