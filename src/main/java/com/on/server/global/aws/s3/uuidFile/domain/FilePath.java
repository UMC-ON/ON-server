package com.on.server.global.aws.s3.uuidFile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilePath {
    DISPATCH_CERTIFY("dispatch_certify"),
    POST("post"),
    COMMENT("comment"),
    CHAT("chat");

    private final String path;

}
