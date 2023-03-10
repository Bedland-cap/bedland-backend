package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.internal.ManagerDto;
import com.capgemini.bedland.member.api.MemberEntity;
import com.capgemini.bedland.member.api.MemberProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
public class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberProvider memberProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Test
    void shouldReturnAllMembersWhenGettingAllMembers() {
        //given
        List<MemberDto> memberDtos = memberMapper.entities2DTOs(memberRepository.findAll());
        //when
        List<MemberDto> memberDtos1 = memberProvider.getAll();
        //then
        assertNotEquals(0, memberDtos.size());
        assertEquals(memberDtos.size(), memberDtos1.size());
        assertEquals(memberDtos, memberDtos1);
    }

    @Test
    void shouldFindMemberByIdWhenGettingMemberById() {
        //given
        MemberDto memberDto = memberMapper.entity2Dto(memberRepository.findAll().get(0));
        //when
        MemberDto foundMember = memberProvider.getById(memberDto.getId());
        //then
        assertNotEquals(null, foundMember.getId());
        assertNotEquals(null, foundMember);
        assertEquals(foundMember, memberDto);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingMemberByIdAndMemberIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> memberProvider.getById(sampleID));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingMemberByIdGivenIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> memberProvider.getById(sampleID));
    }

    @Test
    void shouldCreateMemberWhenCreatingMember() {
        //given
        MemberDto newMember = MemberDto.builder().login("jwick").password("password123").name("John").lastName("Wick").email("jwick@gmail.com").phoneNumber("666666666").flatId(1L).isOwner(false).build();
        List<MemberEntity> membersBeforeSavingNewOne = memberRepository.findAll();
        //when
        MemberDto createdMember = memberService.create(newMember);
        List<MemberEntity> membersAfterSavingOne = memberRepository.findAll();
        //then
        assertNotEquals(membersBeforeSavingNewOne.size(), membersAfterSavingOne.size());
        assertEquals(membersBeforeSavingNewOne.size() + 1, membersAfterSavingOne.size());
        assertTrue(memberRepository.existsById(createdMember.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingMemberWhoHasID() {
        //given
        MemberDto newMember = MemberDto.builder().login("jwick").password("password123").name("John").lastName("Wick").email("jwick@gmail.com").phoneNumber("666666666").flatId(1L).isOwner(false).build();
        newMember.setId(9999L);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> memberService.create(newMember));
    }

    @Test
    void shouldDeleteMemberWhenDeletingMember() {
        //given
        List<MemberDto> membersBeforeDeletingOne = memberProvider.getAll();
        MemberDto memberToDeleteDto =memberProvider.getAll().get(0);
        Long id = memberToDeleteDto.getId();
        //when
        memberService.delete(id);
        List<MemberEntity>memberAfterDeletingOne = memberRepository.findAll();
        //then
        assertFalse(memberRepository.existsById(id));
        assertNotEquals(membersBeforeDeletingOne.size(),memberAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingMemberWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> memberService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingMemberWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.delete(null));
    }

    @Test
    void shouldUpdateMemberWhenUpdatingMember() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll().get(0);
        String oldName = memberToUpdate.getName();
        String newName = "John";
        memberToUpdate.setName(newName);
        //when
        memberService.update(memberToUpdate);
        MemberDto updatedMember = memberProvider.getById(memberToUpdate.getId());
        //then
        assertNotEquals(oldName, newName);
        assertEquals(memberToUpdate.getId(), updatedMember.getId());
        assertEquals(newName, updatedMember.getName());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingMemberWithNullID() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll().get(0);
        memberToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> memberService.update(memberToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingMemberWithIDNotPresentInDB() {
        //given
        MemberDto memberToUpdate = memberProvider.getAll().get(0);
        memberToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> memberService.update(memberToUpdate));
    }

}
