package co.edu.uniandes.dse.museoartemoderno.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MovimientoArtisticoArtistaService;

@RestController
@RequestMapping("/movimientoartisticos")
public class MovimientoArtisticoArtistaController 
{
	@Autowired
	private MovimientoArtisticoArtistaService movimientoArtisticoArtistaService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve el artista con el Id recibido en la URL, relacionado a un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO getArtista(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException, IllegalOperationException
	{
		ArtistaEntity artistaEntity = movimientoArtisticoArtistaService.getArtista(movimientoId, artistaId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}
	
	/**
	 * Busca y devuelve todos los artistas que existen de un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/artistas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ArtistaDetailDTO> getArtistas(@PathVariable("movimientoId") Long movimientoId) throws EntityNotFoundException
	{
		List<ArtistaEntity> artistas = movimientoArtisticoArtistaService.getArtistas(movimientoId);
		return modelMapper.map(artistas, new TypeToken<List<ArtistaDetailDTO>>() {
		}.getType());

	}
	
	/**
	 * Asocia un artista existente con un movimiento artistico existente
	 */
	@PostMapping(value = "/{movimientoId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO addArtista(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException
	{
		ArtistaEntity artistaEntity = movimientoArtisticoArtistaService.addArtista(movimientoId, artistaId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}
	
	/**
	 * Actualiza la lista de artistas de un movimiento con la lista que recibe en el cuerpo
	 */
	@PutMapping(value = "/{movimientoId}/artistas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ArtistaDetailDTO> replaceArtistas(@PathVariable("movimientoId") Long movimientoId, @RequestBody List<ArtistaDTO> artistas) throws EntityNotFoundException
	{
		List<ArtistaEntity> entidades = modelMapper.map(artistas, new TypeToken<List<ArtistaEntity>>() {
		}.getType());
		List<ArtistaEntity> artistaList = movimientoArtisticoArtistaService.addArtistas(movimientoId, entidades);
		return modelMapper.map(artistaList, new TypeToken<List<ArtistaDetailDTO>>() {
		}.getType());
	}
	
	/**
	 * Elimina la conexion entre un movimiento artistico y artista que se recibe en la url
	 */
	@DeleteMapping(value = "/{movimientoId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeArtista(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException
	{
		movimientoArtisticoArtistaService.removeArtista(movimientoId, artistaId);
	}
}
