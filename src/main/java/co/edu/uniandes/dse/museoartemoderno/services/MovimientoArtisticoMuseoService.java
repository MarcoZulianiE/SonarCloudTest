package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientoArtisticoMuseoService 
{
	@Autowired
	private MovimientoArtisticoRepository movimientoArtisticoRepository;

	@Autowired
	private MuseoRepository museoRepository;

	/**
	 * Asocia un museo a un movimiento artistico cuyo Id se recibe por parametro
	 * @param movimientoId - ID del movimiento artistico
	 * @param museoId - Id del museo
	 * @throws EntityNotFoundException si no se encuentra el movimiento o el museo
	 * @return Instancia de museo que se asocio
	 */
	@Transactional
	public MuseoEntity addMuseo(Long movimientoId, Long museoId) throws EntityNotFoundException
	{
		log.info("Inicio proceso de asociar el museo "+museoId+" con el movimiento artistico "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(museoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MUSEO_NOT_FOUND);
		}
		movimientoEntity.get().getMuseos().add(museoEntity.get());
		log.info("Fin proceso de asociar el museo "+museoId+" con el movimiento artistico "+movimientoId);
		return museoEntity.get();
	}

	/**
	 * Obtiene una coleccion de instancias de MuseoEntity asociadas a una instancia de MovimientoArtisticoEntity
	 * @param movimientoId - Id del movimiento artistico
	 * @throws EntityNotFoundException si no se encuentra el movimiento
	 * @return Coleccion de museos asociados con el movimiento artistico
	 */
	@Transactional
	public List<MuseoEntity> getMuseos(Long movimientoId) throws EntityNotFoundException
	{
		log.info("Inicio proceso de obtener museos asociados con el movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}

		log.info("Inicio proceso de obtener museos asociados con el movimiento "+movimientoId);
		return movimientoEntity.get().getMuseos();
	}

	/**
	 * Obtiene una instancia de MuseoEntity asociada a una instancia de MovimientoArtisticoEntity
	 * @param movimientoId - Id del movimiento artistico
	 * @param museoId - Id del museo
	 * @throws EntityNotFoundEception si no se encuentra el movimiento o el museo
	 * @return la entidad de MuseoEntity
	 */
	@Transactional
	public MuseoEntity getMuseo(Long museoId, Long movimientoId) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia el proceso de obtener el museo "+museoId+" asociado con el movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if(museoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MUSEO_NOT_FOUND);
		}
		log.info("Inicia el proceso de obtener el museo "+museoId+" asociado con el movimiento "+movimientoId);
		if(movimientoEntity.get().getMuseos().contains(museoEntity.get()))
		{
			return museoEntity.get();
		}
		throw new IllegalOperationException("El museo no esta asociado con el movimiento artistico");
	}

	/**
	 * Reemplaza las instancias de Museo asociadas a una instancia de MovimientoArtistico
	 * @param museos - Coleccion de instancias de MuseoEntity
	 * @param movimientoId - Id del movimiento artistico
	 * @throws EntityNotFoundException si no se encuentra el movimiento
	 * @return nueva coleccion de MuseoEntity asociada a la instancia MovimientoArtisticoEntity
	 */
	@Transactional
	public List<MuseoEntity> addMuseos(Long movimientoId, List<MuseoEntity> museos) throws EntityNotFoundException
	{
		log.info("Inicia el proceso de reemplazar la coleccion de instancias de MuseoEntity del movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}

		for(MuseoEntity museo: museos)
		{
			Optional<MuseoEntity> museoEntity = museoRepository.findById(museo.getId());
			if(museoEntity.isEmpty())
			{
				throw new EntityNotFoundException(ErrorMessage.MUSEO_NOT_FOUND);
			}
			if(!movimientoEntity.get().getMuseos().contains(museoEntity.get()))
			{
				movimientoEntity.get().getMuseos().add(museoEntity.get());
			}
		}

		log.info("Termina el proceso de reemplazar la coleccion de instancias de MuseoEntity del movimiento "+movimientoId);
		return museos;
	}

	/**
	 * Desasocia un Museo de un MovimientoAristico existente
	 * @param movimientoId - Id del movimiento
	 * @param museoId
	 * @throws EntityNotFoundException si no se encuentra el museo o el movimiento artistico
	 */
	@Transactional
	public void removeMuseo(Long movimientoId, Long museoId) throws EntityNotFoundException
	{
		log.info("Inicio proceso desasociacion del museo "+museoId+" con el movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(museoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MUSEO_NOT_FOUND);
		}

		movimientoEntity.get().getMuseos().remove(museoEntity.get());

		log.info("Fin proceso desasociacion del museo "+museoId+" con el movimiento "+movimientoId);
	}
}
