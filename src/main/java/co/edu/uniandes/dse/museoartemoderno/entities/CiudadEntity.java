package co.edu.uniandes.dse.museoartemoderno.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Getter
@Setter
public class CiudadEntity extends BaseEntity {
	
	private String nombreCiudad;
	
    private String coordenadasCiudad;
    private String imagen;

	@PodamExclude
	@ManyToOne
	private PaisEntity pais;

}
