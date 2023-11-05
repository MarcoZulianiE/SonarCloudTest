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

import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.MuseoDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoService;

@RestController
@RequestMapping("/Museos")
public class MuseoController {
	
	@Autowired
	private MuseoService museoService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public MuseoDTO create(@RequestBody MuseoDTO MuseoDTO) throws IllegalOperationException, EntityNotFoundException {
		MuseoEntity museoEntity = museoService.createMuseo(modelMapper.map(MuseoDTO, MuseoEntity.class));
		return modelMapper.map(museoEntity, MuseoDTO.class);	
	}

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<MuseoDetailDTO> findAll() {
		List<MuseoEntity> museoEntity = museoService.getMuseos();
		return modelMapper.map(museoEntity, new TypeToken<List<MuseoDetailDTO>>() {
		}.getType());
	}
	
	@GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MuseoDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            MuseoEntity museoEntity = museoService.getMuseo(id);
            return modelMapper.map(museoEntity, MuseoDetailDTO.class);
    }
	
	@PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MuseoDTO update(@PathVariable("id") Long id, @RequestBody MuseoDTO MuseoDTO)
                    throws EntityNotFoundException, IllegalOperationException {
            MuseoEntity museoEntity = museoService.updateMuseo(id, modelMapper.map(MuseoDTO, MuseoEntity.class));
            return modelMapper.map(museoEntity, MuseoDTO.class);
    }
	
	@DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
            museoService.deleteMuseo(id);
    }
}
