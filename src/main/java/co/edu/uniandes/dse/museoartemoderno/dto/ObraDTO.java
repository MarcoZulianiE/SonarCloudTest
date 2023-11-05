package co.edu.uniandes.dse.museoartemoderno.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ObraDTO {
	
	private Long id;
	private String nombre;
	private String tipo;
	private String descripcion;
	private Date fechaPublicacion;
	private String imagen;
	
	private MuseoDTO museo;
	private ArtistaDTO artista;
	private MovimientoArtisticoDTO movimiento;

}
