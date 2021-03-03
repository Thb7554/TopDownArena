class Vision{
  int x,y;
  PVector d;
  
  public Vision(int xIn,int yIn,PVector dir){
    x = xIn;
    y = yIn;
    d = dir;
  }
  
  public void Draw(){
    line(x,y,x+d.x,y+d.y);
  }
}
