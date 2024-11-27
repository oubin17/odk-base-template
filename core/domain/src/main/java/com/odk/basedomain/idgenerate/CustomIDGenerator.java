package com.odk.basedomain.idgenerate;

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
        return SnowflakeIdUtil.nextId();
    }
}
