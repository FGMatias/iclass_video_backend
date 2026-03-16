package com.iclass.video.repository;

import com.iclass.video.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Boolean existsByName(String name);
    List<Branch> findByCompanyId(Integer companyId);
}
