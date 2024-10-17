package br.encibra.desafio.domain.services.unit;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.UserRepository;
import br.encibra.desafio.domain.services.EncryptionService;
import br.encibra.desafio.domain.services.UserService;
import br.encibra.desafio.exceptions.PasswordDecryptionException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User request = new User();
        request.setId(1L);

        when(userRepository.save(request)).thenReturn(request);

        User savedUser = userService.save(request);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        verify(userRepository, times(1)).save(request);
    }

    @Test
    void testFindByIdUserExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(1L);
        });

        assertEquals(1L, exception.getResourceId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdWithDecryptUserExists() throws Exception {
        User user = new User();
        user.setId(1L);
        Password password = new Password();
        password.setValor("encryptedPassword");
        user.setPasswords(List.of(password));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encryptionService.decrypt("encryptedPassword")).thenReturn("decryptedPassword");

        User decryptedUser = userService.findByIdWithDecrypt(1L);

        assertNotNull(decryptedUser);
        assertEquals(1L, decryptedUser.getId());
        assertEquals("decryptedPassword", decryptedUser.getPasswords().get(0).getValor());
        verify(encryptionService, times(1)).decrypt("encryptedPassword");
    }

    @Test
    void testDecryptUserPasswordsThrowsException() throws Exception {
        User user = new User();
        user.setId(1L);
        Password password = new Password();
        password.setValor("encryptedPassword");
        user.setPasswords(List.of(password));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(encryptionService.decrypt("encryptedPassword")).thenThrow(new RuntimeException("Decryption error"));

        PasswordDecryptionException exception = assertThrows(PasswordDecryptionException.class, () -> {
            userService.findByIdWithDecrypt(1L);
        });

        assertTrue(exception.getMessage().contains("Error decrypting password"));
        verify(encryptionService, times(1)).decrypt("encryptedPassword");
    }

    @Test
    void testFindByIdWithNoPasswordsToDecrypt() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPasswords(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findByIdWithDecrypt(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(encryptionService, never()).decrypt(anyString());
    }

}
