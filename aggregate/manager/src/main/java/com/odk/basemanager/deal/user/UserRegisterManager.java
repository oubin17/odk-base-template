package com.odk.basemanager.deal.user;

import com.odk.basedomain.domain.UserDomain;
import com.odk.baseinfra.security.IDecrypt;
import com.odk.baseinfra.security.IEncrypt;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserRegisterManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserRegisterManager {

    private UserDomain userDomain;

    private IDecrypt iDecrypt;

    private IEncrypt iEncrypt;

    /**
     * 这里校验密码是否通过公钥加密
     *
     * @param userRegisterDTO
     * @return
     */
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        String password = userRegisterDTO.getIdentifyValue();
        String decrypt = iDecrypt.decrypt(password);
        userRegisterDTO.setIdentifyValue(iEncrypt.encrypt(decrypt));
        return userDomain.registerUser(userRegisterDTO);
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }
}
