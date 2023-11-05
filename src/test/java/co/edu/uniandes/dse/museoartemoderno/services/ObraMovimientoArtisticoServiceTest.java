package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import co.edu.uniandes.dse.museoartemoderno.services.ObraMovimientoArtisticoService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoService;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;



@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ObraMovimientoArtisticoService.class, MovimientoArtisticoService.class })

public class ObraMovimientoArtisticoServiceTest {
	
	@Autowired
	private ObraMovimientoArtisticoService obraMovimientoArtisticoService;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
	

	private List<MovimientoArtisticoEntity> MovimientoArtisticoList = new ArrayList<>();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<MuseoEntity> museoList = new ArrayList<>();
	private List<Date> fechaList = new ArrayList<>();

	
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
		entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ObraEntity").executeUpdate();
	}
	
	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		
	
		//Creación de varios Museos para añadirlos al MovimientoArtistico y tener los datos completos.
		
		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(entity);
			museoList.add(entity);
		}             
		
		//Creación de varios Artistas para añadirlos al MovimientoArtistico y tener los datos completos.

		for (int i = 0; i < 3; i++) {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(entity);
			artistaList.add(entity);
		}
		
		//Creación de Obras para llevar a cabo las pruebas.
				
		for (int i = 0; i < 3; i++) {
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(entity);
			obraList.add(entity);
		}
		
		//Creación de MovimientoArtisticos para llevar a cabo las pruebas.
	
		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			MovimientoArtisticoList.add(entity);
		}
		
		//Se completan los datos no relevantes para las pruebas del MovimientoArtistico.
		
		
		//Creación del lugar de origen.
		
		PaisEntity lugarOrigen = factory.manufacturePojo(PaisEntity.class);		
		
		// Creación de la fecha de apogeo.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 3; i++) {
			
			MovimientoArtisticoEntity entity = MovimientoArtisticoList.get(i);
			Date fechaApogeo = fechaList.get(0);
			entity.setFechaApogeo(fechaApogeo);
			entity.setLugarOrigen(lugarOrigen);
			
			for (int j = 0; j < 3; j++) {
				MuseoEntity museoEntity = museoList.get(j);
				ArtistaEntity entityArtista = artistaList.get(j);
				ObraEntity obraEntity = obraList.get(j); 
							
				entity.getMuseos().add(museoEntity);
				entity.getArtistas().add(entityArtista);
				entity.getObras().add(obraEntity);
			}

		}
		
		//Se añaden distintos datos a las Obras de prueba para realizar los test.
			
		//El MovimientoArtistico número 0 de la lista no tiene ninguna relación con Obra.
		
		//Relación bidireccional de MovimientoArtistico a Obra
		MovimientoArtisticoList.get(1).setObras(obraList);
		
		for (ObraEntity obraEntity : obraList) {
			obraEntity.setMovimiento(MovimientoArtisticoList.get(1));
		}
		
		//Relación unidireccional de MovimientoArtistico con Obra.
		MovimientoArtisticoList.get(2).setObras(obraList);
		
			
	}
	

	/**
	 * Prueba para asociar un MovimientoArtistico a una Obra.
	 *
	 */
	@Test
	void testAddMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
		
		MovimientoArtisticoEntity newMovimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);	
		entityManager.persist(newMovimientoArtistico);
		
		ObraEntity obraEntity = obraList.get(0);
		obraMovimientoArtisticoService.removeMovimientoArtistico(obraEntity.getId());
		

		obraMovimientoArtisticoService.addMovimientoArtistico(obraEntity.getId(), newMovimientoArtistico.getId());

		MovimientoArtisticoEntity MovimientoArtisticoEntity = obraMovimientoArtisticoService.getMovimientoArtistico(obraEntity.getId());

		assertNotNull(MovimientoArtisticoEntity);

		assertEquals(MovimientoArtisticoEntity.getId(), newMovimientoArtistico.getId());
		assertEquals(MovimientoArtisticoEntity.getNombre(), newMovimientoArtistico.getNombre());
		assertEquals(MovimientoArtisticoEntity.getLugarOrigen(), newMovimientoArtistico.getLugarOrigen());
		assertEquals(MovimientoArtisticoEntity.getFechaApogeo(), newMovimientoArtistico.getFechaApogeo());

	}
	

	/**
	 * Prueba para asociar un MovimientoArtistico a una Obra que no existe.
	 *
	 */

	@Test
	void testAddMovimientoArtisticoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			MovimientoArtisticoEntity newMovimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(newMovimientoArtistico);
			obraMovimientoArtisticoService.addMovimientoArtistico(0L, newMovimientoArtistico.getId());
		});
	}

	/**
	 * Prueba para asociar un MovimientoArtistico que no existe a una Obra.
	 *
	 */
	@Test
	void testAddInvalidMovimientoArtistico() {
		assertThrows(EntityNotFoundException.class, () -> {
			ObraEntity obra = obraList.get(0);
			obraMovimientoArtisticoService.addMovimientoArtistico(obra.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar el MovimientoArtistico de una Obra.
	 */
	@Test
	void testGetMuseo() throws EntityNotFoundException {
		ObraEntity obra = obraList.get(2);	
		MovimientoArtisticoEntity MovimientoArtisticoEntity = obraMovimientoArtisticoService.getMovimientoArtistico(obra.getId());		
		assertNotNull(MovimientoArtisticoEntity);
	}
	

	/**
	 * Prueba para consultar el MovimientoArtistico de una Obra que no existe.
	 */
	@Test
	void testGetMovimientoArtisticoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			obraMovimientoArtisticoService.getMovimientoArtistico(0L);
		});
	}

	/**
	 * Prueba para consultar un MovimientoArtistico que no existe de una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidMovimientoArtistico() {
		assertThrows(EntityNotFoundException.class, () -> {
			
			ObraEntity obra = obraList.get(1);
			obraMovimientoArtisticoService.addMovimientoArtistico(obra.getId(), 0L);
			obraMovimientoArtisticoService.getMovimientoArtistico(obra.getId());
		});
	}

	/**
	 * Prueba para consultar un MovimientoArtistico que no está asociado a una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMovimientoArtisticoNotAssociatedObra() {
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);

		MovimientoArtisticoEntity MovimientoArtisticoEntity2 = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		obraEntity.setMovimiento(MovimientoArtisticoEntity2);
		
		MovimientoArtisticoEntity MovimientoArtisticoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		entityManager.persist(MovimientoArtisticoEntity);
		
		
		System.out.println(obraEntity.getId());
		System.out.println("HOLI");

		System.out.println(MovimientoArtisticoEntity.getId());

		assertTrue(obraEntity.getMovimiento().getId() != MovimientoArtisticoEntity.getId());
		
	}

	/**
	 * Prueba para actualizar el MovimientoArtistico de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
		
		MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);

		entityManager.persist(entity);
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);
			
		obraMovimientoArtisticoService.addMovimientoArtistico(obraEntity.getId(), entity.getId());
		MovimientoArtisticoEntity MovimientoArtisticoEntity = obraMovimientoArtisticoService.getMovimientoArtistico(obraEntity.getId());
		assertEquals(MovimientoArtisticoEntity.getId(),entity.getId());
		
	}
	

	/**
	 * Prueba para actualizar el MovimientoArtistico de una Obra que no existe.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceMovimientoArtisticoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);

			entityManager.persist(entity);
			obraMovimientoArtisticoService.addMovimientoArtistico(0L, entity.getId());
		});
	}

	/**
	 * Prueba para actualizar un MovimientoArtistico que no existe de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceInvalidMovimientoArtistico() {
		assertThrows(EntityNotFoundException.class, () -> {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entity.setId(0L);
			
			ObraEntity obra = obraList.get(0);
			obraMovimientoArtisticoService.addMovimientoArtistico(obra.getId(), entity.getId());
		});
	}

	/**
	 * Prueba desasociar un MovimientoArtistico con una Obra.
	 *
	 */
	@Test
	void testRemoveMovimientoArtistico() throws EntityNotFoundException {
	
		for (ObraEntity obra : obraList) {
			obraMovimientoArtisticoService.removeMovimientoArtistico(obra.getId());
			assertEquals(obraMovimientoArtisticoService.getMovimientoArtistico(obra.getId()),null);
		}	
	}

	/**
	 * Prueba desasociar un MovimientoArtistico con una Obra que no existe.
	 *
	 */
	@Test
	void testRemoveMovimientoArtisticoInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
				obraMovimientoArtisticoService.removeMovimientoArtistico(0L);	
		});
	}


}