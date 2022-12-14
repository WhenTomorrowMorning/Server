package com.y2sg.wtm.domain.auth.application;

import java.security.Key;
import java.util.Date;

import com.y2sg.wtm.global.config.security.OAuth2Config;
import com.y2sg.wtm.global.config.security.token.UserPrincipal;
import com.y2sg.wtm.domain.auth.dto.TokenMapping;

import com.y2sg.wtm.global.error.DefaultAuthenticationException;
import com.y2sg.wtm.global.error.DefaultException;
import com.y2sg.wtm.global.payload.ErrorCode;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomTokenProviderService {

    @Autowired
    private OAuth2Config oAuth2Config;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public TokenMapping refreshToken(final Authentication authentication, final String refreshToken) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());

        String secretKey = oAuth2Config.getAuth().getTokenSecret();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String accessToken = Jwts.builder()
                                .setSubject(Long.toString(userPrincipal.getId()))
                                .setIssuedAt(new Date())
                                .setExpiration(accessTokenExpiresIn)
                                .signWith(key, SignatureAlgorithm.HS512)
                                .compact();

        return TokenMapping.builder()
                        .userEmail(userPrincipal.getEmail())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
    }

    public TokenMapping createToken(final Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());
        Date refreshTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getRefreshTokenExpirationMsec());

        String secretKey = oAuth2Config.getAuth().getTokenSecret();

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String accessToken = Jwts.builder()
                                .setSubject(Long.toString(userPrincipal.getId()))
                                .setIssuedAt(new Date())
                                .setExpiration(accessTokenExpiresIn)
                                .signWith(key, SignatureAlgorithm.HS512)
                                .compact();

        String refreshToken = Jwts.builder()
                                .setExpiration(refreshTokenExpiresIn)
                                .signWith(key, SignatureAlgorithm.HS512)
                                .compact();

        return TokenMapping.builder()
                    .userEmail(userPrincipal.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    public Long getUserIdFromToken(final String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationById(final String token){
        Long userId = getUserIdFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationByEmail(final String email){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }

    public Long getExpiration(final String token) {
        // accessToken ?????? ????????????
        Date expiration = Jwts.parserBuilder().setSigningKey(oAuth2Config.getAuth().getTokenSecret()).build().parseClaimsJws(token).getBody().getExpiration();
        // ?????? ??????
        Long now = new Date().getTime();
        //?????? ??????
        return (expiration.getTime() - now);
    }

    public boolean validateToken(final String token) {
        try {
            //log.info("bearerToken = {} \n oAuth2Config.getAuth()={}", token, oAuth2Config.getAuth().getTokenSecret());
            Jwts.parserBuilder().setSigningKey(oAuth2Config.getAuth().getTokenSecret()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            log.error("????????? JWT ???????????????.");
        } catch (ExpiredJwtException ex) {
            log.error("????????? JWT ???????????????.");
        } catch (UnsupportedJwtException ex) {
            log.error("???????????? ?????? JWT ???????????????.");
        } catch (IllegalArgumentException ex) {
            log.error("JWT ????????? ?????????????????????.");
        }
        return false;
    }
}
