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
import co.edu.uniandes.dse.museoartemoderno.services.ArtistaMovimientoArtisticoService;

@RestController
@RequestMapping("/artistas")
public class ArtistaMovimientoArtisticoController {

	@Autowired
	private ArtistaMovimientoArtisticoService artistaMovimientoArtisticoService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/{artistaId}/movimientos/{movimientoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MovimientoArtisticoDetailDTO addMovimiento(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
            MovimientoArtisticoEntity movimientoEntity = artistaMovimientoArtisticoService.addMovimientoArtistico(artistaId, movimientoId);
            return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
    }
	
	@GetMapping(value = "/{artistaId}/movimientos/{movimientoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MovimientoArtisticoDetailDTO getMovimiento(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException, IllegalOperationException {
			MovimientoArtisticoEntity movimientoArtisticoEntity = artistaMovimientoArtisticoService.getMovimientoArtistico(artistaId, movimientoId);
            return modelMapper.map(movimientoArtisticoEntity, MovimientoArtisticoDetailDTO.class);
    }
	
	@PutMapping(value = "/{artistaId}/movimientos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MovimientoArtisticoDetailDTO> addMovimientos(@PathVariable("artistaId") Long artistaId, @RequestBody List<MovimientoArtisticoDTO> movimientos) throws EntityNotFoundException {
            List<MovimientoArtisticoEntity> entities = modelMapper.map(movimientos, new TypeToken<List<MovimientoArtisticoEntity>>() {
            }.getType());
            List<MovimientoArtisticoEntity> authorsList = artistaMovimientoArtisticoService.replaceMovimientosArtisticos(artistaId, entities);
            return modelMapper.map(authorsList, new TypeToken<List<MovimientoArtisticoDetailDTO>>() {
            }.getType());
    }
	
	@GetMapping(value = "/{artistaId}/movimientos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MovimientoArtisticoDetailDTO> getMovimientos(@PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
            List<MovimientoArtisticoEntity> movimientoArtisticoEntity = artistaMovimientoArtisticoService.getMovimientosArtisticos(artistaId);
            return modelMapper.map(movimientoArtisticoEntity, new TypeToken<List<MovimientoArtisticoDetailDTO>>() {
            }.getType());
    }

	@DeleteMapping(value = "/{artistaId}/movimientos/{movimientoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeMovimiento(@PathVariable("movimientoId") Long movimientoId, @PathVariable("artistaId") Long artistaId) throws EntityNotFoundException {
		artistaMovimientoArtisticoService.removeMovimientoArtistico(artistaId, movimientoId);
	}	
}
