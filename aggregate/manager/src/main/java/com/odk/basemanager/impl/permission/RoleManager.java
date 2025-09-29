package com.odk.basemanager.impl.permission;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.common.CommonStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.cache.pointcut.UserCacheClean;
import com.odk.basedomain.domain.PermissionDomain;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.basedomain.model.permission.UserRoleRelDO;
import com.odk.basedomain.repository.permission.UserRoleRelRepository;
import com.odk.basedomain.repository.permission.UserRoleRepository;
import com.odk.basemanager.api.common.IEventPublish;
import com.odk.basemanager.api.permission.IRoleManager;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.entity.PermissionEntity;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PermissionManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Slf4j
@Service
public class RoleManager implements IRoleManager {

    private UserRoleRepository userRoleRepository;

    private UserRoleRelRepository relRepository;

    private UserQueryDomain userQueryDomain;

    private PermissionDomain permissionDomain;

    private IEventPublish eventPublish;

    public PermissionEntity getAllPermissions(String userId) {
        return permissionDomain.getPermissionByUserId(userId);

    }

    @Override
    public String addRole(String roleCode, String roleName) {
        UserRoleDO userRoleDO = userRoleRepository.findByRoleCodeAndTenantId(roleCode, TenantIdContext.getTenantId());
        AssertUtil.isNull(userRoleDO, BizErrorCode.PARAM_ILLEGAL, "角色码重复，添加角色失败");
        UserRoleDO addRole = new UserRoleDO();
        addRole.setRoleCode(roleCode);
        addRole.setRoleName(roleName);
        addRole.setStatus(CommonStatusEnum.NORMAL.getCode());
        UserRoleDO save = userRoleRepository.save(addRole);
//        eventPublish.publish(new UserCacheCleanEvent(save.getId(), UserCacheSceneEnum.USER_ROLE, CacheActionEnum.ADD));
        return save.getId();
    }

    @Override
    @UserCacheClean(scene = UserCacheSceneEnum.USER_ROLE)
    public Boolean deleteRole(String roleId) {
        Optional<UserRoleDO> userRoleDO = userRoleRepository.findById(roleId);
        AssertUtil.isTrue(userRoleDO.isPresent(), BizErrorCode.PARAM_ILLEGAL, "角色不存在");
        AssertUtil.equal(userRoleDO.get().getTenantId(), TenantIdContext.getTenantId(), BizErrorCode.PARAM_ILLEGAL);
        UserRoleDO updateUserRoleDO = userRoleDO.get();
        updateUserRoleDO.setStatus(CommonStatusEnum.DELETE.getCode());
        userRoleRepository.save(userRoleDO.get());
        return true;
    }

    @Override
    public List<UserRoleDTO> roleList() {
        return userRoleRepository.findByStatusAndTenantId(CommonStatusEnum.NORMAL.getCode(), TenantIdContext.getTenantId()).stream().map(userRoleDO -> {
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setId(userRoleDO.getId());
            userRoleDTO.setRoleCode(userRoleDO.getRoleCode());
            userRoleDTO.setRoleName(userRoleDO.getRoleName());
            userRoleDTO.setStatus(userRoleDO.getStatus());
            return userRoleDTO;
        }).collect(Collectors.toList());

    }

    @Override
    public String addUserRoleRela(String roleId, String userId) {
        this.userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.USER_ID).userId(userId).build());
        UserRoleRelDO userRoleRelDO = relRepository.findByUserIdAndRoleIdAndTenantId(userId, roleId, TenantIdContext.getTenantId());
        AssertUtil.isNull(userRoleRelDO, BizErrorCode.PARAM_ILLEGAL, "用户已具备该权限");

        Optional<UserRoleDO> userRoleDO = userRoleRepository.findById(roleId);
        AssertUtil.isTrue(userRoleDO.isPresent(), BizErrorCode.PARAM_ILLEGAL, "角色不存在");
        AssertUtil.equal(userRoleDO.get().getTenantId(), TenantIdContext.getTenantId(), BizErrorCode.PARAM_ILLEGAL);
        UserRoleRelDO roleRelDO = new UserRoleRelDO();
        roleRelDO.setUserId(userId);
        roleRelDO.setRoleId(roleId);
        UserRoleRelDO save = relRepository.save(roleRelDO);

        return save.getId();
    }

    @Override
    public Boolean deleteUserRoleRela(String roleId, String userId) {
        UserRoleRelDO userRoleRelDO = relRepository.findByUserIdAndRoleIdAndTenantId(userId, roleId, TenantIdContext.getTenantId());
        AssertUtil.notNull(userRoleRelDO, BizErrorCode.PARAM_ILLEGAL, "用户不具备该权限");
        relRepository.deleteById(userRoleRelDO.getId());
        return true;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setRelRepository(UserRoleRelRepository relRepository) {
        this.relRepository = relRepository;
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }

    @Autowired
    public void setPermissionDomain(PermissionDomain permissionDomain) {
        this.permissionDomain = permissionDomain;
    }

    @Autowired
    public void setEventPublish(IEventPublish eventPublish) {
        this.eventPublish = eventPublish;
    }
}
