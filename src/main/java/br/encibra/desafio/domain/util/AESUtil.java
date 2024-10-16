package br.encibra.desafio.domain.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class AESUtil {

	private SecretKey secretKey;
	private int T_LEN = 128;

	@Value("${aes.secret-key}")
	private String base64Key;

	public void init() throws Exception {
		if (secretKey == null) {
			byte[] decodedKey = Base64.getDecoder().decode(base64Key);
			secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
			System.out.println(getSecretKeyInBase64());
		}
	}

	private String getSecretKeyInBase64() {
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	public String encrypt(String plainText) throws Exception {
		byte[] iv = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);

		Cipher encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, iv);
		encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

		byte[] encryptedBytes = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedBytes.length);
		byteBuffer.put(iv);
		byteBuffer.put(encryptedBytes);

		return Base64.getEncoder().encodeToString(byteBuffer.array());
	}

	public String decrypt(String encryptedText) throws Exception {

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

		ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedBytes);

		byte[] iv = new byte[12];
		byteBuffer.get(iv);

		byte[] cipherText = new byte[byteBuffer.remaining()];
		byteBuffer.get(cipherText);

		Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, iv);
		decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

		byte[] decryptedBytes = decryptCipher.doFinal(cipherText);

		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	private String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decode(String encryptedText) throws Exception {
		return Base64.getDecoder().decode(encryptedText);
	}

}
