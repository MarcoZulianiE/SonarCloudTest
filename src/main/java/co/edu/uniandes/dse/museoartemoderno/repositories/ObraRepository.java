package co.edu.uniandes.dse.museoartemoderno.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.museoartemoderno.entities.ObraEntity;


@Repository
public interface ObraRepository extends JpaRepository<ObraEntity,Long>{

}
