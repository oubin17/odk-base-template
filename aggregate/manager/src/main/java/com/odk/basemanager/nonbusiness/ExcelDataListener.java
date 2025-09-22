package com.odk.basemanager.nonbusiness;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * ExcelDataListener
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/22
 */
public class ExcelDataListener<T> implements ReadListener<T> {


    /**
     * 每隔5条存储一次，实际使用中可以100条，然后清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private final Consumer<T> consumer;

    public ExcelDataListener(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        if (consumer != null) {
            consumer.accept(data);
        } else {
            cachedDataList.add(data);
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        // 这里可以添加存储数据库的逻辑
    }
}
