package com.odk.basemanager.deal.user;

import com.odk.basedomain.domain.UserDomain;
import com.odk.baseutil.dto.UserRegisterDTO;
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
public class UserRegisterManager {

    private UserDomain userDomain;

    public Long registerUser(UserRegisterDTO userRegisterDTO) {
        return userDomain.registerUser(userRegisterDTO);
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }
}
