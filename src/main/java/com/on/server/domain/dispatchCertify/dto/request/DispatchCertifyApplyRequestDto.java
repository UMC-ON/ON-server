package com.on.server.domain.dispatchCertify.dto.request;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.user.domain.DispatchType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class DispatchCertifyApplyRequestDto {

    @NotBlank(message = "파견/교환 여부는 필수 입력 값입니다.")
    private DispatchType dispatchType;

    @NotBlank(message = "파견 대학교는 필수 입력 값입니다.")
    private String dispatchedUniversity;

    @URL(message = "URL 형식이 아닙니다.")
    private String universityUrl;

    @NotBlank(message = "국가는 필수 입력 값입니다.")
    private String country;

}
