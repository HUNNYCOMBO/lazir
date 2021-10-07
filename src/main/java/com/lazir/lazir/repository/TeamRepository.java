package com.lazir.lazir.repository;

import java.util.List;

import com.lazir.lazir.domain.Account;
import com.lazir.lazir.domain.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryExtension{

    Team findByURL(String path);

    boolean existsByURL(String url);

    List<Team> findFirst12ByPublishedAndClosedOrderByCreateTimeDesc(boolean b, boolean c);

    List<Team> findByMembers(Account account);

    List<Team> findByWaitting(Account account);

    List<Team> findByManager(Account account);


}
