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

import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.PaisDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.services.ArtistaPaisService;

@RestController
@RequestMapping("/artistas")
public class ArtistaPaisController {

	@Autowired
	private ArtistaPaisService artistaPaisService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PutMapping(value = "/{artistaId}/lugarNacimiento")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO replacePaisNacimiento(@PathVariable("artistaId") Long artistaId, @RequestBody PaisDTO paisDTO) throws EntityNotFoundException {
		ArtistaEntity artistaEntity = artistaPaisService.replaceLugarNacimiento(artistaId, paisDTO.getId());
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}
	
	@PutMapping(value = "/{artistaId}/lugarFallecimiento")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO replacePaisFallecimiento(@PathVariable("artistaId") Long artistaId, @RequestBody PaisDTO paisDTO) throws EntityNotFoundException {
		ArtistaEntity artistaEntity = artistaPaisService.replaceLugarFallecimiento(artistaId, paisDTO.getId());
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}
}
