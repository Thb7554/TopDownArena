class Buff{
  boolean positive;
  boolean Timed = false;
  String name;
  boolean on = true;
  
  public Buff(String n, boolean p){
    name = n;
    positive = p;
  }
  
  public void Use(Player p){
    
  }
  
  protected float timer;
  protected float maxTimer;
  
  public void setTimer(float t){
    maxTimer = t;
    timer = maxTimer;
    on = true;
  }
  
  public void updateTimer(){
    if(Timed){
       timer -= .1f;
    
      if(timer <= 0){
        timer = 0;
        on = false;
      }
    }
  }
  
  public float getTimer(){
    return timer; 
  }
  
  public void Draw(){
    
  }
  
  public void Background(){
    noStroke();
    fill(0,0,0,150);
    ellipse(0,0,30,30);
    
    if(maxTimer != 0){
       fill(200,255,0,230);
      noStroke();
      arc(0, 0, 28, 28,-2*PI*timer/maxTimer-HALF_PI,-HALF_PI);
    }
    
    fill(250,25,25);
    if(positive){
      fill(50,250,10);
    }
    ellipse(0,0,22,22);
    
    fill(0,0,0);
    text(name,0,10);
    fill(255,255,255);
    text(name,0,9);
    
    
  }
}

class SprintBuff extends Buff{
  float amt;
  public SprintBuff(String s, float a){
    super(s,true);
    amt = a;
  }
  
  public void Use(Player p){
    p.bSpd += amt;
  }
  
  public void Draw(){
    this.Background();
  }
}
