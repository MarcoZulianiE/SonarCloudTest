package co.edu.uniandes.dse.museoartemoderno.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Getter
@Setter
public class MuseoEntity extends BaseEntity {
	
	private String nombre;

	private String img;
	
	private String direccion;
	
//	private List<String> salasExposicion = new ArrayList<>();
	
	private Integer totalObrasExhibidas;
	
	
	@PodamExclude
	@ManyToMany(mappedBy = "museos")
	private List<ArtistaEntity> artistas = new ArrayList<>();
	
	
	@PodamExclude
	@OneToMany(mappedBy = "museo")
	private List<ObraEntity> obras = new ArrayList<>();
	
	
	@PodamExclude
	@ManyToMany(mappedBy = "museos")
	private List<MovimientoArtisticoEntity> movimientos = new ArrayList<>();
	
	
	@PodamExclude
	@ManyToOne
	private PaisEntity ubicacion;
	
	
}
