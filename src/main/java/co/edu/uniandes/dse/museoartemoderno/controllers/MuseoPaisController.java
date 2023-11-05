package co.edu.uniandes.dse.museoartemoderno.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.PaisDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoPaisService;

@RequestMapping("/Museos")
public class MuseoPaisController {

	@Autowired
	private MuseoPaisService museoPaisService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PutMapping(value = "/{museoId}/ubicacion")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO replacePaisNacimiento(@PathVariable("museoId") Long museoId, @RequestBody PaisDTO paisDTO) throws EntityNotFoundException {
		MuseoEntity museoEntity = museoPaisService.replacePais(museoId, paisDTO.getId());
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}

}