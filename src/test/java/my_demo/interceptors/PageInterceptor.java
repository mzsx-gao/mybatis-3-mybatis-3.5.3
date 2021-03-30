package my_demo.interceptors;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

/*
 *是在mybatis里面执行sql语句的时候做一个拦截
 * 比如要修改参数，要做缓存都可以用拦截
 *
 * 1、Executor
 * 2、StatementHandler
 * 3、ResultSetHandler
 * 4、ParamterHanlder
 *
 * 这些接口都可以做拦截
 * */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        logger.info("=============PageInterceptor.intercept=============");

        if (invocation.getTarget() instanceof StatementHandler) {
            //RoutingStatementHandler
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

            Field delegate = getField(statementHandler, "delegate");
            StatementHandler prepareStatement = (StatementHandler) delegate.get(statementHandler);

            Field boundSql = getField(prepareStatement, "boundSql");
            BoundSql bsinstance = (BoundSql) boundSql.get(prepareStatement);

            Field sql = getField(bsinstance, "sql");
            String sqlStr = (String) sql.get(bsinstance);

//            Page page = (Page) bsinstance.getParameterObject();
//
//            if (!page.isPage()) {
//                return invocation.proceed();
//            }
            sqlStr = pageSql(sqlStr, null);//拼接sql
            sql.set(bsinstance, sqlStr);
        }
        return invocation.proceed();
    }

    private String pageSql(String sql, Object page) {

        //select * from zg_order limit offset,pagesize;

        StringBuffer sb = new StringBuffer();
        sb.append(sql);
        sb.append(" limit ");
        sb.append(1);
        sb.append("," + 10);
        return sb.toString();
    }

    private Field getField(Object o, String name) {
//        Field field = ReflectionUtils.findField(o.getClass(), name);
//        ReflectionUtils.makeAccessible(field);
//        return field;
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
