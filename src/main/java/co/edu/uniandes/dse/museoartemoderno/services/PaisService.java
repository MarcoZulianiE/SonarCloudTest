package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaisService {
	@Autowired
	PaisRepository paisRepository;
	
	/**
	 * Crea un pais
	 * @param pais - Entidad a crear
	 * @throws EntityNotFoundException si no se encuentra la entidad
	 * @throws IllegalOperationException si se cumple alguna regla
	 */
	@Transactional
	public PaisEntity createPais(PaisEntity pais) throws EntityNotFoundException, IllegalOperationException{
		log.info("inicia proceso de creacion de pais");

		if (!paisRepository.findByNombrePais(pais.getNombrePais()).isEmpty()) {
			throw new IllegalOperationException("Pais name already exists");
		}
		if (!validateNombre(pais.getNombrePais()))
		{
			throw new IllegalOperationException("Nombre Pais is not valid");
		}
		if (!validateNombre(pais.getCoordenadasPais())) {
			throw new IllegalOperationException("Coordenadas are not valid");
		}
		
		if (!paisRepository.findByCoordenadasPais(pais.getCoordenadasPais()).isEmpty())
		{
			throw new IllegalOperationException("Coordenadas Pais already exists");
		}
		
		log.info("Termina proceso de creación del pais");

		return paisRepository.save(pais);	
	}
	/**
	 * Obtiene la lista de los registros de Pais.
	 *
	 * @return Colección de objetos de PaisEntity.
	 */
	@Transactional
	public List<PaisEntity> getPaises() {
		log.info("Inicia proceso de consultar todos los paises");
		return paisRepository.findAll();
	}
	
	/**
	 * Encuentra en la base de datos el pais con un id especifico
	 * @param paisID - Id del pais que se quiere obtener
	 * @return - El pais con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public PaisEntity getPais(Long paisId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el pais con id: " + paisId);
		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException("PAIS NOT FOUND");
		
		log.info("Termina proceso de consultar el pais con id: " + paisId);
		return paisEntity.get();
	}
	
	/**
	 * Actualiza la información de una instancia de Pais.
	 *
	 * @param paisId     Identificador de la instancia a actualizar
	 * @param paisEntity Instancia de AuthorEntity con los nuevos datos.
	 * @return Instancia de PaisEntity con los datos actualizados.
	 * @throws IllegalOperationException 
	 */
	@Transactional
	public PaisEntity updatePais(Long paisId, PaisEntity pais) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar el pais con id = ", paisId);
		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		
		if (!validateNombre(pais.getNombrePais()))
		{
			throw new IllegalOperationException("Nombre Pais is not valid");
		}
		if (!validateNombre(pais.getCoordenadasPais()))
		{
			throw new IllegalOperationException("Coordenadas are not valid");
		}
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException("PAIS_NOT_FOUND");
		
		log.info("Termina proceso de actualizar el pais con id = ", paisId);
		pais.setId(paisId);
		return paisRepository.save(pais);
	}
	
	@Transactional
	public void deletePais(Long paisId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el pais con id: ", paisId);
		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);
		
		if (paisEntity.isEmpty())
			throw new EntityNotFoundException("PAIS NOT FOUND");
		
		paisRepository.deleteById(paisId);
		log.info("Termina proceso de borrar el pais con id: " + paisId);
	}
	
	
	private boolean validateNombre(String nombre) {
		return !(nombre == null || nombre.isEmpty());
	}

	
}
