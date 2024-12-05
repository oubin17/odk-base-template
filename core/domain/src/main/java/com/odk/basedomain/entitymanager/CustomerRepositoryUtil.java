package com.odk.basedomain.entitymanager;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

/**
 * CustomerRepositoryUtil
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/12/5
 */
public class CustomerRepositoryUtil {

    /**
     * 查询结果为空，返回null
     *
     * @param query
     * @param <T>
     * @return
     */
    public static <T> T getSingleResultSafelyByCatch(Query query) {
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 返回list，取第一条
     *
     * @param query
     * @return
     * @param <T>
     */
    public static <T> T getSingleResultSafelyByResultList(Query query) {
        List<T> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * 通过optional返回
     * @param query
     * @return
     */
    public static Optional<Object> getSingleResultSafelyByOptional(Query query) {
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
