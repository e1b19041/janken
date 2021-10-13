package oit.is.z0160.kaizi.janken.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import oit.is.z0160.kaizi.janken.model.Entry;
import oit.is.z0160.kaizi.janken.model.Janken;

@Controller
public class Lec02Controller{

  @Autowired
  private Entry entry;

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

    return "lec02.html";
  }

  @GetMapping("/lec02")
  public String lec02(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);
    model.addAttribute("userName",loginUser);

    return "lec02.html";
  }



}
