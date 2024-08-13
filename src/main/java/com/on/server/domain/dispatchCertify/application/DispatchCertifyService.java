package com.on.server.domain.dispatchCertify.application;

import com.on.server.domain.dispatchCertify.domain.repository.DispatchCertifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchCertifyService {

    private final DispatchCertifyRepository dispatchCertifyRepository;
}
