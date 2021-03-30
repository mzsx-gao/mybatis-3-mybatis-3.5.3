package my_demo.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
*是在mybatis里面执行sql语句的时候做一个拦截
* 比如要修改参数，要做缓存都可以用拦截
*
* 1、Executor
* 2、StatementHandler
* 3、ResultSetHandler
* 4、ParamterHanlder
* 这些接口都可以做拦截
* */
@Intercepts({@Signature(type = Executor.class,method = "query", args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
@Slf4j
public class ExecutorInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        logger.info("=============ExecutorInterceptor.intercept=============");
        List<Object> list = new ArrayList<>();
        Object target = invocation.getTarget();
        if(target instanceof Executor) {
            String cacheResult = "";//从缓存中读取
            if("".equals(cacheResult)) {
                //穿透过去
                return invocation.proceed();
            }
        }
        return list;
    }
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
