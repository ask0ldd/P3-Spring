package com.example.immo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.immo.models.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

}