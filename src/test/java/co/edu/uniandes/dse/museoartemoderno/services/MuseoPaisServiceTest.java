package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
@Import({MuseoPaisService.class, MuseoService.class})
public class MuseoPaisServiceTest {
	
	@Autowired
	private MuseoPaisService museoPaisService;

	@Autowired
	private MuseoService museoService;
	
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
		
		museo = factory.manufacturePojo(MuseoEntity.class);
		museo.setArtistas(artistaList);
		museo.setObras(obraList);
		museo.setMovimientos(movimientoArtisticoList);
		entityManager.persist(museo);

		for(int i = 0; i<3; i++)
		{
			PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
			entityManager.persist(paisEntity);
			paisList.add(paisEntity);
		}
		museo.setUbicacion(paisList.get(0));
	}
	
	
	/**
	 * Prueba para actualizar los Paiss de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePais() throws EntityNotFoundException 	{
		PaisEntity pais =  paisList.get(0);
		museoPaisService.replacePais(museo.getId(), pais.getId());
		MuseoEntity museoEntity = museoService.getMuseo(museo.getId());
		assertEquals(museoEntity.getUbicacion().getId(), pais.getId());

	}
	
	
	/**
	 * Prueba para actualizar los Paiss que no existen de un Museo.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePaisInvalidMuseo() {
		assertNotNull(paisList.get(0));
		assertThrows(EntityNotFoundException.class, ()->{
			museoPaisService.replacePais(0L, paisList.get(0).getId());
		});
	}
	

	/**
	 * Prueba para actualizar un Pais de un Museo que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidPais() {
		assertThrows(EntityNotFoundException.class, ()->{
			MuseoEntity entity = museo;
			museoPaisService.replacePais(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba desasociar un Pais con un Museo.
	 *
	 */
	@Test
	void testRemovePais() throws EntityNotFoundException {
		museoPaisService.removePais(museo.getId());
		MuseoEntity response = museoService.getMuseo(museo.getId());
		assertNull(response.getUbicacion());
	}



	/**
	 * Prueba desasociar un Pais con un Museo que no existe.
	 *
	 */
	@Test
	void testRemovePaisInvalidMuseo(){
		assertThrows(EntityNotFoundException.class, ()->{
			museoPaisService.removePais(0L);
		});
	}

}