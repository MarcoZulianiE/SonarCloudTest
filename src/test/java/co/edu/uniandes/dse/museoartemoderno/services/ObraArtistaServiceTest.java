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

import co.edu.uniandes.dse.museoartemoderno.services.ObraArtistaService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.museoartemoderno.services.MuseoService;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;



@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ObraArtistaService.class, ArtistaService.class })

public class ObraArtistaServiceTest {
	
	@Autowired
	private ObraArtistaService obraArtistaService;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
	

	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<MovimientoArtisticoEntity> movimientoList = new ArrayList<>();
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
		entityManager.getEntityManager().createQuery("delete from ArtistaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ObraEntity").executeUpdate();
	}
	
	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		
	
		//Creación de varios Museos para añadirlos al Artista y tener los datos completos.
		
		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(entity);
			museoList.add(entity);
		}             
		
		//Creación de varios Movimientos para añadirlos al Artista y tener los datos completos.

		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			movimientoList.add(entity);
		}
		
		//Creación de Obras para llevar a cabo las pruebas.
				
		for (int i = 0; i < 3; i++) {
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(entity);
			obraList.add(entity);
		}
		
		//Creación de Artistas para llevar a cabo las pruebas.
	
		for (int i = 0; i < 3; i++) {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(entity);
			artistaList.add(entity);
		}
		
		//Se completan los datos no relevantes para las pruebas del Artista.
		
		
		//Creación del lugar de nacimiento y fallecimiento.
		
		PaisEntity lugarNacimiento = factory.manufacturePojo(PaisEntity.class);
		PaisEntity lugarFallecimiento = factory.manufacturePojo(PaisEntity.class);
		
		
		// Creación de las fechas de nacimiento y fallecimiento.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(sdf.parse("2000-05-20"));
			fechaList.add(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 3; i++) {
			
			ArtistaEntity entity = artistaList.get(i);
			Date fechaNacimiento = fechaList.get(0);
			Date fechaFallecimiento = fechaList.get(1);
			entity.setFechaNacimiento(fechaNacimiento);
			entity.setFechaFallecimiento(fechaFallecimiento);
			entity.setLugarFallecimiento(lugarFallecimiento);
			entity.setLugarNacimiento(lugarNacimiento);
			
			for (int j = 0; j < 3; j++) {
				MuseoEntity museoEntity = museoList.get(j);
				MovimientoArtisticoEntity entityMovimiento = movimientoList.get(j);
				ObraEntity obraEntity = obraList.get(j); 
							
				entity.getMuseos().add(museoEntity);
				entity.getMovimientos().add(entityMovimiento);
				entity.getObras().add(obraEntity);
			}

		}
		
		//Se añaden distintos datos a las Obras de prueba para realizar los test.
			
		//El artista número 0 de la lista no tiene ninguna relación con Obra.
		
		//Relación bidireccional de Artista a Obra
		artistaList.get(1).setObras(obraList);
		
		for (ObraEntity obraEntity : obraList) {
			obraEntity.setArtista(artistaList.get(1));
		}
		
		//Relación unidireccional de Artista con Obra.
		artistaList.get(2).setObras(obraList);
		
			
	}
	

	/**
	 * Prueba para asociar un Artista a una Obra.
	 *
	 */
	@Test
	void testAddArtista() throws EntityNotFoundException, IllegalOperationException {
		
		ArtistaEntity newArtista = factory.manufacturePojo(ArtistaEntity.class);	
		entityManager.persist(newArtista);
		
		ObraEntity obraEntity = obraList.get(0);
		obraArtistaService.removeArtista(obraEntity.getId());
		

		obraArtistaService.addArtista(obraEntity.getId(), newArtista.getId());

		ArtistaEntity artistaEntity = obraArtistaService.getArtista(obraEntity.getId());

		assertNotNull(artistaEntity);

		assertEquals(artistaEntity.getId(), newArtista.getId());
		assertEquals(artistaEntity.getNombre(), newArtista.getNombre());
		assertEquals(artistaEntity.getLugarNacimiento(), newArtista.getLugarNacimiento());
		assertEquals(artistaEntity.getLugarFallecimiento(), newArtista.getLugarFallecimiento());
		assertEquals(artistaEntity.getFechaNacimiento(), newArtista.getFechaNacimiento());
		assertEquals(artistaEntity.getFechaFallecimiento(), newArtista.getFechaFallecimiento());

	}
	

	/**
	 * Prueba para asociar un Artista a una Obra que no existe.
	 *
	 */

	@Test
	void testAddArtistaInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			ArtistaEntity newArtista = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(newArtista);
			obraArtistaService.addArtista(0L, newArtista.getId());
		});
	}

	/**
	 * Prueba para asociar un Artista que no existe a una Obra.
	 *
	 */
	@Test
	void testAddInvalidArtista() {
		assertThrows(EntityNotFoundException.class, () -> {
			ObraEntity obra = obraList.get(0);
			obraArtistaService.addArtista(obra.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar el Artista de una Obra.
	 */
	@Test
	void testGetArtista() throws EntityNotFoundException {
		ObraEntity obra = obraList.get(2);	
		ArtistaEntity artistaEntity = obraArtistaService.getArtista(obra.getId());		
		assertNotNull(artistaEntity);
	}
	

	/**
	 * Prueba para consultar el Artista de una Obra que no existe.
	 */
	@Test
	void testGetArtistaInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			obraArtistaService.getArtista(0L);
		});
	}

	/**
	 * Prueba para consultar un Artista que no existe de una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidArtista() {
		assertThrows(EntityNotFoundException.class, () -> {
			
			ObraEntity obra = obraList.get(1);
			obraArtistaService.addArtista(obra.getId(), 0L);
			obraArtistaService.getArtista(obra.getId());
		});
	}

	/**
	 * Prueba para consultar un Artista que no está asociado a una Obra.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetArtistaNotAssociatedObra() {
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);

		ArtistaEntity artistaEntity2 = factory.manufacturePojo(ArtistaEntity.class);
		obraEntity.setArtista(artistaEntity2);
		
		ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
		entityManager.persist(artistaEntity);
		
		
		System.out.println(obraEntity.getId());
		System.out.println("HOLI");

		System.out.println(artistaEntity.getId());

		assertTrue(obraEntity.getArtista().getId() != artistaEntity.getId());
		
	}

	/**
	 * Prueba para actualizar el Artista de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceArtista() throws EntityNotFoundException, IllegalOperationException {
		
		ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);

		entityManager.persist(entity);
		
		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);
			
		obraArtistaService.addArtista(obraEntity.getId(), entity.getId());
		ArtistaEntity artistaEntity = obraArtistaService.getArtista(obraEntity.getId());
		assertEquals(artistaEntity.getId(),entity.getId());
		
	}
	

	/**
	 * Prueba para actualizar el Artista de una Obra que no existe.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceArtistaInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);

			entityManager.persist(entity);
			obraArtistaService.addArtista(0L, entity.getId());
		});
	}

	/**
	 * Prueba para actualizar un Artista que no existe de una Obra.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceInvalidArtista() {
		assertThrows(EntityNotFoundException.class, () -> {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entity.setId(0L);
			
			ObraEntity obra = obraList.get(0);
			obraArtistaService.addArtista(obra.getId(), entity.getId());
		});
	}

	/**
	 * Prueba desasociar un Artista con una Obra.
	 *
	 */
	@Test
	void testRemoveArtista() throws EntityNotFoundException {
	
		for (ObraEntity obra : obraList) {
			obraArtistaService.removeArtista(obra.getId());
			assertEquals(obraArtistaService.getArtista(obra.getId()),null);
		}	
	}

	/**
	 * Prueba desasociar un Artista con una Obra que no existe.
	 *
	 */
	@Test
	void testRemoveArtistaInvalidObra() {
		assertThrows(EntityNotFoundException.class, () -> {
				obraArtistaService.removeArtista(0L);	
		});
	}


}