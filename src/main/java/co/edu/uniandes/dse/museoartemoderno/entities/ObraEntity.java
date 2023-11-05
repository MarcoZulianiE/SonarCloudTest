package co.edu.uniandes.dse.museoartemoderno.entities;

import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.uniandes.dse.museoartemoderno.podam.DateStrategy;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class ObraEntity extends BaseEntity{
	
	private String nombre;
	private String tipo;
	private String descripcion;
	private String imagen;
	
	@PodamExclude
	@ManyToOne
	private MuseoEntity museo;
	
	@PodamExclude
	@ManyToOne
	private ArtistaEntity artista;
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaPublicacion;
	
	@PodamExclude
	@ManyToOne
	private MovimientoArtisticoEntity movimiento;
	
	


}
