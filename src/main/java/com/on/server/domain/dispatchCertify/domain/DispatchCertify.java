package com.on.server.domain.dispatchCertify.domain;

import com.on.server.domain.user.domain.DispatchType;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dispatch_certify")
public class DispatchCertify extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "dispatch_type")
    private DispatchType dispatchType;

    @Column(name = "dispatched_university")
    private String dispatchedUniversity;

    @Column(name = "university_url")
    private String universityUrl;

    @Column(name = "country")
    private String country;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "uuid_file_id_list")
    private List<UuidFile> uuidFileList;

    @Enumerated(EnumType.STRING)
    @Column(name = "permit_status")
    private PermitStatus permitStatus;

    public void setPermitStatus(PermitStatus permitStatus) {
        this.permitStatus = permitStatus;
    }

}
