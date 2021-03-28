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
package my_demo.mapper;

import my_demo.entity.EmailSexBean;
import my_demo.entity.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TUserMapper {

  TUser selectByPrimaryKey(Integer id);

  List<TUser> selectAll();

  List<TUser> selectByEmailAndSex1(Map<String, Object> param);

  List<TUser> selectByEmailAndSex2(@Param("email") String email, @Param("sex") Byte sex);

  List<TUser> selectByEmailAndSex3(EmailSexBean esb);

  List<TUser> selectUserJobs1();

  List<TUser> selectUserJobs2();


}
