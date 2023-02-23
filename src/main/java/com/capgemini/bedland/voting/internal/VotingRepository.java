package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.voting.api.VotingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends JpaRepository <VotingEntity, Long> {

}
