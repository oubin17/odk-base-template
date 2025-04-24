package com.odk.basemanager.deal.nonbusiness;

import com.odk.baseinfra.security.IDecrypt;
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

    private final IDecrypt iDecrypt;

    public String encode(String rawData) {
        return iDecrypt.encrypt(rawData);
    }
}
