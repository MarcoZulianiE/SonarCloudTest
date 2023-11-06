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
public class ComentarioEntity extends BaseEntity{
	
	private String calificacion;
	private String contenido;
		
	@PodamExclude
	@ManyToOne
	private ObraEntity obra;
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaPublicacion;
	
}
