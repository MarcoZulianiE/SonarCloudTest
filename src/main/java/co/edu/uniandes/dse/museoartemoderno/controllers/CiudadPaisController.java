package co.edu.uniandes.dse.museoartemoderno.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.museoartemoderno.dto.PaisDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.CiudadPaisService;

@RestController
public class CiudadPaisController {

	@Autowired
	private CiudadPaisService ciudadPaisService;
    @Autowired
    private ModelMapper modelMapper;
	@PostMapping(value = "/{ciudadId}/paises/{paisId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PaisDetailDTO addAuthor(@PathVariable("paisId") Long paisId, @PathVariable("ciudadId") Long ciudadId)
                    throws EntityNotFoundException {
            PaisEntity paisEntity = ciudadPaisService.addPais(ciudadId, paisId);
            return modelMapper.map(paisEntity, PaisDetailDTO.class);
    }
	
	
	@GetMapping(value = "/{ciudadId}/paises/{paisId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PaisDetailDTO getAuthor(@PathVariable("paisId") Long paisId, @PathVariable("ciudadId") Long ciudadId)
                    throws EntityNotFoundException, IllegalOperationException {
            PaisEntity paisEntity = ciudadPaisService.getPais(ciudadId);
            return modelMapper.map(paisEntity, PaisDetailDTO.class);
    }
	
	@DeleteMapping(value = "/{ciudadId}/paises/{paisId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("paisId") Long paisId, @PathVariable("ciudadId") Long ciudadId)
                    throws EntityNotFoundException {
            ciudadPaisService.removePais(ciudadId);
    }	

    
}
