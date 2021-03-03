class AB_Remote extends Ability{
  SprintBuff remoteB;
  public AB_Remote(String n, Boolean p){
    super(n,p);
    remoteB = new SprintBuff("Remote",2.25);
   
    setTimer(60);
  }
  
  public void Use(Player p){
    if(this.Active(p)){
      remoteB.Timed = true;
      remoteB.setTimer(20);
      
      p.bList.add(remoteB);
    }
  }
}

class AB_Scared extends Ability{
  SprintBuff scaredB;
  public AB_Scared(){
    super("Scared",true);
    scaredB = new SprintBuff("Scared",0.15);
  }
  
  public void Passive(Player p){
    if(!playerList.get(current_player).bList.contains(scaredB)){p.bList.add(scaredB);};
  }
}
