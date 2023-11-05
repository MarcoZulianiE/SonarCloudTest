package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
@Import(MovimientoArtisticoMuseoService.class)
class MovimientoArtisticoMuseoServiceTest
{
	@Autowired
	private MovimientoArtisticoMuseoService movimientoArtisticoMuseoService;

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
		for(int i = 1; i<= 3; i++)
		{
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(sdf.parse("2000-10-20"));

			fechaList.add(new Date());
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		for(int i = 1; i<=3; i++)
		{
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}
		for(int i = 1; i<=3; i++)
		{
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artistaEntity);
			artistaList.add(artistaEntity);
		}

		movimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		movimientoArtistico.setFechaApogeo(fechaList.get(0));
		movimientoArtistico.setLugarOrigen(paisList.get(1));
		movimientoArtistico.setObras(obraList);
		movimientoArtistico.setArtistas(artistaList);
		entityManager.persist(movimientoArtistico);

		for(int i = 1; i<=3; i++)
		{
			MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museoEntity);
			museoEntity.getMovimientos().add(movimientoArtistico);
			museoList.add(museoEntity);
			movimientoArtistico.getMuseos().add(museoEntity);
		}
	}

	/**
	 * Prueba de asociar un museo con un movimiento artistico
	 */
	@Test
	void testAddMuseo() throws EntityNotFoundException, IllegalOperationException
	{
		MovimientoArtisticoEntity newMovimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		newMovimiento.setObras(obraList);
		newMovimiento.setMuseos(museoList);
		newMovimiento.setLugarOrigen(paisList.get(2));
		newMovimiento.setFechaApogeo(fechaList.get(0));
		newMovimiento.setArtistas(artistaList);
		entityManager.persist(newMovimiento);

		MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
		entityManager.persist(museoEntity);

		movimientoArtisticoMuseoService.addMuseo(newMovimiento.getId(), museoEntity.getId());

		MuseoEntity lastMuseo = movimientoArtisticoMuseoService.getMuseo(museoEntity.getId(), newMovimiento.getId());
		assertEquals(museoEntity.getId(), lastMuseo.getId());
		assertEquals(museoEntity.getNombre(), lastMuseo.getNombre());
		assertEquals(museoEntity.getDireccion(), lastMuseo.getDireccion());
	}
	
	/** 
	 * Prueba de asociar un museo inexistente a un movimiento artistico
	 */
	@Test
	void testAddInvalidMuseo()
	{
		assertThrows(EntityNotFoundException.class, () -> {
			movimientoArtisticoMuseoService.addMuseo(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba de asociar un artista a un movimiento artistico inexistente
	 */
	@Test
	void testAddMuseoInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoMuseoService.addMuseo(0L, museoList.get(0).getId());
		});
	}

	/**
	 * Prueba de obtener la coleccion de instancias de Museo asociadas con una instancia de MovimientoArtistico
	 */
	@Test
	void testGetMuseos() throws EntityNotFoundException
	{
		List<MuseoEntity> museos = movimientoArtisticoMuseoService.getMuseos(movimientoArtistico.getId());

		assertEquals(museoList.size(), museos.size());

		for(int i = 0; i<museoList.size();i++)
		{
			assertTrue(museos.contains(museoList.get(i)));
		}
	}
	
	/**
	 * Prueba de obtener la coleccion de instancias de museo asociadas con un movimiento inexistente
	 */
	@Test
	void testGetMuseosInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class,()->{
			movimientoArtisticoMuseoService.getMuseos(0L);
		});
	}
	
	/**
	 * Prueba de obtener una instancia de Museo asociada a una instancia de MovimientoArtistico
	 */
	@Test
	void testGetMuseo() throws EntityNotFoundException, IllegalOperationException
	{
		MuseoEntity museoEntity = museoList.get(0);
		MuseoEntity museo = movimientoArtisticoMuseoService.getMuseo(museoEntity.getId(), movimientoArtistico.getId());
		assertNotNull(museo);

		assertEquals(museoEntity.getId(), museo.getId());
		assertEquals(museoEntity.getNombre(), museo.getNombre());
		assertEquals(museoEntity.getDireccion(), museo.getDireccion());
	}
	
	/**
	 * Prueba de obtener un museo inexistente asociado con un movimiento
	 */
	@Test
	void testGetInvalidMuseo()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoMuseoService.getMuseo(0L, movimientoArtistico.getId());
		});
	}
	
	/**
	 * Prueba de obtener un museo asociado con un movimiento inexistente
	 */
	@Test
	void testGetMuseoInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoMuseoService.getMuseo(museoList.get(0).getId(), 0L);
		});
	}

	/**
	 * Prueba de obtener un museo no asociado con un movimiento 
	 */
	@Test
	void testGetMuseoNotAssociatedMuseo()
	{
		assertThrows(IllegalOperationException.class, ()->{
			MuseoEntity museo = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museo);
			movimientoArtisticoMuseoService.getMuseo(museo.getId(), movimientoArtistico.getId());
		});
	}
	
	/**
	 * Prueba de desasociar un Museo de un MovimientoArtistico
	 */
	@Test
	void testRemoveMuseo() throws EntityNotFoundException
	{
		for(MuseoEntity museo: museoList)
		{
			movimientoArtisticoMuseoService.removeMuseo(movimientoArtistico.getId(), museo.getId());
		}
		assertTrue(movimientoArtisticoMuseoService.getMuseos(movimientoArtistico.getId()).isEmpty());
	}
	
	/**
	 * Prueba de desasociar un museo inexistente de un movimiento artistico
	 */
	@Test
	void testRemoveInvalidMuseo()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoMuseoService.removeMuseo(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba de desasociar un museo de un movimiento artistico inexistente
	 */
	@Test
	void testRemoveMuseoInvalidMuseo()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoMuseoService.removeMuseo(0L, museoList.get(0).getId());
		});
	}
	
	
}