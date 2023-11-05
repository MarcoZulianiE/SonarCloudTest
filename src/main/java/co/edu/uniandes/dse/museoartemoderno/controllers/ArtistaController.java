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

import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.ArtistaService;

@RestController
@RequestMapping("/artistas")
public class ArtistaController {
	
	@Autowired
	private ArtistaService artistaService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ArtistaDTO create(@RequestBody ArtistaDTO artistaDTO) throws IllegalOperationException, EntityNotFoundException {
		ArtistaEntity artistaEntity = artistaService.createArtista(modelMapper.map(artistaDTO, ArtistaEntity.class));
		return modelMapper.map(artistaEntity, ArtistaDTO.class);	
	}

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ArtistaDetailDTO> findAll() {
		List<ArtistaEntity> books = artistaService.getArtistas();
		return modelMapper.map(books, new TypeToken<List<ArtistaDetailDTO>>() {
		}.getType());
	}
	
	@GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ArtistaDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            ArtistaEntity artistaEntity = artistaService.getArtista(id);
            return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
    }
	
	@PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ArtistaDTO update(@PathVariable("id") Long id, @RequestBody ArtistaDTO artistaDTO)
                    throws EntityNotFoundException, IllegalOperationException {
            ArtistaEntity artistaEntity = artistaService.updateArtista(id, modelMapper.map(artistaDTO, ArtistaEntity.class));
            return modelMapper.map(artistaEntity, ArtistaDTO.class);
    }
	
	@DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
            artistaService.deleteArtista(id);
    }
}
