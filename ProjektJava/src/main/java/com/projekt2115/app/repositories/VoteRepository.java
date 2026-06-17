package com.projekt2115.app.repositories;

import com.projekt2115.app.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    boolean existsByUserIdAndPollId(Long userId, Long pollId);
}
