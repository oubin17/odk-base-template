package com.odk.basedomain.idgenerate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.odk.base.idgenerator.SnowflakeIdUtil;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * CustomIDGenerator
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/27
 */
public class CustomIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws MappingException {
        //这里如果Object中的id不为空，则直接使用object中的id，实现逻辑待优化
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(object));
        return jsonObject.get("id") == null ? String.valueOf(SnowflakeIdUtil.nextId()) : jsonObject.getString("id") ;
    }
}
