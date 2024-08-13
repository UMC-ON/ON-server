package com.on.server.domain.dispatchCertify.domain.repository;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchCertifyRepository extends JpaRepository<Long, DispatchCertify> {
}
