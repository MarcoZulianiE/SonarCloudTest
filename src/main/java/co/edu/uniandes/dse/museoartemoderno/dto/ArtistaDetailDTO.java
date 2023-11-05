package co.edu.uniandes.dse.museoartemoderno.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistaDetailDTO extends ArtistaDTO{
	private List<MuseoDTO> museos = new ArrayList<>();
	private List<ObraDTO> obras = new ArrayList<>();
	private List<MovimientoArtisticoDTO> movimientos = new ArrayList<>();
}
