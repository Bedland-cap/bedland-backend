package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.VotingOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOptionEntity, Long> {

}
