package co.edu.uniandes.dse.museoartemoderno.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.museoartemoderno.entities.PaisEntity;

public interface PaisRepository extends JpaRepository<PaisEntity, Long> {
	List<PaisEntity> findByNombrePais(String nombre);
	List<PaisEntity> findByCoordenadasPais(String coordenadasPais);

}
