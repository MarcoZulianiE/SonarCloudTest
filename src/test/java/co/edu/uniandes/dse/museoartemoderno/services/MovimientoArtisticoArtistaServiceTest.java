package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MovimientoArtisticoArtistaService.class)
class MovimientoArtisticoArtistaServiceTest 
{
	@Autowired
	private MovimientoArtisticoArtistaService movimientoArtisticoArtistaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private MovimientoArtisticoEntity movimientoArtistico = new MovimientoArtisticoEntity();

	private List<MuseoEntity> museoList = new ArrayList<>();

	private List<ObraEntity> obraList = new ArrayList<>();

	private List<ArtistaEntity> artistaList = new ArrayList<>();

	private List<Date> fechaList = new ArrayList<>();

	private List<PaisEntity> paisList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() 
	{
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que estan implicadas en la prueba
	 */
	private void clearData()
	{
		entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity");
		entityManager.getEntityManager().createQuery("delete from ObraEntity");
		entityManager.getEntityManager().createQuery("delete from MuseoEntity");
		entityManager.getEntityManager().createQuery("delete from ArtistaEntity");
		entityManager.getEntityManager().createQuery("delete from PaisEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de la prueba
	 */
	private void insertData()
	{
		for(int i = 1; i<=3; i++)
		{
			MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museoEntity);
			museoList.add(museoEntity);
		}
		for(int i = 1; i<= 3; i++)
		{
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(sdf.parse("2000-10-20"));

			fechaList.add(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for(int i = 1; i<=3; i++)
		{
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}

		movimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		movimientoArtistico.setFechaApogeo(fechaList.get(0));
		movimientoArtistico.setMuseos(museoList);
		movimientoArtistico.setLugarOrigen(paisList.get(1));
		movimientoArtistico.setObras(obraList);
		entityManager.persist(movimientoArtistico);

		for(int i = 1; i<=3; i++)
		{
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artistaEntity);
			artistaEntity.getMovimientos().add(movimientoArtistico);
			artistaList.add(artistaEntity);
			movimientoArtistico.getArtistas().add(artistaEntity);
		}
	}

	/**
	 * Prueba de asociar un artista a un movimiento artistico
	 */
	@Test
	void testAddArtista() throws EntityNotFoundException, IllegalOperationException
	{
		MovimientoArtisticoEntity newMovimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		newMovimiento.setObras(obraList);
		newMovimiento.setMuseos(museoList);
		newMovimiento.setLugarOrigen(paisList.get(2));
		newMovimiento.setFechaApogeo(fechaList.get(0));
		newMovimiento.setArtistas(artistaList);
		entityManager.persist(newMovimiento);

		ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
		entityManager.persist(artistaEntity);

		movimientoArtisticoArtistaService.addArtista(newMovimiento.getId(), artistaEntity.getId());

		ArtistaEntity lastArtista = movimientoArtisticoArtistaService.getArtista(newMovimiento.getId(), artistaEntity.getId());
		assertEquals(artistaEntity.getId(), lastArtista.getId());
		assertEquals(artistaEntity.getNombre(), lastArtista.getNombre());
	}

	/** 
	 * Prueba de asociar un artista inexistente a un movimiento artistico
	 */
	@Test
	void testAddInvalidArtista()
	{
		assertThrows(EntityNotFoundException.class, () -> {
			movimientoArtisticoArtistaService.addArtista(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba de asociar un artista a un movimiento artistico inexistente
	 */
	@Test
	void testAddArtistaInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.addArtista(0L, artistaList.get(0).getId());
		});
	}

	/**
	 * Prueba para obtener la coleccion de artistas de un movimiento artistico
	 */
	@Test
	void testGetArtistas() throws EntityNotFoundException
	{
		List<ArtistaEntity> artistas = movimientoArtisticoArtistaService.getArtistas(movimientoArtistico.getId());

		assertEquals(artistaList.size(), artistas.size());

		for(int i=0; i<artistaList.size(); i++)
		{
			assertTrue(artistas.contains(artistaList.get(i)));
		}
	}
	
	/**
	 * Prueba para obtener la coleccion de artistas de un movimiento artistico inexistente
	 */
	@Test
	void testGetArtistasInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.getArtistas(0L);
		});
	}


	/**
	 * Prueba para obtener un artista asiciado a un movimiento artistico
	 */
	@Test
	void testGetArtista() throws EntityNotFoundException, IllegalOperationException
	{
		ArtistaEntity artistaEntity = artistaList.get(0);
		ArtistaEntity artista = movimientoArtisticoArtistaService.getArtista(movimientoArtistico.getId(), artistaEntity.getId());
		assertNotNull(artista);

		assertEquals(artista.getId(), artistaEntity.getId());
		assertEquals(artista.getNombre(), artistaEntity.getNombre());
	}
	
	/**
	 * Prueba para obtener un artista inexistente asociado a un movimiento artistico
	 */
	@Test
	void testGetInvalidArtista()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.getArtista(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para obtener un artista asociado a un movimiento artistico inexistente
	 */
	@Test
	void testGetArtistaInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.getArtista(0L, artistaList.get(0).getId());
		});
	}
	
	/**
	 * Prueba para obtener un artista que no esta asociado a un movimiento artistico
	 */
	@Test
	void testGetNotAssociatedArtista()
	{
		assertThrows(IllegalOperationException.class, ()->{
			ArtistaEntity nuevoArtiata = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(nuevoArtiata);
			movimientoArtisticoArtistaService.getArtista(movimientoArtistico.getId(), nuevoArtiata.getId());
		});
	}

	/**
	 * Prueba para desasociar una instancia de artista asociada a una instancia de movimiento artistico
	 */
	@Test
	void testRemoveArtista() throws EntityNotFoundException
	{
		for(ArtistaEntity artista: artistaList)
		{
			movimientoArtisticoArtistaService.removeArtista(movimientoArtistico.getId(), artista.getId());
		}
		assertTrue(movimientoArtisticoArtistaService.getArtistas(movimientoArtistico.getId()).isEmpty());
	}
	
	/**
	 * Prueba para desasociar una instancia inexistente de artista a un movimiento artistico
	 */
	@Test
	void testRemoveInvalidArtista()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.removeArtista(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para desasociar una instancia de artista a un movimiento artistico inexistente
	 */
	@Test
	void testRemoveArtistaInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoArtistaService.removeArtista(0L, artistaList.get(0).getId());
		});
	}
	
	/**
	 * Prueba para actualizar los artistas de un movimiento artistico
	 */
	@Test
	void testAddArtistas() throws EntityNotFoundException
	{
		List<ArtistaEntity> nuevosArtistas = new ArrayList<>();
		for(int i = 1; i<4;i++)
		{
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artistaEntity);
			nuevosArtistas.add(artistaEntity);
		}
		
		movimientoArtisticoArtistaService.addArtistas(movimientoArtistico.getId(), nuevosArtistas);
		
		for(ArtistaEntity artista: nuevosArtistas)
		{
			ArtistaEntity artis = entityManager.find(ArtistaEntity.class, artista.getId());
			assertTrue(artis.getMovimientos().contains(movimientoArtistico));
		}
	}
	
	/**
	 * Pruba para actualizar los artistas de un movimiento artistico inexistente
	 */
	@Test 
	void testAddArtistasInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			List<ArtistaEntity> nuevos = new ArrayList<>();
			movimientoArtisticoArtistaService.addArtistas(0L, nuevos);
		});
	}
	

}
