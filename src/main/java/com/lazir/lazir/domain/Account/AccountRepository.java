package com.lazir.lazir.domain.Account;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//생략가능 어노테이션. DAO역할을 하는 repository
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long>{

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);
}
