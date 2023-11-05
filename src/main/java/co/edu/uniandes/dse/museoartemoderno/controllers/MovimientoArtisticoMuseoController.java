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

import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MovimientoArtisticoMuseoService;

@RestController
@RequestMapping("/movimientoartisticos")
public class MovimientoArtisticoMuseoController 
{
	@Autowired
	MovimientoArtisticoMuseoService movimientoArtisticoMuseoService;
	
	@Autowired 
	ModelMapper modelMapper;
	
	/**
	 * Busca y devuelve el museo con el Id recibido en la URL, relacionado a un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO getMuseo(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException, IllegalOperationException
	{
		MuseoEntity museoEntity = movimientoArtisticoMuseoService.getMuseo(museoId, movimientoId);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}
	
	/**
	 * Busca y devuelve todos los museos que existen de un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/museos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<MuseoDetailDTO> getMuseos(@PathVariable("movimientoId") Long movimientoId) throws EntityNotFoundException
	{
		List<MuseoEntity> museos = movimientoArtisticoMuseoService.getMuseos(movimientoId);
		return modelMapper.map(museos, new TypeToken<List<MuseoDetailDTO>>() {
		}.getType());

	}
	
	/**
	 * Asocia un museo existente con un movimiento artistico existente
	 */
	@PostMapping(value = "/{movimientoId}/museos/{museoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO addMuseo(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoID") Long museoID) throws EntityNotFoundException
	{
		MuseoEntity museoEntity = movimientoArtisticoMuseoService.addMuseo(movimientoId, museoID);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}
	
	/**
	 * Actualiza la lista de museos asociados a un movimiento artistico existente
	 */
	@PutMapping(value = "/{movimientoId}/museos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<MuseoDetailDTO> replaceMuseo(@PathVariable("movimientoId") Long movimientoId, @RequestBody List<MuseoDTO> museos) throws EntityNotFoundException
	{
		List<MuseoEntity> entidades = modelMapper.map(museos, new TypeToken<List<MuseoEntity>>() {
		}.getType());
		List<MuseoEntity> museoList = movimientoArtisticoMuseoService.addMuseos(movimientoId, entidades);
		return modelMapper.map(museoList, new TypeToken<List<MuseoDetailDTO>>() {
		}.getType());
	}
	
	/**
	 * Elimina la conexion entre un movimiento artistico y museo que se recibe en la url
	 */
	@DeleteMapping(value = "/{movimientoId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeArtista(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException
	{
		movimientoArtisticoMuseoService.removeMuseo(movimientoId, museoId);
	}
}
