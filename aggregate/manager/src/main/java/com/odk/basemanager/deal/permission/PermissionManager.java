package com.odk.basemanager.deal.permission;

import com.odk.base.enums.common.CommonStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.PermissionDomain;
import com.odk.basedomain.entity.PermissionEntity;
import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.basedomain.model.permission.UserRoleRelDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.permission.PermissionRepository;
import com.odk.basedomain.repository.permission.UserRoleRelRepository;
import com.odk.basedomain.repository.permission.UserRoleRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * PermissionManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Slf4j
@Service
public class PermissionManager {

    private UserBaseRepository userBaseRepository;

    private UserRoleRepository userRoleRepository;

    private PermissionRepository permissionRepository;

    private UserRoleRelRepository relRepository;

    private PermissionDomain permissionDomain;

    /**
     * 查找用户权限
     *
     * @param userId
     * @return
     */
    public PermissionEntity getAllPermissions(Long userId) {
        return permissionDomain.getPermissionByUserId(userId);
    }

    /**
     * 添加角色
     *
     * @param roleCode
     * @param roleName
     * @return
     */
    public Long addRole(String roleCode, String roleName) {
        UserRoleDO userRoleDO = userRoleRepository.findByRoleCode(roleCode);
        AssertUtil.isNull(userRoleDO, BizErrorCode.PARAM_ILLEGAL, "角色码重复，添加角色失败");
        UserRoleDO addRole = new UserRoleDO();
        addRole.setRoleCode(roleCode);
        addRole.setRoleName(roleName);
        addRole.setStatus(CommonStatusEnum.NORMAL.getCode());
        UserRoleDO save = userRoleRepository.save(addRole);
        return save.getId();
    }

    public Long addUserRoleRela(Long roleId, Long userId) {
        UserRoleRelDO userRoleRelDO = relRepository.findByUserIdAndRoleId(userId, roleId);
        AssertUtil.isNull(userRoleRelDO, BizErrorCode.PARAM_ILLEGAL, "用户已具备该权限");

        Optional<UserRoleDO> userRoleDO = userRoleRepository.findById(roleId);
        AssertUtil.isTrue(userRoleDO.isPresent(), BizErrorCode.PARAM_ILLEGAL, "角色不存在");
        Optional<UserBaseDO> userBaseDO = userBaseRepository.findById(userId);
        AssertUtil.isTrue(userBaseDO.isPresent(), BizErrorCode.PARAM_ILLEGAL, "用户不存在");

        UserRoleRelDO roleRelDO = new UserRoleRelDO();
        roleRelDO.setUserId(userId);
        roleRelDO.setRoleId(roleId);
        UserRoleRelDO save = relRepository.save(roleRelDO);
        return save.getId();

    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setRelRepository(UserRoleRelRepository relRepository) {
        this.relRepository = relRepository;
    }

    @Autowired
    public void setPermissionDomain(PermissionDomain permissionDomain) {
        this.permissionDomain = permissionDomain;
    }
}
