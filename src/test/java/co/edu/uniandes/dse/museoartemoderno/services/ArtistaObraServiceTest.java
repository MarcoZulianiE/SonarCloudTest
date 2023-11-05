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
@Import(ArtistaObraService.class)
public class ArtistaObraServiceTest {

	@Autowired
	private ArtistaObraService artistaObraService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private ArtistaEntity artista = new ArtistaEntity();
	private List<ObraEntity> obraList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
	private List<Date> fechaList = new ArrayList<>();
	private List<MuseoEntity> museoList = new ArrayList<>();
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

		artista = factory.manufacturePojo(ArtistaEntity.class);
		artista.setFechaNacimiento(fechaList.get(0));
		artista.setFechaFallecimiento(fechaList.get(1));
		artista.setLugarNacimiento(paisList.get(0));
		artista.setLugarFallecimiento(paisList.get(0));
		artista.setMuseos(museoList);
		artista.setMovimientos(movimientoArtisticoList);
		entityManager.persist(artista);

		for (int i = 0; i < 3; i++) {
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraEntity.setArtista(artista);
			obraList.add(obraEntity);
			artista.getObras().add(obraEntity);	
		}


	}

	/**
	 * Prueba para asociar una Obra a un Artista.
	 *
	 */
	@Test
	void testAddObra() throws EntityNotFoundException, IllegalOperationException {
		ArtistaEntity newArtista = factory.manufacturePojo(ArtistaEntity.class);
		//Datos que deben ser diferentes a null para crear un artista
		newArtista.setFechaNacimiento(fechaList.get(0));
		newArtista.setFechaFallecimiento(fechaList.get(1));
		newArtista.setObras(obraList);
		newArtista.setLugarNacimiento(paisList.get(0));
		newArtista.setLugarFallecimiento(paisList.get(1));
		newArtista.setMuseos(museoList);
		newArtista.setMovimientos(movimientoArtisticoList);
		entityManager.persist(newArtista);

		ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
		entityManager.persist(obraEntity);

		ObraEntity response = artistaObraService.addObra(obraEntity.getId(),newArtista.getId());

		assertNotNull(response);
		assertEquals(obraEntity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar una Obra que no existe a un Artista.
	 *
	 */
	@Test
	void testAddInvalidObra() {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity newArtista = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newArtista.setFechaNacimiento(fechaList.get(0));
			newArtista.setFechaFallecimiento(fechaList.get(1));
			newArtista.setObras(obraList);
			newArtista.setLugarNacimiento(paisList.get(0));
			newArtista.setLugarFallecimiento(paisList.get(1));
			newArtista.setMuseos(museoList);
			newArtista.setMovimientos(movimientoArtisticoList);
			entityManager.persist(newArtista);
			artistaObraService.addObra(newArtista.getId(), 0L);
		});
	}

	/**
	 * Prueba para asociar una Obra a un Artista que no existe.
	 *
	 */
	@Test
	void testAddObraInvalidArtista() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			ObraEntity obra = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obra);
			artistaObraService.addObra(0L, obra.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de Obras de un Artista.
	 */
	@Test
	void testGetObras() throws EntityNotFoundException {
		List<ObraEntity> obraEntity = artistaObraService.getObras(artista.getId());

		assertEquals(obraList.size(), obraEntity.size());

		for (int i = 0; i < obraList.size(); i++) {
			assertTrue(obraEntity.contains(obraList.get(0)));
		}
	}

	/**
	 * Prueba para consultar la lista de obras de un artista que no existe.
	 */
	@Test
	void testGetObrasInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaObraService.getObras(0L);
		});
	}

	/**
	 * Prueba para consultar una Obra de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetObra() throws EntityNotFoundException, IllegalOperationException {
		ObraEntity obraEntity = obraList.get(0);
		ObraEntity obra = artistaObraService.getObra(artista.getId(), obraEntity.getId());
		assertNotNull(obra);

		assertEquals(obraEntity.getId(), obra.getId());
		assertEquals(obraEntity.getNombre(), obra.getNombre());
		assertEquals(obraEntity.getDescripcion(), obra.getDescripcion());
	}

	/**
	 * Prueba para consultar una Obra que no existe de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidObra()  {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaObraService.getObra(artista.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar una Obra de un Artista que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetObraInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			ObraEntity authorEntity = obraList.get(0);
			artistaObraService.getObra(0L, authorEntity.getId());
		});
	}

	/**
	 * Prueba para obtener una Obra no asociada a un artista.
	 *
	 */
	@Test
	void testGetNotAssociatedObra() {
		assertThrows(IllegalOperationException.class, ()->{
			ArtistaEntity newArtista = factory.manufacturePojo(ArtistaEntity.class);
			//Datos que deben ser diferentes a null para crear un artista
			newArtista.setFechaNacimiento(fechaList.get(0));
			newArtista.setFechaFallecimiento(fechaList.get(1));
			newArtista.setObras(obraList);
			newArtista.setLugarNacimiento(paisList.get(0));
			newArtista.setLugarFallecimiento(paisList.get(1));
			newArtista.setMuseos(museoList);
			newArtista.setMovimientos(movimientoArtisticoList);
			entityManager.persist(newArtista);
			ObraEntity obra = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obra);
			artistaObraService.getObra(newArtista.getId(), obra.getId());
		});
	}

	/**
	 * Prueba para actualizar las Obras de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceObras() throws EntityNotFoundException {
		List<ObraEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(entity);
			artista.getObras().add(entity);
			nuevaLista.add(entity);
		}
		artistaObraService.replaceObras(artista.getId(), nuevaLista);

		List<ObraEntity> obraEntities = artistaObraService.getObras(artista.getId());
		for (ObraEntity aNuevaLista : nuevaLista) {
			assertTrue(obraEntities.contains(aNuevaLista));
		}
	}

	/**
	 * Prueba para actualizar las Obras de un Artista que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceObrasInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
				entity.setArtista(artista);
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			artistaObraService.replaceObras(0L, nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar las Obras que no existen de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidAuthors() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> nuevaLista = new ArrayList<>();
			ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			artistaObraService.replaceObras(artista.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar una Obra de un Artista que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceObrasInvalidObra(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<ObraEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				ObraEntity entity = factory.manufacturePojo(ObraEntity.class);
				entity.setArtista(artista);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			artistaObraService.replaceObras(0L, nuevaLista);
		});
	}
}
