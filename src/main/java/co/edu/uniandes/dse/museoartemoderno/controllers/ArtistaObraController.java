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
import co.edu.uniandes.dse.museoartemoderno.services.ArtistaObraService;

@RestController
@RequestMapping("/artistas")
public class ArtistaObraController {

	@Autowired
	private ArtistaObraService artistaObraService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/{artistaId}/obras/{obraId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ObraDetailDTO addObra(@PathVariable("obraId") Long obraId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
		ObraEntity obraEntity = artistaObraService.addObra(artistaId, obraId);
		return modelMapper.map(obraEntity, ObraDetailDTO.class);
	}

	@GetMapping(value = "/{artistaId}/obras/{obraId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ObraDetailDTO getObra(@PathVariable("obraId") Long obraId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException, IllegalOperationException {
			ObraEntity obraEntity = artistaObraService.getObra(artistaId, obraId);
            return modelMapper.map(obraEntity, ObraDetailDTO.class);
    }
	
	@PutMapping(value = "/{artistaId}/obras")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ObraDetailDTO> addObras(@PathVariable("artistaId") Long artistaId, @RequestBody List<ObraDTO> obras) throws EntityNotFoundException {
            List<ObraEntity> entities = modelMapper.map(obras, new TypeToken<List<ObraEntity>>() {
            }.getType());
            List<ObraEntity> obrasList = artistaObraService.replaceObras(artistaId, entities);
            return modelMapper.map(obrasList, new TypeToken<List<ObraDetailDTO>>() {
            }.getType());
    }
	
	@GetMapping(value = "/{artistaId}/obras")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ObraDetailDTO> getObras(@PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
            List<ObraEntity> obraEntity = artistaObraService.getObras(artistaId);
            return modelMapper.map(obraEntity, new TypeToken<List<ObraDetailDTO>>() {
            }.getType());
    }
}
