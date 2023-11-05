package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArtistaService {

	@Autowired
	ArtistaRepository artistaRepository;
	
	@Autowired
	PaisRepository paisRepository;
	
	/**
	 * Guarda un nuevo artista en la base de datos si cumple con las reglas de negocio
	 * @param artistaEntity - Entidad de la cual se verificaran las reglas de negocio
	 * @return - La entidad de artista para guardar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ArtistaEntity createArtista(ArtistaEntity artistaEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación del artista");
		
		if (!validateNombre(artistaEntity.getNombre()))
			throw new IllegalOperationException("Nombre is not valid");	
		
		if (!artistaRepository.findByNombre(artistaEntity.getNombre()).isEmpty())
			throw new IllegalOperationException("Nombre already exists");
		
		if (artistaEntity.getFechaFallecimiento() == null)
			throw new IllegalOperationException(ErrorMessage.FECHA_FALLECIMIENTO_INVALIDA);
		
		if (!validateFechaFallecimiento(artistaEntity.getFechaFallecimiento()))
			throw new IllegalOperationException(ErrorMessage.FECHA_FALLECIMIENTO_INVALIDA);	
		
		if (artistaEntity.getFechaNacimiento() == null)
			throw new IllegalOperationException(ErrorMessage.FECHA_NACIMIENTO_INVALIDA);
		
		if (!validateFechaNacimiento(artistaEntity.getFechaNacimiento(),artistaEntity.getFechaFallecimiento()))
			throw new IllegalOperationException(ErrorMessage.FECHA_NACIMIENTO_INVALIDA);
		
		if (artistaEntity.getLugarNacimiento() == null)
			throw new IllegalOperationException("Lugar Nacimiento is not valid");	
		
		Optional<PaisEntity> paisEntity1 = paisRepository.findById(artistaEntity.getLugarNacimiento().getId());
		if (paisEntity1.isEmpty())
			throw new IllegalOperationException("Lugar Nacimiento is not valid");
		
		if (artistaEntity.getLugarFallecimiento() == null)
			throw new IllegalOperationException("Lugar Fallecimiento is not valid");
		
		Optional<PaisEntity> paisEntity2 = paisRepository.findById(artistaEntity.getLugarFallecimiento().getId());
		if (paisEntity2.isEmpty())
			throw new IllegalOperationException("Lugar Fallecimiento is not valid");
		
		if (artistaEntity.getMovimientos() == null)
			throw new IllegalOperationException("Movimientos is not valid");
		
		if (artistaEntity.getMuseos() == null)
			throw new IllegalOperationException("Museos is not valid");
		
		if (artistaEntity.getObras() == null)
			throw new IllegalOperationException("Obras is not valid");
		
		log.info("Termina proceso de creación del artista");
		return artistaRepository.save(artistaEntity);
	}
	
	/**
	 * @return Lista de todas las entidades de tipo Artista
	 */
	@Transactional
    public List<ArtistaEntity> getArtistas() {
            log.info("Inicia proceso de consulta de todos los artistas");
            log.info("Finaliza proceso de consulta de todos los artistas");
            return artistaRepository.findAll();
    }
	
	/**
	 * Encuentra en la base de datos el artista con un id especifico
	 * @param artistaId - Id del artista que se quiere obtener
	 * @return - El artista con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public ArtistaEntity getArtista(Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el artista con id: " + artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		log.info("Termina proceso de consultar el artista con id: " + artistaId);
		return artistaEntity.get();
	}
	/**
	 * Actualiza los datos de un artista
	 * @param artistaId - id del artista que se quiere actualizar
	 * @param artista - la entidad artista con los datos nuevos
	 * @return La entidad del artista actualizada
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ArtistaEntity updateArtista(Long artistaId, ArtistaEntity artista) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar el artista con id: ", artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);

		if (!validateNombre(artista.getNombre()))
			throw new IllegalOperationException("Nombre is not valid");	

		artista.setId(artistaId);
		log.info("Termina proceso de actualizar el artista con id: ", artistaId);
		return artistaRepository.save(artista);
	}
	
	/**
	 * Eliminar artista dado su Id 
	 * @param artistaId - Id del artista que se quiere eliminar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public void deleteArtista(Long artistaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el libro con id: ", artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		List<ObraEntity> obras = artistaEntity.get().getObras();
		if (!obras.isEmpty())
			throw new IllegalOperationException("Unable to delete arista because it has associated obras");
		
		artistaRepository.deleteById(artistaId);
		log.info("Termina proceso de borrar el libro con id: " + artistaId);
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