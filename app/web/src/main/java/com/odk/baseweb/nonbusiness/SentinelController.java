package com.odk.baseweb.nonbusiness;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.odk.baseweb.interceptor.sentinelConfig.SentinelResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SentinelController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/7/4
 */
@RestController
@RequestMapping("/sentinel")
public class SentinelController {


    private static final Logger logger = LoggerFactory.getLogger(SentinelController.class);

    private final AtomicInteger atomicInteger = new AtomicInteger(0);


    @SentinelResource(value = SentinelResourceConstants.TEST_RESOURCE, blockHandler = "blockHandler", fallback = "fallback")
    @GetMapping("/test")
    public Integer unionDataTest() {

        int i = atomicInteger.incrementAndGet();
        logger.info("调用次数：" + i);
//        int x = 1 / 0;

//        throw new RuntimeException("异常");
//        logger.info("调用次数：" + i);
        return i;

    }

    /**
     * 权限接口限流或降级时返回空
     *
     * @param blockException
     * @return
     */
    public Integer blockHandler(BlockException blockException) {
        logger.error("调用权限接口限流，", blockException);
        return 0;
    }

    /**
     * 发生熔断或者异常时，调用fallback
     *
     * @param throwable
     * @return
     */
    public Integer fallback(Throwable throwable) {
        logger.error("调用权限接口发生异常", throwable);
        return -1; // 或者返回自定义错误码
    }

}
