package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.voting_response.api.VotingResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface VotingResponseRepository extends JpaRepository<VotingResponseEntity, Long> {

}
