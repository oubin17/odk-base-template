package com.odk.basemanager.nonbusiness;

import com.odk.baseinfra.security.IEncryption;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * EncodeManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/24
 */
@Service
@AllArgsConstructor
public class EncodeManager {

    private final IEncryption encryption;

    public String encode(String rawData) {
        return encryption.rsaEncode(rawData);
    }
}
