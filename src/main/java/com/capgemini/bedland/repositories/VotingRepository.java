package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.VotingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends JpaRepository <VotingEntity, Long> {

}
