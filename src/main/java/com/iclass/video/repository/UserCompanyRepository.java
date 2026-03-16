package com.iclass.video.repository;

import com.iclass.video.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Integer> {

    @Query("SELECT uc FROM UserCompany uc " +
            "JOIN FETCH uc.user u " +
            "WHERE uc.company.id = :companyId")
    List<UserCompany> findByCompanyIdWithUsers(Integer companyId);

    List<UserCompany> findByUser_Id(Integer userId);
    List<UserCompany> findByCompanyId(Integer companyId);
    List<UserCompany> findByCompany_Id(Integer companyId);
    Optional<UserCompany> findFirstByUser_Id(Integer userId);
    void deleteByUser_Id(Integer userId);
}
