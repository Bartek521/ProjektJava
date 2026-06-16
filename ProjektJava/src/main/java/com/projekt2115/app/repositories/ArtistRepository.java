package com.projekt2115.app.repositories;

import com.projekt2115.app.models.Artist;
import com.projekt2115.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Artist findByUser(User user);
}
