package oit.is.z0160.kaizi.janken.model;

public class Janken{
  public String hand;
  public String cpuHand;
  public String syouhai;

  public Janken(String hand){
    this.hand=hand;
    this.cpuHand="Gu";

    if(this.hand.equals("Gu")){
      this.syouhai="Draw";
    }
    if(this.hand.equals("Choki")){
      this.syouhai="You lose";
    }
    if(this.hand.equals("Pa")){
      this.syouhai="You Win!";
    }
  }

}
