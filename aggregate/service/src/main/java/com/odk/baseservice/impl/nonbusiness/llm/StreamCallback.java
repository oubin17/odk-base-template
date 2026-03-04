package com.odk.baseservice.impl.nonbusiness.llm;

/**
 * StreamCallback
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/3/4
 */
public interface StreamCallback {
    void onData(String data);

    void onComplete();

    void onError(String error);
}
