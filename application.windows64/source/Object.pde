class Object{
  int x,y;
  int xW,yH;
  int h;
  
  public Object(Room r, int xIn, int yIn, int hIn, int xWIn, int yHIn){
    x = xIn;
    y = yIn;
    h = hIn;
    xW = xWIn;
    yH = yHIn;
    
    r.wallList.add(new Wall(true, 
            (int)(x-(float)xW/2), 
            (int)(y-(float)yH/2),
            (int)(x-(float)xW/2), 
            (int)(y+(float)yH/2)));
    r.wallList.add(new Wall(true, 
            (int)(x-(float)xW/2), 
            (int)(y+(float)yH/2),
            (int)(x+(float)xW/2), 
            (int)(y+(float)yH/2)));
    r.wallList.add(new Wall(true, 
            (int)(x-(float)xW/2), 
            (int)(y-(float)yH/2),
            (int)(x+(float)xW/2), 
            (int)(y-(float)yH/2)));
    r.wallList.add(new Wall(true, 
            (int)(x+(float)xW/2), 
            (int)(y+(float)yH/2),
            (int)(x+(float)xW/2), 
            (int)(y-(float)yH/2)));
  }
  
  public void DrawShadows(){
    strokeWeight(0);
    fill(10,10,10,150);
    rect((x-(float)xW/2)+4,
         (y-(float)yH/2)+4,
         xW+2*h,
         yH+2*h
    );
  }
  
  public void DrawObj(){
    strokeWeight(1);
    fill(220,220,220);
    rect((x-(float)xW/2),
         (y-(float)yH/2),
         xW,
         yH
    );

    
    rect((x-(float)xW/2)-2*h,
         (y-(float)yH/2)-2*h,
         xW,
         yH
    );
  }

  
  
}
