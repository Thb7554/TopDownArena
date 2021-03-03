class StartButton extends Button{
  public StartButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
       debug ^= true;
       roomDebug = false;
       BaseRoom();
       GenerateMap();
       LockDoors();
       buttonList = new ArrayList<Button>();
     };
  }
}

class RoomButton extends Button{
  public RoomButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
       roomDebug ^= true;
       this.enabled = false;
       wallB.enabled = true;
       nextwallB.enabled = true;
       doorB.enabled = true;
       newroomB.enabled = true;
       nextroomB.enabled = true;
       nextdoorB.enabled = true;
       walldoorB.enabled = true;
       saveB.enabled = true;
       xBig.enabled = true;
       yBig.enabled = true;
       xSma.enabled = true;
       ySma.enabled = true;
       
       //buttonList.add(new WallButton(x,y+100,w,h,"Wall"));
     };
  }
}

class WallButton extends Button{
  int qq = 0;
  int ww = 0;
  int ee = 100;
  int rr = 100;
  public WallButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Draw(){
    fill(c);
    rect(x,y,w,h);
    fill(0,0,0);
    text(s,x,y+h/2);
    text("A: " + qq, x,y-50);
    text("B: " + ww, x,y-40);
    text("C: " + ee, x,y-30);
    text("D: " + rr, x,y-20);
    
  }
  
  public void Click(){
     if(Clicked()){
        roomList.get(current_room).AddWall(qq,ww,ee,rr);
        movingWall = true;
        current_wall = roomList.get(current_room).wallList.size()-1;
     };
  }
}


class XBigger extends Button{
   public XBigger(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        roomList.get(current_room).w +=10;
     };
  }
}
class XSmaller extends Button{
   public XSmaller(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        roomList.get(current_room).w -=10;
     };
  }
}
class YBigger extends Button{
   public YBigger(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        roomList.get(current_room).h +=10;
     };
  }
}
class YSmaller extends Button{
   public YSmaller(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        roomList.get(current_room).h -=10;
     };
  }
}

class NextWallButton extends Button{
  public NextWallButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        current_wall++;
        if(current_wall > roomList.get(current_room).wallList.size()-1){
          current_wall = 0; 
        }
     };
  }
}

class DoorButton extends Button{
  public DoorButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
    if(Clicked()){
        movingWall = false;
        roomList.get(current_room).AddDoor(0,0,0,100);
        current_door = roomList.get(current_room).doorList.size()-1;
     };
  }
}

class NewRoomButton extends Button{
  public NewRoomButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
    if(Clicked()){
        roomList.add(new Room(0,0,200,200));
        current_room++;
        if(current_room > roomList.size()-1){
          current_room = 0; 
        }
     };
  }
}

class NextRoomButton extends Button{
  public NextRoomButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        current_room++;
        if(current_room > roomList.size()-1){
          current_room = 0; 
        }
     };
  }
}

class NextDoorButton extends Button{
  public NextDoorButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
     if(Clicked()){
        current_door++;
        if(current_door > roomList.get(current_room).doorList.size()-1){
          current_door = 0; 
        }
     };
  }
}

class WallDoorButton extends Button{
  public WallDoorButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Draw(){
    fill(c);
    rect(x,y,w,h);
    fill(0,0,0);
    if(movingWall)
    {
      text("WALL",x,y+h/2);
    }
    else
    {
      text("DOOR",x,y+h/2);
    }
    
  }
  
  public void Click(){
     if(Clicked()){
       movingWall ^= true;
     }
  }
}

class SaveButton extends Button{
  public SaveButton(int xIn, int yIn, int wIn, int hIn, String sIn){
    super( xIn,  yIn,  wIn,  hIn, sIn);
  }
  
  public void Click(){
    if(Clicked()){
        SaveRooms();
     };
  }
}
