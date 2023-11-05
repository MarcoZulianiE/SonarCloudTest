package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.transaction.Transactional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(ObraService.class)
public class ObraServiceTest {
	
	@Autowired
	private ObraService obraService;
	
	@Autowired 
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
	private List<ObraEntity> obraList = new ArrayList<>();
	private ArtistaEntity artistaEntity;
	private MuseoEntity museoEntity;
	private MovimientoArtisticoEntity movimientoEntity;
	
	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
	
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ArtistaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from MuseoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ObraEntity").executeUpdate();
	}
	
	private void insertData() {
		for (int i =0; i<4; i++) {
			ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
			entityManager.persist(obraEntity);
			obraList.add(obraEntity);			
		}
		
		artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
		obraList.get(2).setArtista(artistaEntity);;
		entityManager.persist(artistaEntity);

		
		movimientoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
		obraList.get(1).setMovimiento(movimientoEntity);
		entityManager.persist(movimientoEntity);
		
		museoEntity = factory.manufacturePojo(MuseoEntity.class);
		museoEntity.getObras().add(obraList.get(0));
		obraList.get(0).setMuseo(museoEntity);
		entityManager.persist(museoEntity);
	}

	/**
	 * Prueba para crear una Obra.
	 */
	
	@Test
	void testCreateObra() throws EntityNotFoundException, IllegalOperationException {
		ObraEntity newEntity = factory.manufacturePojo(ObraEntity.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			newEntity.setFechaPublicacion(sdf.parse("1995-05-20"));
			} 
		catch (ParseException e) {
			e.printStackTrace();
			}
		
		newEntity.setArtista(artistaEntity);
		newEntity.setMuseo(museoEntity);
		newEntity.setMovimiento(movimientoEntity);
		
		ObraEntity result = obraService.createObra(newEntity);	
		
		assertNotNull(result);

		ObraEntity entity = entityManager.find(ObraEntity.class, result.getId());
		
		entity.setArtista(artistaEntity);
		entity.setMuseo(museoEntity);
		entity.setMovimiento(movimientoEntity);
				
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getArtista(), entity.getArtista());
		assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
	}
	
	
	/**
	 * Prueba para crear una Obra con una fecha de publicación mayor que la fecha actual.
	 * @throws IllegalOperationException 
	 */
	@Test
	void testCreateObraInvalidFechaPublicacion() {
		assertThrows(IllegalOperationException.class, ()->{
			ObraEntity newEntity = factory.manufacturePojo(ObraEntity.class);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());        
			calendar.add(Calendar.DATE, 15);
			newEntity.setFechaPublicacion(calendar.getTime());
			obraService.createObra(newEntity);
		});
	}
	
	/**
	 * Prueba para consultar una Obra.
	 */
	@Test
	void testGetObra() throws EntityNotFoundException {
		ObraEntity obraEntity = obraList.get(0);

		ObraEntity resultEntity = obraService.getObra(obraEntity.getId());
		assertNotNull(resultEntity);

		assertEquals(obraEntity.getId(), resultEntity.getId());
		assertEquals(obraEntity.getNombre(), resultEntity.getNombre());
		assertEquals(obraEntity.getFechaPublicacion(), resultEntity.getFechaPublicacion());
		assertEquals(obraEntity.getDescripcion(), resultEntity.getDescripcion());
	}
	
	/**
	 * Prueba para consultar una Obra que no existe.
	 */
	@Test
	void testGetInvalidObra() {
		assertThrows(EntityNotFoundException.class, ()->{
			obraService.getObra(0L);
		});
	}

	/**
	 * Prueba para actualizar una Obra.
	 */
	@Test
	void testUpdateObra() throws EntityNotFoundException, IllegalOperationException {
		ObraEntity obraEntity = obraList.get(0);
		ObraEntity pojoEntity = factory.manufacturePojo(ObraEntity.class);

		pojoEntity.setId(obraEntity.getId());

		obraService.updateObra(obraEntity.getId(), pojoEntity);

		ObraEntity response = entityManager.find(ObraEntity.class, obraEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getNombre(), response.getNombre());
		assertEquals(pojoEntity.getFechaPublicacion(), response.getFechaPublicacion());
		assertEquals(pojoEntity.getDescripcion(), response.getDescripcion());
	}
	
	/**
	 * Prueba para actualizar una Obra que no existe.
	 */
	@Test
	void testUpdateInvalidObra()  {
		assertThrows(EntityNotFoundException.class, ()->{
			ObraEntity pojoEntity = factory.manufacturePojo(ObraEntity.class);
			obraService.updateObra(0L, pojoEntity);	
		});
	}

	/**
	 * Prueba para eliminar una Obra
	 *
	 */
	@Test
	void testDeleteObra() throws EntityNotFoundException, IllegalOperationException {
		ObraEntity obraEntity = obraList.get(3);
		obraService.deleteObra(obraEntity.getId());
		ObraEntity deleted = entityManager.find(ObraEntity.class, obraEntity.getId());
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar una Obra que no existe
	 *
	 */
	@Test
	void testDeleteInvalidObra() {
		assertThrows(EntityNotFoundException.class, ()->{
			obraService.deleteObra(0L);
		});
	}

	/**
	 * Prueba para eliminar una Obra asociada a un Artista
	 *
	 */
	@Test
	void testDeleteObraWithArtista() {
		assertThrows(IllegalOperationException.class, () -> {
			obraService.deleteObra(obraList.get(2).getId());
		});
	}

	/**
	 * Prueba para eliminar una Obra asociada a un Movimiento Artistico
	 *
	 */
	@Test
	void testDeleteObraWithMovimientoArtistico() {
		assertThrows(IllegalOperationException.class, () -> {
			obraService.deleteObra(obraList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para eliminar una Obra asociada a un Museo
	 *
	 */
	@Test
	void testDeleteObraWithMuseo() {
		assertThrows(IllegalOperationException.class, () -> {
			obraService.deleteObra(obraList.get(0).getId());
		});
	}
		
		
}
