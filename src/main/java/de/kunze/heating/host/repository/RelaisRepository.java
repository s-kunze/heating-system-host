package de.kunze.heating.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.kunze.heating.host.model.Relais;

@Repository
public interface RelaisRepository extends JpaRepository<Relais, Long> {

}
