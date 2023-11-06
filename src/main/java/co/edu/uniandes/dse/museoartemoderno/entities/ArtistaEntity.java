package co.edu.uniandes.dse.museoartemoderno.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.uniandes.dse.museoartemoderno.podam.DateStrategy;

import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Getter
@Setter
@Entity
public class ArtistaEntity extends BaseEntity {

	private String nombre;
	private String imagen;
	
	@PodamExclude
	@ManyToOne
	private PaisEntity lugarNacimiento;
	
	@PodamExclude
	@ManyToOne
	private PaisEntity lugarFallecimiento;
	
	@PodamExclude
	@ManyToMany
	private List<MuseoEntity> museos = new ArrayList<>();
	
	@PodamExclude
	@OneToMany(mappedBy = "artista")
	private List<ObraEntity> obras = new ArrayList<>();
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaNacimiento = new Date();
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaFallecimiento = new Date();
	
	@PodamExclude
	@ManyToMany
	private List<MovimientoArtisticoEntity> movimientos = new ArrayList<>();
	
}
