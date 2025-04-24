package com.odk.baseinfra.security;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * RsaDecrypt
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
@Slf4j
@Service
public class RsaDecrypt implements InitializingBean, IDecrypt {

    @Value("${security.private-key-path}")
    private String privateKeyPath;

    private PrivateKey privateKey;

    @Override
    public String decrypt(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);
            return new String(cipher.doFinal(encryptedData), "UTF-8");

        } catch (IllegalBlockSizeException e) {
            log.error("解密数据长度不符合算法要求", e);
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "数据长度不符合算法要求");
        } catch (BadPaddingException e) {
            log.error("解密密钥不匹配", e);
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "解密密钥不匹配");
        } catch (IOException e) {
            log.error("密钥文件读取失败", e);
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "密钥文件读取失败");
        } catch (Exception e) {
            log.error("系统级错误", e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR, "系统级错误");
        }
    }

    private PrivateKey loadPrivateKey(String keyPath) throws Exception {
        // 1. 读取PEM文件内容
        String pemContent = new String(Files.readAllBytes(Paths.get(keyPath)));

        // 2. 清理PEM格式（自动兼容带/不带头尾标记的情况）
        String privateKeyPEM = pemContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""); // 移除所有空白字符

        // 3. Base64解码
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        // 4. 生成密钥规范
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        // 5. 创建密钥实例
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        privateKey = loadPrivateKey(privateKeyPath);
    }


}
