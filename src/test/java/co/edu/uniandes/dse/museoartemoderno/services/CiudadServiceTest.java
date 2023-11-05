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

import co.edu.uniandes.dse.museoartemoderno.entities.CiudadEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(CiudadService.class)
public class CiudadServiceTest {
	@Autowired
	private CiudadService ciudadService;
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
    private List<CiudadEntity> ciudadList = new ArrayList<>();
	private List<PaisEntity> paisList = new ArrayList<>();
    @BeforeEach
    void setUp() {
            clearData();
            insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CiudadEntity");
        entityManager.getEntityManager().createQuery("delete from PaisEntity");
}
    private void insertData() {
        for (int i = 0; i < 3; i++) {
                PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
                entityManager.persist(paisEntity);
                paisList.add(paisEntity);
        }

        for (int i = 0; i < 3; i++) {
                CiudadEntity ciudadEntity = factory.manufacturePojo(CiudadEntity.class);
                ciudadEntity.setPais(paisList.get(0));
                entityManager.persist(ciudadEntity);
                ciudadList.add(ciudadEntity);
        }
}
	/**
	 * Prueba para crear una ciudad
	 */
    @Test
    void testCreateCiudad() throws EntityNotFoundException, IllegalOperationException {
            CiudadEntity newEntity = factory.manufacturePojo(CiudadEntity.class);
            newEntity.setPais(paisList.get(0));
            CiudadEntity result = ciudadService.createCiudad(newEntity);
            assertNotNull(result);
            CiudadEntity entity = entityManager.find(CiudadEntity.class, result.getId());
            assertEquals(newEntity.getNombreCiudad(), entity.getNombreCiudad());
            assertEquals(newEntity.getCoordenadasCiudad(), entity.getCoordenadasCiudad());


    }
	/**
	 * Prueba para crear una ciudad con nombre invalido(nombre vacio)
	 */
    @Test
    void testCreateCiudadNombreInvalido() {
            assertThrows(IllegalOperationException.class, () -> {
                    CiudadEntity newEntity = factory.manufacturePojo(CiudadEntity.class);
                    newEntity.setPais(paisList.get(0));
                    newEntity.setNombreCiudad("");
                    ciudadService.createCiudad(newEntity);
            });
    }
	/**
	 * Prueba para crear una ciudad con nombre ya existente
	 */
    @Test
    void testCreateCiudadNombreExistente() {
            assertThrows(IllegalOperationException.class, () -> {
                    CiudadEntity newEntity = factory.manufacturePojo(CiudadEntity.class);
                    newEntity.setPais(paisList.get(0));
                    newEntity.setNombreCiudad(ciudadList.get(0).getNombreCiudad());
                    ciudadService.createCiudad(newEntity);
            });
    }
	/**
	 * Prueba para crear una ciudad con coordenadas invalidas(vacias)
	 */
    @Test
    void testCreateCiudadCoordenadasInvalido() {
            assertThrows(IllegalOperationException.class, () -> {
                    CiudadEntity newEntity = factory.manufacturePojo(CiudadEntity.class);
                    newEntity.setPais(paisList.get(0));
                    newEntity.setCoordenadasCiudad("");
                    ciudadService.createCiudad(newEntity);
            });
    }
	/**
	 * Prueba para crear una ciudad con coordenadas ya existentes
	 */
    @Test
    void testCreateCiudadCoordenadasExistentes() {
            assertThrows(IllegalOperationException.class, () -> {
                    CiudadEntity newEntity = factory.manufacturePojo(CiudadEntity.class);
                    newEntity.setPais(paisList.get(0));
                    newEntity.setCoordenadasCiudad(ciudadList.get(0).getCoordenadasCiudad());
                    ciudadService.createCiudad(newEntity);
            });
    }
	/**
	 * Prueba para consultar la lista de ciudades
	 */
    @Test

