package co.edu.uniandes.dse.museoartemoderno.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MovimientoArtisticoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;
import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;
import co.edu.uniandes.dse.museoartemoderno.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.museoartemoderno.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MovimientoArtisticoService.class)
class MovimientoArtisticoServiceTest 
{
	@Autowired
	private MovimientoArtisticoService movimientoArtisticoService;

	@Autowired
	private TestEntityManager entityManager;


	private PodamFactory factory = new PodamFactoryImpl();

	private List<MovimientoArtisticoEntity> movimientoArtisticoList = new ArrayList<>();

	private List<MuseoEntity> museoList = new ArrayList<>();

	private List<ObraEntity> obraList = new ArrayList<>();

	private List<ArtistaEntity> artistaList = new ArrayList<>();

    private List<Date> fechaList = new ArrayList<>();

    private List<PaisEntity> paisList = new ArrayList<>();
    
    
	@BeforeEach
	void setUp() throws Exception 
	{
		clearData();
        insertData();
	}
	
	 /**
     * Limpia las tablas que estan implicadas en la prueba
     */
    private void clearData()
    {
    	entityManager.getEntityManager().createQuery("delete from MovimientoArtisticoEntity");
    	entityManager.getEntityManager().createQuery("delete from ObraEntity");
    	entityManager.getEntityManager().createQuery("delete from MuseoEntity");
    	entityManager.getEntityManager().createQuery("delete from ArtistaEntity");
    	entityManager.getEntityManager().createQuery("delete from PaisEntity");
    }
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de la prueba
     */
    private void insertData()
    {
    	for(int i = 1; i<=3; i++)
            {
                    MuseoEntity museoEntity = factory.manufacturePojo(MuseoEntity.class);
                    entityManager.persist(museoEntity);
                    museoList.add(museoEntity);
            }
            for(int i = 1; i<= 3; i++)
            {
                    ObraEntity obraEntity = factory.manufacturePojo(ObraEntity.class);
                    entityManager.persist(obraEntity);
                    obraList.add(obraEntity);
            }
            for(int i = 1; i<=3; i++)
            {
                    ArtistaEntity artistaEntity = factory.manufacturePojo(ArtistaEntity.class);
                    entityManager.persist(artistaEntity);
                    artistaList.add(artistaEntity);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                    fechaList.add(sdf.parse("1995-05-20"));
                    fechaList.add(sdf.parse("2000-10-20"));

                    fechaList.add(new Date());
            } catch (ParseException e) {
                    e.printStackTrace();
            }

            for(int i = 1; i<=3; i++)
            {
                    PaisEntity paisEntity = factory.manufacturePojo(PaisEntity.class);
                    entityManager.persist(paisEntity);
                    paisList.add(paisEntity);
            }


            for(int i=1; i<=3;i++)
            {
                    MovimientoArtisticoEntity movimientoEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
                    movimientoEntity.setArtistas(artistaList);
                    movimientoEntity.setMuseos(museoList);
                    movimientoEntity.setObras(obraList);
                    movimientoEntity.setFechaApogeo(fechaList.get(0));
                    movimientoEntity.setLugarOrigen(paisList.get(1));
                    entityManager.persist(movimientoEntity);
                    movimientoArtisticoList.add(movimientoEntity);
            }
    }

    /**
     * Prueba para crear un movimiento artistico
     */
    @Test
    void testCrearMovimiento() throws IllegalOperationException, EntityNotFoundException
    {
            MovimientoArtisticoEntity newEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
            
            newEntity.setObras(obraList);
            newEntity.setMuseos(museoList);
            newEntity.setArtistas(artistaList);
            newEntity.setFechaApogeo(fechaList.get(2));
            newEntity.setLugarOrigen(paisList.get(0));

            MovimientoArtisticoEntity result = movimientoArtisticoService.createMovimientoArtistico(newEntity);
            assertNotNull(result);

            MovimientoArtisticoEntity entity = entityManager.find(MovimientoArtisticoEntity.class, result.getId());

            assertEquals(newEntity.getId(), entity.getId());
            assertEquals(newEntity.getNombre(), entity.getNombre());
    }
    
    /**
     * Prueba para crear un movimiento artistico con nombre invalido
     */
     @Test
     void testCrearInvalidNameMovimiento()
     {
    	 assertThrows(IllegalOperationException.class, () -> {
 			MovimientoArtisticoEntity newEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
 			//Datos que deben ser diferentes a null para crear un artista
 			newEntity.setNombre("");
 			movimientoArtisticoService.createMovimientoArtistico(newEntity);
 		});
     }
     
     /**
      * Prueba para crear un movimiento artistico con nombre invalido
      */
      @Test
      void testCrearNullNameMovimiento()
      {
     	 assertThrows(IllegalOperationException.class, () -> {
  			MovimientoArtisticoEntity newEntity = factory.manufacturePojo(MovimientoArtisticoEntity.class);
  			//Datos que deben ser diferentes a null para crear un artista
  			newEntity.setNombre(null);
  			movimientoArtisticoService.createMovimientoArtistico(newEntity);
  		});
      }
    
    /**
     * Prueba para consultar la lista de movimientos artisticos
     */
    @Test
    void testGetMovimientos()
    {
            List<MovimientoArtisticoEntity> movimientosList = movimientoArtisticoService.getMovimientosArtisticos();
            assertEquals(movimientoArtisticoList.size(), movimientosList.size());

            for(MovimientoArtisticoEntity movimientoEntity: movimientoArtisticoList)
            {
                    boolean encontrado = false;
                    for(MovimientoArtisticoEntity movimientoEnBD: movimientosList)
                    {
                            if(movimientoEntity.getId().equals(movimientoEnBD.getId()))
                            {
                                    encontrado = true;
                            }
                    }
                    assertTrue(encontrado);
            }
    }
    
    /**
     * Prueba para consultar un movimiento
     */
    @Test
    void testGetMovimiento() throws EntityNotFoundException
    {
            MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoList.get(0);

            MovimientoArtisticoEntity movimientoConsultado = movimientoArtisticoService.getMovimientoArtistico(movimientoEntity.getId());
            assertNotNull(movimientoConsultado);

            assertEquals(movimientoEntity.getId(), movimientoConsultado.getId());
            assertEquals(movimientoEntity.getNombre(), movimientoConsultado.getNombre());
    }
    
    /**
     * Prueba para consultar un movimiento que no existe
     */
    @Test
    void testGetMovimientoInvalido()
    {
            assertThrows(EntityNotFoundException.class, ()->{
                    movimientoArtisticoService.getMovimientoArtistico(0L);
            });
    }
    
    /**
     * Prueba para eliminar un movimiento
     */
    @Test
    void testDeleteMovimientoArtistico() throws EntityNotFoundException, IllegalOperationException
    {
            MovimientoArtisticoEntity movimientoEntity = movimientoArtisticoList.get(0);
            movimientoArtisticoService.deleteMovimientoArtistico(movimientoEntity.getId());
            MovimientoArtisticoEntity movimientoBorrado = entityManager.find(MovimientoArtisticoEntity.class, movimientoEntity.getId());
            assertNull(movimientoBorrado);
    }
    
    /**
     * Prueba para eliminar un movimiento que no existe
     */
    @Test
    void testDeleteInvalidMovimientoArtistico()
    {
    	 assertThrows(EntityNotFoundException.class, ()->{
             movimientoArtisticoService.deleteMovimientoArtistico(0L);
     });
    }
}
