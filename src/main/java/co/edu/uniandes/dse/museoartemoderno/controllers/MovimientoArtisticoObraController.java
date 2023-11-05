package co.edu.uniandes.dse.museoartemoderno.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.museoartemoderno.dto.ObraDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.ObraDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MovimientoArtisticoObraService;

@RestController
@RequestMapping("/movimientoartisticos")
public class MovimientoArtisticoObraController 
{
	@Autowired
	MovimientoArtisticoObraService movimientoArtisticoObraService;

	@Autowired 
	ModelMapper modelMapper;

	/**
	 * Guarda una obra dentro de un movimiento artistico
	 */
	@PostMapping(value = "/{movimientoId}/obras/{obraId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ObraDetailDTO addObra(@PathVariable("movimientoId") Long movimientoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException
	{
		ObraEntity obraEntity = movimientoArtisticoObraService.addObra(movimientoId, obraId);
		return modelMapper.map(obraEntity, ObraDetailDTO.class);
	}
	
	/**
	 * Agrega obras a un movimiento artistico
	 */
	@PutMapping(value = "/{movimientoId}/obras")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ObraDetailDTO> addObras(@PathVariable("movimientoId") Long movimientoId, @RequestBody List<ObraDTO> obras) throws EntityNotFoundException
	{
		List<ObraEntity> entities = modelMapper.map(obras, new TypeToken<List<ObraEntity>>() {
        }.getType());
		List<ObraEntity> entidades = movimientoArtisticoObraService.replaceObras(movimientoId, entities);
		return modelMapper.map(entidades, new TypeToken<List<ObraDetailDTO>>() {
        }.getType());
	}
	
	
	/**
	 * Busca y devuelve una obra asociada a un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/obras/{obraId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ObraDetailDTO getObra(@PathVariable("movimientoId") Long movimientoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException, IllegalOperationException
	{
		ObraEntity obraEntity = movimientoArtisticoObraService.getObra(movimientoId, obraId);
		return modelMapper.map(obraEntity, ObraDetailDTO.class);
	}
	
	/**
	 * Busca y devuelve todas las obras de un movimiento artistico
	 */
	@GetMapping(value = "/{movimientoId}/obras")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ObraDetailDTO> getObras(@PathVariable("movimientoId") Long movimientoId) throws EntityNotFoundException
	{
		List<ObraEntity> obraEntity = movimientoArtisticoObraService.getObras(movimientoId);
        return modelMapper.map(obraEntity, new TypeToken<List<ObraDetailDTO>>() {
        }.getType());
	}
	
}
