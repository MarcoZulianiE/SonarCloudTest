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

import co.edu.uniandes.dse.museoartemoderno.dto.MovimientoArtisticoDTO;
import co.edu.uniandes.dse.museoartemoderno.dto.MovimientoArtisticoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoMovimientoArtisticoService;

@RestController
@RequestMapping("/Museos")
public class MuseoMovimientoArtisticoController {

	@Autowired
	private MuseoMovimientoArtisticoService museoMovimientoArtisticoService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/{museoId}/movimientos/{movimientoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MovimientoArtisticoDetailDTO addAuthor(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
            MovimientoArtisticoEntity movimientoEntity = museoMovimientoArtisticoService.addMovimientoArtistico(museoId, movimientoId);
            return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
    }
	
	@GetMapping(value = "/{museoId}/movimientos/{movimientoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MovimientoArtisticoDetailDTO getAuthor(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException, IllegalOperationException {
			MovimientoArtisticoEntity movimientoArtisticoEntity = museoMovimientoArtisticoService.getMovimientoArtistico(museoId, movimientoId);
            return modelMapper.map(movimientoArtisticoEntity, MovimientoArtisticoDetailDTO.class);
    }
	
	@PutMapping(value = "/{museoId}/movimientos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MovimientoArtisticoDetailDTO> addAuthors(@PathVariable("museoId") Long museoId, @RequestBody List<MovimientoArtisticoDTO> movimientos) throws EntityNotFoundException {
            List<MovimientoArtisticoEntity> entities = modelMapper.map(movimientos, new TypeToken<List<MovimientoArtisticoEntity>>() {
            }.getType());
            List<MovimientoArtisticoEntity> authorsList = museoMovimientoArtisticoService.replaceMovimientoArtisticos(museoId, entities);
            return modelMapper.map(authorsList, new TypeToken<List<MovimientoArtisticoDetailDTO>>() {
            }.getType());
    }
	
	@GetMapping(value = "/{museoId}/movimientos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MovimientoArtisticoDetailDTO> getAuthors(@PathVariable("museoId") Long museoId) throws EntityNotFoundException {
            List<MovimientoArtisticoEntity> movimientoArtisticoEntity = museoMovimientoArtisticoService.getMovimientoArtisticos(museoId);
            return modelMapper.map(movimientoArtisticoEntity, new TypeToken<List<MovimientoArtisticoDetailDTO>>() {
            }.getType());
    }

	@DeleteMapping(value = "/{museoId}/movimientos/{movimientoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("movimientoId") Long movimientoId, @PathVariable("museoId") Long museoId) throws EntityNotFoundException {
		museoMovimientoArtisticoService.removeMovimientoArtistico(museoId, movimientoId);
	}	
}