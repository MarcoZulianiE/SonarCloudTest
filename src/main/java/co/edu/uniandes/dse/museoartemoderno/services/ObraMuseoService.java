package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;


import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;

@Slf4j
@Service
public class ObraMuseoService {
	
	@Autowired
	private ObraRepository obraRepository;

	@Autowired
	private MuseoRepository museoRepository;
	
	
	/**
	 * Asocia una Museo existente a una Obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @param museoId   Identificador de la instancia de museo
	 * @return Instancia de MuseoEntity que fue asociada a Obra
	 */

	@Transactional
	public MuseoEntity addMuseo(Long obraId, Long museoId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar un museo a la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Obra no encontrada.");

		if (museoEntity.isEmpty())
			throw new EntityNotFoundException("Museo no encontado.");
		
		obraEntity.get().setMuseo(museoEntity.get());
		log.info("Termina proceso de asociar un museo a la obra con id = {0}", obraId);
		return museoEntity.get();
	}

	/**
	 * Obtiene el museo en el que está exhibida una obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @return Museo asociada a la obra dado su id
	 */
	@Transactional
	public MuseoEntity getMuseo(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el museo de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");
		
		log.info("Termina proceso de consultar el museo de la obra con id = {0}", obraId);
		return (obraEntity.get()).getMuseo();
	}
	
	


	/**
	 * Desasocia un Museo existente de una Obra existente
	 * @param obraId Identificador de la instancia de obra
	 */
	@Transactional
	public void removeMuseo(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un museo de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");


		obraEntity.get().setMuseo(null);
		log.info("Finaliza proceso de borrar un museo de la obra con id = {0}", obraId);
	}

}
