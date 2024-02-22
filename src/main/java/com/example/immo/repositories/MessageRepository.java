package com.example.immo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.immo.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
