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
@Import(MuseoObraService.class)
public class MuseoObraServiceTest {
	
	@Autowired
	private MuseoObraService museoObraService;

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
			ArtistaEntity museoEntity = factory.manufacturePojo(ArtistaEntity.class);
			entityManager.persist(museoEntity);
			artistaList.add(museoEntity);
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
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraEntity.setMuseo(museo);
			obraList.add(obraEntity);
			museo.getObras().add(obraEntity);
		}
	}
	
	
	/**
	 * Prueba para asociar un Obra a un Museo.
	 *
	 */
	@Test
	void testAddObra() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
		//Datos que deben ser diferentes a null para crear un Obra
		newMuseo.setArtistas(artistaList);
		newMuseo.setObras(obraList);
		newMuseo.setMovimientos(movimientoArtisticoList);
		newMuseo.setUbicacion(paisList.get(0));
		entityManager.persist(newMuseo);

		ObraEntity obra = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obra);

		museoObraService.addObra(newMuseo.getId(), obra.getId());

		ObraEntity lastMovimiento = museoObraService.getObra(newMuseo.getId(), obra.getId());
		assertEquals(obra.getId(), lastMovimiento.getId());
		assertEquals(obra.getNombre(), lastMovimiento.getNombre());
	}
	
	
	/**
	 * Prueba para asociar un Museo que no existe a un Obra.
	 *
	 */
	@Test
	void testAddInvalidObra() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Obra
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			museoObraService.addObra(newMuseo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para asociar un Obra a un Museo que no existe.
	 *
	 */
	@Test
	void testAddObraInvalidMuseo() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			ObraEntity Obra = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(Obra);
			museoObraService.addObra(0L, Obra.getId());
		});
	}
	
	
//	/**
//	 * Prueba para consultar la lista de Obras de un Museo.
//	 */
//	@Test
//	void testGetObras() throws EntityNotFoundException {
//		List<ObraEntity> ObraEntities = museoObraService.getObras(museo.getId());
//
//		assertEquals(movimientoArtisticoList.size(), ObraEntities.size());
//
//		for (int i = 0; i < movimientoArtisticoList.size(); i++) {
//			assertTrue(ObraEntities.contains(obraList.get(0)));
//		}
//	}
	
	
	/**
	 * Prueba para consultar la lista de Obras de un Museo que no existe.
	 */
	@Test
	void testGetObrasInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoObraService.getObras(0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un Obra de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetObra() throws EntityNotFoundException, IllegalOperationException {
		ObraEntity ObraEntity = obraList.get(0);
		ObraEntity Obra = museoObraService.getObra(museo.getId(), ObraEntity.getId());
		assertNotNull(Obra);

		assertEquals(ObraEntity.getId(), Obra.getId());
		assertEquals(ObraEntity.getNombre(), Obra.getNombre());
	}
	
	
	/**
	 * Prueba para consultar un Obra que no existe de un Museo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidObra()  {
		assertThrows(EntityNotFoundException.class, ()->{
			museoObraService.getObra(museo.getId(), 0L);
		});
	}
	
	
	/**
	 * Prueba para consultar un Obra de un Museo que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetObraInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity museoEntity = museo;
			museoObraService.getObra(0L, museoEntity.getId());
		});
	}
	
	
	/**
	 * Prueba para obtener un Obra no asociado a un Museo.
	 *
	 */
	@Test
	void testGetNotAssociatedObra() {
		assertThrows(IllegalOperationException.class, ()->{
			MuseoEntity newMuseo = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Obra
			newMuseo.setArtistas(artistaList);
			newMuseo.setObras(obraList);
			newMuseo.setMovimientos(movimientoArtisticoList);
			newMuseo.setUbicacion(paisList.get(0));
			entityManager.persist(newMuseo);
			ObraEntity Obra = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(Obra);
			museoObraService.getObra(newMuseo.getId(), Obra.getId());
		});
	}
	
	
	/**
	 * Prueba para actualizar los Obras de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceObras() throws EntityNotFoundException {
		List<ObraEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(entity);
			museo.getObras().add(entity);
			nuevaLista.add(entity);
		}
		museoObraService.replaceObras(museo.getId(), nuevaLista);

		List<ObraEntity> ObraEntity = museoObraService.getObras(museo.getId());
		for (ObraEntity aNuevaLista : nuevaLista) {
			assertTrue(ObraEntity.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los Obras que no existen de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidObras() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> nuevaLista = new ArrayList<>();
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			museoObraService.replaceObras(museo.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar un Obra de un Museo que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceObrasInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
				entity.setMuseo(museo);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			museoObraService.replaceObras(0L, nuevaLista);
		});
	}

//	/**
//	 * Prueba desasociar un Obra con un Museo.
//	 *
//	 */
//	@Test
//	void testRemoveObra() throws EntityNotFoundException {
//		for (ObraEntity obra : obraList) {
//			museoObraService.removeObra(museo.getId(), obra.getId());
//		}
//		assertTrue(museoObraService.getObras(museo.getId()).isEmpty());
//	}

	/**
	 * Prueba desasociar un Obra que no existe con un Museo.
	 *
	 */
	@Test
	void testRemoveInvalidObra(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoObraService.removeObra(museo.getId(), 0L);
		});
	}

	/**
	 * Prueba desasociar un Obra con un Museo que no existe.
	 *
	 */
	@Test
	void testRemoveObraInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoObraService.removeObra(0L, museo.getId());
		});
	}

}
