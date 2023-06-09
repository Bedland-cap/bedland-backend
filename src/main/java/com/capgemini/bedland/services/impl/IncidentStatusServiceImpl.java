package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.entities.IncidentEntity;
import com.capgemini.bedland.entities.IncidentStatusEntity;
import com.capgemini.bedland.enums.IncidentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.IncidentStatusMapper;
import com.capgemini.bedland.providers.IncidentStatusProvider;
import com.capgemini.bedland.repositories.IncidentRepository;
import com.capgemini.bedland.repositories.IncidentStatusRepository;
import com.capgemini.bedland.services.IncidentStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class IncidentStatusServiceImpl implements IncidentStatusService, IncidentStatusProvider {

    @Autowired
    private IncidentStatusMapper incidentStatusMapper;
    @Autowired
    private IncidentStatusRepository incidentStatusRepository;
    @Autowired
    private IncidentRepository incidentRepository;
    private final String idIsNull = "Given ID is null";

    @Override
    public List<IncidentStatusDto> getAll() {
        return incidentStatusMapper.entities2DTOs(incidentStatusRepository.findAll());
    }

    @Override
    public IncidentStatusDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        return incidentStatusMapper.entity2Dto(incidentStatusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public IncidentStatusDto create(IncidentStatusDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. IncidentStatus can't be created");
        }
        IncidentStatusEntity createIncidentStatus = incidentStatusRepository.save(repackDtoToEntity(request));
        createIncidentStatus.setIncidentStatusName(request.getIncidentStatusName());
        IncidentEntity incident = incidentRepository.findById(request.getIncidentId()).orElseThrow(() -> new NotFoundException(request.getIncidentId()));
        incident.setUpdateDate(createIncidentStatus.getCreateDate());
        incidentRepository.save(incident);
        return incidentStatusMapper.entity2Dto(createIncidentStatus);
    }

    @Override
    public void createByIncidentId(Long incidentId) {
        if (incidentId == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        IncidentStatusEntity createIncidentStatus = new IncidentStatusEntity();
        createIncidentStatus.setIncidentStatusName(IncidentStatusName.CREATED);
        createIncidentStatus.setIncidentEntity(incidentRepository.findById(incidentId)
                .orElseThrow(() -> new NotFoundException(incidentId)));
        incidentStatusRepository.save(createIncidentStatus);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        if (!incidentStatusRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        incidentStatusRepository.deleteById(id);
    }

    @Override
    public IncidentStatusDto update(IncidentStatusDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!incidentStatusRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        IncidentStatusEntity updateIncidentStatus = incidentStatusRepository.save(repackDtoToEntity(request));
        return incidentStatusMapper.entity2Dto(updateIncidentStatus);
    }

    private IncidentStatusEntity repackDtoToEntity(IncidentStatusDto dto) {
        IncidentStatusEntity entity = incidentStatusMapper.dto2Entity(dto);
        entity.setIncidentEntity(incidentRepository.findById(dto.getIncidentId())
                .orElseThrow(() -> new NotFoundException(dto.getIncidentId())));
        return entity;
    }

}
