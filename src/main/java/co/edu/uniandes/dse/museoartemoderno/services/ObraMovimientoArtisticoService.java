package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;


import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;

@Slf4j
@Service
public class ObraMovimientoArtisticoService {
	
	@Autowired
	private ObraRepository obraRepository;

	@Autowired
	private MovimientoArtisticoRepository movRepository;
	
	
	/**
	 * Asocia un movimiento artistico existente a una Obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @param artistaId   Identificador de la instancia de movimiento
	 * @return Instancia de MovimientoArtisticoEntity que fue asociada a Obra
	 */

	@Transactional
	public MovimientoArtisticoEntity addMovimientoArtistico(Long obraId, Long movimientoId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar un movimiento artistico a la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		Optional<MovimientoArtisticoEntity> movEntity = movRepository.findById(movimientoId);

		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Obra no encontrada.");

		if (movEntity.isEmpty())
			throw new EntityNotFoundException("Movimiento artistico no encontado.");
		
		(obraEntity.get()).setMovimiento(movEntity.get());
		log.info("Termina proceso de asociar un movimiento artistico a la obra con id = {0}", obraId);
		return movEntity.get();
	}

	/**
	 * Obtiene el movimiento artistico de una obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @return Movimiento Artistico asociada a la obra dado su id
	 */
	@Transactional
	public MovimientoArtisticoEntity getMovimientoArtistico(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar un movimiento artistico de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");
		
		log.info("Termina proceso de consultar un movimiento artistico de la obra con id = {0}", obraId);
		return obraEntity.get().getMovimiento();
	}


	/**
	 * Desasocia un Movimiento Artistico existente de una Obra existente
	 * @param obraId Identificador de la instancia de obra
	 */
	@Transactional
	public void removeMovimientoArtistico(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el movimiento artistico de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");


		obraEntity.get().setMovimiento(null);
		log.info("Finaliza proceso de borrar el movimiento artistico de la obra con id = {0}", obraId);
	}

}