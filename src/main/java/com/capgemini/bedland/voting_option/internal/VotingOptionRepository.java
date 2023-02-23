package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOptionEntity, Long> {

}
