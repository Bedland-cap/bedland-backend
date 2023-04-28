package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.VotingResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingResponseRepository extends JpaRepository<VotingResponseEntity, Long> {

}
