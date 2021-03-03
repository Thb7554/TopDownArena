class Door{
  int x,y,j,k;
  color c;
  int dir = 5;
  Door connector;
  
  public Door(int xIn, int yIn, int jIn, int kIn){
    x = xIn;
    y = yIn;
    j = jIn;
    k = kIn;
   
    c = color(200,220,0);
  }
  
  public void calcDir(Room r){
    dir = 5;
    if(x == r.x && j == r.x){
      dir = 4; 
    }
    if(y == r.y && k == r.y){
      dir = 8; 
    }
    if(x == r.w && j == r.w){
      dir = 6; 
    }
    if(y == r.h && k == r.h){
      dir = 2; 
    }
  }
  
  public void DrawDoor(){
    strokeWeight(6);
    
    switch(dir){
      case 2:
        stroke(200,200,0);
      break;
      case 4:
        stroke(45,0,150);
      break;
      case 6:
        stroke(0,65,190);
      break;
      case 8:
       stroke(220,240,40);
      break;
      default:
        stroke(200,200,200);
      break;
    }
    
    if(roomDebug){
      line(x,y,j,k);
    }
   
    //c = color(0,0,0);
    strokeWeight(1);
  }
}
