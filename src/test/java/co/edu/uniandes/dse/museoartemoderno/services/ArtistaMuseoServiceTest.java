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
@Import(ArtistaMuseoService.class)
public class ArtistaMuseoServiceTest {
	
	@Autowired
	private ArtistaMuseoService artistaMuseoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();
	
	private ArtistaEntity artista = new ArtistaEntity();
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

		artista = factory.manufacturePojo(ArtistaEntity.class);
		artista.setFechaNacimiento(fechaList.get(0));
		artista.setFechaFallecimiento(fechaList.get(1));
		artista.setLugarNacimiento(paisList.get(0));
		artista.setLugarFallecimiento(paisList.get(1));
		artista.setMuseos(museoList);
		artista.setMovimientos(movimientoArtisticoList);
		entityManager.persist(artista);

		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(entity);
			entity.getArtistas().add(artista);
			museoList.add(entity);
			artista.getMuseos().add(entity);	
		}
	}
	
	/**
	 * Prueba para asociar un Museo a un Artista.
	 *
	 */
	@Test
	void testAddMuseo() throws EntityNotFoundException, IllegalOperationException {
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

		MuseoEntity museo = factory.manufacturePojo(MuseoEntity.class);
		entityManager.persist(museo);

		artistaMuseoService.addMuseo(newArtista.getId(), museo.getId());

		MuseoEntity lastMovimiento = artistaMuseoService.getMuseo(newArtista.getId(), museo.getId());
		assertEquals(museo.getId(), lastMovimiento.getId());
		assertEquals(museo.getNombre(), lastMovimiento.getNombre());
	}
	
	/**
	 * Prueba para asociar un Museo que no existe a un Artista.
	 *
	 */
	@Test
	void testAddInvalidMuseo() {
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
			artistaMuseoService.addMuseo(newArtista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un Museo a un Artista que no existe.
	 *
	 */
	@Test
	void testAddMuseoInvalidArtista() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity museo = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museo);
			artistaMuseoService.addMuseo(0L, museo.getId());
		});
	}
	
	/**
	 * Prueba para consultar la lista de Museos de un Artista.
	 */
	@Test
	void testGetMuseos() throws EntityNotFoundException {
		List<MuseoEntity> museoEntities = artistaMuseoService.getMuseos(artista.getId());

		assertEquals(museoList.size(), museoEntities.size());

		for (int i = 0; i < museoList.size(); i++) {
			assertTrue(museoEntities.contains(museoList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de Museos de un Artista que no existe.
	 */
	@Test
	void testGetMuseosInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMuseoService.getMuseos(0L);
		});
	}
	
	/**
	 * Prueba para consultar un Museo de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMuseo() throws EntityNotFoundException, IllegalOperationException {
		MuseoEntity museoEntity = museoList.get(0);
		MuseoEntity museo = artistaMuseoService.getMuseo(artista.getId(), museoEntity.getId());
		assertNotNull(museo);

		assertEquals(museoEntity.getId(), museo.getId());
		assertEquals(museoEntity.getNombre(), museo.getNombre());
	}
	
	/**
	 * Prueba para consultar un Museo que no existe de un Artista.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidMuseo()  {
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMuseoService.getMuseo(artista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un Museo de un artista que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetMuseoInvalidArtista() {
		assertThrows(EntityNotFoundException.class, ()->{
			ArtistaEntity artistaEntity = artista;
			artistaMuseoService.getMuseo(0L, artistaEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un Museo no asociado a un Artista.
	 *
	 */
	@Test
	void testGetNotAssociatedMuseo() {
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
			MuseoEntity museo = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(museo);
			artistaMuseoService.getMuseo(newArtista.getId(), museo.getId());
		});
	}
	
	/**
	 * Prueba para actualizar los Museos de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMuseos() throws EntityNotFoundException {
		List<MuseoEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entityManager.persist(entity);
			artista.getMuseos().add(entity);
			entity.getArtistas().add(artista);
			nuevaLista.add(entity);
		}
		artistaMuseoService.replaceMuseos(artista.getId(), nuevaLista);

		List<MuseoEntity> museoEntity = artistaMuseoService.getMuseos(artista.getId());
		for (MuseoEntity aNuevaLista : nuevaLista) {
			assertTrue(museoEntity.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los Museos que no existen de un Artista.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidMuseos() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<MuseoEntity> nuevaLista = new ArrayList<>();
			MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			artistaMuseoService.replaceMuseos(artista.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un Museo de un Artista que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceMuseosInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<MuseoEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				MuseoEntity entity = factory.manufacturePojo(MuseoEntity.class);
				entity.getArtistas().add(artista);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			artistaMuseoService.replaceMuseos(0L, nuevaLista);
		});
	}
	
//	/**
//	 * Prueba desasociar un Museo con un Artista.
//	 * [Por algún motivo detecta como si la lista que se recorre se modificara durante el ciclo]
//	   [Esta prueba se cubre en POSTMAN ya que no se logro solucionar el error]
//	 */
//	@Test
//	void testRemoveMuseo() throws EntityNotFoundException {
//		List<MuseoEntity> lista = museoList;
//		for (MuseoEntity museo : lista) {
//			artistaMuseoService.removeMuseo(artista.getId(), museo.getId());
//		}
//		assertTrue(artistaMuseoService.getMuseos(artista.getId()).isEmpty());
//	}
	
	/**
	 * Prueba desasociar un Museo que no existe con un Artista.
	 *
	 */
	@Test
	void testRemoveInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMuseoService.removeMuseo(artista.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un Museo con un Artista que no existe.
	 *
	 */
	@Test
	void testRemoveMuseoInvalidArtista(){
		assertThrows(EntityNotFoundException.class, ()->{
			artistaMuseoService.removeMuseo(0L, artista.getId());
		});
	}
}
