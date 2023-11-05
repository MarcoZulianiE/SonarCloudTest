package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.MuseoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.ObraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MuseoObraService {

	@Autowired
	private ObraRepository obraRepository;

	@Autowired
	private MuseoRepository museoRepository;
	
	private String museoNotFound = "MUSEO NOT FOUND";
	
	private String obraNotFound = "OBRA NOT FOUND";
	
	/**
     * Asocia una obra existente a un Museo
     *
     * @param MuseoId - Id del Museo a asociar
     * @param obraId - Id de la obra a asociar
     * @return Instancia de obra que fue asociada al Museo
     */
    @Transactional
    public ObraEntity addObra(Long museoId, Long obraId) throws EntityNotFoundException {
            log.info("Inicia proceso de asociarle una obra al Museo con id: " + museoId);
            Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
            if (obraEntity.isEmpty())
                    throw new EntityNotFoundException(obraNotFound);

            Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
            if (museoEntity.isEmpty())
                    throw new EntityNotFoundException(museoNotFound);

            museoEntity.get().getObras().add(obraEntity.get());
            log.info("Termina proceso de asociarle una obra al Museo con id: " + museoId);
            return obraEntity.get();
    }
    
    /**
     * Devuelve una lista de todas las obras asociadas con un Museo
     * @param MuseoId - Id del Museo a buscar en las obras
     * @return - Lista de obras asociadas al Museo
     * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
     */
    @Transactional
    public List<ObraEntity> getObras(Long museoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar todas la obras del Museo con id: ", museoId);
            Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
            if (museoEntity.isEmpty())
                    throw new EntityNotFoundException(museoNotFound);
            log.info("Finaliza proceso de consultar todos los autores del libro con id: ", museoId);
            return museoEntity.get().getObras();
    }
    
    /**
     * 
     * @param MuseoId - Id de un Museo
     * @param obraId - Id de una obra
     * @return la obra del Museo
     * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
     * @throws IllegalOperationException - Exception que se lanza si no se cumple alguna regla de negocio
     */
    @Transactional
    public ObraEntity getObra(Long museoId, Long obraId)
                    throws EntityNotFoundException, IllegalOperationException {
            log.info("Inicia proceso de consultar una obra del Museo con id: " + museoId);
            Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
            Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

            if (obraEntity.isEmpty())
                    throw new EntityNotFoundException(obraNotFound);

            if (museoEntity.isEmpty())
                    throw new EntityNotFoundException(museoNotFound);
            log.info("Termina proceso de consultar una obra del Museo con id: " + museoId);
            if (museoEntity.get().getObras().contains(obraEntity.get()))
                    return obraEntity.get();

            throw new IllegalOperationException("La obra no esta asociada con el Museo");
    }
    
    /**
     * Remplaza la lista de obras de un Museo
     * @param MuseoId - Id de un Museo
     * @param list - Lista de obras a relacionar con el Museo
     * @return - Lista de obras asociadas 
     * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
     */
    @Transactional
    public List<ObraEntity> replaceObras(Long museoId, List<ObraEntity> list) throws EntityNotFoundException {
            log.info("Inicia proceso de reemplazar las obras del Museo con id: " + museoId);
            Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);
            if (museoEntity.isEmpty())
                    throw new EntityNotFoundException(museoNotFound);

            for (ObraEntity obra : list) {
                    Optional<ObraEntity> obraEntity = obraRepository.findById(obra.getId());
                    if (obraEntity.isEmpty())
                            throw new EntityNotFoundException(obraNotFound);

                    if (!museoEntity.get().getObras().contains(obraEntity.get()))
                    		museoEntity.get().getObras().add(obraEntity.get());
            }
            log.info("Termina proceso de reemplazar las obras del Museo con id: " + museoId);
            return getObras(museoId);
    }
    
    /**
     * Desasocia una obra existente de un Museo existente
     * @param MuseoId   Identificador de la instancia de Book
     * @param obraId Identificador de la instancia de Author
     */
    @Transactional
    public void removeObra(Long museoId, Long obraId) throws EntityNotFoundException {
            log.info("Inicia proceso de borrar una obra del Museo con id: " + museoId);
            Optional<ObraEntity> obraEntity = obraRepository.findById(obraId);
            Optional<MuseoEntity> museoEntity = museoRepository.findById(museoId);

            if (obraEntity.isEmpty())
                    throw new EntityNotFoundException(obraNotFound);

            if (museoEntity.isEmpty())
                    throw new EntityNotFoundException(museoNotFound);

            museoEntity.get().getObras().remove(obraEntity.get());

            log.info("Termina proceso de borrar una obra del Museo con id: " + museoId);
    }
}
    