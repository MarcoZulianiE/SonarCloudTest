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

import co.edu.uniandes.dse.museoartemoderno.dto.ObraDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.ObraDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.ObraService;

@RestController
@RequestMapping("/obras")
public class ObraController {
	
	@Autowired
	private ObraService obraService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@PostMapping 
	@ResponseStatus(code = HttpStatus.CREATED)
	public ObraDTO create (@RequestBody ObraDTO obraDTO) throws IllegalOperationException, EntityNotFoundException{
		
		ObraEntity obraEntity = obraService.createObra(modelMapper.map(obraDTO, ObraEntity.class));
		
		return modelMapper.map(obraEntity, ObraDTO.class);
	}
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ObraDetailDTO> findAll() {
		List<ObraEntity> obras = obraService.getAllObras();
		return modelMapper.map(obras, new TypeToken<List<ObraDetailDTO>>() {
		}.getType());
	}
	
	@GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ObraDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            ObraEntity obraEntity = obraService.getObra(id);
            return modelMapper.map(obraEntity, ObraDetailDTO.class);
    }
	
	@PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ObraDTO update(@PathVariable("id") Long id, @RequestBody ObraDTO obraDTO)
                    throws EntityNotFoundException, IllegalOperationException {
            ObraEntity obraEntity = obraService.updateObra(id, modelMapper.map(obraDTO, ObraEntity.class));
            return modelMapper.map(obraEntity, ObraDTO.class);
    }
	
	@DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
            obraService.deleteObra(id);
    }
	

}