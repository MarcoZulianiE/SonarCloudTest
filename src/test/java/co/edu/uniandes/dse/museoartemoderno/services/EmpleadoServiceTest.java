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
import co.edu.uniandes.dse.museoartemoderno.entities.EmpleadoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(EmpleadoService.class)
public class EmpleadoServiceTest {

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EmpleadoEntity> empleadoList = new ArrayList<>();
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
		entityManager.getEntityManager().createQuery("delete from EmpleadoEntity");
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
			EmpleadoEntity empleadoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			empleadoEntity.setFechaNacimiento(fechaList.get(0));
			empleadoEntity.setLugarNacimiento(paisList.get(0));
			entityManager.persist(empleadoEntity);
			empleadoList.add(empleadoEntity);
		}		

	}

	/**
	 * Prueba para crear un Empleado
	 */
	@Test
	void testCreateEmpleado() throws EntityNotFoundException, IllegalOperationException {
		EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
		//Datos que deben ser diferentes a null para crear un empleado
		newEntity.setFechaNacimiento(fechaList.get(0));
		newEntity.setLugarNacimiento(paisList.get(0));
		EmpleadoEntity result = empleadoService.createEmpleado(newEntity);
		assertNotNull(result);

		EmpleadoEntity entity = entityManager.find(EmpleadoEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}

	/**
	 * Prueba para crear un Empleado con Nombre invalido
	 */
	@Test
	void testCreateEmpleadoWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setNombre("");
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para crear un Empleado con un Nombre ya existente.
	 */
	@Test
	void testCreateEmpleadoWithStoredNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			newEntity.setNombre(empleadoList.get(0).getNombre());
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para crear un Empleado con una fecha de Nacimiento invalida
	 */
	@Test
	void testCreateEmpleadoWithInvalidFechaNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(2));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para crear un Empleado con una fecha de Nacimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullFechaNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(null);
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullFechaFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullLugarNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(null);
			empleadoService.createEmpleado(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullLugarFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Empleado con un Lugar Nacimiento que no existe
	 */
	@Test
	void testCreateBookWithInvalidLugarNacimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity empleadoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			PaisEntity paisEntity = new PaisEntity();
			paisEntity.setId(0L);
			empleadoEntity.setLugarNacimiento(paisEntity);
			empleadoService.createEmpleado(empleadoEntity);
		});		
	}
	
	/**
	 * Prueba para crear un Empleado con un Lugar Fallecimiento que no existe
	 */
	@Test
	void testCreateBookWithInvalidLugarFallecimiento() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity empleadoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			PaisEntity paisEntity = new PaisEntity();
			paisEntity.setId(0L);
			empleadoService.createEmpleado(empleadoEntity);
		});		
	}
	
	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullMuseos() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullObras() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}
	
	/**
	 * Prueba para crear un Empleado con una Fecha de Fallecimiento nula.
	 */
	@Test
	void testCreateEmpleadoWithNullMovimientos() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity newEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			newEntity.setFechaNacimiento(fechaList.get(0));
			newEntity.setLugarNacimiento(paisList.get(0));
			empleadoService.createEmpleado(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Empleados.
	 */
	@Test
	void testGetEmpleados() {
		List<EmpleadoEntity> list = empleadoService.getEmpleados();
		assertEquals(empleadoList.size(), list.size());
		for (EmpleadoEntity entity : list) {
			boolean found = false;
			for (EmpleadoEntity storedEntity : empleadoList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Empleado.
	 */
	@Test
	void testGetEmpleado() throws EntityNotFoundException {
		EmpleadoEntity entity = empleadoList.get(0);
		EmpleadoEntity resultEntity = empleadoService.getEmpleado(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
	}

	/**
	 * Prueba para consultar un Empleado que no existe.
	 */
	@Test
	void testGetInvalidEmpleado() {
		assertThrows(EntityNotFoundException.class,()->{
			empleadoService.getEmpleado(0L);
		});
	}

	/**
	 * Prueba para actualizar un Empleado.
	 */
	@Test
	void testUpdateEmpleado() throws EntityNotFoundException, IllegalOperationException {
		EmpleadoEntity empleadoEntity = empleadoList.get(0);
		EmpleadoEntity pojoEntity = factory.manufacturePojo(EmpleadoEntity.class);

		pojoEntity.setId(empleadoEntity.getId());
		//Datos que deben ser diferentes a null para crear un empleado
		pojoEntity.setFechaNacimiento(fechaList.get(0));
		pojoEntity.setLugarNacimiento(paisList.get(0));

		empleadoService.updateEmpleado(empleadoEntity.getId(), pojoEntity);

		EmpleadoEntity response = entityManager.find(EmpleadoEntity.class, empleadoEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getNombre(), response.getNombre());
	}

	/**
	 * Prueba para actualizar un Empleado invalido.
	 */
	@Test
	void testUpdateEmpleadoInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			EmpleadoEntity pojoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			pojoEntity.setFechaNacimiento(fechaList.get(0));

			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setId(0L);
			empleadoService.updateEmpleado(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Empleado con Nombre ivalido.
	 */
	@Test
	void testUpdateEmpleadoWithNoValidNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity entity = empleadoList.get(0);
			EmpleadoEntity pojoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			pojoEntity.setFechaNacimiento(fechaList.get(0));

			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setNombre("");
			pojoEntity.setId(entity.getId());
			empleadoService.updateEmpleado(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Empleado con Nombre invalido.
	 */
	@Test
	void testUpdateEmpleadoWithNoValidNombre2() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity entity = empleadoList.get(0);
			EmpleadoEntity pojoEntity = factory.manufacturePojo(EmpleadoEntity.class);
			//Datos que deben ser diferentes a null para crear un empleado
			pojoEntity.setFechaNacimiento(fechaList.get(0));

			pojoEntity.setLugarNacimiento(paisList.get(0));
			pojoEntity.setNombre(null);
			pojoEntity.setId(entity.getId());
			empleadoService.updateEmpleado(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Empleado.
	 */
	@Test
	void testDeleteEmpleado() throws EntityNotFoundException, IllegalOperationException {
		EmpleadoEntity empleadoEntity = empleadoList.get(0);
		empleadoService.deleteEmpleado(empleadoEntity.getId());
		EmpleadoEntity deleted = entityManager.find(EmpleadoEntity.class, empleadoEntity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Empleado que no existe.
	 */
	@Test
	void testDeleteInvalidEmpleado() {
		assertThrows(EntityNotFoundException.class, ()->{
			empleadoService.deleteEmpleado(0L);
		});
	}

	/**
	 * Prueba para eliminar un Empleado con una Obras asociadas.
	 */
	@Test
	void testDeleteEmpleadoWithObras() {
		assertThrows(IllegalOperationException.class, () -> {
			EmpleadoEntity entity = empleadoList.get(0);
			entity.setMuseo(museoList.get(0));
			empleadoService.deleteEmpleado(entity.getId());
		});
	}

}