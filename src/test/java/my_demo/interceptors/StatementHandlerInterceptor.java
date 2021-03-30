package my_demo.interceptors;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "query",
        args = {Statement.class, ResultHandler.class})})
public class StatementHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        PreparedStatement pst = (PreparedStatement)invocation.getArgs()[0];
        ResultSetMetaData rsmd = pst.getMetaData();
        String table_name = rsmd.getTableName(3);

        System.out.println("tableName:" + table_name);
        //表列数
        int size = rsmd.getColumnCount();
        for (int i = 0; i < size; i++) {
            String columeName = rsmd.getColumnName(i + 1);
            System.out.println("columeName:" + columeName);
        }
//        Object[] args = invocation.getArgs();
//        Connection con = (Connection) args[0];
//
//        DatabaseMetaData metaData = con.getMetaData();
//        ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});

//        while (rs.next()) {
//            String table_name = rs.getString("TABLE_NAME");
//            String table_type = rs.getString("TABLE_TYPE");
//            System.out.println("tableType:" + table_type + "<-->tableName:" + table_name);
//        }
//        getTableNames(con);

        return invocation.proceed();
    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames(Connection connection) throws Throwable {
        List<String> tableNames = new ArrayList<>();
        ResultSet rs = null;
        //获取数据库的元数据
        DatabaseMetaData db = connection.getMetaData();
        //从元数据中获取到所有的表名
        rs = db.getTables(null, null, null, new String[]{"TABLE"});
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            tableNames.add(tableName);
            System.out.println("tableName:" + tableName);
            getColumnNames(tableName,connection);
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName, Connection connection) throws Throwable {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        PreparedStatement pStemt = null;
        pStemt = connection.prepareStatement("select * from " + tableName);
        //结果集元数据
        ResultSetMetaData rsmd = pStemt.getMetaData();
        //表列数
        int size = rsmd.getColumnCount();
        for (int i = 0; i < size; i++) {
            String columeName = rsmd.getColumnName(i + 1);
            columnNames.add(columeName);
            System.out.println("columeName:" + columeName);
        }
        return columnNames;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof RoutingStatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
