package br.encibra.desafio.domain.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AESUtil {

	private static final int T_LEN = 128;
	private static final int IV_LENGTH = 12;

	private SecretKey secretKey;

	@Value("${aes.secret-key}")
	private String base64Key;

	public void init() throws Exception {
		if (secretKey == null) {
			byte[] decodedKey = Base64.getDecoder().decode(base64Key);
			secretKey = new SecretKeySpec(decodedKey, "AES");
			log.info("AES secret key initialized successfully.");
		} else {
			log.warn("AES secret key has already been initialized.");
		}
	}

	public String encrypt(String plainText) throws Exception {
		log.info("Starting encryption process for plaintext.");
		byte[] iv = generateIV();

		Cipher encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, iv);
		encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

		byte[] encryptedBytes = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return combineIVAndCiphertext(iv, encryptedBytes);
	}

	public String decrypt(String encryptedText) throws Exception {
		log.info("Starting decryption process for encrypted text.");
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
		ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedBytes);

		byte[] iv = new byte[IV_LENGTH];
		byteBuffer.get(iv);

		byte[] cipherText = new byte[byteBuffer.remaining()];
		byteBuffer.get(cipherText);

		Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, iv);
		decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

		byte[] decryptedBytes = decryptCipher.doFinal(cipherText);
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	private byte[] generateIV() {
		byte[] iv = new byte[IV_LENGTH];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return iv;
	}

	private String combineIVAndCiphertext(byte[] iv, byte[] encryptedBytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedBytes.length);
		byteBuffer.put(iv);
		byteBuffer.put(encryptedBytes);
		String encryptedText = Base64.getEncoder().encodeToString(byteBuffer.array());
		log.info("Encryption successful. Resulting encrypted text length: {}", encryptedText.length());
		return encryptedText;
	}
}
