package com.placement.app.repository;

import com.placement.app.entity.CompanyDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyDriveRepository extends JpaRepository<CompanyDrive, Long> {
    List<CompanyDrive> findByCreatedById(Long createdById);
}
