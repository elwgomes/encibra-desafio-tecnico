package br.encibra.desafio.domain.services.unit;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.PasswordRepository;
import br.encibra.desafio.domain.services.EncryptionService;
import br.encibra.desafio.domain.services.PasswordService;
import br.encibra.desafio.domain.services.TokenService;
import br.encibra.desafio.domain.services.UserService;
import br.encibra.desafio.exceptions.DatabaseException;
import br.encibra.desafio.exceptions.PasswordLimitExceededException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordServiceTest {

    @InjectMocks
    private PasswordService passwordService;

    @Mock
    private PasswordRepository passwordRepository;

    @Mock
    private PasswordHttpMapper mapper;

    @Mock
    private UserService userService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private TokenService tokenService;

    private PasswordHttpRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new PasswordHttpRequest();
        request.setValor("password123");
        request.setDescription("Test password");
        request.setTags(Collections.singletonList("test").toString());

        user = new User();
        user.setId(1L);
        user.setPasswords(new ArrayList<>());
    }

    @Test
    void testAddPasswordToUser_Success() throws Exception {
        PasswordHttpRequest request = new PasswordHttpRequest();
        request.setValor("rawPassword");

        String token = "validToken";
        User user = new User();
        user.setId(1L);

        when(tokenService.validateJwtToken(token)).thenReturn("1");
        when(userService.findById(1L)).thenReturn(user);
        when(encryptionService.encrypt("rawPassword")).thenReturn("hashedPassword");

        Password password = new Password();
        password.setValor("hashedPassword");
        when(mapper.toDomain(request, user)).thenReturn(password);

        user.setPasswords(new ArrayList<>());

        Password savedPassword = passwordService.addPasswordToUser(request, token);

        assertNotNull(savedPassword);
        assertEquals("hashedPassword", savedPassword.getValor());
        assertEquals(1, user.getPasswords().size());
    }

    @Test
    void testAddPasswordToUser_TokenValidationFails() {
        String token = "invalidToken";

        when(tokenService.validateJwtToken(token)).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () ->
                passwordService.addPasswordToUser(request, token)
        );
    }

    @Test
    void testUpdate_PasswordNotFound() {
        Long passwordId = 1L;
        String token = "validToken";

        when(tokenService.validateJwtToken(token)).thenReturn("1");
        when(passwordRepository.findById(passwordId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                passwordService.update(passwordId, request, token)
        );
    }

    @Test
    void testDelete_Success() {
        String token = "validToken";
        Long passwordId = 1L;
        Long userId = 1L;

        when(tokenService.validateJwtToken(token)).thenReturn(String.valueOf(userId));

        User user = new User(userId, "Test User", new ArrayList<>());

        Password password = new Password();
        password.setId(passwordId);
        password.setUser(user);

        when(passwordRepository.findById(passwordId)).thenReturn(Optional.of(password));

        doNothing().when(passwordRepository).deleteById(passwordId);

        assertDoesNotThrow(() -> {
            passwordService.delete(passwordId, token);
        });

        verify(passwordRepository, times(1)).deleteById(passwordId);
    }


    @Test
    void testDelete_PasswordNotFound() {
        Long passwordId = 1L;
        String token = "validToken";

        when(tokenService.validateJwtToken(token)).thenReturn("1");
        when(passwordRepository.findById(passwordId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                passwordService.delete(passwordId, token)
        );
    }

    @Test
    void testDelete_DataIntegrityViolation() {
        String token = "validToken";
        Long passwordId = 1L;
        Long userId = 1L;

        when(tokenService.validateJwtToken(token)).thenReturn(String.valueOf(userId));

        Password password = new Password();
        password.setId(passwordId);

        User user = new User(userId, "Test User", new ArrayList<>());
        password.setUser(user); // Certifique-se de que o usuário associado é o mesmo

        when(passwordRepository.findById(passwordId)).thenReturn(Optional.of(password));

        doThrow(new DataIntegrityViolationException("Integrity violation"))
                .when(passwordRepository).deleteById(passwordId);

        DatabaseException thrown = assertThrows(DatabaseException.class, () -> {
            passwordService.delete(passwordId, token);
        });

        assertEquals("Integrity violation", thrown.getMessage());
    }

    @Test
    void testFindAll_Success() {
        String token = "validToken";
        when(tokenService.validateJwtToken(token)).thenReturn("1");
        when(passwordRepository.findAllByUserId(1L)).thenReturn(new ArrayList<>());

        List<Password> passwords = passwordService.findAll(token);

        assertNotNull(passwords);
        assertTrue(passwords.isEmpty());
        verify(passwordRepository).findAllByUserId(1L);
    }

    @Test
    void testValidatePasswordLimit_Success() {
        List<Password> passwords = new ArrayList<>(Collections.nCopies(19, new Password()));
        assertDoesNotThrow(() ->
                passwordService.validatePasswordLimit(passwords, 20)
        );
    }

    @Test
    void testValidatePasswordLimit_ExceedsLimit() {
        List<Password> passwords = new ArrayList<>(Collections.nCopies(20, new Password()));

        assertThrows(PasswordLimitExceededException.class, () ->
                passwordService.validatePasswordLimit(passwords, 20)
        );
    }

}
