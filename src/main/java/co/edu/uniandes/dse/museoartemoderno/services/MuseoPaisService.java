package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MuseoPaisService {

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private MuseoRepository museoRepository;
	
	private String museoNotFound = "MUSEO NOT FOUND";
	
	private String paisNotFound = "PAIS NOT FOUND";
	
	
	/**
	 * Remplaza el Pais del Museo por una nuevo
	 * @param MuseoId - Id del Museo del cual se quiere modificar el Pais 
	 * @param PaisId - Id del nuevo Pais por el cual se quiere remplazar
	 * @return - Nuevo Pais asociado al Museo
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public MuseoEntity replacePais(Long museoId, Long paisId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el museo con id: ", museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);

		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException(paisNotFound);

		museoEntity.get().setUbicacion(paisEntity.get());
		log.info("Termina proceso de actualizar el museo con id: ", museoId);

		return museoEntity.get();
	}
	
	
	/**
	 * Elimina el Pais asociado a un Museo
	 * @param MuseoId - Id del Museo del cual se quiere eliminar el Pais
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public void removePais(Long museoId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar la Pais de  del Museo con id: ", museoId);
		Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
		if (museoEntity.isEmpty())
			throw new EntityNotFoundException(museoNotFound);

		if (museoEntity.get().getUbicacion() == null) {
			throw new EntityNotFoundException("El museo no tiene Pais");
		}
		Optional<PaisEntity> paisEntity = paisRepository.findById(museoEntity.get().getUbicacion().getId());

		paisEntity.ifPresent(pais -> {
			museoEntity.get().setUbicacion(null);
			pais.getMuseos().remove(museoEntity.get());
		});

		log.info("Termina proceso de borrar el Pais del Museo con id: ", museoId);
	}
	

}
