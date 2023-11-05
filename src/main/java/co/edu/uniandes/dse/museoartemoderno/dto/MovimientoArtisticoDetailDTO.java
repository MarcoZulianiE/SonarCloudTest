package co.edu.uniandes.dse.museoartemoderno.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoArtisticoDetailDTO extends MovimientoArtisticoDTO
{
	private List<ObraDTO> obras = new ArrayList<>();

	private List<MuseoDTO> museos = new ArrayList<>();

	private List<ArtistaDTO> artistas = new ArrayList<>();
}
