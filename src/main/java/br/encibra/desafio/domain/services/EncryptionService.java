package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.util.AESUtil;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    public String encrypt(String data) throws Exception {
        AESUtil.init();
        return AESUtil.encrypt(data);
    }

    public String decrypt(String encryptedData) throws Exception {
        AESUtil.init();
        return AESUtil.decrypt(encryptedData);
    }

}
