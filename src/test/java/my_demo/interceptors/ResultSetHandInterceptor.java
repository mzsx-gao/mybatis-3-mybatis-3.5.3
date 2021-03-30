package my_demo.interceptors;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/*
* 是在mybatis里面执行sql语句的时候做一个拦截
* 比如要修改参数，要做缓存都可以用拦截
* 1、Executor
* 2、StatementHandler
* 3、ResultSetHandler
* 4、ParamterHanlder
*
* 这些接口都可以做拦截
* */
@Intercepts({@Signature(type = ResultSetHandler.class,method = "handleResultSets", args = {Statement.class})})
public class ResultSetHandInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.info("=============ResultSetHandInterceptor.intercept=============");
        Object target = invocation.getTarget();
        if(target instanceof ResultSetHandler) {
           //1、先获取查询结果
            ResultSetHandler resultSetHandler = (ResultSetHandler)target;
            List<Object> result = (List<Object>)invocation.proceed();

            //2、然后根据返回的list去修改里面的信息
            //这里是你写的修改代码
            //3、修改完成后就返回
            return result;
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if(target instanceof ResultSetHandler) {
            return Plugin.wrap(target,this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
