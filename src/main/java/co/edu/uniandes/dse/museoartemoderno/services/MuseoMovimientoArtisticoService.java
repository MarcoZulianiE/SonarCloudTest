package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MuseoMovimientoArtisticoService {

	
	@Autowired
	private MuseoRepository museoRepository;

	@Autowired
	private MovimientoArtisticoRepository movimientoArtisticoRepository;
	
	
	/**
	 * Asocia un movimiento artistico al museo cuyo id es dado por parametro
	 * @param museoId - Id del museo a asociar
	 * @param movimientoArtisticoId - Id del movimiento artistico a asociar
	 * @return - instancia de movimiento artistico que fue asociada al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public MovimientoArtisticoEntity addMovimientoArtistico(Long museoId, Long movimientoArtisticoId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un movimientoArtistico al museo con id: " + museoId);
		
		Optional<MovimientoArtisticoEntity> movimientoArtisticoEntity = movimientoArtisticoRepository.findById(movimientoArtisticoId);

		if (movimientoArtisticoEntity.isEmpty())
			throw new EntityNotFoundException("MOVIMIENTO ARTISTICO NOT FOUND");
		
		
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("MUSEO NOT FOUND");

		movimientoArtisticoEntity.get().getMuseos().add(museoEntity.get());
		log.info("Termina proceso de asociarle un movimientoArtistico al museo con id: ", museoId);
		return movimientoArtisticoEntity.get();
	}
	
	
	/**
	 * Devuelve una lista de todos los movimientos artisticos asociados con el museo cuyo id llega por parametro
	 * @param museoId - Id del museo a buscar en los movimientos artisticos
	 * @return - Lista de movimientos artisticos asociados al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<MovimientoArtisticoEntity> getMovimientoArtisticos(Long museoId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los movimientoArtisticos asociados al museo con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("MUSEO NOT FOUND");

		List<MovimientoArtisticoEntity> movimientoArtisticos = movimientoArtisticoRepository.findAll();
		List<MovimientoArtisticoEntity> movimientoArtisticosList = new ArrayList<>();

		for (MovimientoArtisticoEntity movimientoArtistico : movimientoArtisticos) {
			if (movimientoArtistico.getMuseos().contains(museoEntity.get())) {
				movimientoArtisticosList.add(movimientoArtistico);
			}
		}
		log.info("Termina proceso de consultar todos los movimientoArtisticos del museo con id: " + museoId);
		return movimientoArtisticosList;
	}
	
	
	/**
	 * Obtiene un movimiento artistico con id especifico asociado a un museo dado su id
	 * @param movimientoArtisticoId - Id de un movimiento artistico
	 * @param museoId - Id de un museo
	 * @return el movimiento artistico del museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public MovimientoArtisticoEntity getMovimientoArtistico(Long museoId, Long movimientoArtisticoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el movimientoArtistico con id: " + movimientoArtisticoId + ", del museo con id: " + museoId);
		Optional<MovimientoArtisticoEntity> movimientoArtisticoEntity = movimientoArtisticoRepository.findById(movimientoArtisticoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("MUSEO NOT FOUND");
		
		if (movimientoArtisticoEntity.isEmpty())
			throw new EntityNotFoundException("MOVIMIENTO ARTISTICO NOT FOUND");

		log.info("Termina proceso de consultar el movimientoArtistico con id: " + movimientoArtisticoId + ", del museo con id: " + museoId);
		if (movimientoArtisticoEntity.get().getMuseos().contains(museoEntity.get()))
			return movimientoArtisticoEntity.get();

		throw new IllegalOperationException("El Movimiento Artistico no esta asociado con el Museo");
	}
	
	
	/**
	 * Remplaza los movimientos artisticos asociados a un museo cuyo id es dado por parametro
	 * @param museoId - Id del museo
	 * @param movimientoArtisticos - Instancias de movimientos artisticos a vincular con el museo
	 * @return - Lista de nuevos movimientos artisticos asociados al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<MovimientoArtisticoEntity> replaceMovimientoArtisticos(Long museoId, List<MovimientoArtisticoEntity> movimientoArtisticos) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los movimientoArtisticos asociados al museo con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("MUSEO NOT FOUND");

		for (MovimientoArtisticoEntity movimientoArtistico : movimientoArtisticos) {
			Optional<MovimientoArtisticoEntity> movimientoArtisticoEntity = movimientoArtisticoRepository.findById(movimientoArtistico.getId());
			if (movimientoArtisticoEntity.isEmpty())
				throw new EntityNotFoundException("MOVIMIENTO ARTISTICO NOT FOUND");

			if (!movimientoArtisticoEntity.get().getMuseos().contains(museoEntity.get()))
				movimientoArtisticoEntity.get().getMuseos().add(museoEntity.get());
		}
		log.info("Finaliza proceso de reemplazar los movimientos artisticos asociados al museo con id: " + museoId);
		return movimientoArtisticos;
	}
	
	
	/**
	 * Elimina un movimiento artistico asociado al museo
	 * @param museoId - Id del museo del cual se quiere eliminar el movimiento artistico
	 * @param movimientoArtisticoId - Id del movimiento artistico a eliminar del museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public void removeMovimientoArtistico(Long museoId, Long movimientoArtisticoId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un museo del movimientoArtistico con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("MUSEO NOT FOUND");

		Optional<MovimientoArtisticoEntity> movimientoArtisticoEntity = movimientoArtisticoRepository.findById(movimientoArtisticoId);
		if (movimientoArtisticoEntity.isEmpty())
			throw new EntityNotFoundException("MOVIMIENTO ARTISTICO NOT FOUND");

		movimientoArtisticoEntity.get().getMuseos().remove(museoEntity.get());
		log.info("Finaliza proceso de borrar un museo del movimientoArtistico con id: " + museoId);
	}


}