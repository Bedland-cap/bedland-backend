package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {

}
