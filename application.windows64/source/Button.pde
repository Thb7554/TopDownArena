

class Button{
  int x,y,w,h;
  String s;
  color c;
  boolean enabled;
  public Button(int xIn, int yIn, int wIn, int hIn, String sIn){
    x = xIn;
    y = yIn;
    w = wIn;
    h = hIn;
    s = sIn;
    enabled = true;
    c = color(255,255,255);
  }
  
  public void Change() {};
  
  public void Draw(){
    fill(c);
    rect(x,y,w,h);
    fill(0,0,0);
    
    text(s,x,y+h/2);
  }
  
  public void Update(){
    //update colors 
  }
  
  public void Click(){};
  
  public boolean Clicked(){
    if(mouseX < x+w && mouseX > x && mouseY < y+h && mouseY > y){
      c = color(200,250,10);
      return true;
    }
    else{
      c = color(255,255,255);
      return false;
    }
  }
}
