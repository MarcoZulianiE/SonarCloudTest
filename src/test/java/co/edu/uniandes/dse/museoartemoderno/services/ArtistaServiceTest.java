package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
@Import(ArtistaService.class)
public class ArtistaServiceTest {

	@Autowired
	private ArtistaService artistaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<ArtistaEntity> artistaList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
	private List<Date> fechaList = new ArrayList<>();
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
		for (int i = 0; i < 6; i++) {
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}

		for (int i = 0; i < 3; i++) {
			MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museoEntity);
			museoList.add(museoEntity);
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaList.add(sdf.parse("1995-05-20"));
			fechaList.add(sdf.parse("2000-05-20"));
			fechaList.add(sdf.parse("2021-05-20"));
			fechaList.add(sdf.parse("2020-05-20"));
			fechaList.add(sdf.parse("2023-05-20"));
			fechaList.add(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			artistaEntity.setFechaNacimiento(fechaList.get(0));
			artistaEntity.setFechaFallecimiento(fechaList.get(1));
			artistaEntity.setLugarNacimiento(paisList.get(0));
			artistaEntity.setLugarFallecimiento(paisList.get(1));
			artistaEntity.setMuseos(museoList);
			artistaEntity.setMovimientos(movimientoArtisticoList);
			entityManager.persist(artistaEntity);
			artistaList.add(artistaEntity);
		}		

	}

	/**
	 * Prueba para crear un Artista
	 */
	@Test
	void testCreateArtista() throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
		//Datos que deben ser diferentes a null para crear un artista
		newEntity.setFechaNacimiento(fechaList.get(0));
		newEntity.setFechaFallecimiento(fechaList.get(1));
		newEntity.setObras(obraList);
		newEntity.setLugarNacimiento(paisList.get(0));
		newEntity.setLugarFallecimiento(paisList.get(1));
		newEntity.setMuseos(museoList);
		newEntity.setMovimientos(movimientoArtisticoList);
		ArtistaEntity result = artistaService.createArtista(newEntity);
		assertNotNull(result);

		ArtistaEntity entity = entityManager.find(ArtistaEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}

	/**
	 * Prueba para crear un Artista con Nombre invalido
	 */
	@Test
	void testCreateArtistaWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			newEntity.setNombre("");
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para crear un Artista con un Nombre ya existente.
	 */
	@Test
	void testCreateArtistaWithStoredNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			newEntity.setNombre(artistaList.get(0).getNombre());
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para crear un Artista con una fecha de Nacimiento invalida
	 */
	@Test
	void testCreateArtistaWithInvalidFechaNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(2));
			newEntity.setFechaFallecimiento(fechaList.get(3));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para crear un Artista con una fecha de Fallecimiento invalida
	 */
	// @Test
	// void testCreateArtistaWithInvalidFechaFallecimiento() {
	// 	assertThrows(IllegalOperationException.class, () -> {
	// 		ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
	// 		//Datos que deben ser diferentes a null para crear un artista
	// 		newEntity.setFechaNacimiento(fechaList.get(0));
	// 		newEntity.setFechaFallecimiento(fechaList.get(4));
	// 		newEntity.setObras(obraList);
	// 		newEntity.setLugarNacimiento(paisList.get(0));
	// 		newEntity.setLugarFallecimiento(paisList.get(1));
	// 		newEntity.setMuseos(museoList);
	// 		newEntity.setMovimientos(movimientoArtisticoList);
	// 		artistaService.createArtista(newEntity);
	// 	});
	// }

	/**
	 * Prueba para crear un Artista con una fecha de Nacimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullFechaNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(null);
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullFechaFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(null);
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullLugarNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(null);
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullLugarFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(null);
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Artista con un Lugar Nacimiento que no existe
	 */
	@Test
	void testCreateBookWithInvalidLugarNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			PaisEntity paisEntity = new PaisEntity();
			paisEntity.setId(0L);
			artistaEntity.setLugarNacimiento(paisEntity);
			artistaService.createArtista(artistaEntity);
		});		
	}
	
	/**
	 * Prueba para crear un Artista con un Lugar Fallecimiento que no existe
	 */
	@Test
	void testCreateBookWithInvalidLugarFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
			PaisEntity paisEntity = new PaisEntity();
			paisEntity.setId(0L);
			artistaEntity.setLugarFallecimiento(paisEntity);
			artistaService.createArtista(artistaEntity);
		});		
	}
	
	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullMuseos() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(null);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullObras() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(null);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(movimientoArtisticoList);
			artistaService.createArtista(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Artista con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateArtistaWithNullMovimientos() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity newEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setFechaFallecimiento(fechaList.get(1));
			newEntity.setObras(obraList);
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setLugarFallecimiento(paisList.get(1));
			newEntity.setMuseos(museoList);
			newEntity.setMovimientos(null);
			artistaService.createArtista(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Artistas.
	 */
	@Test
	void testGetArtistas() {
		List<ArtistaEntity> list = artistaService.getArtistas();
		assertEquals(artistaList.size(), list.size());
		for (ArtistaEntity entity : list) {
			boolean found = false;
			for (ArtistaEntity storedEntity : artistaList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Artista.
	 */
	@Test
	void testGetArtista() throws EntityNotFoundException {
		ArtistaEntity entity = artistaList.get(0);
		ArtistaEntity resultEntity = artistaService.getArtista(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
	}

	/**
	 * Prueba para consultar un Artista que no existe.
	 */
	@Test
	void testGetInvalidArtista() {
		assertThrows(EntityNotFoundException.class,()->{
			artistaService.getArtista(0L);
		});
	}

	/**
	 * Prueba para actualizar un Artista.
	 */
	@Test
	void testUpdateArtista() throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity artistaEntity = artistaList.get(0);
		ArtistaEntity pojoEntity = factory.manufacturePojo(ArtistaEntity.class);

		pojoEntity.setId(artistaEntity.getId());
		//Datos que deben ser diferentes a null para crear un artista
		pojoEntity.setFechaNacimiento(fechaList.get(0));
		pojoEntity.setFechaFallecimiento(fechaList.get(1));
		pojoEntity.setObras(obraList);
		pojoEntity.setLugarNacimiento(paisList.get(0));
		pojoEntity.setLugarFallecimiento(paisList.get(1));
		pojoEntity.setMuseos(museoList);
		pojoEntity.setMovimientos(movimientoArtisticoList);

		artistaService.updateArtista(artistaEntity.getId(), pojoEntity);

		ArtistaEntity response = entityManager.find(ArtistaEntity.class, artistaEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getNombre(), response.getNombre());
	}

	/**
	 * Prueba para actualizar un Artista invalido.
	 */
	@Test
	void testUpdateArtistaInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			ArtistaEntity pojoEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			pojoEntity.setFechaNacimiento(fechaList.get(0));
			pojoEntity.setFechaFallecimiento(fechaList.get(1));
			pojoEntity.setObras(obraList);
			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setLugarFallecimiento(paisList.get(1));
			pojoEntity.setMuseos(museoList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setId(0L);
			artistaService.updateArtista(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Artista con Nombre ivalido.
	 */
	@Test
	void testUpdateArtistaWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity entity = artistaList.get(0);
			ArtistaEntity pojoEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			pojoEntity.setFechaNacimiento(fechaList.get(0));
			pojoEntity.setFechaFallecimiento(fechaList.get(1));
			pojoEntity.setObras(obraList);
			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setLugarFallecimiento(paisList.get(1));
			pojoEntity.setMuseos(museoList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setNombre("");
			pojoEntity.setId(entity.getId());
			artistaService.updateArtista(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Artista con Nombre invalido.
	 */
	@Test
	void testUpdateArtistaWithNoValidNombre2() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity entity = artistaList.get(0);
			ArtistaEntity pojoEntity = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			pojoEntity.setFechaNacimiento(fechaList.get(0));
			pojoEntity.setFechaFallecimiento(fechaList.get(1));
			pojoEntity.setObras(obraList);
			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setLugarFallecimiento(paisList.get(1));
			pojoEntity.setMuseos(museoList);
			pojoEntity.setMovimientos(movimientoArtisticoList);
			pojoEntity.setNombre(null);
			pojoEntity.setId(entity.getId());
			artistaService.updateArtista(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Artista.
	 */
	@Test
	void testDeleteArtista() throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity artistaEntity = artistaList.get(0);
		artistaService.deleteArtista(artistaEntity.getId());
		ArtistaEntity deleted = entityManager.find(ArtistaEntity.class, artistaEntity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Artista que no existe.
	 */
	@Test
	void testDeleteInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaService.deleteArtista(0L);
		});
	}

	/**
	 * Prueba para eliminar un Artista con una Obras asociadas.
	 */
	@Test
	void testDeleteArtistaWithObras() {
		assertThrows(IllegalOperationException.class, () -> {
			ArtistaEntity entity = artistaList.get(0);
			entity.setObras(obraList);
			artistaService.deleteArtista(entity.getId());
		});
	}

}