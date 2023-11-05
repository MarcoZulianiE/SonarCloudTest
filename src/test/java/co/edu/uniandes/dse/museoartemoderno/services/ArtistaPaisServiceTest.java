package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ArtistaPaisService.class, ArtistaService.class })
public class ArtistaPaisServiceTest {

	@Autowired
	private ArtistaPaisService artistaPaisService;
	
	@Autowired
	private ArtistaService artistaService;

	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();

	private ArtistaEntity artista = new ArtistaEntity();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
	private List<Date> fechaList = new ArrayList<>();
	private List<MuseoEntity> museoList = new ArrayList<>();
	private List<MovimientoArtisticoEntity> movimientoArtisticoList = new ArrayList<>();
	
	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
	
	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ArtistaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from PaisEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from MuseoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ObraEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity").executeUpdate();
	}
	
	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		for (int i = 0; i < 6; i++) {
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}

		for (int i = 0; i < 3; i++) {
			MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museoEntity);
			museoList.add(museoEntity);
		}
		
		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity movimientoArtisticoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimientoArtisticoEntity);
			movimientoArtisticoList.add(movimientoArtisticoEntity);
		}
		
		for (int i = 0; i < 3; i++) {
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(sdf.parse("2000-05-20"));
			fechaList.add(sdf.parse("2021-05-20"));
			fechaList.add(sdf.parse("2020-05-20"));
			fechaList.add(sdf.parse("2023-05-20"));
			fechaList.add(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		artista = factory.manufacturePojo(ArtistaEntity.class);
		artista.setFechaNacimiento(fechaList.get(0));
		artista.setFechaFallecimiento(fechaList.get(1));
		artista.setLugarNacimiento(paisList.get(0));
		artista.setLugarFallecimiento(paisList.get(0));
		artista.setMuseos(museoList);
		artista.setMovimientos(movimientoArtisticoList);
		artista.setObras(obraList);
		entityManager.persist(artista);
	}
	
	/**
	 * Prueba para remplazar el Pais de Nacimiento asociado a Artista.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePaisNacimiento() throws EntityNotFoundException {
		ArtistaEntity entity = artista;
		artistaPaisService.replaceLugarNacimiento(entity.getId(), paisList.get(1).getId());
		entity = artistaService.getArtista(entity.getId());
		assertEquals(entity.getLugarNacimiento(), paisList.get(1));
	}
	
	/**
	 * Prueba para remplazar el Pais de Fallecimiento asociado a un Artista
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Test
	void testReplacePaisFallecimiento() throws EntityNotFoundException {
		ArtistaEntity entity = artista;
		artistaPaisService.replaceLugarFallecimiento(entity.getId(), paisList.get(1).getId());
		entity = artistaService.getArtista(entity.getId());
		assertEquals(entity.getLugarFallecimiento(), paisList.get(1));
	}
	
	/**
	 * Prueba para remplazar los Artistas asociados a un Pais de un Pais de Fallecimiento 
	 * que no existe
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Test
	void testReplaceLugarNacimientoInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaPaisService.replaceLugarNacimiento(0L, paisList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar los Artistas asociados a un Pais de un Pais de Fallecimiento 
	 * que no existe
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Test
	void testReplaceLugarFallecimientoInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaPaisService.replaceLugarFallecimiento(0L, paisList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar los Artistas asociado a un Pais de Nacimiento que no existe
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Test
	void testReplaceLugarNacimientoInvalidPais() {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity entity = artista;
			artistaPaisService.replaceLugarNacimiento(entity.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para remplazar los Artistas asociados a un Pais de Fallecimiento que no existe
	 * @throws EntityNotFoundException - Exception que se lanza si no se encuentra la entidad
	 */
	@Test
	void testReplaceLugarFallecimientoInvalidPais() {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity entity = artista;
			artistaPaisService.replaceLugarFallecimiento(entity.getId(), 0L);
		});
	}
}
