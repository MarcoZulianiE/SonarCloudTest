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

import co.edu.uniandes.dse.museoartemoderno.dto.ObraDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.ObraDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoObraService;

@RestController
@RequestMapping("/museos")
public class MuseoObraController {

	@Autowired
	private MuseoObraService museoObraService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/{museoId}/obras/{obraId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ObraDetailDTO addObra(@PathVariable("obraId") Long obraId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
		ObraEntity obraEntity = museoObraService.addObra(museoId, obraId);
		return modelMapper.map(obraEntity, ObraDetailDTO.class);
	}

	@GetMapping(value = "/{museoId}/obras/{obraId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ObraDetailDTO getObra(@PathVariable("obraId") Long obraId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException, IllegalOperationException {
			ObraEntity obraEntity = museoObraService.getObra(museoId, obraId);
            return modelMapper.map(obraEntity, ObraDetailDTO.class);
    }
	
	@PutMapping(value = "/{museoId}/obras")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ObraDetailDTO> addObras(@PathVariable("museoId") Long museoId, @RequestBody List<ObraDTO> obras) throws EntityNotFoundException {
            List<ObraEntity> entities = modelMapper.map(obras, new TypeToken<List<ObraEntity>>() {
            }.getType());
            List<ObraEntity> obrasList = museoObraService.replaceObras(museoId, entities);
            return modelMapper.map(obrasList, new TypeToken<List<ObraDetailDTO>>() {
            }.getType());
    }
	
	@GetMapping(value = "/{museoId}/obras")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ObraDetailDTO> getObras(@PathVariable("museoId") Long museoId) throws EntityNotFoundException {
            List<ObraEntity> obraEntity = museoObraService.getObras(museoId);
            return modelMapper.map(obraEntity, new TypeToken<List<ObraDetailDTO>>() {
            }.getType());
    }

	@DeleteMapping(value = "/{museoId}/obras/{obraId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("obraId") Long obraId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
		museoObraService.removeObra(museoId, obraId);
	}	
}
