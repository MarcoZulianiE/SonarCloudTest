package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientoArtisticoObraService 
{
	@Autowired
	private MovimientoArtisticoRepository movimientoArtisticoRepository;

	@Autowired
	private ObraRepository obraRepository;


	/**
	 * Agregar una obra a un Movimiento Artistico
	 * @param movimientoId - Id del movimiento
	 * @param obraId - Id de la obra
	 * @throws EntityNotFoundException si no se encuentra el movimiento o la obra
	 * @return La obra agregada
	 */
	@Transactional
	public ObraEntity addObra(Long movimientoId, Long obraId) throws EntityNotFoundException
	{
		log.info("Inicia el proceso de agregar la obra "+obraId+" al movimiento artistico "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);

		if(obraEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
		}
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		obraEntity.get().setMovimiento(movimientoEntity.get());
		log.info("Finaliza el proceso de agregar la obra "+obraId+" al movimiento artistico "+movimientoId);
		return obraEntity.get();
	}

	/**
	 * Obtener todas las obras asociadas a un movimiento artistico
	 * @param movimientoId - Id del movimiento artistico
	 * @throws EntityNotFoundException si el movimiento artistico no existe
	 * @return La lista de obras asociadas
	 */
	@Transactional
	public List<ObraEntity> getObras(Long movimientoId) throws EntityNotFoundException
	{
		log.info("Inicio proceso de obtener las obras asociadas al movimiento "+movimientoId);

		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		
		log.info("Fin proceso de obtener las obras asociadas al movimiento "+movimientoId);
		return movimientoEntity.get().getObras();
	}

	/**
	 * Obtener una instancia de ObraEntity asociadada a una instancia existente de MovimientoArtisticoEntity
	 * @param movimientoId - Id del movimiento
	 * @param obraId - Id de la obra
	 * @throws EntityNotFoundException si no se encuentra el movimiento o la obra
	 * @return La obra asociada al movimiento 
	 */
	@Transactional
	public ObraEntity getObra(Long movimientoId, Long obraId) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia el proceso de obtener la obra "+obraId+" asociada al movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);

		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(obraEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
		}

		log.info("Finaliza el proceso de obtener la obra "+obraId+" asociada al movimiento "+movimientoId);		
		if(obraEntity.get().getMovimiento().equals(movimientoEntity.get()))
		{
			return obraEntity.get();
		}

		throw new IllegalOperationException("La obra no esta asociada con el movimiento artistico");
	}

	/**
	 * Remplaza	las obras de un movimiento artistico
	 * @param movimientoId - Id del movimiento
	 * @param obras - Lista de obras que seran los del movimiento
	 * @throws EntityNotFoundException si no se encuentra el movimiento
	 */
	@Transactional
	public List<ObraEntity> replaceObras(Long movimientoId, List<ObraEntity> pObras) throws EntityNotFoundException
	{
		log.info("Incio proceso de actualizar las obras del movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}	

		for(ObraEntity obra: pObras)
		{
			Optional<ObraEntity> o = obraRepository.findById(obra.getId());
			if(o.isEmpty())
			{
				throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
			}
			o.get().setMovimiento(movimientoEntity.get());
		}
		log.info("Incio proceso de actualizar las obras del movimiento "+movimientoId);
		return pObras;
	}
}