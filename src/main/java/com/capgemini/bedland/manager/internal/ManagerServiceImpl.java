package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.api.ManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ManagerServiceImpl implements ManagerService, ManagerProvider {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public List<ManagerDto> getAll() {
        return managerMapper.entities2DTOs(managerRepository.findAll());
    }

    @Override
    public ManagerDto getById(final Long id) {
        return managerMapper.entity2Dto(managerRepository.findById(id)
                                                         .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public ManagerDto create(final ManagerDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Manager can't be created");
        }
        ManagerEntity newManager = managerMapper.dto2Entity(request);
        ManagerEntity createdManager = managerRepository.save(newManager);
        return managerMapper.entity2Dto(createdManager);
    }

    @Override
    public ManagerDto update(final ManagerDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!managerRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        ManagerEntity updatedManager = managerMapper.dto2Entity(request);
        return managerMapper.entity2Dto(managerRepository.save(updatedManager));
    }

    @Override
    public void delete(final Long id) {
        if (!managerRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        managerRepository.deleteById(id);
    }

}
