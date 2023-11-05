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
import org.springframework.web.bind.annotation.DeleteMapping;


import co.edu.uniandes.dse.museoartemoderno.dto.PaisDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.PaisDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.PaisService;

@RestController	
@RequestMapping("/paises")
public class PaisController {
	@Autowired
	private PaisService paisService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PaisDTO create(@RequestBody PaisDTO paisDTO) throws IllegalOperationException, EntityNotFoundException {
		PaisEntity paisEntity = paisService.createPais(modelMapper.map(paisDTO, PaisEntity.class));
		return modelMapper.map(paisEntity, PaisDTO.class);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PaisDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		PaisEntity paisEntity = paisService.getPais(id);
		return modelMapper.map(paisEntity, PaisDetailDTO.class);
	}

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<PaisDetailDTO> findAll() {
		List<PaisEntity> paises = paisService.getPaises();
		return modelMapper.map(paises, new TypeToken<List<PaisDetailDTO>>() {
		}.getType());
	}
	
		
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PaisDTO update(@PathVariable("id") Long id, @RequestBody PaisDTO paisDTO) 
			throws EntityNotFoundException, IllegalOperationException {
		PaisEntity paisEntity = paisService.updatePais(id, modelMapper.map(paisDTO, PaisEntity.class));
		return modelMapper.map(paisEntity, PaisDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		paisService.deletePais(id);
	}
	}
	
	

