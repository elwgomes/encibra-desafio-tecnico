package br.encibra.desafio.domain.services.unit;
import br.encibra.desafio.domain.services.EncryptionService;
import br.encibra.desafio.domain.util.AESUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    @Mock
    private AESUtil aesUtil;

    @InjectMocks
    private EncryptionService encryptionService;

    @Test
    void testEncryptSuccess() throws Exception {
        String data = "plainText";
        String encryptedData = "encryptedText";

        doNothing().when(aesUtil).init();
        when(aesUtil.encrypt(data)).thenReturn(encryptedData);

        String result = encryptionService.encrypt(data);

        assertEquals(encryptedData, result);
        verify(aesUtil, times(1)).init();
        verify(aesUtil, times(1)).encrypt(data);
    }

    @Test
    void testDecryptSuccess() throws Exception {
        String encryptedData = "encryptedText";
        String decryptedData = "plainText";

        doNothing().when(aesUtil).init();
        when(aesUtil.decrypt(encryptedData)).thenReturn(decryptedData);
        String result = encryptionService.decrypt(encryptedData);

        assertEquals(decryptedData, result);
        verify(aesUtil, times(1)).init();
        verify(aesUtil, times(1)).decrypt(encryptedData);
    }

    @Test
    void testEncryptThrowsException() throws Exception {
        String data = "plainText";

        doNothing().when(aesUtil).init();
        when(aesUtil.encrypt(data)).thenThrow(new Exception("Encryption error"));

        Exception exception = assertThrows(Exception.class, () -> encryptionService.encrypt(data));

        assertEquals("Encryption error", exception.getMessage());
        verify(aesUtil, times(1)).init();
        verify(aesUtil, times(1)).encrypt(data);
    }

    @Test
    void testDecryptThrowsException() throws Exception {
        String encryptedData = "encryptedText";

        doNothing().when(aesUtil).init();
        when(aesUtil.decrypt(encryptedData)).thenThrow(new Exception("Decryption error"));

        Exception exception = assertThrows(Exception.class, () -> encryptionService.decrypt(encryptedData));

        assertEquals("Decryption error", exception.getMessage());
        verify(aesUtil, times(1)).init();
        verify(aesUtil, times(1)).decrypt(encryptedData);
    }
}