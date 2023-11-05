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
@Import(PaisService.class)
public class PaisServiceTest {
	@Autowired
	private PaisService paisService;
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();
	
	private List<PaisEntity> paisList = new ArrayList<>();
    private List<CiudadEntity> ciudadList = new ArrayList<>();
    
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
        
        for (int i = 0; i<3; i++)
        {
            CiudadEntity ciudadEntity = factory.manufacturePojo(CiudadEntity.class);
            entityManager.persist(ciudadEntity);
            ciudadList.add(ciudadEntity);
    }
		ciudadList.get(0).setPais(paisList.get(0));
		paisList.get(0).getCiudades().add(ciudadList.get(0));
      
}
	/**
	 * Prueba para crear un pais
	 */
    @Test
    void testCreatePais() throws EntityNotFoundException, IllegalOperationException {
            PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
            PaisEntity result = paisService.createPais(newEntity);
            assertNotNull(result);
            PaisEntity entity = entityManager.find(PaisEntity.class, result.getId());
            assertEquals(newEntity.getNombrePais(), entity.getNombrePais());
            assertEquals(newEntity.getCoordenadasPais(), entity.getCoordenadasPais());


    }
	/**
	 * Prueba para crear un pais con nombre invalido(nombre vacio)
	 */

    @Test
    void testCreatePaisNombreInvalido() {
            assertThrows(IllegalOperationException.class, () -> {
                    PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                    newEntity.setNombrePais("");
                    paisService.createPais(newEntity);
            });
    }
    
	/**
	 * Prueba para crear un pais con nombre invalido(no hay nombre)
	 */
    
    @Test
    void testCreatePaisNombreInvalido2() {
            assertThrows(IllegalOperationException.class, () -> {
                    PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                    newEntity.setNombrePais(null);
                    paisService.createPais(newEntity);
            });
    }
    
	/**
	 * Prueba para crear un pais con nombre ya existente
	 */
    @Test
    void testCreatePaisNombreExistente() {
            assertThrows(IllegalOperationException.class, () -> {
                    PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                    newEntity.setNombrePais(paisList.get(0).getNombrePais());
                    paisService.createPais(newEntity);
            });
    }
	/**
	 * Prueba para crear un pais con coordenadas invalidas(vacias)
	 */
    @Test
    void testCreatePaisCoordenadasInvalido() {
            assertThrows(IllegalOperationException.class, () -> {
                    PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                    newEntity.setCoordenadasPais("");
                    paisService.createPais(newEntity);
            });
    }
    
	/**
	 * Prueba para crear un pais sin coordenadas
	 */
    void testCreatePaisCoordenadasInvalido2() {
        assertThrows(IllegalOperationException.class, () -> {
                PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                newEntity.setCoordenadasPais(null);
                paisService.createPais(newEntity);
        });
}
	/**
	 * Prueba para crear un pais con coordenadas ya existentes
	 */
    @Test
    void testCreatePaisCoordenadasExistentes() {
            assertThrows(IllegalOperationException.class, () -> {
                    PaisEntity newEntity = factory.manufacturePojo(PaisEntity.class);
                    newEntity.setCoordenadasPais(paisList.get(0).getCoordenadasPais());
                    paisService.createPais(newEntity);
            });
    }
	/**
	 * Prueba para consultar la lista de paises
	 */
    @Test

    void testGetPaises() {

    List<PaisEntity> list = paisService.getPaises();

    assertEquals(paisList.size(), list.size());

    for (PaisEntity entity : list) {

    boolean found = false;

    for (PaisEntity storedEntity : paisList) {

    if (entity.getId().equals(storedEntity.getId())) {

    found = true;

    }

    }

    assertTrue(found);

    }

    }
    /**

    * Prueba para consultar un pais.

    */
    @Test

    void testGetPais() throws EntityNotFoundException {

    PaisEntity entity = paisList.get(0);

    PaisEntity resultEntity = paisService.getPais(entity.getId());

    assertNotNull(resultEntity);

    assertEquals(entity.getId(), resultEntity.getId());
    assertEquals(entity.getNombrePais(), resultEntity.getNombrePais());
    assertEquals(entity.getCoordenadasPais(), resultEntity.getCoordenadasPais());




    }
    /**

    * Prueba para consultar un pais que no existe.

    */

    @Test

    void testGetInvalidPais() {

    assertThrows(EntityNotFoundException.class,()->{

    paisService.getPais(0L);

    });

    }
    
    /**

    * Prueba para actualizar un pais.

    */

    @Test

    void testUpdatePais() throws EntityNotFoundException, IllegalOperationException {

    PaisEntity entity = paisList.get(0);

    PaisEntity pojoEntity = factory.manufacturePojo(PaisEntity.class);

    pojoEntity.setId(entity.getId());

    paisService.updatePais(entity.getId(), pojoEntity);

    PaisEntity resp = entityManager.find(PaisEntity.class, entity.getId());

    assertEquals(pojoEntity.getId(), resp.getId());

    assertEquals(pojoEntity.getNombrePais(), resp.getNombrePais());
    assertEquals(pojoEntity.getCoordenadasPais(), resp.getCoordenadasPais());
    }

    /**

    * Prueba para actualizar un pais inválido.

    */

    @Test

    void testUpdatePaisInvalid() {

    assertThrows(EntityNotFoundException.class, () -> {

    PaisEntity pojoEntity = factory.manufacturePojo(PaisEntity.class);

    pojoEntity.setId(0L);

    paisService.updatePais(0L, pojoEntity);

    });

    }
    
    
    /**

    * Prueba para actualizar un pais con coordenadas invalidas.

    */

    @Test

    void testUpdatePaisWithNoValidCoordenadas() {

    assertThrows(IllegalOperationException.class, () -> {

    PaisEntity entity = paisList.get(0);

    PaisEntity pojoEntity = factory.manufacturePojo(PaisEntity.class);

    pojoEntity.setCoordenadasPais("");

    pojoEntity.setId(entity.getId());

    paisService.updatePais(entity.getId(), pojoEntity);

    });
}
    /**

    * Prueba para actualizar un pais con nombre invalido.

    */

    @Test

    void testUpdatePaisWithNoValidNombre() {

    assertThrows(IllegalOperationException.class, () -> {

    PaisEntity entity = paisList.get(0);

    PaisEntity pojoEntity = factory.manufacturePojo(PaisEntity.class);

    pojoEntity.setNombrePais("");

    pojoEntity.setId(entity.getId());

    paisService.updatePais(entity.getId(), pojoEntity);

    });
}    
    /**

    * Prueba para eliminar un pais.

    */

    @Test

    void testDeletePais() throws EntityNotFoundException, IllegalOperationException {

    	PaisEntity entity = paisList.get(1);

    paisService.deletePais(entity.getId());

    PaisEntity deleted = entityManager.find(PaisEntity.class, entity.getId());

    assertNull(deleted);

    } 
    /**

    * Prueba para eliminar un pais que no existe.

    */

    @Test

    void testDeleteInvalidPais() {

    assertThrows(EntityNotFoundException.class, ()->{

    paisService.deletePais(0L);

    });

    }
    /**

    * Prueba para eliminar un pais con ciudades asociadas.

    */
    
	void testDeletePaisWithCiudades() {
		assertThrows(IllegalOperationException.class, () -> {
			PaisEntity entity = paisList.get(0);
			paisService.deletePais(entity.getId());
		});
	}
    
    
}
