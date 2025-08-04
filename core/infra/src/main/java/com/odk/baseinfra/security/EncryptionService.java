package com.odk.baseinfra.security;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.security.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * EncryptionService 加解密类
 * 整体的加解密流程是：前端先通过公钥加密，将密文传给后端，后端调用 {@link IEncryption#rsaDecode(String)} 接口进行解密
 * 解密成功后，后端再通过 {@link IEncryption#bcryptEncode(String)} 接口进行二次加密，保存到数据库中
 * 特殊情况下，在公钥加密前，可以再通过 hash 加密，防止服务端拿到明文密码，但是这样会降低密码的熵值。
 * 公私钥生产环境不要存储在本地，可以使用第三方服务，如阿里云，AWS 等。
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@Slf4j
@Service
public class EncryptionService implements InitializingBean, IEncryption {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${security.public-key-path}")
    private String publicKeyPath;

    @Value("${security.private-key-path}")
    private String privateKeyPath;

    private PublicKey publicKey;

    private PrivateKey privateKey;


    @Override
    public String bcryptEncode(String rawData) {
        return bCryptPasswordEncoder.encode(rawData);
    }

    @Override
    public boolean bcryptMatches(String rawData, String encodedData) {
        return bCryptPasswordEncoder.matches(rawData, encodedData);
    }

    @Override
    public String rsaEncode(String rawData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalBlockSizeException e) {
            log.error("加密数据长度超过117字节", e);
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, "加密数据过长（最大117字节）");
        } catch (BadPaddingException e) {
            log.error("加密填充异常", e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR, "加密配置错误");
        } catch (Exception e) {
            log.error("加密系统错误", e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR, "加密失败");
        }
    }

    @Override
    public String rsaDecode(String encodedData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedData = Base64.getDecoder().decode(encodedData);
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

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    // 新增公钥加载方法
    private PublicKey loadPublicKey(String keyPath) throws Exception {
        String pemContent = new String(Files.readAllBytes(Paths.get(keyPath)));
        String publicKeyPEM = pemContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
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
        publicKey = loadPublicKey(publicKeyPath);
        privateKey = loadPrivateKey(privateKeyPath);
    }
}
