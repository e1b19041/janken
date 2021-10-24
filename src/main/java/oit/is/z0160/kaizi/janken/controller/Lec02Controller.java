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


@Controller
public class Lec02Controller{

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;


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
  public String matchjanken(@RequestParam String hand, ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    Match match = new Match();
    Janken janken =new Janken(hand);
    User user1 = userMapper.selectByName(loginUser);
    User user2 = userMapper.selectByName("CPU");
    match.setUser1(user1.getId());
    match.setUser2(user2.getId());
    match.setUser1Hand(janken.hand);
    match.setUser2Hand(janken.cpuHand);
    matchMapper.insertMatch(match);
    model.addAttribute("janken",janken);
    model.addAttribute("hand",janken.hand);
    model.addAttribute("cpuHand",janken.cpuHand);
    model.addAttribute("syouhai",janken.syouhai);

    return "match.html";
  }
}
