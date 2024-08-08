package ai.wbw.service.common.aop;

import ai.wbw.service.common.bean.DBContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 数据库读写分离
 */
@Aspect
@Component
public class DataSourceAop {


    @Pointcut("!@annotation(ai.wbw.service.common.annotation.Master) " +
            "&& (execution(* ai.wbw.service.core.service.impl.*.*(..))" +
            "|| execution(* com.baomidou.mybatisplus.extension.service.impl.*.*(..)))")
    public void readPointcut() {

    }

    /**
     * 判断哪些需要读从数据库，其余的走主数据库
     * 切面未介入的走默认数据库，也可以
     */
    @Before("readPointcut()")
    public void before(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        if (StringUtils.startsWithAny(methodName, "get", "select", "find", "query")) {
            DBContextHolder.slave();
        }else {
            DBContextHolder.master();
        }
    }
}