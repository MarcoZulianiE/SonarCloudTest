package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.ComentarioEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.ComentarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    
    
	/**
	 * Guarda una nueva comentario en la base de datos si cumple con las reglas de negocio
	 * @param ComentarioEntity - Entidad de la cual se verificaran las reglas de negocio
	 * @return - La entidad de Comentario para guardar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ComentarioEntity createComentario(ComentarioEntity comentarioEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia el proceso de creación de la Comentario");
		
		
		if (comentarioEntity.getCalificacion() == null)
			throw new IllegalOperationException("La calificacion del Comentario ingresado es nulo.");	
		
		if ((comentarioEntity.getFechaPublicacion()).after(new Date()))
			throw new IllegalOperationException("La fecha de publicación no es válida.");
	
		log.info("Termina el proceso de creación de la Comentario");
		return comentarioRepository.save(comentarioEntity);
	}
    
    
	/**
	 * @return Lista de todas las entidades de tipo Comentario
	 */
	@Transactional
    public List<ComentarioEntity> getAllComentarios() {
    	log.info("Inicia proceso de consulta de todas las comentarios");
    	return comentarioRepository.findAll();
    }
	
	
	/**
	 * Encuentra en la base de datos la Comentario con un id especifico
	 * @param comentarioId - Id de la comentario que se quiere obtener
	 * @return - La comentario con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public ComentarioEntity getComentario(Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la comentario con id: " + comentarioId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException("No encontró ninguna comentario con el ID dado. ");
		
		log.info("Termina proceso de consultar la comentario con id: " + comentarioId);
		return comentarioEntity.get();
	}
	
	/**
	 * Actualizar una comentario dada su Id
	 * @param comentarioId - Id de la comentario que se quiere actualizar
	 * @param comentario - Entidad Comentario con los cambios
	 * @return - Entidad Comentario actualizada
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ComentarioEntity updateComentario(Long comentarioId, ComentarioEntity comentario)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la Comentario con id: " + comentarioId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException("No se encontró ninguna comentario con el ID dado. ");

		comentario.setId(comentarioId);
		log.info("Termina proceso de actualizar la comentario con id: " + comentarioId);
		return comentarioRepository.save(comentario);
	}
	
	/**
	 * Eliminar una comentario dado su Id 
	 * @param comentarioId - Id de la Comentario que se quiere eliminar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public void deleteComentario(Long comentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la Comentario con id: ", comentarioId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException("No se encontró ninguna comentario con el ID dado. ");
	
		ObraEntity obra = comentarioEntity.get().getObra();

		if (obra != null)
			throw new IllegalOperationException("No se puede eliminar el comentario ya que está asociado a una obra");
		
		comentarioRepository.deleteById(comentarioId);
		log.info("Termina proceso de borrar la Comentario con id: " + comentarioId);
	}

	public void getComentariosByObra(Long obraId) {	}

	public void getComentarioByObraId(Long obraId) { }
	
	
}