package com.devnetwork.infrastructure.persistence.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataConnectionRepository extends JpaRepository<ConnectionJpaEntity, UUID> {

    @Query("""
            select c from ConnectionJpaEntity c
            where (c.requesterId = :a and c.addresseeId = :b)
               or (c.requesterId = :b and c.addresseeId = :a)
            """)
    Optional<ConnectionJpaEntity> findBetween(@Param("a") UUID userId, @Param("b") UUID otherUserId);

    @Query("select c from ConnectionJpaEntity c where c.requesterId = :userId or c.addresseeId = :userId")
    List<ConnectionJpaEntity> findAllInvolving(@Param("userId") UUID userId);
}
