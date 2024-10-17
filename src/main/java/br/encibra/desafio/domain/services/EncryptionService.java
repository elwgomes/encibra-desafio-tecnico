package br.encibra.desafio.domain.services;

import org.springframework.stereotype.Service;

import br.encibra.desafio.domain.util.AESUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EncryptionService {

	private final AESUtil aesUtil;

	public String encrypt(String data) throws Exception {
		aesUtil.init();
		return aesUtil.encrypt(data);
	}

	public String decrypt(String encryptedData) throws Exception {
		aesUtil.init();
		return aesUtil.decrypt(encryptedData);
	}

}
