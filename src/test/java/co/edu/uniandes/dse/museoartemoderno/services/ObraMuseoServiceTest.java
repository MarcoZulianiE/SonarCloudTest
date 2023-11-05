package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.uniandes.dse.museoartemoderno.services.ObraMuseoService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoService;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;



@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ObraMuseoService.class, MuseoService.class })

public class ObraMuseoServiceTest {
	
	@Autowired
	private ObraMuseoService obraMuseoService;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
	
	private ArtistaEntity artistaEntity = new ArtistaEntity();
	private MovimientoArtisticoEntity movimientoEntity = new MovimientoArtisticoEntity();

	private List<ObraEntity> obraList = new ArrayList<>();
	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<MovimientoArtisticoEntity> movimientoList = new ArrayList<>();
	private List<MuseoEntity> museoList = new ArrayList<>();
	
	 /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
            clearData();
            insertData();
    }
	
	public void clearData()
	{
		entityManager.getEntityManager().createQuery("delete from MuseoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ObraEntity").executeUpdate();
	}
	
	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		
		//Creación de un artista y movimiento para añadirlos a las obras de prueba y tener los datos completos.
		
		artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
		entityManager.persist(artistaEntity);

		movimientoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		entityManager.persist(movimientoEntity);
		
		//Creación de varios artistas para añadirlos al museo y tener los datos completos.
		
		for (int i = 0; i < 5; i++) {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(entity);
			artistaList.add(entity);
		}             
		
		//Creación de varios movimientos para añadirlos al museo y tener los datos completos.

		for (int i = 0; i < 5; i++) {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			movimientoList.add(entity);
		}
		
		//Creación de Obras para llevar a cabo las pruebas.
				
		for (int i = 0; i < 5; i++) {
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(entity);
			obraList.add(entity);
		}
		
		//Creación de Museos para llevar a cabo las pruebas.
	
		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entity.getArtistas().add(artistaEntity);
			entity.getMovimientos().add(movimientoEntity);
			entityManager.persist(entity);
			museoList.add(entity);
		}
		
		//Se completan los datos no relevantes para las pruebas del Museo.
		
		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = museoList.get(i);
			
			for (int j = 0; j < 5; j++) {
				ArtistaEntity entityArtista = artistaList.get(j);
				MovimientoArtisticoEntity entityMovimiento = movimientoList.get(j);
							
				entity.getArtistas().add(entityArtista);
				entity.getMovimientos().add(entityMovimiento);
			}

		}
		
		//Se añaden distintos datos a las Obras de prueba para realizar los test.
			
		
		obraList.get(1).setMovimiento(movimientoEntity);
		obraList.get(1).setArtista(artistaEntity);
		
		 
		obraList.get(0).setMovimiento(movimientoEntity);
		obraList.get(0).setArtista(artistaEntity);
		                      
		museoList.get(0).getObras().add(obraList.get(2));		
		obraList.get(2).setMuseo(museoList.get(0));
		
		museoList.get(1).getObras().add(obraList.get(3));		
		obraList.get(3).setMuseo(museoList.get(1));
		
		museoList.get(2).getObras().add(obraList.get(4));		
		obraList.get(4).setMuseo(museoList.get(2));
		
		
	}
	

	/**
	 * Prueba para asociar un Museo a una Obra.
	 *
	 */
	@Test
	void testAddMuseo() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
		newMuseo.getArtistas().add(artistaEntity);
		newMuseo.getMovimientos().add(movimientoEntity);
		
	
		entityManager.persist(newMuseo);
		ObraEntity obraEntity = obraList.get(0);
				
		obraMuseoService.addMuseo(obraEntity.getId(), newMuseo.getId());

		MuseoEntity museoEntity = obraMuseoService.getMuseo(obraEntity.getId());

		assertNotNull(museoEntity);

		assertEquals(museoEntity.getId(), newMuseo.getId());
		assertEquals(museoEntity.getNombre(), newMuseo.getNombre());
		assertEquals(museoEntity.getDireccion(), newMuseo.getDireccion());
		assertEquals(museoEntity.getTotalObrasExhibidas(), newMuseo.getTotalObrasExhibidas());

	}
	

	/**
	 * Prueba para asociar un Museo a una Obra que no existe.
	 *
	 */

	@Test
	void testAddMuseoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			newMuseo.getArtistas().add(artistaEntity);
			newMuseo.getMovimientos().add(movimientoEntity);
			entityManager.persist(newMuseo);
			obraMuseoService.addMuseo(0L, newMuseo.getId());
		});
	}

	/**
	 * Prueba para asociar un Museo que no existe a una Obra.
	 *
	 */
	@Test
	void testAddInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, () -> {
			ObraEntity obra = obraList.get(1);
			obraMuseoService.addMuseo(obra.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar el Museo de una Obra.
	 */
	@Test
	void testGetMuseo() throws EntityNotFoundException {
		ObraEntity obra = obraList.get(2);	
		MuseoEntity museoEntity = obraMuseoService.getMuseo(obra.getId());		
		assertNotNull(museoEntity);
	}
	

	/**
	 * Prueba para consultar el Museo de una Obra que no existe.
	 */
	@Test
	void testGetMuseoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			obraMuseoService.getMuseo(0L);
		});
	}

	/**
	 * Prueba para consultar un Museo que no existe de una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, () -> {
			
			ObraEntity obra = obraList.get(1);
			obraMuseoService.addMuseo(obra.getId(),0L);
			obraMuseoService.getMuseo(obra.getId());
		});
	}

	/**
	 * Prueba para consultar un Museo que no está asociado a una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMuseoNotAssociatedObra() {
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);
		
		MuseoEntity museoEntity2 = factory.manufacturePojo(MuseoEntity.class);
		obraEntity.setMuseo(museoEntity2);

		MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
		museoEntity.getArtistas().add(artistaEntity);
		museoEntity.getMovimientos().add(movimientoEntity);
		entityManager.persist(museoEntity);
		
		assertTrue(obraEntity.getMuseo().getId() != museoEntity.getId());
		
	}

	/**
	 * Prueba para actualizar el Museo de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceMuseo() throws EntityNotFoundException, IllegalOperationException {
		
		MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
		entity.getArtistas().add(artistaEntity);
		entity.getMovimientos().add(movimientoEntity);
		entityManager.persist(entity);
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);
			
		obraMuseoService.addMuseo(obraEntity.getId(), entity.getId());
		MuseoEntity museoEntity = obraMuseoService.getMuseo(obraEntity.getId());
		assertEquals(museoEntity.getId(),entity.getId());
		
	}
	

	/**
	 * Prueba para actualizar el Museo de una Obra que no existe.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceMuseoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entity.getArtistas().add(artistaEntity);
			entity.getMovimientos().add(movimientoEntity);
			entityManager.persist(entity);
			
			
			obraMuseoService.addMuseo(0L, entity.getId());
		});
	}

	/**
	 * Prueba para actualizar un Museo que no existe de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, () -> {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entity.getArtistas().add(artistaEntity);
			entity.getMovimientos().add(movimientoEntity);
			entity.setId(0L);
			
			ObraEntity obra = obraList.get(0);
			obraMuseoService.addMuseo(obra.getId(), entity.getId());
		});
	}

	/**
	 * Prueba desasociar un Museo con una Obra.
	 *
	 */
	@Test
	void testRemoveMuseo() throws EntityNotFoundException {
	
		for (ObraEntity obra : obraList) {
			obraMuseoService.removeMuseo(obra.getId());
			assertEquals(obraMuseoService.getMuseo(obra.getId()),null);
		}	
	}

	/**
	 * Prueba desasociar un Museo con una Obra que no existe.
	 *
	 */
	@Test
	void testRemoveMuseoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
				obraMuseoService.removeMuseo(0L);	
		});
	}


}