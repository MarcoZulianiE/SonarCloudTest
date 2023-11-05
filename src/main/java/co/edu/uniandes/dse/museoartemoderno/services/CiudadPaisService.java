package co.edu.uniandes.dse.museoartemoderno.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import co.edu.uniandes.dse.museoartemoderno.repositories.CiudadRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import co.edu.uniandes.dse.museoartemoderno.entities.CiudadEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;

@Slf4j
@Service
@Component
@ComponentScan
public class CiudadPaisService {
		
		@Autowired
		private CiudadRepository ciudadRepository;

		@Autowired
		private PaisRepository paisRepository;
		
		
		/**
		 * Asocia un Pais existente a una Ciudad
		 *
		 * @param obraId Identificador de la instancia de ciudad
		 * @param museoId   Identificador de la instancia de pais
		 * @return Instancia de PaisEntity que fue asociada a ciudad
		 */

		@Transactional
		public PaisEntity addPais(Long ciudadId, Long paisId) throws EntityNotFoundException {
			log.info("Inicia proceso de asociar un museo a la obra con id = ", ciudadId);
			Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
			Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);

			if (ciudadEntity.isEmpty())
				throw new EntityNotFoundException("Ciudad no encontrada.");

			if (paisEntity.isEmpty())
				throw new EntityNotFoundException("Pais no encontrado.");
			
			ciudadEntity.get().setPais(paisEntity.get());
			log.info("Termina proceso de asociar un pais a la ciudad con id = ", ciudadId);
			return paisEntity.get();
		}

		/**
		 * Obtiene el pais en el que está una ciudad
		 *
		 * @param ciudadId Identificador de la instancia de ciudad
		 * @return Pais asociado a la ciudad dado su id
		 */
		@Transactional
		public PaisEntity getPais(Long ciudadId) throws EntityNotFoundException {
			log.info("Inicia proceso de consultar el pais de la ciudad con id = ", ciudadId);
			Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
			if (ciudadEntity.isEmpty() )
				throw new EntityNotFoundException("Ninguna ciudad fue encontrada con el id dado.");

			log.info("Termina proceso de consultar el pais de la ciudad con id = ", ciudadId);
			return (ciudadEntity.get()).getPais();
		}


		/**
		 * Desasocia un Pais existente de una ciudad existente
		 * @param ciudadId Identificador de la instancia de ciudad
		 */
		@Transactional
		public void removePais(Long ciudadId) throws EntityNotFoundException {
			log.info("Inicia proceso de borrar un pais de la ciudad con id = ", ciudadId);
			Optional<CiudadEntity> ciudadEntity = ciudadRepository.findById(ciudadId);
			if (ciudadEntity.isEmpty())
				throw new EntityNotFoundException("Ninguna ciudad fue encontrada con el id dado.");


			ciudadEntity.get().setPais(null);
			log.info("Finaliza proceso de borrar un pais de la ciudad con id = ", ciudadId);
		}

	


}
