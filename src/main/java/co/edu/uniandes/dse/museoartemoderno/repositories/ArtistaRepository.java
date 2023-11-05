package co.edu.uniandes.dse.museoartemoderno.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.museoartemoderno.entities.ArtistaEntity;

@Repository
public interface ArtistaRepository extends JpaRepository<ArtistaEntity, Long> {
	List<ArtistaEntity> findByNombre(String nombre);
}