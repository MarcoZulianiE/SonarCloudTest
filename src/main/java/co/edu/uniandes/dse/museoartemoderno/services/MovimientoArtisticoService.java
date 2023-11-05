package co.edu.uniandes.dse.museoartemoderno.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientoArtisticoService {

	@Autowired
	MovimientoArtisticoRepository movimientoArtisticoRepository;
	
	/**
	 *Da todos los movimientos artisticos 
	 * @return lista con todos los movimientos artisticos
	 */
	@Transactional
	public List<MovimientoArtisticoEntity> getMovimientosArtisticos()
	{
		return movimientoArtisticoRepository.findAll();
	}
	
	/**
	 * Da un movimiento artistico a partir de su Id
	 * @param pId - Id del movimiento que se quiere buscar
	 * @return El movimiento artistico con el Id buscado
	 * @throws EntityNotFoundException si no se encuentra la entidad buscada
	 */
	@Transactional
	public MovimientoArtisticoEntity getMovimientoArtistico(Long pId) throws EntityNotFoundException
	{
		log.info("Inicia proceso de obtener de un movimiento artisitico con id "+ pId);
		Optional<MovimientoArtisticoEntity> movimientoBuscado = movimientoArtisticoRepository.findById(pId);
		if(movimientoBuscado.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		log.info("Termina el proceso de obtener de un movimiento artisitico con id "+ pId);
		return movimientoBuscado.get();
	}
	
	/**
	 * Crea un movimiento artistico
	 * @param pMovimientoArtistico - Entidad a crear
	 * @throws EntityNotFoundException si no se encuentra la entidad
	 * @throws IllegalOperationException si se cumple alguna regla
	 */
	@Transactional
	public MovimientoArtisticoEntity createMovimientoArtistico(MovimientoArtisticoEntity pMovimientoArtistico) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia el proceso de creacion de un movimiento artistico");
		if(pMovimientoArtistico.getNombre()==null||pMovimientoArtistico.getNombre().equals(""))
		{
			throw new IllegalOperationException("Nombre no valido");
		}
			
		log.info("Termina el proceso de creacion de un movimiento artistico");
		return movimientoArtisticoRepository.save(pMovimientoArtistico);
	}
	
	/**
	 * Elimina un movimiento artistico a partir de su Id
	 * @param pId - Id del movimiento artistico que se quiere eliminar
	 */
	@Transactional
	public void deleteMovimientoArtistico(Long pId) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia el proceso de eliminar de un movimiento artisitico con id  "+ pId);
		Optional<MovimientoArtisticoEntity> movimientoBorrar = movimientoArtisticoRepository.findById(pId);

		if(movimientoBorrar.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		log.info("Termina el proceso de eliminar de un movimiento artisitico con id  "+ pId);
		movimientoArtisticoRepository.deleteById(pId);
	}
	
	/**
	 * Actualiza la informacion de un movimiento artistico
	 * @param movimientoId - Id del movimiento a actualizar
	 * @param movimientoArtistico - Informacion nueva
	 * @return Movimiento artistico actualizado
	 */
	@Transactional
	public MovimientoArtisticoEntity updateMovimientoArtistico(Long pId, MovimientoArtisticoEntity movimientoArtistico) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia proceso de actualizar el movimiento artistico con id: ", pId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(pId);
		if(movimientoEntity.isEmpty()) 
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(movimientoArtistico.getNombre()==null||movimientoArtistico.getNombre().equals(""))
		{
			throw new IllegalOperationException("Nombre no valido");
		}
		movimientoArtistico.setId(pId);
		log.info("Termina proceso de actualizar el movimiento artistico con id: ", pId);
		return movimientoArtisticoRepository.save(movimientoArtistico);
	}
	
}
