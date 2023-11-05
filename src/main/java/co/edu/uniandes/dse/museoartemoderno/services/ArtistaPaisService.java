package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArtistaPaisService {

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private ArtistaRepository artistaRepository;
	
	/**
	 * Remplazar el pais de un artista.
	 *
	 * @param artistaId      id del artista que se quiere actualizar.
	 * @param paisId El id del pais que se será del artista.
	 * @return el nuevo artista.
	 */

	@Transactional
	public ArtistaEntity replaceLugarNacimiento(Long artistaId, Long paisId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el artista con id: ", artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);

		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PAIS_NOT_FOUND);

		artistaEntity.get().setLugarNacimiento(paisEntity.get());
		log.info("Termina proceso de actualizar el artista con id: ", artistaId);

		return artistaEntity.get();
	}
	
	/**
	 * Remplazar el pais de un artista.
	 *
	 * @param artistaId      id del artista que se quiere actualizar.
	 * @param paisId El id del pais que se será del artista.
	 * @return el nuevo artista.
	 */

	@Transactional
	public ArtistaEntity replaceLugarFallecimiento(Long artistaId, Long paisId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el artista con id: ", artistaId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if (artistaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);

		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PAIS_NOT_FOUND);

		artistaEntity.get().setLugarFallecimiento(paisEntity.get());
		log.info("Termina proceso de actualizar el artista con id: ", artistaId);

		return artistaEntity.get();
	}	
}