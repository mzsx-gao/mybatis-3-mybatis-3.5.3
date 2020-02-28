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
package my_demo;

import my_demo.entity.EmailSexBean;
import my_demo.entity.TUser;
import my_demo.mapper.TUserMapper;
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
    //--------------------第一阶段---------------------------
    // 1.读取mybatis配置文件创SqlSessionFactory
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    inputStream.close();
  }

  @Test
  // 快速入门
  public void quickStart() throws IOException {
    //--------------------第二阶段---------------------------
    // 2.获取sqlSession
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 3.获取对应mapper
    TUserMapper mapper = sqlSession.getMapper(TUserMapper.class);

    //--------------------第三阶段---------------------------
    // 4.执行查询语句并返回单条数据
//    TUser user = mapper.selectByPrimaryKey(1);
//    System.out.println(user);

    System.out.println("----------------------------------");

    // 5.执行查询语句并返回多条数据
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
    Map<String, Object> params = new HashMap<>();
    params.put("email", email);
    params.put("sex", sex);
    List<TUser> list1 = mapper.selectByEmailAndSex1(params);
    System.out.println(list1.size());

    // 第二种方式直接使用参数
    List<TUser> list2 = mapper.selectByEmailAndSex2(email, sex);
    System.out.println(list2.size());

    // 第三种方式用对象
    EmailSexBean esb = new EmailSexBean();
    esb.setEmail(email);
    esb.setSex(sex);
    List<TUser> list3 = mapper.selectByEmailAndSex3(esb);
    System.out.println(list3.size());
  }


}
