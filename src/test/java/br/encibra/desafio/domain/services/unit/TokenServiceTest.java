package br.encibra.desafio.domain.services.unit;

import br.encibra.desafio.domain.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "testSecret");
    }

    @Test
    void testGenerateJwtToken_Success() {
        String token = tokenService.generateJwtToken(1L);
        assertNotNull(token, "Token should not be null");
    }

    @Test
    void testGenerateJwtToken_ThrowsExceptionForInvalidSecret() {
        ReflectionTestUtils.setField(tokenService, "secret", "");
        assertThrows(RuntimeException.class, () -> tokenService.generateJwtToken(1L),
                "Should throw exception for invalid secret");
    }

    @Test
    void testValidateJwtToken_Success() {
        String token = tokenService.generateJwtToken(1L);
        String subject = tokenService.validateJwtToken(token);
        assertEquals("1", subject, "The subject should match the userId");
    }

    @Test
    void testValidateJwtToken_ThrowsExceptionForInvalidToken() {
        assertThrows(RuntimeException.class, () -> tokenService.validateJwtToken("invalidToken"),
                "Should throw exception for invalid token");
    }

    @Test
    void testExtractTokenFromRequest_WithBearerToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        String token = tokenService.extractTokenFromRequest(request);
        assertEquals("validToken", token, "Token should be extracted correctly");
    }

    @Test
    void testExtractTokenFromRequest_NoBearerToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = tokenService.extractTokenFromRequest(request);
        assertNull(token, "Token should be null when no Authorization header is provided");
    }

    @Test
    void testGenerateExpiration() {
        Instant expiration = tokenService.generateExpiration();
        Instant now = Instant.now();
        assertTrue(expiration.isAfter(now), "Expiration should be after current time");
        assertTrue(expiration.isBefore(now.plus(Duration.ofHours(3))), "Expiration should be within 2 hours from now");
    }

}
