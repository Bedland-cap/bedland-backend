package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.flat.api.FlatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {

}
