package co.edu.uniandes.dse.museoartemoderno.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.museoartemoderno.dto.MovimientoArtisticoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.PaisDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.services.MovimientoArtisticoPaisService;

@RestController
@RequestMapping("/movimientoartisticos")
public class MovimientoArtisticoPaisController 
{
	@Autowired
	MovimientoArtisticoPaisService movimientoArtisticoPaisService;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * Reemplaza la instancia de pais asociada a un movimiento artistico
	 */
	@PutMapping(value = "/{movimientoId}/lugarOrigen")
	@ResponseStatus(code = HttpStatus.OK)
	public MovimientoArtisticoDetailDTO replacePais(@PathVariable("movimientoId") Long movimientoId, @RequestBody PaisDTO paisDTO) throws EntityNotFoundException
	{
		MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoPaisService.replacePais(movimientoId, paisDTO.getId());
		return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
	}
	
	
}
