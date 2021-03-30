/**
 *    Copyright 2009-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package my_demo.interceptors;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;


@Intercepts({
	@Signature(type= StatementHandler.class,method="query",args={Statement.class, ResultHandler.class})
//	@Signature(type=StatementHandler.class,method="query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})
})
@Slf4j
public class ThresholdInterceptor implements Interceptor {
	
	private long threshold;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

        log.info("=============ExecutorInterceptor.intercept=============");
		long begin = System.currentTimeMillis();
		Object ret = invocation.proceed();
		long end=System.currentTimeMillis();
		long runTime = end - begin;
		if(runTime>=threshold){
			Object[] args = invocation.getArgs();
			Statement stat = (Statement) args[0];
			MetaObject metaObjectStat = SystemMetaObject.forObject(stat);
			PreparedStatementLogger statementLogger = (PreparedStatementLogger)metaObjectStat.getValue("h");
            ClientPreparedStatement pstat = (ClientPreparedStatement)statementLogger.getPreparedStatement();
			System.out.println("sql语句：“"+pstat.getPreparedSql()+"”执行时间为："+runTime+"毫秒，已经超过阈值！");
		}
		return ret;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.threshold = Long.valueOf(properties.getProperty("threshold"));
	}
}
