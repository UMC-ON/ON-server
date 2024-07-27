package com.on.server.global.login.application;

import capstone.safeat.member.application.MemberReader;
import capstone.safeat.member.application.MemberUpdater;
import capstone.safeat.member.domain.Member;
import capstone.safeat.oauth.application.OAuthMemberClientComposite;
import capstone.safeat.oauth.domain.OAuthMemberInfo;
import capstone.safeat.oauth.domain.OAuthServerType;
import com.on.server.global.login.dto.LoginResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final OAuthMemberClientComposite oauthMemberClientComposite;
  private final MemberUpdater memberUpdater;
  private final MemberReader memberReader;
  private final JwtProvider jwtProvider;

  @Transactional
  public LoginResponse createToken(final String oauthType, final String code) {
    final OAuthServerType oauthServerType = OAuthServerType.fromName(oauthType);
    final OAuthMemberInfo oauthMemberInfo = oauthMemberClientComposite
        .fetchMemberInfo(oauthServerType, code);

    final Member member = memberReader.readBy(oauthMemberInfo)
        .orElseGet(() -> memberUpdater.registerNewMember(oauthMemberInfo));

    return new LoginResponse(
        member.getId(),
        jwtProvider.createAccessTokenWith(member.getId()),
        member.isRegistered()
    );
  }
}
