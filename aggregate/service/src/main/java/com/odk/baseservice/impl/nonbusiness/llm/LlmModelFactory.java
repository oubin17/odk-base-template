package com.odk.baseservice.impl.nonbusiness.llm;

import com.odk.base.exception.BizException;
import com.odk.baseservice.impl.nonbusiness.llm.adapter.ModelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ModelFactory
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/11/28
 */
@Component
public class LlmModelFactory {

    private final Map<ModelEnum, ModelAdapter> adapters = new ConcurrentHashMap<>();

    @Autowired
    public LlmModelFactory(List<ModelAdapter> adapterList) {
        for (ModelAdapter adapter : adapterList) {
            adapters.put(adapter.getType(), adapter);
        }
    }

    /**
     * 获取模型处理类
     *
     * @param modelName
     * @return
     */
    public ModelAdapter getAdapter(ModelEnum modelName) {
        ModelAdapter adapter = adapters.get(modelName);
        if (adapter == null) {
            throw new BizException("不支持的模型类型: " + modelName.getModelName());
        }
        return adapter;
    }
}
