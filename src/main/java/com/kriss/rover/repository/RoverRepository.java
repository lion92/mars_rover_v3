package com.kriss.rover.repository;

import com.kriss.rover.entity.Rover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoverRepository extends JpaRepository<Rover, Long> {

}