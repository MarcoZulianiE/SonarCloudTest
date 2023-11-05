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
import co.edu.uniandes.dse.museoartemoderno.services.ArtistaMuseoService;

@RestController
@RequestMapping("/artistas")
public class ArtistaMuseoController {

	@Autowired
	private ArtistaMuseoService artistaMuseoService;

	@Autowired 
	private ModelMapper modelMapper;

	@PostMapping(value = "/{artistaId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO addMuseo(@PathVariable("museoId") Long museoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
		MuseoEntity museoEntity = artistaMuseoService.addMuseo(artistaId, museoId);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}

	@GetMapping(value = "/{artistaId}/museos/{museoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MuseoDetailDTO getMuseo(@PathVariable("museoId") Long museoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity museoEntity = artistaMuseoService.getMuseo(artistaId, museoId);
		return modelMapper.map(museoEntity, MuseoDetailDTO.class);
	}

	@PutMapping(value = "/{artistaId}/museos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<MuseoDetailDTO> addMuseos(@PathVariable("artistaId") Long artistaId, @RequestBody List<MuseoDTO> museos)
			throws EntityNotFoundException {
		List<MuseoEntity> entities = modelMapper.map(museos, new TypeToken<List<MuseoEntity>>() {
		}.getType());
		List<MuseoEntity> museosList = artistaMuseoService.replaceMuseos(artistaId, entities);
		return modelMapper.map(museosList, new TypeToken<List<MuseoDetailDTO>>() {
		}.getType());
	}
	
	@GetMapping(value = "/{artistaId}/museos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MuseoDetailDTO> getMuseos(@PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
            List<MuseoEntity> museoEntity = artistaMuseoService.getMuseos(artistaId);
            return modelMapper.map(museoEntity, new TypeToken<List<MuseoDetailDTO>>() {
            }.getType());
    }
	
	@DeleteMapping(value = "/{artistaId}/museos/{museoId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("museoId") Long museoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
		artistaMuseoService.removeMuseo(artistaId, museoId);
    }
}
