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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
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
@Import(MuseoMovimientoArtisticoService.class)
public class MuseoMovimientoArtisticoServiceTest {

	@Autowired
	private MuseoMovimientoArtisticoService museoMovimientoArtisticoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();
	
	private MuseoEntity museo = new MuseoEntity();
	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
	private List<MovimientoArtisticoEntity> movimientoArtisticoList = new ArrayList<>();

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

		for (int i = 0; i < 3; i++) {
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}

		for (int i = 0; i < 3; i++) {
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artistaEntity);
			artistaList.add(artistaEntity);
		}

		for (int i = 0; i < 3; i++) {
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			
			obraList.add(obraEntity);
			museo.getObras().add(obraEntity);
		}
		
		museo = factory.manufacturePojo(MuseoEntity.class);
		museo.setArtistas(artistaList);
		museo.setObras(obraList);
		museo.setMovimientos(movimientoArtisticoList);
		museo.setUbicacion(paisList.get(0));
		entityManager.persist(museo);

		for (int i = 0; i < 3; i++) {		
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			entity.getMuseos().add(museo);
			movimientoArtisticoList.add(entity);
			museo.getMovimientos().add(entity);	
		}
	}
	
	
	/**
	 * Prueba para asociar un MovimientoArtistico a un Museo.
	 *
	 */
	@Test
	void testAddMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
		//Datos que deben ser diferentes a null para crear un MovimientoArtistico
		newMuseo.setArtistas(artistaList);
		newMuseo.setObras(obraList);
		newMuseo.setMovimientos(movimientoArtisticoList);
		newMuseo.setUbicacion(paisList.get(0));
		entityManager.persist(newMuseo);

		MovimientoArtisticoEntity MovimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		entityManager.persist(MovimientoArtistico);

		museoMovimientoArtisticoService.addMovimientoArtistico(newMuseo.getId(), MovimientoArtistico.getId());

		MovimientoArtisticoEntity lastMovimiento = museoMovimientoArtisticoService.getMovimientoArtistico(newMuseo.getId(), MovimientoArtistico.getId());
		assertEquals(MovimientoArtistico.getId(), lastMovimiento.getId());
		assertEquals(MovimientoArtistico.getNombre(), lastMovimiento.getNombre());
	}
	
	
	/**
	 * Prueba para asociar un Museo que no existe a un MovimientoArtistico.
	 *
	 */
	@Test
	void testAddInvalidMovimientoArtistico() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un MovimientoArtistico
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			museoMovimientoArtisticoService.addMovimientoArtistico(newMuseo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para asociar un MovimientoArtistico a un Museo que no existe.
	 *
	 */
	@Test
	void testAddMovimientoArtisticoInvalidMuseo() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			MovimientoArtisticoEntity MovimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(MovimientoArtistico);
			museoMovimientoArtisticoService.addMovimientoArtistico(0L, MovimientoArtistico.getId());
		});
	}
	
	
//	/**
//	 * Prueba para consultar la lista de MovimientoArtisticos de un Museo.
//	 */
//	@Test
//	void testGetMovimientoArtisticos() throws EntityNotFoundException {
//		List<MovimientoArtisticoEntity> MovimientoArtisticoEntities = museoMovimientoArtisticoService.getMovimientoArtisticos(museo.getId());
//
//		assertEquals(movimientoArtisticoList.size(), MovimientoArtisticoEntities.size());
//
//		for (int i = 0; i < movimientoArtisticoList.size(); i++) {
//			assertTrue(MovimientoArtisticoEntities.contains(movimientoArtisticoList.get(0)));
//		}
//	}
	
	
	/**
	 * Prueba para consultar la lista de MovimientoArtisticos de un Museo que no existe.
	 */
	@Test
	void testGetMovimientoArtisticosInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoMovimientoArtisticoService.getMovimientoArtisticos(0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un MovimientoArtistico de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
		MovimientoArtisticoEntity MovimientoArtisticoEntity = movimientoArtisticoList.get(0);
		MovimientoArtisticoEntity MovimientoArtistico = museoMovimientoArtisticoService.getMovimientoArtistico(museo.getId(), MovimientoArtisticoEntity.getId());
		assertNotNull(MovimientoArtistico);

		assertEquals(MovimientoArtisticoEntity.getId(), MovimientoArtistico.getId());
		assertEquals(MovimientoArtisticoEntity.getNombre(), MovimientoArtistico.getNombre());
	}
	
	
	/**
	 * Prueba para consultar un MovimientoArtistico que no existe de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidMovimientoArtistico()  {
		assertThrows(EntityNotFoundException.class, ()->{
			museoMovimientoArtisticoService.getMovimientoArtistico(museo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un MovimientoArtistico de un Museo que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMovimientoArtisticoInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity museoEntity = museo;
			museoMovimientoArtisticoService.getMovimientoArtistico(0L, museoEntity.getId());
		});
	}
	
	
	/**
	 * Prueba para obtener un MovimientoArtistico no asociado a un Museo.
	 *
	 */
	@Test
	void testGetNotAssociatedMovimientoArtistico() {
		assertThrows(IllegalOperationException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un MovimientoArtistico
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			MovimientoArtisticoEntity MovimientoArtistico = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(MovimientoArtistico);
			museoMovimientoArtisticoService.getMovimientoArtistico(newMuseo.getId(), MovimientoArtistico.getId());
		});
	}
	
	
	/**
	 * Prueba para actualizar los MovimientoArtisticos de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMovimientoArtisticos() throws EntityNotFoundException {
		List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			museo.getMovimientos().add(entity);
			entity.getMuseos().add(museo);
			nuevaLista.add(entity);
		}
		museoMovimientoArtisticoService.replaceMovimientoArtisticos(museo.getId(), nuevaLista);

		List<MovimientoArtisticoEntity> MovimientoArtisticoEntity = museoMovimientoArtisticoService.getMovimientoArtisticos(museo.getId());
		for (MovimientoArtisticoEntity aNuevaLista : nuevaLista) {
			assertTrue(MovimientoArtisticoEntity.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los MovimientoArtisticos que no existen de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidMovimientoArtisticos() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			museoMovimientoArtisticoService.replaceMovimientoArtisticos(museo.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar un MovimientoArtistico de un Museo que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMovimientoArtisticosInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
				entity.getMuseos().add(museo);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			museoMovimientoArtisticoService.replaceMovimientoArtisticos(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un MovimientoArtistico con un Museo.
	 *
	 */
	@Test
	void testRemoveMovimientoArtistico() throws EntityNotFoundException {
		for (MovimientoArtisticoEntity MovimientoArtistico : movimientoArtisticoList) {
			museoMovimientoArtisticoService.removeMovimientoArtistico(museo.getId(), MovimientoArtistico.getId());
		}
		assertTrue(museoMovimientoArtisticoService.getMovimientoArtisticos(museo.getId()).isEmpty());
	}

	/**
	 * Prueba desasociar un MovimientoArtistico que no existe con un Museo.
	 *
	 */
	@Test
	void testRemoveInvalidMovimientoArtistico(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoMovimientoArtisticoService.removeMovimientoArtistico(museo.getId(), 0L);
		});
	}

	/**
	 * Prueba desasociar un MovimientoArtistico con un Museo que no existe.
	 *
	 */
	@Test
	void testRemoveMovimientoArtisticoInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoMovimientoArtisticoService.removeMovimientoArtistico(0L, museo.getId());
		});
	}

}
