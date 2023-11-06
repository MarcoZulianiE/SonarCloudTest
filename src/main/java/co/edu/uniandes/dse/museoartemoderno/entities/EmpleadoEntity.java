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
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Getter
@Setter
@Entity
public class EmpleadoEntity extends BaseEntity {

	private String nombre;
	private String imagen;
	private String direccion;
	private String barrio;
	
	@PodamExclude
	@ManyToOne
	private PaisEntity lugarNacimiento;
	
	@PodamExclude
	@ManyToOne
	private MuseoEntity museo;
	
	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date fechaNacimiento = new Date();	
	
}
