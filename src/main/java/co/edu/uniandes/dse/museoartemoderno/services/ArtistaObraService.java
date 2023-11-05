package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArtistaObraService {

	@Autowired
	private ObraRepository obraRepository;

	@Autowired
	private ArtistaRepository artistaRepository;

	/**
	 * Asocia una obra existente a un artista
	 *
	 * @param artistaId - Id del artista a asociar
	 * @param obraId - Id de la obra a asociar
	 * @return Instancia de obra que fue asociada al artista
	 */
	@Transactional
	public ObraEntity addObra(Long obraId, Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle una obra al artista con id: ", artistaId);
		
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if(obraEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
		
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		obraEntity.get().setArtista(artistaEntity.get());
		log.info("Termina proceso de agregarle una obra al artista con id: ", artistaId);
		return obraEntity.get();
	}

	/**
	 * Devuelve una lista de todas las obras asociadas con un artista
	 * @param artistaId - Id del artista a buscar en las obras
	 * @return - Lista de obras asociadas al artista
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<ObraEntity> getObras(Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todas la obras del artista con id: ", artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los autores del libro con id: ", artistaId);
		return artistaEntity.get().getObras();
	}

	/**
	 * 
	 * @param artistaId - Id de un artista
	 * @param obraId - Id de una obra
	 * @return la obra del artista
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ObraEntity getObra(Long artistaId, Long obraId)throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar una obra del artista con id: " + artistaId);
		
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
		if(obraEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
				
		log.info("Termina proceso de consultar una obra del artista con id: " + artistaId);
		
		if(artistaEntity.get().getObras().contains(obraEntity.get()))
			return obraEntity.get();
		
		throw new IllegalOperationException("La obra no esta asociada con el artista");
	}

	/**
	 * Remplaza la lista de obras de un artista
	 * @param artistaId - Id de un artista
	 * @param list - Lista de obras a relacionar con el artista
	 * @return - Lista de obras asociadas 
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<ObraEntity> replaceObras(Long artistaId, List<ObraEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las obras del artista con id: " + artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		
		for(ObraEntity obra : list) {
			Optional<ObraEntity> o = obraRepository.findById(obra.getId());
			if(o.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.OBRA_NOT_FOUND);
			
			o.get().setArtista(artistaEntity.get());
		}		
		log.info("Termina proceso de reemplazar las obras del artista con id: " + artistaId);
		return list;
	}
}