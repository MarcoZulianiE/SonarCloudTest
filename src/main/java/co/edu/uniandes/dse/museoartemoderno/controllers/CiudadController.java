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

import co.edu.uniandes.dse.museoartemoderno.dto.CiudadDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.CiudadEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.CiudadService;
@RestController
@RequestMapping("/ciudades")
public class CiudadController {
    @Autowired
	private CiudadService ciudadService;
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CiudadDTO create(@RequestBody CiudadDTO ciudadDTO) throws IllegalOperationException, EntityNotFoundException {
    	CiudadEntity ciudadEntity = ciudadService.createCiudad(modelMapper.map(ciudadDTO, CiudadEntity.class));
    	
     return modelMapper.map(ciudadEntity, CiudadDTO.class);
    }
    
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CiudadDTO> findAll() {
            List<CiudadEntity> ciudades = ciudadService.getCiudades();
            return modelMapper.map(ciudades, new TypeToken<List<CiudadDTO>>() {
            }.getType());
    }
    
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CiudadDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            CiudadEntity ciudadEntity = ciudadService.getCiudad(id);
            return modelMapper.map(ciudadEntity, CiudadDTO.class);
    }
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CiudadDTO update(@PathVariable("id") Long id, @RequestBody CiudadDTO ciudadDTO)
                    throws EntityNotFoundException, IllegalOperationException {
            CiudadEntity ciudadEntity = ciudadService.updateCiudad(id, modelMapper.map(ciudadDTO, CiudadEntity.class));
            return modelMapper.map(ciudadEntity, CiudadDTO.class);
    }
    
    
}


