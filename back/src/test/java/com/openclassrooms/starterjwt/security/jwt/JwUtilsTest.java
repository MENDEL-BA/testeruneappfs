package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class JwUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private final String email = "user@yopmail.com";
    private final String firstName = "user";
    private final String lastName = "userlast";
    private final String password = "passer";
    private User user;
    private UserDetailsImpl userDetailsImpl;
    //@Value("${oc.app.jwtSecret}")
    private final static String JWT_SECRET ="openclassrooms";
    // @Value("${oc.app.jwtExpirationMs}")
    private static int JWT_EXPIRATION_MS = 86400000;

    @BeforeEach
    void start() {
        user = new User(1L, email, lastName, firstName, password, true, LocalDateTime.now(),
                LocalDateTime.now());
        userDetailsImpl = new UserDetailsImpl(1L, email, firstName, lastName, true, password);
    }

    @Test
    @DisplayName("Recuperation du username par le token")
    void testTetUserNameFromJwtTokenTest(){
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);
        String name = jwtUtils.getUserNameFromJwtToken(token);

        Assertions.assertEquals(userDetailsImpl.getUsername(), name);
    }

    @Test
    @DisplayName("Test validation token valide")
    void testValidationJwtToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        String token = Jwts.builder().setSubject(userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();

        Assertions.assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Test validation token corrompu")
    void testValidateTokenCorompuTest() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        Assertions.assertFalse(jwtUtils.validateJwtToken("TokenCorompu"));
    }

    @Test
    @DisplayName("Test validation token expire")
    void testValidateJwtTokenExpired() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);

        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);

        Assertions.assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Test validation token vide")
    void testValidateVideJwtTokenTest() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);

        Assertions.assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    @DisplayName("Test validation token avec une signature non valid")
    void testValidateJwtTokenNotSignatureValidTest() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        String token = Jwts.builder().setSubject(userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, "NaNNaNNaNaNNaNNaNNaNNaN").compact();

        Assertions.assertFalse(jwtUtils.validateJwtToken(token));
    }

}
