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
@Import(MuseoArtistaService.class)
public class MuseoArtistaServiceTest {
	
	@Autowired
	private MuseoArtistaService museoArtistaService;

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
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);
		}

		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity movimientoArtisticoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimientoArtisticoEntity);
			movimientoArtisticoList.add(movimientoArtisticoEntity);
		}
		
		museo = factory.manufacturePojo(MuseoEntity.class);
		museo.setArtistas(artistaList);
		museo.setObras(obraList);
		museo.setMovimientos(movimientoArtisticoList);
		museo.setUbicacion(paisList.get(0));
		entityManager.persist(museo);

		for (int i = 0; i < 3; i++) {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(entity);
			entity.getMuseos().add(museo);
			artistaList.add(entity);
			museo.getArtistas().add(entity);
		}
	}
	
	
	/**
	 * Prueba para asociar un Artista a un Museo.
	 *
	 */
	@Test
	void testAddArtista() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
		//Datos que deben ser diferentes a null para crear un artista
		newMuseo.setArtistas(artistaList);
		newMuseo.setObras(obraList);
		newMuseo.setMovimientos(movimientoArtisticoList);
		newMuseo.setUbicacion(paisList.get(0));
		entityManager.persist(newMuseo);

		ArtistaEntity artista = factory.manufacturePojo(ArtistaEntity.class);
		entityManager.persist(artista);

		museoArtistaService.addArtista(newMuseo.getId(), artista.getId());

		ArtistaEntity lastMovimiento = museoArtistaService.getArtista(newMuseo.getId(), artista.getId());
		assertEquals(artista.getId(), lastMovimiento.getId());
		assertEquals(artista.getNombre(), lastMovimiento.getNombre());
	}
	
	
	/**
	 * Prueba para asociar un Museo que no existe a un Artista.
	 *
	 */
	@Test
	void testAddInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			museoArtistaService.addArtista(newMuseo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para asociar un Artista a un Museo que no existe.
	 *
	 */
	@Test
	void testAddArtistaInvalidMuseo() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity artista = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artista);
			museoArtistaService.addArtista(0L, artista.getId());
		});
	}
	
	/**
	 * Prueba para consultar la lista de Artistas de un Museo.
	 */
	@Test
	void testGetArtistas() throws EntityNotFoundException {
		List<ArtistaEntity> artistaEntities = museoArtistaService.getArtistas(museo.getId());

		assertEquals(movimientoArtisticoList.size(), artistaEntities.size());

		for (int i = 0; i < movimientoArtisticoList.size(); i++) {
			assertTrue(artistaEntities.contains(artistaList.get(0)));
		}
	}
	
	
	/**
	 * Prueba para consultar la lista de Artistas de un Museo que no existe.
	 */
	@Test
	void testGetArtistasInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoArtistaService.getArtistas(0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un Artista de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetArtista() throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity artistaEntity = artistaList.get(0);
		ArtistaEntity artista = museoArtistaService.getArtista(museo.getId(), artistaEntity.getId());
		assertNotNull(artista);

		assertEquals(artistaEntity.getId(), artista.getId());
		assertEquals(artistaEntity.getNombre(), artista.getNombre());
	}
	
	
	/**
	 * Prueba para consultar un Artista que no existe de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidArtista()  {
		assertThrows(EntityNotFoundException.class, ()->{
			museoArtistaService.getArtista(museo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un Artista de un Museo que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetArtistaInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity museoEntity = museo;
			museoArtistaService.getArtista(0L, museoEntity.getId());
		});
	}
	
	
	/**
	 * Prueba para obtener un Artista no asociado a un Museo.
	 *
	 */
	@Test
	void testGetNotAssociatedArtista() {
		assertThrows(IllegalOperationException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			ArtistaEntity artista = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(artista);
			museoArtistaService.getArtista(newMuseo.getId(), artista.getId());
		});
	}
	
	
	/**
	 * Prueba para actualizar los Artistas de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceArtistas() throws EntityNotFoundException {
		List<ArtistaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(entity);
			museo.getArtistas().add(entity);
			entity.getMuseos().add(museo);
			nuevaLista.add(entity);
		}
		museoArtistaService.replaceArtistas(museo.getId(), nuevaLista);

		List<ArtistaEntity> artistaEntity = museoArtistaService.getArtistas(museo.getId());
		for (ArtistaEntity aNuevaLista : nuevaLista) {
			assertTrue(artistaEntity.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los Artistas que no existen de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidArtistas() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ArtistaEntity> nuevaLista = new ArrayList<>();
			ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			museoArtistaService.replaceArtistas(museo.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar un Artista de un Museo que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceArtistasInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<ArtistaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				ArtistaEntity entity = factory.manufacturePojo(ArtistaEntity.class);
				entity.getMuseos().add(museo);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			museoArtistaService.replaceArtistas(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un Artista con un Museo.
	 *
	 */
	@Test
	void testRemoveArtista() throws EntityNotFoundException {
		for (ArtistaEntity artista : artistaList) {
			museoArtistaService.removeArtista(museo.getId(), artista.getId());
		}
		assertTrue(museoArtistaService.getArtistas(museo.getId()).isEmpty());
	}

	/**
	 * Prueba desasociar un Artista que no existe con un Museo.
	 *
	 */
	@Test
	void testRemoveInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoArtistaService.removeArtista(museo.getId(), 0L);
		});
	}

	/**
	 * Prueba desasociar un Artista con un Museo que no existe.
	 *
	 */
	@Test
	void testRemoveArtistaInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoArtistaService.removeArtista(0L, museo.getId());
		});
	}

}

