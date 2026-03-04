package com.odk.baseservice.impl.nonbusiness.llm;

import lombok.Getter;

/**
 * ModelEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/11/28
 */
@Getter
public enum ModelEnum {

    QWEN3_32B_128K("ascend-qwen3-32b-128k");

    /**
     * 模型名称：这里只是用来描述模型名称，由于线下和线上使用的模型名称不同，所以这里不是代表实际使用的模型名称。
     */
    private final String modelName;

    ModelEnum(String modelName) {
        this.modelName = modelName;
    }

    /**
     * 获取模型
     *
     * @param modelName
     * @return
     */
    public static ModelEnum getByModelName(String modelName) {
        for (ModelEnum value : ModelEnum.values()) {
            if (value.modelName.equals(modelName)) {
                return value;
            }
        }
        return null;
    }
}
