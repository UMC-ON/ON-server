package com.on.server.domain.dispatchCertify.persentation;

import com.on.server.domain.dispatchCertify.application.DispatchCertifyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "교환/파견교 인증", description = "DispatchCertify API, 교환/파견교 인증 관련 API")
public class DispatchCertifyController {

    private final DispatchCertifyService dispatchCertifyService;
}
