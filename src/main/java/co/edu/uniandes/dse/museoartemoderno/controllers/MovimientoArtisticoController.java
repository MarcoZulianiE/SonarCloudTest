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
import co.edu.uniandes.dse.museoartemoderno.services.MovimientoArtisticoService;

@RestController
@RequestMapping("/movimientoartisticos")
public class MovimientoArtisticoController 
{
	  @Autowired
      private MovimientoArtisticoService movimientoArtisticoService;

      @Autowired
      private ModelMapper modelMapper;
      
      @PostMapping
      @ResponseStatus(code = HttpStatus.CREATED)
      public MovimientoArtisticoDTO create(@RequestBody MovimientoArtisticoDTO movimientoDTO) throws EntityNotFoundException, IllegalOperationException
      {
    	  MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoService.createMovimientoArtistico(modelMapper.map(movimientoDTO, MovimientoArtisticoEntity.class));
    	  //Aca se retorna un DTO, solo que ahora si tiene un Id
    	  return modelMapper.map(movimientoEntity, MovimientoArtisticoDTO.class);
      }
      
      @GetMapping
      @ResponseStatus(code = HttpStatus.OK)
      public List<MovimientoArtisticoDetailDTO> findall()
      {
    	  List<MovimientoArtisticoEntity> movimientos = movimientoArtisticoService.getMovimientosArtisticos();
    	  return modelMapper.map(movimientos, new TypeToken<List<MovimientoArtisticoDetailDTO>>() {
  		}.getType());
      }
      
      @GetMapping(value = "/{id}")
      @ResponseStatus(code = HttpStatus.OK)
      public MovimientoArtisticoDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException
      {
    	  MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoService.getMovimientoArtistico(id);
    	  return modelMapper.map(movimientoEntity, MovimientoArtisticoDetailDTO.class);
      }
      
      @PutMapping(value = "/{id}")
      @ResponseStatus(code = HttpStatus.OK)
      public MovimientoArtisticoDTO update(@PathVariable("id") Long id, @RequestBody MovimientoArtisticoDTO movimientoDTO) throws EntityNotFoundException, IllegalOperationException
      {
    	 MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoService.updateMovimientoArtistico(id, modelMapper.map(movimientoDTO, MovimientoArtisticoEntity.class));
    	 return modelMapper.map(movimientoEntity, MovimientoArtisticoDTO.class);
      }
      
      @DeleteMapping(value = "/{id}")
      @ResponseStatus(code = HttpStatus.NO_CONTENT)
      public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException
      {
    	  movimientoArtisticoService.deleteMovimientoArtistico(id);
      }
}
