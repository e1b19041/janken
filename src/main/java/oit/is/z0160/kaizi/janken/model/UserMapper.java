package oit.is.z0160.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  @Select("SELECT * from users")
  ArrayList<User>
  selectAllUser();

  @Select("select * from users where name = #{name}")
  User selectByName(String name);

}
