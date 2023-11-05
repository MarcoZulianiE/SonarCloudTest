package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
@Import(MuseoService.class)
public class MuseoServiceTest {


	@Autowired
	private MuseoService museoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
	private List<MuseoEntity> museoList = new ArrayList<>();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<MovimientoArtisticoEntity> movimientoArtisticoList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ArtistaEntity");
		entityManager.getEntityManager().createQuery("delete from PaisEntity");
		entityManager.getEntityManager().createQuery("delete from MuseoEntity");
		entityManager.getEntityManager().createQuery("delete from ObraEntity");
		entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity");
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
		}

		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity movimientoArtisticoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimientoArtisticoEntity);
			movimientoArtisticoList.add(movimientoArtisticoEntity);
		}
		

		for (int i = 0; i < 3; i++) {
			MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
			museoEntity.setArtistas(artistaList);
			museoEntity.setObras(obraList);
			museoEntity.setMovimientos(movimientoArtisticoList);
			museoEntity.setUbicacion(paisList.get(0));
			entityManager.persist(museoEntity);
			museoList.add(museoEntity);
		}		

	}

	/**
	 * Prueba para crear un Museo
	 */
	@Test
	void testCreateMuseo() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity newEntity = factory.manufacturePojo(MuseoEntity.class);
		//Datos que deben ser diferentes a null para crear un Museo
		newEntity.setArtistas(artistaList);
		newEntity.setObras(obraList);
		newEntity.setMovimientos(movimientoArtisticoList);
		newEntity.setUbicacion(paisList.get(0));
		MuseoEntity result = museoService.createMuseo(newEntity);
		assertNotNull(result);

		MuseoEntity entity = entityManager.find(MuseoEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}

	/**
	 * Prueba para crear un Museo con Nombre invalido
	 */
	@Test
	void testCreateMuseoWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			MuseoEntity newEntity = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Museo
			newEntity.setArtistas(artistaList);
			newEntity.setObras(obraList);
			newEntity.setMovimientos(movimientoArtisticoList);
			newEntity.setUbicacion(paisList.get(0));
			newEntity.setNombre("");
			museoService.createMuseo(newEntity);
		});
	}

	/**
	 * Prueba para crear un Museo con un Nombre ya existente.
	 */
	@Test
	void testCreateMuseoWithStoredNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			MuseoEntity newEntity = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Museo
			newEntity.setArtistas(artistaList);
			newEntity.setObras(obraList);
			newEntity.setMovimientos(movimientoArtisticoList);
			newEntity.setUbicacion(paisList.get(0));
			newEntity.setNombre(museoList.get(0).getNombre());
			museoService.createMuseo(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Museos.
	 */
	@Test
	void testGetMuseos() {
		List<MuseoEntity> list = museoService.getMuseos();
		assertEquals(museoList.size(), list.size());
		for (MuseoEntity entity : list) {
			boolean found = false;
			for (MuseoEntity storedEntity : museoList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Museo.
	 */
	@Test
	void testGetMuseo() throws EntityNotFoundException {
		MuseoEntity entity = museoList.get(0);
		MuseoEntity resultEntity = museoService.getMuseo(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
	}

	/**
	 * Prueba para consultar un Museo que no existe.
	 */
	@Test
	void testGetInvalidMuseo() {
		assertThrows(EntityNotFoundException.class,()->{
			museoService.getMuseo(0L);
		});
	}

	/**
	 * Prueba para actualizar un Museo.
	 */
	@Test
	void testUpdateMuseo() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity MuseoEntity = museoList.get(0);
		MuseoEntity pojoEntity = factory.manufacturePojo(MuseoEntity.class);

		pojoEntity.setId(MuseoEntity.getId());
		//Datos que deben ser diferentes a null para crear un Museo
		pojoEntity.setArtistas(artistaList);
		pojoEntity.setObras(obraList);
		pojoEntity.setMovimientos(movimientoArtisticoList);
		pojoEntity.setUbicacion(paisList.get(0));

		museoService.updateMuseo(MuseoEntity.getId(), pojoEntity);

		MuseoEntity response = entityManager.find(MuseoEntity.class, MuseoEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getNombre(), response.getNombre());
	}

	/**
	 * Prueba para actualizar un Museo invalido.
	 */
	@Test
	void testUpdateMuseoInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			MuseoEntity pojoEntity = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Museo
			pojoEntity.setArtistas(artistaList);
			pojoEntity.setObras(obraList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setUbicacion(paisList.get(0));
			pojoEntity.setId(0L);
			museoService.updateMuseo(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Museo con Nombre ivalido.
	 */
	@Test
	void testUpdateMuseoWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			MuseoEntity entity = museoList.get(0);
			MuseoEntity pojoEntity = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Museo
			pojoEntity.setArtistas(artistaList);
			pojoEntity.setObras(obraList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setUbicacion(paisList.get(0));
			pojoEntity.setNombre("");
			pojoEntity.setId(entity.getId());
			museoService.updateMuseo(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Museo con Nombre invalido.
	 */
	@Test
	void testUpdateMuseoWithNoValidNombre2() {
		assertThrows(IllegalOperationException.class, () -> {
			MuseoEntity entity = museoList.get(0);
			MuseoEntity pojoEntity = factory.manufacturePojo(MuseoEntity.class);
			//Datos que deben ser diferentes a null para crear un Museo
			pojoEntity.setArtistas(artistaList);
			pojoEntity.setObras(obraList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setUbicacion(paisList.get(0));
			pojoEntity.setNombre(null);
			pojoEntity.setId(entity.getId());
			museoService.updateMuseo(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Museo.
	 */
	@Test
	void testDeleteMuseo() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity MuseoEntity = museoList.get(0);
		museoService.deleteMuseo(MuseoEntity.getId());
		MuseoEntity deleted = entityManager.find(MuseoEntity.class, MuseoEntity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Museo que no existe.
	 */
	@Test
	void testDeleteInvalidMuseo() {
		assertThrows(EntityNotFoundException.class, ()->{
			museoService.deleteMuseo(0L);
		});
	}

//	/**
//	 * Prueba para eliminar un Museo con una Obras asociadas.
//	 */
//	@Test
//	void testDeleteMuseoWithObras() {
//		assertThrows(IllegalOperationException.class, () -> {
//			MuseoEntity entity = museoList.get(0);
//			entity.setObras(obraList);
//			museoService.deleteMuseo(entity.getId());
//		});
//	}

}