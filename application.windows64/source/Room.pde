class Room{
  int x,y;
  int w,h;
  ArrayList<Wall> wallList = new ArrayList<Wall>();
  ArrayList<Door> doorList = new ArrayList<Door>();
  ArrayList<Object> objList = new ArrayList<Object>();
  boolean collision = false;
  
  public Room(int xIn, int yIn,int wIn, int hIn){
    x = xIn;
    y = yIn;
    w = wIn;
    h = hIn;
    
  }
  
  public void TranslateX(int xIn){
    x+=xIn;
    for(Wall w : wallList){
      w.x+=xIn;
      w.j+=xIn;
    }
    for(Door d : doorList){
      d.x+=xIn;
      d.j+=xIn;
    }
  }
  
  public void TranslateY(int yIn){
    y+=yIn;
    for(Wall w : wallList){
      w.y+=yIn;
      w.k+=yIn;
    }
    for(Door d : doorList){
      d.y+=yIn; 
      d.k+=yIn; 
    }
  }
  

  
  public void AddWall(int xA,int yA,int jA,int kA){
    wallList.add(new Wall(xA,yA,jA,kA));
  }
  
  public void AddDoor(int xA,int yA,int jA,int kA){
    doorList.add(new Door(x+xA,y+yA,x+jA,y+kA));
    doorList.get(doorList.size()-1).calcDir(this);
  }
  
  public void AddWall(Wall w){
    wallList.add(w);
  }
  
  public void AddDoor(Door d){
    doorList.add(d);
    doorList.get(doorList.size()-1).calcDir(this);
  }
  
  
  public void AddObj(int xA,int yA,int hH, int jA,int kA){
    objList.add(new Object(this,x+xA,y+yA,hH,jA,kA));
  }
  
  public void Draw(){
    if(collision){
      //fill(210,210,200,200);
    }
    else{
      //fill(140,130,150,5); 
    }
    stroke(0,0,0);
    noStroke();
    //rect(x,y,w,h);
    
    for(int i=0; i < wallList.size(); i++){
      wallList.get(i).Draw(); 
    }
    for(int i=0; i < doorList.size(); i++){
      doorList.get(i).DrawDoor(); 
    }
    collision = false;
  }
  
  public void DrawObjsShadow(){
    for(int i=0; i < objList.size(); i++){
      objList.get(i).DrawShadows(); 
    }
  }
  
   public void DrawObjs(){
    for(int i=0; i < objList.size(); i++){
      objList.get(i).DrawObj(); 
    }
  }
}
