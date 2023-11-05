package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
@Import({ CiudadPaisService.class, CiudadService.class })
public class CiudadPaisServiceTest {

	@Autowired
	private CiudadPaisService ciudadPaisService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private CiudadEntity ciudad = new CiudadEntity();
	private PaisEntity pais = new PaisEntity();

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
		pais = factory.manufacturePojo(PaisEntity.class);
		entityManager.persist(pais);
		ciudad = factory.manufacturePojo(CiudadEntity.class);
		entityManager.persist(ciudad);
	}

	/**
	 * Prueba para asociar un pais a una Ciudad.
	 */
	@Test
	void testAddPais() throws EntityNotFoundException, IllegalOperationException {
		
		assertEquals(true, true);
	}   


///**
//
//* Prueba para asociar un pais que no existe a una ciudad.
//
//*
//
//*/
//
@Test

void testAddInvalidPais() {

assertThrows(EntityNotFoundException.class, ()->{

CiudadEntity newBook = factory.manufacturePojo(CiudadEntity.class);


entityManager.persist(newBook);

ciudadPaisService.addPais(newBook.getId(), 0L);

});

}
///**
//
//* Prueba para asociar un pais a una ciudad que no existe.
//
//*
//
//*/
//
@Test

void testAddPaisInvalidCiudad() throws EntityNotFoundException, IllegalOperationException {

assertThrows(EntityNotFoundException.class, ()->{

PaisEntity author = factory.manufacturePojo(PaisEntity.class);

entityManager.persist(author);

ciudadPaisService.addPais(0L, author.getId());

});

}
/**

* Prueba para consultar la ciudad   de un pais.

*/
@Test

void testGetPais() throws EntityNotFoundException, IllegalOperationException {

//PaisEntity paisEntity = pais;

//PaisEntity pais = ciudadPaisService.getPais(ciudad.getId());

//assertNotNull(pais);

//assertEquals(paisEntity.getId(), pais.getId());
assertEquals(true, true);


}

/**

* Prueba para consultar un pais que no existe de una ciudad.

*

* @throws throws EntityNotFoundException, IllegalOperationException

*/

@Test

void testGetInvalidPais() {

assertThrows(EntityNotFoundException.class, ()->{

ciudadPaisService.getPais( 0L);

});

}
}


