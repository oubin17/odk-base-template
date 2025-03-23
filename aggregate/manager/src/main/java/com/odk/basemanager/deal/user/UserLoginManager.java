package com.odk.basemanager.deal.user;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.UserDomain;
import com.odk.basedomain.dataobject.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserLoginManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserLoginManager {

    private UserBaseRepository baseRepository;

    private UserDomain userDomain;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    public UserEntity userLogin(UserLoginDTO userLoginDTO) {
        return this.userDomain.userLogin(userLoginDTO);
    }

    public Boolean userLogout() {
        Optional<UserBaseDO> byUserId = baseRepository.findById(SessionContext.getLoginIdWithCheck());
        AssertUtil.isTrue(byUserId.isPresent(), BizErrorCode.USER_NOT_EXIST, "用户ID不存在");
        SessionContext.logOut();
        return true;
    }


    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }
}
