package com.dsas.repository;

import com.dsas.entity.MaterialInRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialInRecordRepository extends JpaRepository<MaterialInRecord, Long> {
}





