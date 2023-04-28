package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
    Optional<ManagerEntity> findByLogin(String login);
    Optional<ManagerEntity> findByToken(String token);

}
