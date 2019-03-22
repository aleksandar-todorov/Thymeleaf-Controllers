package org.softuni.residentevil.repository;

import org.softuni.residentevil.domain.entities.Virus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Validated
@Repository
public interface VirusRepository extends JpaRepository<Virus, String> {
}
