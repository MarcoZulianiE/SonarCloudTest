package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MuseoArtistaService {

	
	@Autowired
	private MuseoRepository museoRepository;

	@Autowired
	private ArtistaRepository artistaRepository;
	
	private String museoNotFound = "MUSEO NOT FOUND";
	
	private String artistaNotFound = "ARTISTA NOT FOUND";
	
	
	/**
	 * Asocia un artista al museo cuyo id es dado por parametro
	 * @param museoId - Id del museo a asociar
	 * @param artistaId - Id del artista a asociar
	 * @return - instancia de artista que fue asociada al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public ArtistaEntity addArtista(Long museoId, Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un artista al museo con id: " + museoId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);
		
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(artistaNotFound);

		artistaEntity.get().getMuseos().add(museoEntity.get());
		log.info("Termina proceso de asociarle un artista al museo con id: ", museoId);
		return artistaEntity.get();
	}
	
	
	/**
	 * Devuelve una lista de todos los artistas asociados con el museo cuyo id llega por parametro
	 * @param museoId - Id del museo a buscar en los artista
	 * @return - Lista de artistas asociados al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<ArtistaEntity> getArtistas(Long museoId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los artistas asociados al museo con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);

		List<ArtistaEntity> artistas = artistaRepository.findAll();
		List<ArtistaEntity> artistasList = new ArrayList<>();

		for (ArtistaEntity artista : artistas) {
			if (artista.getMuseos().contains(museoEntity.get())) {
				artistasList.add(artista);
			}
		}
		log.info("Termina proceso de consultar todos los artistas del museo con id: " + museoId);
		return artistasList;
	}
	
	
	/**
	 * Obtiene un artista con id especifico asociado a un museo dado su id
	 * @param artistaId - Id de un artista
	 * @param museoId - Id de un museo
	 * @return el artista del museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
	 */
	@Transactional
	public ArtistaEntity getArtista(Long museoId, Long artistaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el artista con id: " + artistaId + ", del museo con id: " + museoId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);
		
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(artistaNotFound);

		log.info("Termina proceso de consultar el artista con id: " + artistaId + ", del museo con id: " + museoId);
		if (artistaEntity.get().getMuseos().contains(museoEntity.get()))
			return artistaEntity.get();

		throw new IllegalOperationException("El Artista no esta asociado con el Museo");
	}
	
	
	/**
	 * Remplaza los artistas asociados a un museo cuyo id es dado por parametro
	 * @param museoId - Id del museo
	 * @param artistas - Instancias de artistas a vincular con el museo
	 * @return - Lista de nuevos artistas asociados al museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public List<ArtistaEntity> replaceArtistas(Long museoId, List<ArtistaEntity> artistas) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los artistas asociados al museo con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);

		for (ArtistaEntity artista : artistas) {
			Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artista.getId());
			if (artistaEntity.isEmpty())
				throw new EntityNotFoundException(artistaNotFound);

			if (!artistaEntity.get().getMuseos().contains(museoEntity.get()))
				artistaEntity.get().getMuseos().add(museoEntity.get());
		}
		log.info("Finaliza proceso de reemplazar los artistas asociados al museo con id: " + museoId);
		return artistas;
	}
	
	
	/**
	 * Elimina un artista asociado al museo
	 * @param museoId - Id del museo del cual se quiere eliminar el artista
	 * @param artistaId - Id del artista a eliminar del museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public void removeArtista(Long museoId, Long artistaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un museo del artista con id: " + museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);

		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(artistaNotFound);

		artistaEntity.get().getMuseos().remove(museoEntity.get());
		log.info("Finaliza proceso de borrar un museo del artista con id: " + museoId);
	}


}
