package com.iclass.video.repository;

import com.iclass.video.entity.UserBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBranchRepository extends JpaRepository<UserBranch, Integer> {
    @Query("SELECT ub FROM UserBranch ub " +
            "JOIN FETCH ub.user u " +
            "WHERE ub.branch.id = :branchId")
    List<UserBranch> findByBranchIdWithUsers(Integer branchId);

    List<UserBranch> findByUser_Id(Integer userId);
    List<UserBranch> findByBranch_Id(Integer branchId);
    Optional<UserBranch> findFirstByUser_Id(Integer userId);
}
