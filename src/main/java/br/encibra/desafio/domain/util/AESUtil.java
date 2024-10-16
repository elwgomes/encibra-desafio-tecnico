package br.encibra.desafio.domain.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AESUtil {

	private static SecretKey secretKey;
	private static int KEY_SIZE = 128;
	private static int T_LEN = 128;

	public static void init() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(KEY_SIZE);
		secretKey = generator.generateKey();
	}

	public static String encrypt(String plainText) throws Exception {
		byte[] messageInByte = plainText.getBytes();
		Cipher encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = encryptCipher.doFinal(messageInByte);
		return encode(encryptedBytes);
	}

	private static String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public static String decrypt(String encryptedText) throws Exception {
		byte[] encryptedBytes = decode(encryptedText);
		Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, encryptedBytes);
		decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
		byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
		return new String(decryptedBytes);
	}

	private static byte[] decode(String encryptedText) throws Exception {
		return Base64.getDecoder().decode(encryptedText);
	}

}
