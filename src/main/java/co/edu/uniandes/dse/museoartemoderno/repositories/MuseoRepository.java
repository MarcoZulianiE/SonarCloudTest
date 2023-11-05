package co.edu.uniandes.dse.museoartemoderno.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.museoartemoderno.entities.MuseoEntity;

@Repository
public interface MuseoRepository extends JpaRepository<MuseoEntity, Long> {
	List<MuseoEntity> findByNombre(String nombre);
}
