package co.edu.uniandes.dse.museoartemoderno.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.museoartemoderno.entities.CiudadEntity;

public interface CiudadRepository extends JpaRepository<CiudadEntity, Long> {
	List<CiudadEntity> findByNombreCiudad(String nombreCiudad);
	List<CiudadEntity> findByCoordenadasCiudad(String coordenadasCiudad);



}
