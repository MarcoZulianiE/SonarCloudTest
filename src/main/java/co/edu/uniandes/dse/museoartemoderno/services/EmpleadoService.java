package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.EmpleadoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.EmpleadoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpleadoService {

	@Autowired
	EmpleadoRepository empleadoRepository;
	
	@Autowired
	PaisRepository paisRepository;
	
	/**
	 * Guarda un nuevo empleado en la base de datos si cumple con las reglas de negocio
	 * @param empleadoEntity - Entidad de la cual se verificaran las reglas de negocio
	 * @return - La entidad de empleado para guardar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public EmpleadoEntity createEmpleado(EmpleadoEntity empleadoEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación del empleado");
		
		if (!validateNombre(empleadoEntity.getNombre()))
			throw new IllegalOperationException("Nombre is not valid");	
		
		if (!empleadoRepository.findByNombre(empleadoEntity.getNombre()).isEmpty())
			throw new IllegalOperationException("Nombre already exists");
		
		if (empleadoEntity.getFechaNacimiento() == null)
			throw new IllegalOperationException(ErrorMessage.FECHA_NACIMIENTO_INVALIDA);

		if (empleadoEntity.getLugarNacimiento() == null)
			throw new IllegalOperationException("Lugar Nacimiento is not valid");	
		
		Optional<PaisEntity> paisEntity1 = paisRepository.findById(empleadoEntity.getLugarNacimiento().getId());
		if (paisEntity1.isEmpty())
			throw new IllegalOperationException("Lugar Nacimiento is not valid");
		
		if (empleadoEntity.getMuseo() == null)
			throw new IllegalOperationException("Museo is not valid");
		
		log.info("Termina proceso de creación del empleado");
		return empleadoRepository.save(empleadoEntity);
	}
	
	/**
	 * @return Lista de todas las entidades de tipo Empleado
	 */
	@Transactional
    public List<EmpleadoEntity> getEmpleados() {
            log.info("Inicia proceso de consulta de todos los empleados");
            log.info("Finaliza proceso de consulta de todos los empleados");
            return empleadoRepository.findAll();
    }
	
	/**
	 * Encuentra en la base de datos el empleado con un id especifico
	 * @param empleadoId - Id del empleado que se quiere obtener
	 * @return - El empleado con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public EmpleadoEntity getEmpleado(Long empleadoId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el empleado con id: " + empleadoId);
		Optional<EmpleadoEntity> empleadoEntity = empleadoRepository.findById(empleadoId);
		
		if (empleadoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		log.info("Termina proceso de consultar el empleado con id: " + empleadoId);
		return empleadoEntity.get();
	}
	/**
	 * Actualiza los datos de un empleado
	 * @param empleadoId - id del empleado que se quiere actualizar
	 * @param empleado - la entidad empleado con los datos nuevos
	 * @return La entidad del empleado actualizada
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public EmpleadoEntity updateEmpleado(Long empleadoId, EmpleadoEntity empleado) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar el empleado con id: ", empleadoId);
		Optional<EmpleadoEntity> empleadoEntity = empleadoRepository.findById(empleadoId);
		if (empleadoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);

		if (!validateNombre(empleado.getNombre()))
			throw new IllegalOperationException("Nombre is not valid");	

		empleado.setId(empleadoId);
		log.info("Termina proceso de actualizar el empleado con id: ", empleadoId);
		return empleadoRepository.save(empleado);
	}
	
	/**
	 * Eliminar empleado dado su Id 
	 * @param empleadoId - Id del empleado que se quiere eliminar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public void deleteEmpleado(Long empleadoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el empleado con id: ", empleadoId);
		Optional<EmpleadoEntity> empleadoEntity = empleadoRepository.findById(empleadoId);
		
		if (empleadoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		MuseoEntity museo = empleadoEntity.get().getMuseo();
		if (museo != null)
			throw new IllegalOperationException("Unable to delete empleado because it has associated museo");
		
		empleadoRepository.deleteById(empleadoId);
		log.info("Termina proceso de borrar el empleado con id: " + empleadoId);
	}

	
	/**
	 * Verifica que el Nombre sea valido.
	 *
	 * @param nombre que se debe verificar
	 * @return true si el ISBN es valido.
	 */
	private boolean validateNombre(String nombre) {
		return !(nombre == null || nombre.isEmpty());
	}
	
	/**
	 * Verifica que la Fecha de Nacimiento sea valida.
	 *
	 * @param fecha que se debe verificar
	 * @return true si la Fecha de Nacimiento es valida.
	 */
	private boolean validateFechaNacimiento(Date fechaNacimiento, Date fechaFallecimiento) {
		return fechaNacimiento.before(fechaFallecimiento);
	}
	
	/**
	 * Verifica que la Fecha de Fallecimiento sea valida.
	 *
	 * @param fecha que se debe verificar
	 * @return true si la Fecha de Fallecimiento es valida.
	 */
	private boolean validateFechaFallecimiento(Date fecha) {
		Date now = new Date();
		return fecha.before(now);
	}
}