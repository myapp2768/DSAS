package com.dsas.repository;

import com.dsas.entity.MaterialOutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialOutRecordRepository extends JpaRepository<MaterialOutRecord, Long> {
}





