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
@Import(ArtistaMovimientoArtisticoService.class)
public class ArtistaMovimientoArtisticoServiceTest {
	
	@Autowired
	private ArtistaMovimientoArtisticoService artistaMovimientoArtisticoService;

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
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			
			obraList.add(obraEntity);
			artista.getObras().add(obraEntity);	
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
//		artista.setMovimientos(movimientoArtisticoList);
		entityManager.persist(artista);

		for (int i = 0; i < 3; i++) {		
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			entity.getArtistas().add(artista);
			movimientoArtisticoList.add(entity);
			artista.getMovimientos().add(entity);	
		}
	}
	
	/**
	 * Prueba para asociar un Movimiento Aartistico a un Artista.
	 *
	 */
	@Test
	void testAddMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
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

		MovimientoArtisticoEntity movimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		entityManager.persist(movimiento);

		artistaMovimientoArtisticoService.addMovimientoArtistico(newArtista.getId(), movimiento.getId());

		MovimientoArtisticoEntity lastMovimiento = artistaMovimientoArtisticoService.getMovimientoArtistico(newArtista.getId(), movimiento.getId());
		assertEquals(movimiento.getId(), lastMovimiento.getId());
		assertEquals(movimiento.getNombre(), lastMovimiento.getNombre());
	}
	
	/**
	 * Prueba para asociar un Movimiento Artistico que no existe a un Artista.
	 *
	 */
	@Test
	void testAddInvalidAuthor() {
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
			artistaMovimientoArtisticoService.addMovimientoArtistico(newArtista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un Movimiento Artistico a un Artista que no existe.
	 *
	 */
	@Test
	void testAddMovimientoArtisticoInvalidArtista() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			MovimientoArtisticoEntity movimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimiento);
			artistaMovimientoArtisticoService.addMovimientoArtistico(0L, movimiento.getId());
		});
	}
	
	/**
	 * Prueba para consultar la lista de ;pvimientos Artisticos de un Artista.
	 */
	@Test
	void testGetMovimientosArtisticos() throws EntityNotFoundException {
		List<MovimientoArtisticoEntity> authorEntities = artistaMovimientoArtisticoService.getMovimientosArtisticos(artista.getId());

		assertEquals(movimientoArtisticoList.size(), authorEntities.size());

		for (int i = 0; i < movimientoArtisticoList.size(); i++) {
			assertTrue(authorEntities.contains(movimientoArtisticoList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de Movimientos Artisticos de un Artista que no existe.
	 */
	@Test
	void testGetMovimientosArtisticoInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMovimientoArtisticoService.getMovimientosArtisticos(0L);
		});
	}
	
	/**
	 * Prueba para consultar un Movimiento Artistico de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException {
		MovimientoArtisticoEntity authorEntity = movimientoArtisticoList.get(0);
		MovimientoArtisticoEntity author = artistaMovimientoArtisticoService.getMovimientoArtistico(artista.getId(), authorEntity.getId());
		assertNotNull(author);

		assertEquals(authorEntity.getId(), author.getId());
		assertEquals(authorEntity.getNombre(), author.getNombre());
	}
	
	/**
	 * Prueba para consultar un Movimiento Artistico que no existe de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidMovimientoArtistico()  {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMovimientoArtisticoService.getMovimientoArtistico(artista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un Movimiento Artistico de un artista que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMovimientoArtisticoInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity authorEntity = artista;
			artistaMovimientoArtisticoService.getMovimientoArtistico(0L, authorEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un Movimiento Artistico no asociado a un Artista.
	 *
	 */
	@Test
	void testGetNotAssociatedMovimientoArtistico() {
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
			MovimientoArtisticoEntity movimiento = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(movimiento);
			artistaMovimientoArtisticoService.getMovimientoArtistico(newArtista.getId(), movimiento.getId());
		});
	}
	
	/**
	 * Prueba para actualizar los Movimientos Artisticos de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMovimientosArtisticos() throws EntityNotFoundException {
		List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entityManager.persist(entity);
			artista.getMovimientos().add(entity);
			entity.getArtistas().add(artista);
			nuevaLista.add(entity);
		}
		artistaMovimientoArtisticoService.replaceMovimientosArtisticos(artista.getId(), nuevaLista);

		List<MovimientoArtisticoEntity> movimientoArtisticoEntity = artistaMovimientoArtisticoService.getMovimientosArtisticos(artista.getId());
		for (MovimientoArtisticoEntity aNuevaLista : nuevaLista) {
			assertTrue(movimientoArtisticoEntity.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los Movimientos Artisticos que no existen de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidMovimientosArtisticos() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
			MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			artistaMovimientoArtisticoService.replaceMovimientosArtisticos(artista.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un Movimiento Artistico de un Artista que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMovimientoArtisticosInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<MovimientoArtisticoEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				MovimientoArtisticoEntity entity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
				entity.getArtistas().add(artista);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			artistaMovimientoArtisticoService.replaceMovimientosArtisticos(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba desasociar un Movimiento Artistico con un Artista.
	 *
	 */
	@Test
	void testRemoveMovimientoArtistico() throws EntityNotFoundException {
		for (MovimientoArtisticoEntity movimiento : movimientoArtisticoList) {
			artistaMovimientoArtisticoService.removeMovimientoArtistico(artista.getId(), movimiento.getId());
		}
		assertTrue(artistaMovimientoArtisticoService.getMovimientosArtisticos(artista.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un Movimiento Artistico que no existe con un Artista.
	 *
	 */
	@Test
	void testRemoveInvalidMovimientoArtistico(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMovimientoArtisticoService.removeMovimientoArtistico(artista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un Movimiento Artistico con un Artista que no existe.
	 *
	 */
	@Test
	void testRemoveMovimientoArtisticoInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMovimientoArtisticoService.removeMovimientoArtistico(0L, artista.getId());
		});
	}
	
}
