package com.dsas.repository;

import com.dsas.entity.MaterialInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Long> {
}





