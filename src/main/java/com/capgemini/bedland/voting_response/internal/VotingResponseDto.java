package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VotingResponseDto extends AbstractDto {

    private Long flatId;
    private Long votingOptionId;

}
