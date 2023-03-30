package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.IncidentDto;
import com.capgemini.bedland.entities.IncidentEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.IncidentMapper;
import com.capgemini.bedland.providers.IncidentProvider;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.IncidentRepository;
import com.capgemini.bedland.services.IncidentService;
import com.capgemini.bedland.services.IncidentStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class IncidentServiceImpl implements IncidentService, IncidentProvider {

    @Autowired
    private IncidentMapper incidentMapper;
    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private IncidentStatusService incidentStatusService;
    private final String idIsNull = "Given ID is null";

    @Override
    public List<IncidentDto> getAll() {
        return incidentMapper.entities2DTOs(incidentRepository.findAll());
    }

    @Override
    public IncidentDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        return incidentMapper.entity2Dto(incidentRepository.findById(id)
                                                           .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public IncidentDto create(IncidentDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Incident can't be created");
        }
        IncidentEntity createIncident = incidentRepository.save(repackDtoToEntity(request));
        incidentStatusService.createByIncidentId(createIncident.getId());
        return incidentMapper.entity2Dto(createIncident);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        if (!incidentRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        incidentRepository.deleteById(id);
    }

    @Override
    public IncidentDto update(IncidentDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!incidentRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        IncidentEntity updateIncident = incidentRepository.save(repackDtoToEntity(request));
        return incidentMapper.entity2Dto(updateIncident);
    }

    private IncidentEntity repackDtoToEntity(IncidentDto dto) {
        IncidentEntity entity = incidentMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                                           .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        return entity;
    }

}