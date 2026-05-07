package com.veciapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.FamilyMember;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

    List<FamilyMember> findByOwnerIdOrderByCreatedAtAsc(Long ownerId);

    List<FamilyMember> findByMemberIdOrderByCreatedAtAsc(Long memberId);

    long countByOwnerId(Long ownerId);

    boolean existsByOwnerIdAndMemberId(Long ownerId, Long memberId);
}
