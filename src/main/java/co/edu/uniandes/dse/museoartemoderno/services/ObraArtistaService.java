package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;


import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;

@Slf4j
@Service
public class ObraArtistaService {
	
	@Autowired
	private ObraRepository obraRepository;

	@Autowired
	private ArtistaRepository artistaRepository;
	
	
	/**
	 * Asocia una Museo existente a una Obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @param artistaId   Identificador de la instancia de artista
	 * @return Instancia de ArtistaEntity que fue asociada a Obra
	 */

	@Transactional
	public ArtistaEntity addArtista(Long obraId, Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar un artista a la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);

		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Obra no encontrada.");

		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException("Artista no encontado.");
		
		(obraEntity.get()).setArtista(artistaEntity.get());
		log.info("Termina proceso de asociar un artista a la obra con id = {0}", obraId);
		return artistaEntity.get();
	}

	/**
	 * Obtiene el artista autor de una obra
	 *
	 * @param obraId Identificador de la instancia de obra
	 * @return Artista asociado a la obra dado su id
	 */
	@Transactional
	public ArtistaEntity getArtista(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el artista de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");
		
		log.info("Termina proceso de consultar el artista de la obra con id = {0}", obraId);
		return (obraEntity.get()).getArtista();
	}


	/**
	 * Desasocia un Artista existente de una Obra existente
	 * @param obraId Identificador de la instancia de obra
	 */
	@Transactional
	public void removeArtista(Long obraId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el artista de la obra con id = {0}", obraId);
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if (obraEntity.isEmpty())
			throw new EntityNotFoundException("Ninguna obra fue encontrada con el id dado.");


		obraEntity.get().setArtista(null);
		log.info("Finaliza proceso de borrar el artista de la obra con id = {0}", obraId);
	}

}
