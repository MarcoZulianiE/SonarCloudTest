package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.CiudadEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.CiudadRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CiudadService {
	@Autowired
	CiudadRepository ciudadRepository;
	
	/**
	 * Crea una ciudad
	 * @param ciudad - Entidad a crear
	 * @throws EntityNotFoundException si no se encuentra la entidad
	 * @throws IllegalOperationException si no se cumple alguna regla
	 */
	public CiudadEntity createCiudad(CiudadEntity ciudadEntity) throws EntityNotFoundException, IllegalOperationException{
		log.info("inicia proceso de creacion de ciudad");
		if (!validateNombre(ciudadEntity.getNombreCiudad()))
			throw new IllegalOperationException("Nombre Ciudad is not valid");
		if (!validateNombre(ciudadEntity.getCoordenadasCiudad()))
			throw new IllegalOperationException("Coordenadas is not valid");
		if (!ciudadRepository.findByNombreCiudad(ciudadEntity.getNombreCiudad()).isEmpty())
			throw new IllegalOperationException("Nombre Ciudad already exists");
		if (!ciudadRepository.findByCoordenadasCiudad(ciudadEntity.getCoordenadasCiudad()).isEmpty())
			throw new IllegalOperationException("Coordenadas Ciudad already exists");
		
		return ciudadRepository.save(ciudadEntity);	
	}
	/**
	 * Obtiene la lista de los registros de Ciudad.
	 *
	 * @return Colección de objetos de CiudadEntity.
	 */
	public List<CiudadEntity> getCiudades() {
		log.info("Inicia proceso de consultar todos las ciudades");
		return ciudadRepository.findAll();
	}
	
	/**
	 * Encuentra en la base de datos la ciudad con un id especifico
	 * @param ciudadID - Id de la ciudad que se quiere obtener
	 * @return - La ciudad con el id dado por parametro
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Transactional
	public CiudadEntity getCiudad(Long ciudadId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la ciudad con id: " + ciudadId);
		Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
		
		if (ciudadEntity.isEmpty())
			throw new EntityNotFoundException("CIUDAD NOT FOUND");
		
		log.info("Termina proceso de consultar la ciudad con id: " + ciudadId);
		return ciudadEntity.get();
	}
	
	/**
	 * Actualiza la información de una instancia de Ciudad.
	 *
	 * @param ciudadId     Identificador de la instancia a actualizar
	 * @param ciudadEntity Instancia de AuthorEntity con los nuevos datos.
	 * @return Instancia de CiudadEntity con los datos actualizados.
	 */
	@Transactional
	public CiudadEntity updateCiudad(Long ciudadId, CiudadEntity ciudad) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la ciudad con id = ", ciudadId);
		Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
		if (!validateNombre(ciudad.getNombreCiudad()))
			throw new IllegalOperationException("Nombre Ciudad is not valid");
		if (!validateNombre(ciudad.getCoordenadasCiudad()))
			throw new IllegalOperationException("Coordenadas is not valid");
		if (!ciudadRepository.findByNombreCiudad(ciudad.getNombreCiudad()).isEmpty())
			throw new IllegalOperationException("Nombre Ciudad already exists");
		if (!ciudadRepository.findByCoordenadasCiudad(ciudad.getCoordenadasCiudad()).isEmpty())
			throw new IllegalOperationException("Coordenadas Ciudad already exists");
		if (ciudadEntity.isEmpty())
			throw new EntityNotFoundException("PAIS_NOT_FOUND");
		
		log.info("Termina proceso de actualizar el pais con id = ", ciudadId);
		ciudad.setId(ciudadId);
		return ciudadRepository.save(ciudad);
	}
	
	@Transactional
	public void deleteCiudad(Long ciudadId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la ciudad con id: ", ciudadId);
		Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
		
		if (ciudadEntity.isEmpty())
			throw new EntityNotFoundException("CIUDAD NOT FOUND");
		
		ciudadRepository.deleteById(ciudadId);
		log.info("Termina proceso de borrar la ciudad con id: " + ciudadId);
	}
	/**
	 * Verifica que el nombre sea valido
	 *
	 * @param nombre
	 * @return true si el nombre es valido.
	 */	
	private boolean validateNombre(String nombre) {
		return !(nombre == null || nombre.isEmpty());
	}
}
