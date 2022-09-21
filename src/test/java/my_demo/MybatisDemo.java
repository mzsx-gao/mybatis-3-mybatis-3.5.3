package my_demo;

import my_demo.entity.EmailSexBean;
import my_demo.entity.TUser;
import my_demo.mapper.TUserMapper;
import mybatis.page.PageUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisDemo {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();
    }

    @Test
    // 快速入门
    public void quickStart() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TUserMapper mapper = sqlSession.getMapper(TUserMapper.class);
        TUser user = mapper.selectByPrimaryKey(1);
        System.out.println(user);
        List<TUser> users = mapper.selectAll();
        for (TUser tUser : users) {
            System.out.println(tUser);
        }
    }

    // 多参数查询
    @Test
    public void testManyParamQuery() {
        // 2.获取sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 3.获取对应mapper
        TUserMapper mapper = sqlSession.getMapper(TUserMapper.class);

        String email = "qq.com";
        Byte sex = 1;

        // 第一种方式使用map
//        Map<String, Object> params = new HashMap<>();
//        params.put("email", email);
//        params.put("sex", sex);
//        List<TUser> list1 = mapper.selectByEmailAndSex1(params);
//        System.out.println(list1.size());

        // 第二种方式直接使用参数
        List<TUser> list2 = mapper.selectByEmailAndSex2(email, sex);
        System.out.println(list2.size());

        // 第三种方式用对象
//        EmailSexBean esb = new EmailSexBean();
//        esb.setEmail(email);
//        esb.setSex(sex);
//        List<TUser> list3 = mapper.selectByEmailAndSex3(esb);
//        System.out.println(list3.size());
    }

    // 1对多两种关联方式
    @Test
    public void testOneToMany() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TUserMapper userMapper = sqlSession.getMapper(TUserMapper.class);

        //第一种方式
//        List<TUser> selectUserJobs1 = userMapper.selectUserJobs1();
//        for (TUser tUser : selectUserJobs1) {
//            System.out.println(tUser);
//        }

        //第二种方式
        List<TUser> users = userMapper.selectUserJobs2();
        for (TUser tUser : users) {
            System.out.println(tUser.getRealName());
        }
        //懒加载模式，调用tUser.getJobs()才会加载具体的数据
        System.out.println(users.get(0).getJobs().size());
    }

    // 分页插件
    @Test
    public void testPagePlugin() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TUserMapper mapper = sqlSession.getMapper(TUserMapper.class);
        String email = "qq.com";
        Byte sex = 1;

        PageUtil.enable(1, 3);//启用分页
        List<TUser> list2 = mapper.selectByEmailAndSex2(email, sex);
        System.out.println(list2.size());
    }
}
