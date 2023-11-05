package co.edu.uniandes.dse.museoartemoderno.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.ObraMuseoService;

@RestController
@RequestMapping("/obras")
public class ObraMuseoController {

	@Autowired
	private ObraMuseoService obraArtistaService;

	@Autowired 
	private ModelMapper modelMapper;

	@PostMapping(value = "/{obraId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO addMuseo(@PathVariable("museoId") Long museoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		MuseoEntity museoEntity = obraArtistaService.addMuseo(obraId, museoId);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}

	@GetMapping(value = "/{obraId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO getMuseo(@PathVariable("museoId") Long museoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity museoEntity = obraArtistaService.getMuseo(obraId);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}
	
	@DeleteMapping(value = "/{obraId}/museos/{museoId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeMuseo(@PathVariable("artistaId") Long museoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		obraArtistaService.removeMuseo(obraId);
    }
}