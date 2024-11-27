package com.odk.basedomain.idgenerate;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * CustomIDGenerator
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/27
 */
public class CustomIDGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws MappingException {
        Serializable id = UUID.randomUUID().toString();
        if (id != null) {
            return id;
        }
        return (Serializable) super.generate(session, object);
    }
}
