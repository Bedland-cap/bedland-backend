package com.capgemini.bedland.voting.internal.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VotingDetailsDto {

   private String votingOptionTitle;
   private int amountOfResponses;
}
