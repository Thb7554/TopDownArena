class Wall{
  int x,y,j,k;
  color c;
  boolean collision;
  boolean rW = false;
  
  public Wall(int xIn, int yIn, int jIn, int kIn){
    x = xIn;
    y = yIn;
    j = jIn;
    k = kIn;
   
    c = color(0,0,0);
  }
  
  public Wall(boolean rWIn, int xIn, int yIn, int jIn, int kIn){
    x = xIn;
    y = yIn;
    j = jIn;
    k = kIn;
    rW = true;
    c = color(0,0,0);
  }
  
  public void Draw(){
    if(rW){
      strokeWeight(1);
    }
    else{
      strokeWeight(3);
    }
    
    stroke(c);
    if(collision){
      //stroke(255,0,0); 
    }
    line(x,y,j,k);
    //c = color(0,0,0);
    //collision = false;
    strokeWeight(1);
    
    if(roomDebug){
      fill(150,0,0);
      text(x +"|"+y,x-10,y-10); 
      fill(0,0,150);
      text(j +"|"+k,j-10,k-10); 
    }
    
  }
}
