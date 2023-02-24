package com.capgemini.bedland.incident.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.internal.FlatRepository;
import com.capgemini.bedland.incident.api.IncidentEntity;
import com.capgemini.bedland.incident.api.IncidentProvider;
import com.capgemini.bedland.incident_status.internal.IncidentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class IncidentServiceImpl implements IncidentService, IncidentProvider {

    @Autowired
    private IncidentMapper incidentMapper;
    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private IncidentStatusService incidentStatusService;

    @Override
    public List<IncidentDto> getAll() {
        return incidentMapper.entities2DTOs(incidentRepository.findAll());
    }

    @Override
    public IncidentDto getById(Long id) {
        return incidentMapper.entity2Dto(incidentRepository.findById(id)
                                                           .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public IncidentDto create(IncidentDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Incident can't be created");
        }
        IncidentEntity createIncident = incidentRepository.save(repackDtoToEntity(request));
        incidentStatusService.createByIncidentId(createIncident.getId()); //create new IncidentStatus when user created new Incident
        return incidentMapper.entity2Dto(createIncident);
    }

    @Override
    public void delete(Long id) {
        if (!incidentRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        incidentRepository.deleteById(id);
    }

    @Override
    public IncidentDto update(IncidentDto request) {
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