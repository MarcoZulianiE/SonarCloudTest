package co.edu.uniandes.dse.museoartemoderno.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Getter
@Setter
public class PaisEntity extends BaseEntity{
	private String nombrePais;
	private String coordenadasPais;
	private String imagen;
	@PodamExclude
	@OneToMany(mappedBy = "pais")
	private List<CiudadEntity> ciudades = new ArrayList<>();
	
	@PodamExclude
	@OneToMany(mappedBy = "lugarFallecimiento")
	private List<ArtistaEntity> artistasFallecimiento = new ArrayList<>();
	
	@PodamExclude
	@OneToMany(mappedBy = "lugarNacimiento")
	private List<ArtistaEntity> artistasNacimiento = new ArrayList<>();
	
	@PodamExclude
	@OneToMany(mappedBy = "ubicacion")
	private List<MuseoEntity> museos = new ArrayList<>();
	
	@PodamExclude
	@OneToMany(mappedBy = "lugarOrigen")
	private List<MovimientoArtisticoEntity> movimientoArtisticos = new ArrayList<>();

}
