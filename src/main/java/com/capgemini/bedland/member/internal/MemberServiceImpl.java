package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.internal.FlatRepository;
import com.capgemini.bedland.member.api.MemberEntity;
import com.capgemini.bedland.member.api.MemberProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class MemberServiceImpl implements MemberService, MemberProvider {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private FlatRepository flatRepository;

    @Override
    public List<MemberDto> getAll() {
        return memberMapper.entities2DTOs(memberRepository.findAll());
    }

    @Override
    public MemberDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return memberMapper.entity2Dto(memberRepository.findById(id)
                                                       .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public MemberDto create(MemberDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Member can't be created");
        }
        MemberEntity createdMember = memberRepository.save(repackDtoToEntity(request));
        return memberMapper.entity2Dto(createdMember);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!memberRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        memberRepository.deleteById(id);
    }

    @Override
    public MemberDto update(MemberDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!memberRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        MemberEntity updateMember = memberRepository.save(repackDtoToEntity(request));
        return memberMapper.entity2Dto(updateMember);
    }

    private MemberEntity repackDtoToEntity(MemberDto dto) {
        MemberEntity entity = memberMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId()).get());
        return entity;
    }

}
