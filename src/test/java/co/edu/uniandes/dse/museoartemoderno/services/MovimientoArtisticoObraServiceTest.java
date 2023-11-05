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
@Import(MovimientoArtisticoObraService.class)
class MovimientoArtisticoObraServiceTest 
{

	@Autowired
	private MovimientoArtisticoObraService movimientoArtisticoObraService;

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
		movimientoArtistico.setArtistas(artistaList);
		entityManager.persist(movimientoArtistico);

		for(int i = 1; i<= 3; i++)
		{
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraEntity.setMovimiento(movimientoArtistico);
			obraList.add(obraEntity);
			movimientoArtistico.getObras().add(obraEntity);
		}
	}

	/**
	 * Prueba para asociar una entidad de Obra con una instancia de MovimientoArtistico
	 */
	@Test
	void testAddObra() throws EntityNotFoundException, IllegalOperationException
	{
		MovimientoArtisticoEntity movimiento = movimientoArtistico;
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);
		ObraEntity respuesta = movimientoArtisticoObraService.addObra(movimiento.getId(), obraEntity.getId());

		assertNotNull(respuesta);
		assertEquals(obraEntity.getId(), respuesta.getId());
	}
	
	/**
	 * Prueba para asociar una obra inexistente con un movimiento
	 */
	@Test 
	void testAddInvalidObra()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoObraService.addObra(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar una obra con un movimiento inexistente
	 */
	@Test
	void testAddObraInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoObraService.addObra(0L, obraList.get(0).getId());
		});
	}

	/**
	 * Prueba para obtener obtener las obras asociadas a un movimiento artistico
	 */
	@Test
	void testGetObras() throws EntityNotFoundException
	{
		List<ObraEntity> obras = movimientoArtisticoObraService.getObras(movimientoArtistico.getId());
		assertEquals(obras.size(), obraList.size());

		for(int i = 0; i<obraList.size();i++)
		{
			assertTrue(obras.contains(obraList.get(i)));
		}
	}
	
	/**
	 * Prueba para obtener las obras asociadas a un movimiento artistico inexistente
	 */
	@Test
	void testGetObrasInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoObraService.getObras(0L);
		});
	}

	/**
	 * Prueba para obtener una obra asociada a un movimiento artistico
	 */
	@Test
	void testGetObra() throws EntityNotFoundException, IllegalOperationException
	{
		ObraEntity obraEntity = obraList.get(0);
		ObraEntity obra = movimientoArtisticoObraService.getObra(movimientoArtistico.getId(), obraEntity.getId());
		assertNotNull(obra);

		assertEquals(obra.getId(), obraEntity.getId());
		assertEquals(obra.getNombre(), obraEntity.getNombre());
	}
	
	/**
	 * Prueba para obtener una obra asociada a un movimiento artistico invalido
	 */
	@Test
	void testGetObraInvalidMovimiento()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoObraService.getObra(0L, obraList.get(0).getId());
		});
	}
	
	/**
	 * Prueba para obtener una obra inexistente asociado a un movimiento artistico
	 */
	@Test
	void testGetInvalidObra()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoObraService.getObra(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para obtener una obra no asociada a un movimiento artistico
	 */
	@Test
	void testGetNotAssociatedObra()
	{
		assertThrows(IllegalOperationException.class, ()->{
			ObraEntity obra = factory.manufacturePojo(ObraEntity.class);
			MovimientoArtisticoEntity movimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimiento);
			entityManager.persist(obra);
			obra.setMovimiento(movimiento);
			movimientoArtisticoObraService.getObra(movimientoArtistico.getId(), obra.getId());
		});
	}

	/**
	 * Prueba para actualizar las obras de una instancia de MovimientoArtistico
	 */
	@Test
	void testReplaceObras() throws EntityNotFoundException
	{
		List<ObraEntity> newObras = new ArrayList<>();
		for(int i = 1; i<=3; i++)
		{
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			newObras.add(obraEntity);
		}

		movimientoArtisticoObraService.replaceObras(movimientoArtistico.getId(), newObras);

		for(ObraEntity obra: newObras)
		{
			ObraEntity o = entityManager.find(ObraEntity.class, obra.getId());
			assertTrue(o.getMovimiento().equals(movimientoArtistico));
		}
	}
	
	/**
	 * Prueva para actualizar las obras de un movimiento artistico enexistente
	 */
	@Test
	void testReplaceObrasMovimientoInvalido()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> obras = new ArrayList<>();
			movimientoArtisticoObraService.replaceObras(0L, obras);
		});
	}
	
	/**
	 * Prueva para actualizar las obras de un movimiento artistico con obras inexistentes
	 */
	@Test
	void testReplaceObraInvalidas()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> obras = new ArrayList<>();
			ObraEntity obra1 = factory.manufacturePojo(ObraEntity.class);
			obra1.setId(0L);
			obras.add(obra1);
			movimientoArtisticoObraService.replaceObras(movimientoArtistico.getId(), obras);
		});
	}
	

}
