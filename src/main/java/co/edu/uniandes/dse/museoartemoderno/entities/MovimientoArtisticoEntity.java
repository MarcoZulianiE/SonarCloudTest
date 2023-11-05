package co.edu.uniandes.dse.museoartemoderno.entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.uniandes.dse.museoartemoderno.podam.DateStrategy;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Entity
@Getter
@Setter
public class MovimientoArtisticoEntity extends BaseEntity{

	private String nombre;
	private String imagen;
	private String descripcion;
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaApogeo;

	@PodamExclude
	@OneToMany(
		mappedBy = "movimiento")
	private List<ObraEntity> obras = new ArrayList<>();


	@PodamExclude
	@ManyToMany(
		mappedBy = "movimientos", fetch = FetchType.LAZY)
	private List<ArtistaEntity> artistas = new ArrayList<>();

	@PodamExclude
	@ManyToMany
	private List<MuseoEntity> museos = new ArrayList<>();

	@PodamExclude
	@ManyToOne
	private PaisEntity lugarOrigen;

	
}
