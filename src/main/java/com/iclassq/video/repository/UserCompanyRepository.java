package com.iclassq.video.repository;

import com.iclassq.video.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Integer> {

    @Query("SELECT uc FROM UserCompany uc " +
            "JOIN FETCH uc.user u " +
            "WHERE uc.company.id = :companyId")
    List<UserCompany> findByCompanyIdWithUsers(Integer companyId);

    List<UserCompany> findByUser_Id(Integer userId);
    List<UserCompany> findByCompany_Id(Integer companyId);
}
