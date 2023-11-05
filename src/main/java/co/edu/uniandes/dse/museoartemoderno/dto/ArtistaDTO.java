package co.edu.uniandes.dse.museoartemoderno.dto;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistaDTO {

	private Long id;
	private String nombre;
	private String imagen;
	private Date fechaNacimiento;
	private Date fechaFallecimiento;	
	private PaisDTO lugarNacimiento;
	private PaisDTO lugarFallecimiento;
}
