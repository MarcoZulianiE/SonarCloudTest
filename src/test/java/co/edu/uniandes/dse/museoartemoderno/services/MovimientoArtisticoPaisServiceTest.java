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
@Import({MovimientoArtisticoPaisService.class, MovimientoArtisticoService.class})
class MovimientoArtisticoPaisServiceTest
{
	@Autowired
	private MovimientoArtisticoPaisService movimientoArtisticoPaisService;

	@Autowired
	private MovimientoArtisticoService movimientoArtisticoService;

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
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artistaEntity);
			artistaList.add(artistaEntity);
		}
		for(int i = 1; i<= 3; i++)
		{
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);
		}
		movimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		movimientoArtistico.setFechaApogeo(fechaList.get(0));
		movimientoArtistico.setObras(obraList);
		movimientoArtistico.setArtistas(artistaList);
		movimientoArtistico.setMuseos(museoList);
		entityManager.persist(movimientoArtistico);

		for(int i = 1; i<=3; i++)
		{
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}
		movimientoArtistico.setLugarOrigen(paisList.get(0));
	}

	/**
	 * Prueba para reemplazar el pais de un movimiento artistico
	 */
	@Test
	void testReplacePais() throws EntityNotFoundException
	{
		PaisEntity pais = paisList.get(1);
		movimientoArtisticoPaisService.replacePais(movimientoArtistico.getId(), pais.getId());
		MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoService.getMovimientoArtistico(movimientoArtistico.getId());
		assertEquals(movimientoEntity.getLugarOrigen().getId(), pais.getId());
	}
	
	/**
	 * Prueba para reemplazar el pais de un movimiento artistico inexistente
	 */
	@Test 
	void testReplacePaisMovimientoInvalido()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoPaisService.replacePais(0L, paisList.get(0).getId());
		});
	}
	
	/**
	 * Prueba para reemplazar un pais inexistente de un movimiento artistico
	 */
	@Test
	void testReplaceInvalidPais()
	{
		assertThrows(EntityNotFoundException.class, ()->{
			movimientoArtisticoPaisService.replacePais(movimientoArtistico.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para desasociar un movimiento artistico de un pais
	 */
	@Test
	void testRemoveMovimientoArtistico() throws EntityNotFoundException
	{
		movimientoArtisticoPaisService.removeMovimientoArtistico(movimientoArtistico.getId());
		MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoService.getMovimientoArtistico(movimientoArtistico.getId());
		assertNull(movimientoEntity.getLugarOrigen());
	}
	
	/**
	 * Prueba para intentar desasociar un movimiento artistico inexistente
	 */
	@Test
	void testRemoveMovimientoArtisticoInvalido()
	{
		 assertThrows(EntityNotFoundException.class, ()->{
            movimientoArtisticoPaisService.removeMovimientoArtistico(0L);
     });
	}
}