    void testGetCiudades() {

    List<CiudadEntity> list = ciudadService.getCiudades();

    assertEquals(ciudadList.size(), list.size());

    for (CiudadEntity entity : list) {

    boolean found = false;

    for (CiudadEntity storedEntity : ciudadList) {

    if (entity.getId().equals(storedEntity.getId())) {

    found = true;

    }

    }

    assertTrue(found);

    }

    }
    /**

    * Prueba para consultar una ciudad.

    */
    @Test

    void testGetCiudad() throws EntityNotFoundException {

    CiudadEntity entity = ciudadList.get(0);

    CiudadEntity resultEntity = ciudadService.getCiudad(entity.getId());

    assertNotNull(resultEntity);

    assertEquals(entity.getId(), resultEntity.getId());
    assertEquals(entity.getNombreCiudad(), resultEntity.getNombreCiudad());
    assertEquals(entity.getCoordenadasCiudad(), resultEntity.getCoordenadasCiudad());




    }
    /**

    * Prueba para consultar una ciudad que no existe.

    */

    @Test

    void testGetInvalidCiudad() {

    assertThrows(EntityNotFoundException.class,()->{

    ciudadService.getCiudad(0L);

    });

    }
    
    /**

    * Prueba para actualizar una ciudad.

    */

    @Test

    void testUpdateCiudad() throws EntityNotFoundException, IllegalOperationException {

    CiudadEntity entity = ciudadList.get(0);

    CiudadEntity pojoEntity = factory.manufacturePojo(CiudadEntity.class);

    pojoEntity.setId(entity.getId());

    ciudadService.updateCiudad(entity.getId(), pojoEntity);

    CiudadEntity resp = entityManager.find(CiudadEntity.class, entity.getId());

    assertEquals(pojoEntity.getId(), resp.getId());

    assertEquals(pojoEntity.getNombreCiudad(), resp.getNombreCiudad());
    assertEquals(pojoEntity.getCoordenadasCiudad(), resp.getCoordenadasCiudad());
    }

    /**

    * Prueba para actualizar una ciudad inválida.

    */

    @Test

    void testUpdateCiudadInvalid() {

    assertThrows(EntityNotFoundException.class, () -> {

    CiudadEntity pojoEntity = factory.manufacturePojo(CiudadEntity.class);

    pojoEntity.setId(0L);

    ciudadService.updateCiudad(0L, pojoEntity);

    });

    }
    
    
    /**

    * Prueba para actualizar una ciudad con coordenadas invalidas.

    */

    @Test

    void testUpdateCiudadWithNoValidCoordenadas() {

    assertThrows(IllegalOperationException.class, () -> {

    CiudadEntity entity = ciudadList.get(0);

    CiudadEntity pojoEntity = factory.manufacturePojo(CiudadEntity.class);

    pojoEntity.setCoordenadasCiudad("");

    pojoEntity.setId(entity.getId());

    ciudadService.updateCiudad(entity.getId(), pojoEntity);

    });
}
    /**

    * Prueba para actualizar una ciudad con nombre invalido.

    */

    @Test

    void testUpdateCiudadWithNoValidNombre() {

    assertThrows(IllegalOperationException.class, () -> {

    CiudadEntity entity = ciudadList.get(0);

    CiudadEntity pojoEntity = factory.manufacturePojo(CiudadEntity.class);

    pojoEntity.setNombreCiudad("");

    pojoEntity.setId(entity.getId());

    ciudadService.updateCiudad(entity.getId(), pojoEntity);

    });
}    
    /**

    * Prueba para eliminar una ciudad.

    */

    @Test

    void testDeleteCiudad() throws EntityNotFoundException, IllegalOperationException {

    	CiudadEntity entity = ciudadList.get(1);

    ciudadService.deleteCiudad(entity.getId());

    CiudadEntity deleted = entityManager.find(CiudadEntity.class, entity.getId());

    assertNull(deleted);

    } 
    /**

    * Prueba para eliminar una ciudad que no existe.

    */

    @Test

    void testDeleteInvalidCiudad() {

    assertThrows(EntityNotFoundException.class, ()->{

    ciudadService.deleteCiudad(0L);

    });

    }
    
    
}
