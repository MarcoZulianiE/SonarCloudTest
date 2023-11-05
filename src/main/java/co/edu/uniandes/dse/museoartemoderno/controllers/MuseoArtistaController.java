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
import co.edu.uniandes.dse.museoartemoderno.services.MuseoArtistaService;

@RestController
@RequestMapping("/Museos")
public class MuseoArtistaController {

	@Autowired
	private MuseoArtistaService museoArtistaService;

	@Autowired 
	private ModelMapper modelMapper;

	@PostMapping(value = "/{museoId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO addArtista(@PathVariable("artistaId") Long artistaId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
		ArtistaEntity artistaEntity = museoArtistaService.addArtista(museoId, artistaId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}

	@GetMapping(value = "/{museoId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO getArtista(@PathVariable("artistaId") Long artistaId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity artistaEntity = museoArtistaService.getArtista(museoId, artistaId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}

	@PutMapping(value = "/{museoId}/artistas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ArtistaDetailDTO> addArtistas(@PathVariable("museoId") Long museoId, @RequestBody List<ArtistaDTO> artistas)
			throws EntityNotFoundException {
		List<ArtistaEntity> entities = modelMapper.map(artistas, new TypeToken<List<ArtistaEntity>>() {
		}.getType());
		List<ArtistaEntity> artistasList = museoArtistaService.replaceArtistas(museoId, entities);
		return modelMapper.map(artistasList, new TypeToken<List<ArtistaDetailDTO>>() {
		}.getType());
	}
	
	@GetMapping(value = "/{museoId}/artistas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ArtistaDetailDTO> getArtistas(@PathVariable("museoId") Long museoId) throws EntityNotFoundException {
            List<ArtistaEntity> artistaEntity = museoArtistaService.getArtistas(museoId);
            return modelMapper.map(artistaEntity, new TypeToken<List<ArtistaDetailDTO>>() {
            }.getType());
    }
	
	@DeleteMapping(value = "/{museoId}/artistas/{artistaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("artistaId") Long artistaId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
		museoArtistaService.removeArtista(museoId, artistaId);
    }
}

