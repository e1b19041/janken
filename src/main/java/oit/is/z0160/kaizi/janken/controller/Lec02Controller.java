package oit.is.z0160.kaizi.janken.controller;

import java.util.ArrayList;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.
Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import oit.is.z0160.kaizi.janken.model.Entry;
import oit.is.z0160.kaizi.janken.model.Janken;
import oit.is.z0160.kaizi.janken.model.User;
import oit.is.z0160.kaizi.janken.model.UserMapper;
import oit.is.z0160.kaizi.janken.model.Match;
import oit.is.z0160.kaizi.janken.model.MatchMapper;
import oit.is.z0160.kaizi.janken.model.MatchInfo;
import oit.is.z0160.kaizi.janken.model.MatchInfoMapper;
import oit.is.z0160.kaizi.janken.service.AsyncKekka;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Controller
public class Lec02Controller{

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  @Autowired
  AsyncKekka kekka;


  /*@GetMapping("/lec02")
  public String lec02() {
    return "lec02.html";
  }
  */

  @PostMapping("/lec02")
  public String lec02(@RequestParam String userName,ModelMap model){
    model.addAttribute("userName",userName);
    return "lec02.html";
  }

  @GetMapping("/lec02janken")
  public String lec02janken(@RequestParam String
    hand,ModelMap model){
    Janken janken =new Janken(hand);
    model.addAttribute("janken",janken);
    model.addAttribute("hand",janken.hand);
    model.addAttribute("cpuHand",janken.cpuHand);
    model.addAttribute("syouhai",janken.syouhai);

    ArrayList<Match> matches = matchMapper.selectAllMatch();
    model.addAttribute("matches", matches);

    return "lec02.html";
  }

  @GetMapping("/lec02")
  public String lec02(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);
    model.addAttribute("userName",loginUser);

    ArrayList<User> users = userMapper.selectAllUser();
    model.addAttribute("users", users);
    ArrayList<Match> matches = matchMapper.selectAllMatch();
    model.addAttribute("matches", matches);
    ArrayList<MatchInfo> matchInfo = matchInfoMapper.selectByIsActive();
    model.addAttribute("matchInfo",matchInfo);

    return "lec02.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam String name,Principal prin,ModelMap model){
    String loginUser=prin.getName();
    model.addAttribute("userName",loginUser);
    model.addAttribute("cpuName",name);
    return "match.html";
  }

  @GetMapping("/matchjanken")
  @Transactional
  public String matchjanken(@RequestParam String name,@RequestParam String hand, ModelMap model, Principal prin) {
    int flag=0;
    String loginUser = prin.getName();
    Match match = new Match();
    Janken janken =new Janken(hand);
    User user1 = userMapper.selectByName(loginUser);
    User user2 = userMapper.selectByName(name);
    MatchInfo matchInfo=new MatchInfo();
    ArrayList<MatchInfo> matchInfoList = matchInfoMapper.selectByIsActive();
    if(matchInfoList.size()==0){
      matchInfo.setUser1(user1.getId());
      matchInfo.setUser2(user2.getId());
      matchInfo.setUser1Hand(janken.hand);
      matchInfo.setIsActive(true);
      matchInfoMapper.insertMatchInfo(matchInfo);
      model.addAttribute("matchInfo",matchInfo);
    } else {
      for(int i=0;i<matchInfoList.size();i++){
        if(matchInfoList.get(i).getIsActive()&&matchInfoList.get(i).getUser2()==user1.getId()){
          match.setUser1(user1.getId());
          match.setUser2(user2.getId());
          match.setUser1Hand(hand);
          match.setUser2Hand(matchInfoList.get(i).getUser1Hand());
          match.setIsActive(true);
          matchMapper.insertMatch(match);
          model.addAttribute("matches", match);
        } else {
          flag=1;
        }
      }
      if(flag==1){
        matchInfo.setUser1(user1.getId());
        matchInfo.setUser2(user2.getId());
        matchInfo.setUser1Hand(janken.hand);
        matchInfo.setIsActive(true);
        matchInfoMapper.insertMatchInfo(matchInfo);
        model.addAttribute("matchInfo",matchInfo);
      }
    }
    model.addAttribute("userName",loginUser);
    return "wait.html";
  }

  @GetMapping("async")
  public SseEmitter async() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.kekka.asyncShowKekka(sseEmitter);
    return sseEmitter;
  }
}
