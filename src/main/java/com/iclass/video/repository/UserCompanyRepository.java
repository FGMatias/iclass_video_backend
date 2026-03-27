package com.iclass.video.repository;

import com.iclass.video.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Integer> {
    List<UserCompany> findByCompanyId(Integer companyId);
    Optional<UserCompany> findFirstByUser_Id(Integer userId);
    Optional<UserCompany> findByUser_IdAndCompany_Id(Integer userId, Integer companyId);
    void deleteByUser_Id(Integer userId);

    @Query("SELECT uc FROM UserCompany uc " +
            "JOIN FETCH uc.company " +
            "WHERE uc.user.id IN :userIds")
    List<UserCompany> findByUserIdIn(@Param("userIds") List<Integer> userIds);
}
