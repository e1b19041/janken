package oit.is.z0160.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

  @Select("SELECT id,user1,user2,user1Hand,user2Hand from matches;")
  ArrayList<Match>
  selectAllMatch();

  @Select("SELECT id,user1,user2,user1Hand,user2Hand from matches where isActive is true;")
  Match selectByIsActive();

  @Select("SELECT id,user1,user2,user1Hand,user2Hand from matches where id = #{id};")
  Match selectById(int id);

  @Insert("INSERT INTO matches (user1,user2,user1Hand,user2Hand,isActive) VALUES (#{user1},#{user2},#{user1Hand},#{user2Hand},#{isActive});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

  @Update("UPDATE MATCHES SET ISACTIVE=true, WHERE ID = #{id}")
  void updateById(int id);
}
