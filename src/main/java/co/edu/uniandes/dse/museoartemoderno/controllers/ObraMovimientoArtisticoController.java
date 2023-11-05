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

import co.edu.uniandes.dse.museoartemoderno.dto.MovimientoArtisticoDetailDTO;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.services.ObraMovimientoArtisticoService;

@RestController
@RequestMapping("/obras")
public class ObraMovimientoArtisticoController {

	@Autowired
	private ObraMovimientoArtisticoService obraMovimientoArtisticoService;

	@Autowired 
	private ModelMapper modelMapper;

	@PostMapping(value = "/{obraId}/movimientoartisticos/{movimientoArtisticoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MovimientoArtisticoDetailDTO addMovimientoArtistico(@PathVariable("movimientoArtisticoId") Long movimientoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		MovimientoArtisticoEntity movimientoEntity = obraMovimientoArtisticoService.addMovimientoArtistico(obraId, movimientoId);
		return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
	}

	@GetMapping(value = "/{obraId}/movimientoartisticos/{movimientoArtisticoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MovimientoArtisticoDetailDTO getMovimientoArtistico(@PathVariable("movimientoArtisticoId") Long movimientoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException, IllegalOperationException {
		MovimientoArtisticoEntity movimientoEntity = obraMovimientoArtisticoService.getMovimientoArtistico(obraId);
		return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
	}
	
	@DeleteMapping(value = "/{obraId}/movimientoartisticos/{movimientoArtisticoId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeMovimientoArtistico(@PathVariable("movimientoArtisticoId") Long movimientoId, @PathVariable("obraId") Long obraId) throws EntityNotFoundException {
		obraMovimientoArtisticoService.removeMovimientoArtistico(obraId);
    }
}