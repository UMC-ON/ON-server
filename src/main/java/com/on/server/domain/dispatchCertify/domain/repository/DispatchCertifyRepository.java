package com.on.server.domain.dispatchCertify.domain.repository;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatchCertifyRepository extends JpaRepository<DispatchCertify, Long> {

    List<DispatchCertify> findAllByUser(User user);

    Page<DispatchCertify> findAllByUser(User user, Pageable pageable);

    Page<DispatchCertify> findByPermitStatus(PermitStatus permitStatus, Pageable pageable);

}
