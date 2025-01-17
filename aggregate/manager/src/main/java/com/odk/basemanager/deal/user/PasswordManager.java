package com.odk.basemanager.deal.user;

import com.odk.basedomain.domain.UserDomain;
import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PasswordManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
@Service
public class PasswordManager {


    private UserDomain userDomain;

    /**
     * 登录态下
     *
     * @return
     */
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        return this.userDomain.updatePassword(passwordUpdateDTO);
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }
}
