package com.odk.baseweb.interceptor.sentinelConfig;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.odk.baseweb.interceptor.sentinelConfig.SentinelResourceConstants.TEST_RESOURCE;

/**
 * SentinelConfig
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/7/4
 */
@Component
public class SentinelConfig {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    /**
     * rule config
     *
     * 熔断降级规则 DegradeRule
     * 流量控制规则 FlowRule
     * 访问控制规则 AuthorityRule
     * 系统保护规则 SystemRule
     * 热点规则 ParamFlowRule
     */
    @PostConstruct
    public void initRole(){
        //熔断
        initDegradeRule(TEST_RESOURCE);
        //通用限流
        initFlowQpsRule(TEST_RESOURCE);
        //热点参数限流
//        initParamFlowRule(TEST_RESOURCE, 0);
    }

    /**
     * 熔断控制：并不是严格认为是 10秒内 2 次异常即熔断
     *
     * 调用接口出现异常，进行熔断
     *  Sentinel 的熔断机制是 被动式的异常统计 + 异步熔断决策，它的工作流程如下：
     * 请求进入方法体（无论是否处于熔断状态）；
     * 方法执行过程中如果抛出异常，Sentinel 会记录异常；
     * Sentinel 根据异常数量、时间窗口等规则判断是否触发熔断；
     * 如果触发熔断，在本次请求中依然要先完成方法体的执行（包括抛异常），然后决定是否走 fallback；
     * 下一次请求进来时，如果熔断器处于打开状态（OPEN），则会直接走 fallback，不执行原方法体。
     * ⚠️ 所以你现象是：前几次请求仍会执行方法体，直到熔断器真正打开（OPEN）之后，才会“跳过”方法体直接走 fallback。
     */
    private void initDegradeRule(String resourceName) {
        List<DegradeRule> rules=new ArrayList<>();
        DegradeRule rule=new DegradeRule(resourceName);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        rule.setCount(2);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }

    /**
     * 通用流量控制：
     * qps > 20，限流
     */
    private void initFlowQpsRule(String resourceName) {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule(resourceName);
        rule.setCount(2);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setLimitApp("default");
        rules.add(rule);

        FlowRuleManager.loadRules(rules);


    }

    /**
     * 热点参数限流 SphU.entry
     * 可以针对每个用户限流
     *
     * 1. 配置参数
     * 2. 配置规则
     * 3. 启动规则
     */
    private void initParamFlowRule(String resourceName, int paramIndex) {
        ParamFlowRule rule = new ParamFlowRule(resourceName)
                .setCount(1) // 每个用户每秒最多 1 次请求
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setParamIdx(paramIndex); // 表示取 SphU.entry 的第 0 个参数作为限流维度（即 userId）

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }


}
