package com.lazir.lazir.repository;

import com.lazir.lazir.domain.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeamRepository extends JpaRepository<Team, Long>{

    Team findByURL(String path);

    boolean existsByURL(String url);

    Team findFirst9ByPublishedAndClosedOrderByCreateTimeDesc(boolean b, boolean c);

}
