package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;
    
    
	/**
	 * Guarda una nueva obra en la base de datos si cumple con las reglas de negocio
	 * @param ObraEntity - Entidad de la cual se verificaran las reglas de negocio
	 * @return - La entidad de Obra para guardar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ObraEntity createObra(ObraEntity obraEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia el proceso de creación de la Obra");
		
		
		if (obraEntity.getNombre() == null)
			throw new IllegalOperationException("El Nombre de la Obra ingresado es nulo.");	
		
		if ((obraEntity.getFechaPublicacion()).after(new Date()))
			throw new IllegalOperationException("La fecha de publicación no es válida.");
	
		log.info("Termina el proceso de creación de la Obra");
		return obraRepository.save(obraEntity);
	}
    
    
	/**
	 * @return Lista de todas las entidades de tipo Obra
	 */
	@Transactional
    public List<ObraEntity> getAllObras() {
    	log.info("Inicia proceso de consulta de todas las obras");
    	return obraRepository.findAll();
    }
	
	
	/**
	 * Encuentra en la base de datos la Obra con un id especifico
	 * @param obraId - Id de la obra que se quiere obtener
	 * @return - La obra con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public ObraEntity getObra(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la obra con id: " + obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("No encontró ninguna obra con el ID dado. ");
		
		log.info("Termina proceso de consultar la obra con id: " + obraId);
		return obraEntity.get();
	}
	
	/**
	 * Actualizar una obra dada su Id
	 * @param obraId - Id de la obra que se quiere actualizar
	 * @param obra - Entidad Obra con los cambios
	 * @return - Entidad Obra actualizada
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ObraEntity updateObra(Long obraId, ObraEntity obra)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la Obra con id: " + obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("No se encontró ninguna obra con el ID dado. ");

		obra.setId(obraId);
		log.info("Termina proceso de actualizar la obra con id: " + obraId);
		return obraRepository.save(obra);
	}
	
	/**
	 * Eliminar una obra dado su Id 
	 * @param obraId - Id de la Obra que se quiere eliminar
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public void deleteObra(Long obraId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la Obra con id: ", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("No se encontró ninguna obra con el ID dado. ");
	
		MovimientoArtisticoEntity movimiento = obraEntity.get().getMovimiento();

		if (movimiento != null)
			throw new IllegalOperationException("No se puede eliminar la obra ya que está asociada a un Movimiento Artístico");

		MuseoEntity museo = obraEntity.get().getMuseo();
		if (museo != null)
			throw new IllegalOperationException("No se puede eliminar la obra ya que está asociada a un Museo");

		ArtistaEntity artista = obraEntity.get().getArtista();
		if (artista != null)
			throw new IllegalOperationException("No se puede eliminar la obra ya que está asociada a un Artista");

		
		obraRepository.deleteById(obraId);
		log.info("Termina proceso de borrar la Obra con id: " + obraId);
	}
	
	
}