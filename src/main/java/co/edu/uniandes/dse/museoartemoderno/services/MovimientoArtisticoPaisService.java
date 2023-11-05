package co.edu.uniandes.dse.museoartemoderno.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.PaisRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientoArtisticoPaisService 
{
	@Autowired
	private MovimientoArtisticoRepository movimientoArtisticoRepository;

	@Autowired
	private PaisRepository paisRepository;


	/**
	 * Reemplazar el pais de un movimiento artistico
	 * @param movimientoId - Id del movimiento
	 * @param paisId - Id del pais
	 * @throws EntityNotFoundException si no se encuentra el pais o el movimiento
	 * @return el movimiento artistico
	 */
	@Transactional
	public MovimientoArtisticoEntity replacePais(Long movimientoId, Long paisId) throws EntityNotFoundException
	{
		log.info("Inicia el proceso de actualizar el movimiento "+movimientoId);

		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<PaisEntity> paisEntity = paisRepository.findById(paisId);

		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(paisEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.PAIS_NOT_FOUND);
		}

		movimientoEntity.get().setLugarOrigen(paisEntity.get());
		log.info("Termina el proceso de actualizar el movimiento "+movimientoId);
		return movimientoEntity.get();
	}

	/**
	 * Eliminar un movimiento artistico de un pais (borrar la relacion)
	 * @param movimientoId - Id del movimiento artistico
	 * @throws EntityNotFoundException si no se encuentra el movimiento
	 */
	@Transactional
	public void removeMovimientoArtistico(Long movimientoId) throws EntityNotFoundException
	{
		log.info("Inicio eliminacion del movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		Optional<PaisEntity> paisEntity = paisRepository.findById(movimientoEntity.get().getLugarOrigen().getId());
		paisEntity.ifPresent(lugarOrigen -> lugarOrigen.getMovimientoArtisticos().remove(movimientoEntity.get()));

		movimientoEntity.get().setLugarOrigen(null);
		log.info("Final eliminacion del movimiento "+movimientoId);
	}
}
