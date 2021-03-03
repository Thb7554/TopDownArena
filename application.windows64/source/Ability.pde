class Ability{
  String name;
  color c;
  boolean passive;
  boolean on;
  
  protected float timer;
  protected float maxTimer;
  
  public void setTimer(float t){
    maxTimer = t;
    timer = 0;
  }
  
  public void Passive(Player p){};
  
  public void updateTimer(){
    timer -= .1f;
    
    if(timer <= 0){
      this.c = color(255,255,255);
      on = true;
      timer = 0;
    }
  }
  
  public float getTimer(){
    return timer; 
  }
  
  
  public Ability(String n, boolean p){
    name = n;
    passive = p;
    c = color(255,255,255);
  };
  
  public void Use(Player p){};
  
  public boolean Active(Player p) {
    if(!passive && timer ==0){
      timer = maxTimer;
      this.c = color(255,50,50,150);
      return true;
    }
    return false;
  };
  
  public void Draw(){
    fill(c);
    if(passive){
      ellipse(25,25,50,50);
    }
    else{
      stroke(0,0,0);
      fill(255,255,255);
      rect(0,0,50,50);
      fill(c);
      noStroke();
      arc(25, 25, 50, 50,-2*PI*timer/maxTimer-HALF_PI,-HALF_PI);
      stroke(0,0,0);
    }
    fill(0,0,0);
    textAlign(CENTER, CENTER);
    text(name,0,0,50,50);
  }
}
