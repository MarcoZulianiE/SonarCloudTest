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

import co.edu.uniandes.dse.museoartemoderno.dto.ArtistaDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.ObraArtistaService;

@RestController
@RequestMapping("/obras")
public class ObraArtistaController {

	@Autowired
	private ObraArtistaService obraArtistaService;

	@Autowired 
	private ModelMapper modelMapper;

	@PostMapping(value = "/{obraId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO addArtista(@PathVariable("artistaId") Long artistaId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		ArtistaEntity artistaEntity = obraArtistaService.addArtista(obraId, artistaId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}

	@GetMapping(value = "/{obraId}/artistas/{artistaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ArtistaDetailDTO getArtista(@PathVariable("artistaId") Long artistaId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity artistaEntity = obraArtistaService.getArtista(obraId);
		return modelMapper.map(artistaEntity, ArtistaDetailDTO.class);
	}
	
	@DeleteMapping(value = "/{obraId}/artistas/{artistaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("artistaId") Long artistaId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		obraArtistaService.removeArtista(obraId);
    }
}