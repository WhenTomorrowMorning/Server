package com.y2sg.wtm.domain.auth.application;

import java.util.Optional;

import com.y2sg.wtm.global.DefaultAssert;
import com.y2sg.wtm.global.config.security.auth.OAuth2UserInfo;
import com.y2sg.wtm.global.config.security.auth.OAuth2UserInfoFactory;
import com.y2sg.wtm.global.config.security.token.UserPrincipal;
import com.y2sg.wtm.domain.user.domain.Provider;
import com.y2sg.wtm.domain.user.domain.Role;
import com.y2sg.wtm.domain.user.domain.User;
import com.y2sg.wtm.domain.user.domain.repository.UserRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomDefaultOAuth2UserService extends DefaultOAuth2UserService{
    
    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception e) {
            DefaultAssert.isAuthentication(e.getMessage());
        }
        return null;
    }

    private OAuth2User processOAuth2User(final OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        DefaultAssert.isAuthentication(!oAuth2UserInfo.getEmail().isEmpty());
        
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            DefaultAssert.isAuthentication(user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())));
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                    .provider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                    .providerId(oAuth2UserInfo.getId())
                    .name(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .imageUrl(oAuth2UserInfo.getImageUrl())
                    .role(Role.USER)
                    .build();
        
        return userRepository.save(user);
    }

    private User updateExistingUser(final User user, final OAuth2UserInfo oAuth2UserInfo) {

        user.updateName(oAuth2UserInfo.getName());
        user.updateImageUrl(oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }
}
