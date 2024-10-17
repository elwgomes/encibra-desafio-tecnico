package br.encibra.desafio.domain.services.integration;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.services.EncryptionService;
import br.encibra.desafio.domain.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void testSaveAndDecryptUserPasswords() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        Password password = new Password();
        String plainPassword = "mySecretPassword";
        String encryptedPassword = encryptionService.encrypt(plainPassword);
        password.setValor(encryptedPassword);
        password.setUser(user);
        user.setPasswords(List.of(password));
        User savedUser = userService.save(user);
        assertNotNull(savedUser.getId());
        assertNotEquals(plainPassword, savedUser.getPasswords().get(0).getValor());
        User decryptedUser = userService.findByIdWithDecrypt(savedUser.getId());
        assertEquals(plainPassword, decryptedUser.getPasswords().get(0).getValor());
    }

}
