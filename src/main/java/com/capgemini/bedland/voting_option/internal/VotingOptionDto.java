package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VotingOptionDto extends AbstractDto {

    private Long votingId;
    private String title;
    private String description;

}
