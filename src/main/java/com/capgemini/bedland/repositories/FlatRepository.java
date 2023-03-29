package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.FlatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {

}
