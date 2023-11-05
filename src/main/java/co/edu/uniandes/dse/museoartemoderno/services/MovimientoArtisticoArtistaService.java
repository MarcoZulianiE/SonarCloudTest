package co.edu.uniandes.dse.museoartemoderno.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.ErrorMessage;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.repositories.ArtistaRepository;
import co.edu.uniandes.dse.museoartemoderno.repositories.MovimientoArtisticoRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MovimientoArtisticoArtistaService 
{
	@Autowired
	private MovimientoArtisticoRepository movimientoArtisticoRepository;
	@Autowired
	private ArtistaRepository artistaRepository;
	
	/**
	 * Asocia un artista a un movimiento artistico cuyo Id es dado por parametro
	 * @param movimientoId - Id del movimiento al que se le asociara el artista
	 * @param artistaID - Id del artistsa que se asociara
	 * @return - instancia del artista que fue asociado
	 * @throws EntityNotFoundException si no se encuentra alguna o cualquier entidad
	 */
	@Transactional
	public ArtistaEntity addArtista(Long movimientoId, Long artistaId) throws EntityNotFoundException
	{
		log.info("Inicia proceso de asociar al movimiento "+movimientoId+" el artista "+artistaId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(artistaEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		}
		artistaEntity.get().getMovimientos().add(movimientoEntity.get());
		log.info("Termina proceso de asociar al movimiento "+movimientoId+" el artista "+artistaId);
		return artistaEntity.get();
	}
	
	/**
	 * Obtiene una coleccion de instancias de ArtistaEntity asociadas a un MovimientoArtistico
	 * @param movimientoId - Id del movimiento artistico
	 * @throws EntityNotFoundException si el movimiento artistico no existe
	 * @return Coleccion de artistas relacionados con un movimiento artistico
	 */ 
	@Transactional
	public List<ArtistaEntity> getArtistas(Long movimientoId) throws EntityNotFoundException
	{
		log.info("Inicia proceso de obtener todos los artistas asociados con el movimiento artistico "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		List<ArtistaEntity> artistas = artistaRepository.findAll();
		List<ArtistaEntity> artistaList = new ArrayList<>();
		for(ArtistaEntity a: artistas)
		{
			if(a.getMovimientos().contains(movimientoEntity.get()))
			{
				artistaList.add(a);
			}
		}
		log.info("Finaliza proceso de obtener todos los artistas asociados con el movimiento artistico "+movimientoId);
		return artistaList;
	}
	
	/**
	 * Obtiene una instancia de ArtistaEntity asociada a una instancia de MovimientoArtisticoEntity
	 * @param movimientoId - MovimientoArtista
	 * @param artistaId - Artista
	 * @throws EntityNotFoundException si el artista o el movimiento no existe
	 * @return La entidad de Artista del movimiento
	 */
	@Transactional
	public ArtistaEntity getArtista(Long movimientoId, Long artistaId) throws EntityNotFoundException, IllegalOperationException
	{
		log.info("Inicia el proceso de obtener el artista "+artistaId+" asociado con el movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}
		if(artistaEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		}
		log.info("Termina el proceso de obtener el artista "+artistaId+" asociado con el movimiento "+movimientoId);
		if(artistaEntity.get().getMovimientos().contains(movimientoEntity.get()))
		{
			return artistaEntity.get();
		}

		throw new IllegalOperationException("El artista no esta asociado con el movimiento artistico");
	}

	/**
	 * Reemplaza las instancias de Artista asociadas a un Movimiento Artistico
	 * @param artistas - Coleccion de instancias de ArtistaEntity a asociar con la instancia de MovimientoArtistico
	 * @param movimientoId - Id del Movimiento al que se le quieren asociar los artistas
	 * @return nueva coleccion de ArtistaEntity asociado a la instancia de MovimientoArtistico
	 * @throws EntityNotFoundException si no se encuentra el movimiento o el artista
	 */
	@Transactional
	public List<ArtistaEntity> addArtistas(Long movimientoId, List<ArtistaEntity> artistas) throws EntityNotFoundException
	{
		log.info("Inicia proceso de de reemplazar los libros asociados al movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}

		for(ArtistaEntity artista: artistas)
		{
			Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artista.getId());
			if(artistaEntity.isEmpty())
			{
				throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
			}
			if(!artistaEntity.get().getMovimientos().contains(movimientoEntity.get()))
			{
				artistaEntity.get().getMovimientos().add(movimientoEntity.get());
			}
		}

		log.info("Finaliza proceso de de reemplazar los libros asociados al movimiento "+movimientoId);
		return artistas;
	}

	/**
	 * Desasocia un Artista de un Movimiento Artistico Existente
	 * @param movimientoId - Id del movimiento
	 * @param artistaId - Id del artista
	 * @throws EntityNotFoundException si no se encuentra el artista o el movimiento
	 */
	@Transactional
	public void removeArtista(Long movimientoId, Long artistaId) throws EntityNotFoundException
	{
		log.info("Inical el proceso de desasociar el artista "+artistaId+" del movimiento "+movimientoId);
		Optional<MovimientoArtisticoEntity> movimientoEntity = movimientoArtisticoRepository.findById(movimientoId);
		if(movimientoEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.MOVIMIENTO_ARTISTICO_NOT_FOUND);
		}

		Optional<ArtistaEntity> artistaEntity = artistaRepository.findById(artistaId);
		if(artistaEntity.isEmpty())
		{
			throw new EntityNotFoundException(ErrorMessage.ARTISTA_NOT_FOUND);
		}

		artistaEntity.get().getMovimientos().remove(movimientoEntity.get());
		log.info("Inical el proceso de desasociar el artista "+artistaId+" del movimiento "+movimientoId);
	}
}
