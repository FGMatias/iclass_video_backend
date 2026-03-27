package com.iclass.video.repository;

import com.iclass.video.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role r WHERE r.id = :roleId")
    List<User> findByRolId(Integer roleId);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.role r " +
            "JOIN UserBranch ub ON ub.user = u " +
            "JOIN ub.branch b " +
            "WHERE r.id = :roleId " +
            "AND b.company.id = :companyId")
    List<User> findByRolIdAndCompanyId(
            @Param("roleId") Integer roleId,
            @Param("companyId") Integer companyId
    );
}
