import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DeathG extends PApplet {

ArrayList<Ability> abilityList = new ArrayList<Ability>();

AB_Remote abRemote = new AB_Remote("Remote", false);
AB_Scared abScared = new AB_Scared();

float switchTimer = 10;

Taytay pOne = new Taytay(50,50);
Player pTwo = new Player(170,170);

int current_player = 0;
int numRoom = 20;
int recursionStop = 100;
//json = loadJSONObject("room.json");



ArrayList<Room> roomList = new ArrayList<Room>();
ArrayList<Room> rList = new ArrayList<Room>();
ArrayList<Integer> keyList = new ArrayList<Integer>();
ArrayList<Player> playerList = new ArrayList<Player>();
ArrayList<Button> buttonList = new ArrayList<Button>();

boolean movingWall;
int doorTimer;
boolean debug;
boolean roomDebug;
int current_room;
int current_wall;
int current_door;
int camera_x;
int camera_y;


StartButton start = new StartButton(50,50,100,50,"Start");
RoomButton roomB = new RoomButton(50,150,150,50,"Room Editor");
WallButton wallB = new WallButton(50,200,150,50,"Add Wall");
NextWallButton nextwallB = new NextWallButton(50,250,150,50,"Next Wall");
DoorButton doorB = new DoorButton(50,350,150,50,"Add Door");
NewRoomButton newroomB = new NewRoomButton(250, 50,100,50,"New Room");
NextRoomButton nextroomB = new NextRoomButton(250,100,100,50,"Next Room");
NextDoorButton nextdoorB = new NextDoorButton(50,400,150,50,"Next Door");
WallDoorButton walldoorB = new WallDoorButton(50,500,150,50, "WALL");
SaveButton saveB = new SaveButton(250,500,150,50, "Save Rooms");
XBigger xBig = new XBigger(350,50,60,50, "X+");
YBigger yBig = new YBigger(410,50,60,50, "Y+");
XSmaller xSma = new XSmaller(350,100,60,50, "X-");
YSmaller ySma = new YSmaller(410,100,60,50, "Y-");

SprintBuff sB = new SprintBuff("Sprint",1.5f);

public void setup(){
  camera_x = width/2;
  camera_y = height/2;
  current_room = 0;
  current_door = 0;
  doorTimer = 10;
  movingWall = true;
  wallB.enabled = false;
  nextwallB.enabled = false;
  doorB.enabled = false;
  newroomB.enabled = false;
  nextroomB.enabled = false;
  nextdoorB.enabled = false;
  walldoorB.enabled = false;
  saveB.enabled = false;
  xBig.enabled = false;
  yBig.enabled = false;
  xSma.enabled = false;
  ySma.enabled = false;
  /*
  JSONObject newRoom = loadJSONObject("data/rooms.json");
  newRoom.setInt("ID",0);
  JSONObject newWall = new JSONObject();
  newWall.setInt("a",0);
  newWall.setInt("b",0);
  newWall.setInt("c",0);
  newWall.setInt("d",120);
  newRoom.setJSONObject("Wall",newWall);
  saveJSONObject(newRoom,"data/rooms.json");
  */  
  
  debug = true;
  roomDebug = false;
  //start.Change = { debug ^= true; };

  if(debug){
    buttonList.add(start);
    buttonList.add(roomB);
    buttonList.add(wallB);
    buttonList.add(nextwallB);
    buttonList.add(doorB);
    buttonList.add(newroomB);
    buttonList.add(nextroomB);
    buttonList.add(nextdoorB);
    buttonList.add(walldoorB);
    buttonList.add(saveB);
    buttonList.add(xBig);
    buttonList.add(yBig);
    buttonList.add(xSma);
    buttonList.add(ySma);
  }
  
  
  Room r = new Room(0,0,200,200);
  /*
  r.AddWall(0,0,0,120);
  r.AddWall(0,0,200,0);
  r.AddWall(200,0,200,200);
  r.AddDoor(200,200,200,250);
  r.AddWall(200,250,200,300);
  r.AddWall(0,300,200,300);
  r.AddWall(0,0,0,300);
  
  r.AddObj(100,100,1,25,25);
  r.AddObj(120,160,2,30,40);
  roomList.add(r);
  
  r = new Room(300,375,200,100);
  r.AddWall(0,0,200,0);
  r.AddWall(200,0,200,25);
  r.AddDoor(200,25,200,75);
  r.AddWall(200,75,200,100);
  r.AddWall(0,100,200,100);
  r.AddWall(0,0,0,25);
  r.AddDoor(0,25,0,75);
  r.AddWall(0,75,0,100);
  */
  roomList.add(r);
  
  LoadRooms();
  
  playerList.add(pOne);
  playerList.add(pTwo);
  
  
}

public void draw(){
  if(debug){
    background(130,180,130);
    if(roomDebug){
      translate(camera_x,camera_y);
      stroke(0,0,0);
      roomList.get(current_room).Draw();
      stroke(255,0,0);
      line(-5,-5,5,5);
      line(-5,5,5,-5);
      //line(wallB.qq,wallB.ww,wallB.ee,wallB.rr);
      translate(-camera_x,-camera_y);
      fill(0,0,0);
      text("ROOMID: " + (current_room+1) + "/" + (roomList.size()),10,10);
      
      Room CurRoom = roomList.get(current_room);
      
      text("WALLID: " + (current_wall+1) + "/" + (CurRoom.wallList.size()),10,20);
      text("DOORID: " + (current_door+1) + "/" + (CurRoom.doorList.size()),10,30);
      
      if(CurRoom.wallList.size() != 0){
        for(Wall w : CurRoom.wallList){
          w.c = color(0,0,0);
        }
        CurRoom.wallList.get(current_wall).c = color(50,220,30);
        
        if(movingWall)debugKeys();
        
      }
      if(CurRoom.doorList.size() != 0 && !movingWall){debugKeys();};
      
      for(Door d : CurRoom.doorList){
        d.calcDir(CurRoom);
      }
    
    }
    
    for(Button b : buttonList){
      if(b.enabled){b.Draw();};
    };
    
    return; 
  }
  switchTimer = min(switchTimer+.5f,10);
  translate(-playerList.get(current_player).x+width/2,-playerList.get(current_player).y+height/2);
  
  
  background(200,160,120);
  for(int i = 0; i < playerList.size();i++){
     playerList.get(i).UpdatePos(rList);
  }
 
  for(int i = 0; i < rList.size();i++){
     rList.get(i).Draw();
  }
  
  for(int i = 0; i < rList.size();i++){
     rList.get(i).DrawObjsShadow();
  }
  
  for(int i = 0; i < playerList.size();i++){
     playerList.get(i).Draw();
  }
  
  for(int i = 0; i < rList.size();i++){
     rList.get(i).DrawObjs();
  }
  
 
  playerList.get(current_player).UpdateTi();
  manageKeys();
  translate(playerList.get(current_player).x-width/2,playerList.get(current_player).y-height/2);
  playerList.get(current_player).UpdateUI();
}

public void debugKeys(){
  if(keyList.contains(37)){
    camera_x++;
  }
  if(keyList.contains(38)){
    camera_y++;
  }
  if(keyList.contains(39)){
    camera_x--;
  }
  if(keyList.contains(40)){
    camera_y--;
  }
  
  if(movingWall){
    Wall cWall = roomList.get(current_room).wallList.get(current_wall);
    if(keyList.contains(17)){
    cWall.x = floor(cWall.x/5)*5;
    cWall.y = floor(cWall.y/5)*5;
    cWall.j = floor(cWall.j/5)*5;
    cWall.k = floor(cWall.k/5)*5;
      
    }
    if(keyList.contains(16)){
      if(keyList.contains(87)){
        cWall.k-=2;
      }
      if(keyList.contains(83)){
        cWall.k+=2;
      }
      if(keyList.contains(65)){
        cWall.j-=2;
      }
      if(keyList.contains(68)){
        cWall.j+=2;
      }
      return;
    }
    if(keyList.contains(87)){
      cWall.y-=2;
    }
    if(keyList.contains(83)){
      cWall.y+=2;
    }
    if(keyList.contains(65)){
      cWall.x-=2;
    }
    if(keyList.contains(68)){
      cWall.x+=2;
    }
  }
  else
  {
    Door cDoor = roomList.get(current_room).doorList.get(current_door);
    doorTimer = max(0,doorTimer-1);
    if(keyList.contains(82)&& doorTimer == 0){
      doorTimer = 10;
      int jj = cDoor.j-cDoor.x+cDoor.y;
      int kk = cDoor.k-cDoor.y+cDoor.x;
      print("J:" + jj);
      print("K:" + kk);
      cDoor.j = kk;
      cDoor.k = jj;
    }
    if(keyList.contains(87) && doorTimer == 0){
      doorTimer = 5;
      cDoor.y-=10;
      cDoor.k-=10;
    }
    if(keyList.contains(83) && doorTimer == 0){
      doorTimer = 5;
      cDoor.y+=10;
      cDoor.k+=10;
    }
    if(keyList.contains(65) && doorTimer == 0){
      doorTimer = 5;
      cDoor.x-=10;
      cDoor.j-=10;
    }
    if(keyList.contains(68) && doorTimer == 0){
      doorTimer = 5;
      cDoor.x+=10;
      cDoor.j+=10;
    } 
  }
  
}


public void manageKeys(){
  int i = 0;
  for(Ability a : playerList.get(current_player).aList){
    if(keyList.contains(49+i)){
      a.Use(playerList.get(current_player));
    }
    i++;
  }
  
  
  
  //SHIFT
  if(keyList.contains(16) && !playerList.get(current_player).bList.contains(sB)){
    playerList.get(current_player).bList.add(sB); 
  }
  else if(!keyList.contains(16)){
    playerList.get(current_player).bList.remove(sB); 
  }
  if(keyList.contains(61)){
    if(switchTimer == 10){
      switchTimer = 0;
      current_player++;
      if(current_player >= playerList.size()){
         current_player =0;
      }
    }
    
  }
  if(keyList.contains(87)){
    playerList.get(current_player).cSpd.add(new PVector(0,-playerList.get(current_player).acl));
  }
  if(keyList.contains(83)){
    playerList.get(current_player).cSpd.add(new PVector(0,playerList.get(current_player).acl));
  }
  if(keyList.contains(65)){
    playerList.get(current_player).cSpd.add(new PVector(-playerList.get(current_player).acl,0));
  }
  if(keyList.contains(68)){
    playerList.get(current_player).cSpd.add(new PVector(playerList.get(current_player).acl,0));
  }
  if(!keyList.contains(87) && !keyList.contains(83) &&
  !keyList.contains(65) && !keyList.contains(68)){
    playerList.get(current_player).cSpd.setMag(max(playerList.get(current_player).cSpd.mag()-.25f,0));  
  }
  //if(!keyList.contains(16)){ 
  //  playerList.get(current_player).cmSpd = playerList.get(current_player).mSpd;
  //}
    
}

public void mousePressed(){
  for(Button b : buttonList){
    if(b.enabled){b.Click();};
  };
}

public void keyPressed(){
  if(!keyList.contains(keyCode)){
    print(keyCode);
    keyList.add(keyCode);
  }
}

public void keyReleased(){
  if(keyList.contains(keyCode)){
    keyList.remove(keyList.indexOf(keyCode));
  }
}

public Room GetRoom(int i){
  JSONArray json = loadJSONArray("data/rooms.json");


  JSONObject jsonR = json.getJSONObject(i);  
  Room r = new Room(jsonR.getInt("x"),jsonR.getInt("y"),jsonR.getInt("w"),jsonR.getInt("h"));
  
  
  JSONArray jsonWL = jsonR.getJSONArray("Walls");  
  for(int j = 0; j < jsonWL.size(); j++){
    JSONObject jsonW = jsonWL.getJSONObject(j);
    Wall w = new Wall(jsonW.getInt("x"),jsonW.getInt("y"),jsonW.getInt("j"),jsonW.getInt("k"));
    
    r.AddWall(w);
  }
  
  JSONArray jsonDL = jsonR.getJSONArray("Doors");  
  for(int j = 0; j < jsonDL.size(); j++){
    JSONObject jsonD = jsonDL.getJSONObject(j); 
    Door d = new Door(jsonD.getInt("x"),jsonD.getInt("y"),jsonD.getInt("j"),jsonD.getInt("k"));
    
    r.AddDoor(d);
  }
  return r;
}

public void LoadRooms(){
  JSONArray json = loadJSONArray("data/rooms.json");
  
  roomList = new ArrayList<Room>();
  for(int i = 0; i < json.size(); i++){
    JSONObject jsonR = json.getJSONObject(i);  
    Room r = new Room(jsonR.getInt("x"),jsonR.getInt("y"),jsonR.getInt("w"),jsonR.getInt("h"));
    
    
    JSONArray jsonWL = jsonR.getJSONArray("Walls");  
    for(int j = 0; j < jsonWL.size(); j++){
      JSONObject jsonW = jsonWL.getJSONObject(j);
      Wall w = new Wall(jsonW.getInt("x"),jsonW.getInt("y"),jsonW.getInt("j"),jsonW.getInt("k"));
      
      r.AddWall(w);
    }
    
    JSONArray jsonDL = jsonR.getJSONArray("Doors");  
    for(int j = 0; j < jsonDL.size(); j++){
      JSONObject jsonD = jsonDL.getJSONObject(j); 
      Door d = new Door(jsonD.getInt("x"),jsonD.getInt("y"),jsonD.getInt("j"),jsonD.getInt("k"));
      
      r.AddDoor(d);
    }
    
    roomList.add(r);
  }
  
}

public void SaveRooms(){
  int id = 0;
  //Cycle through everything in RoomList
  JSONArray rooms = new JSONArray();
  for(Room r : roomList){
    JSONObject newRoom = new JSONObject();

    int wId = 0;
    JSONArray newWalls = new JSONArray();
    for(Wall w : r.wallList){
      JSONObject newWall = new JSONObject();
      
      newWall.setInt("x",w.x);
      newWall.setInt("y",w.y);
      newWall.setInt("j",w.j);
      newWall.setInt("k",w.k);
      
      newWalls.setJSONObject(wId, newWall);
      wId++;
    }
    int dId = 0;
    JSONArray newDoors = new JSONArray();
    for(Door d : r.doorList){
      JSONObject newDoor = new JSONObject();
      
      newDoor.setInt("x",d.x);
      newDoor.setInt("y",d.y);
      newDoor.setInt("j",d.j);
      newDoor.setInt("k",d.k);
      newDoor.setInt("dir",d.dir);
      
      newDoors.setJSONObject(dId, newDoor);
      dId++;
    }
    newRoom.setJSONArray("Walls",newWalls);
    newRoom.setJSONArray("Doors",newDoors);
    newRoom.setInt("x",r.x);
    newRoom.setInt("y",r.y);
    newRoom.setInt("w",r.w);
    newRoom.setInt("h",r.h);
    rooms.setJSONObject(id, newRoom);
    id++;
  }
  saveJSONArray(rooms,"data/rooms.json"); 
}

public void BaseRoom(){
  Room baseRoom = roomList.get(0);
  rList.add(baseRoom);
}

public boolean CollideCheck(Room newR){
  for(Room r : rList){
     if (r.x < newR.x + newR.w && r.x + r.w > newR.x &&
    r.y < newR.y + newR.h && r.h + r.y > newR.y){
      return false; 
    }
  }
  return true;
}

public void LockDoors(){
  for(Room r: rList){
    for(Door d: r.doorList){
      if(d.connector == null){
        r.AddWall(d.x,d.y,d.j,d.k); 
      }
    }
  }
}

public void GenerateMap(){
  while(recursionStop >= 0){
    for(int i = rList.size()-1; i >= 0; i--){
      for(Door d : rList.get(i).doorList){
         if(d.connector == null){
           Room newRoom = GetRoom((int)random(0,roomList.size()));
           //newRoom = JSON.parse(JSON.stringify(baseRoom))
           for(Door din : newRoom.doorList){
             if(d.dir == 2 && din.dir == 8){
               newRoom.TranslateX(d.x-din.x);
               newRoom.TranslateY(d.y+din.y);
               if(CollideCheck(newRoom)){
                 d.connector = din;
                 din.connector = d;
                 rList.add(newRoom);
               }
              
             }
             if(d.dir == 4 && din.dir == 6){
               newRoom.TranslateX(d.x-din.x);
               newRoom.TranslateY(d.y-din.y);
               if(CollideCheck(newRoom)){
                 d.connector = din;
                 din.connector = d;
                 rList.add(newRoom);
               }
             }
             if(d.dir == 6 && din.dir == 4){
               newRoom.TranslateX(d.x+din.x);
               newRoom.TranslateY(d.y-din.y);
               if(CollideCheck(newRoom)){
                 d.connector = din;
                 din.connector = d;
                 rList.add(newRoom);
               }
             }
             if(d.dir == 8 && din.dir == 2){
               newRoom.TranslateX(d.x-din.x);
               newRoom.TranslateY(d.y-din.y);
               if(CollideCheck(newRoom)){
                 d.connector = din;
                 din.connector = d;
                 rList.add(newRoom);
               }
             }
             
             
           }
           
           numRoom--;
           recursionStop--;
           print(numRoom);
           
           if(numRoom < 0){
             return;
           }
         }
         recursionStop--;
         
      }
     
      GenerateMap();
    }
  }
}
class Ability{
  String name;
  int c;
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
class AB_Remote extends Ability{
  SprintBuff remoteB;
  public AB_Remote(String n, Boolean p){
    super(n,p);
    remoteB = new SprintBuff("Remote",2.25f);
   
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
    scaredB = new SprintBuff("Scared",0.15f);
  }
  
  public void Passive(Player p){
    if(!playerList.get(current_player).bList.contains(scaredB)){p.bList.add(scaredB);};
  }
}
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


class Button{
  int x,y,w,h;
  String s;
  int c;
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
class Door{
  int x,y,j,k;
  int c;
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
class Map{
  ArrayList<Room> roomList = new ArrayList<Room>();
  
  public Map(){
    
  }
}
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

class Player{
  ArrayList<Ability> aList;
  float x,y;
  PVector dir;
  PVector cSpd;
  float acl;
  float mSpd;
  float cmSpd;
  float bSpd;
  int currentRoom;
  int radius = 15;
  int maxHp = 8;
  int curHp = maxHp;
  ArrayList<Buff> bList = new ArrayList<Buff>();
  public int c;
  Vision v;
  
  public Player(int xIn, int yIn){
    aList = new ArrayList<Ability>();
    x = xIn;
    y = yIn;
    cSpd = new PVector(0,0);
    acl = .35f;
    mSpd = 1.5f;
    bSpd = 0;
    cmSpd = mSpd;
    dir = new PVector(0,1);
    v = new Vision(xIn,yIn,dir);
    c = color(random(0,250),random(0,250),random(0,250));
  }
  
  public void Draw(){
    
    fill(c);
    stroke(10,10,10);
    strokeWeight(1);
    ellipse(x,y,radius*2,radius*2);
    line(x,y,x+dir.x*radius,y+dir.y*radius);
  }
  
  public void UpdateUI(){
    for(int i = 0; i < maxHp; i++){
      if(i > curHp){
        fill(0,0,0); 
      }
      fill(c);
      rect(i*15+10,25,10,20);
    }
    
    
    translate(0,height-100);
    int i = 1;
    for(Ability a : aList){
      translate(100,0);
      
      a.Draw();
      
      fill(255,255,255);
      rect(20,-8,10,16);
      fill(0,0,0);
      text(str(i),0,-10,50,20);
      i++;
    }
    translate(-100*(i-1),-height+100);
    
    int bInt = 0;
    translate(width-50,50);
    for(Buff b : bList){
      b.Draw();
      translate(0,25);
      if(bInt%2==0){
        translate(25,0); 
      }
      else{
        translate(-25,0); 
      }
      bInt++;
    }
    translate(-width+50,-50);
  }
  
  public void UpdateTi(){
      for(Ability a : aList){
        a.updateTimer();
      }
      for(int i = bList.size()-1; i >= 0; i--){
        Buff b = bList.get(i);
        b.updateTimer();
        if(!b.on){
          bList.remove(b);
          i--;
        }
      }
  }
  
  public void UpdatePos(ArrayList<Room> roomList){
    for(Ability a : aList){
      a.Passive(this);
    }
      
    for(int i = 0; i < roomList.size(); i++){
       int rX = roomList.get(i).x;
       int rY = roomList.get(i).y;
       int rW = roomList.get(i).w;
       int rH = roomList.get(i).h;
       if(x < rX + rW && x > rX && y < rY + rH && y > rY ){
         currentRoom = i;
         roomList.get(i).collision = true;
       }
     }
  
     bSpd = 0;
     for(Buff b : bList){
       b.Use(this); 
     }
      
     if(cSpd.mag() > mSpd+bSpd){
       cSpd.setMag(mSpd+bSpd);
     }
     if(cSpd.mag() > 0){
       dir = new PVector(cSpd.x,cSpd.y).setMag(cSpd.mag());
     }
     
     PVector movementVec = new PVector(x+cSpd.x,y+cSpd.y);
     //PVector speedVec = new PVector(cSpd.x,cSpd.y);
     
     fill(10,100,150);
     text(roomList.get(currentRoom).wallList.size(), 50,50);
     
     for(int i = 0; i < roomList.get(currentRoom).wallList.size(); i++){
       Wall cWall = roomList.get(currentRoom).wallList.get(i);
       if(!roomList.get(currentRoom).collision){
         break;
       }
       //speedVec.setMag(radius);
       //PVector tempVec = wall_Collision(radius,x,y,x+speedVec.x,y+speedVec.y,cWall.x,cWall.y,cWall.j,cWall.k);
       //PVector tempVec = wall_Collision(radius,x,y,cWall.x,cWall.y,x+cSpd.x,y+cSpd.y,cWall.x+cWall.j,cWall.y+cWall.k);
       PVector tempVec = wallCollision(radius,cWall.x,cWall.y,cWall.j,cWall.k,x+cSpd.x,y+cSpd.y,x,y);
       //PVector tempVecc = wallSlide(cWall.x,cWall.y,cWall.j,cWall.k,x+cSpd.x,y+cSpd.y,x,y);
       //if character collides with wall
       
       if(tempVec.x != x+cSpd.x || tempVec.y != y+cSpd.y){
         if(movementVec.x != x+cSpd.x || movementVec.y != y+cSpd.y){
           //ellipse(x,y,200,200);
           movementVec = tempVec.add(movementVec).mult(.5f);
         }
         else{
           movementVec = tempVec;
         }
       }
      
     }

     x = movementVec.x;
     y = movementVec.y;
     
     
  }
}

public float sqr(float x) { return x * x; }
public float dist2(PVector v,PVector w) { return sqr(v.x - w.x) + sqr(v.y - w.y); }
public float distToSegmentSquared(PVector p,PVector v,PVector w) {
  float l2 = dist2(v, w);
  if (l2 == 0) return dist2(p, v);
  float t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
  t = Math.min(Math.max(0, Math.min(1, t)),1);
  
  return dist2(p, new PVector(v.x + t * (w.x - v.x), v.y + t * (w.y - v.y)));
}
public PVector pointOnLine(float radius, PVector p,PVector v,PVector w){
  float l2 = dist2(v, w);
  if (l2 == 0) return null;
  float t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
  t = Math.min(Math.max(0, Math.min(1, t)),1);
 
  float rX = (1 - t) * v.x + t * w.x - p.x;
  float rY = (1 - t) * v.y + t * w.y - p.y;

  //stroke(200,150,10);
  //strokeWeight(15);
  //line(p.x,p.y,p.x+rX,p.y+rY);
  
  PVector pP = new PVector(p.x+rX,p.y+rY);
  PVector vV = new PVector(pP.x-p.x,pP.y-p.y);
  
  vV = vV.normalize();
  
  vV = vV.setMag(radius);
 
  
  //ellipse(pP.x+vV.x,pP.y+vV.y,radius,radius);
  
  //stroke(0,100,150);
  //strokeWeight(3);
  
  PVector pR = new PVector(pP.x-vV.x,pP.y-vV.y);
  
  
  //strokeWeight(1);
  
  return pR;
}
public float distToSegment(PVector p,PVector v,PVector w) {
  float Dist2 = distToSegmentSquared(p, v, w);
  //print("Dist2: " + Dist2);
  //print("Dist:  " + sqrt(Dist2));
  return sqrt(Dist2); 
}

public PVector wallCollision(float radius, float w1x,float w1y,float w2x,float w2y,float x,float y,float xWOS,float yWOS){
  float dist = distToSegmentSquared(new PVector(x,y),new PVector(w2x,w2y),new PVector(w1x,w1y));
  //print(" Dist: " + distToSegment(new PVector(x,y),new PVector(w2x,w2y),new PVector(w1x,w1y)));
  //print(" Dist: " + distToSegmentSquared(new PVector(x,y),new PVector(w2x,w2y),new PVector(w1x,w1y)));
  //ellipse(x,y,dist,dist);
  dist = sqrt(dist);
  
  PVector pLine = pointOnLine(radius, new PVector(x,y),new PVector(w2x,w2y),new PVector(w1x,w1y));
  /*
  stroke(230,20,250);
  ellipse(x,y,5,5);
  ellipse(xWOS,yWOS,5,5);
  stroke(0,200,230);
  ellipse(pLine.x,pLine.y,10,10);
  stroke(0,220,100);
  line(x,y,pLine.x,pLine.y);
  
  fill(180,0,70);
  stroke(160,0,50);
  ellipse(w1x,w1y,radius/2,radius/2);
  ellipse(w2x,w2y,radius/2,radius/2);
  */
  if(dist <= radius){
    return(new PVector(pLine.x,pLine.y));
  }
  
 
  textSize(12);
  //sstext("befAng: " + befAng, w1x-50, w1y-150);
  /*
  text("mA: " + mA, w2x-10, w2y-60);
  text("mB: " + mB, w2x-10, w2y-50);
  text("angle: " + angle, w2x-10, w2y-40);
  text("dist: " + dist, w2x-10, w2y-20);
  */
  //float distSq = sqrt(dist);
  
  
  
  //ellipse(x,y,distSq*2,distSq*2);

  return(new PVector(x,y));
}

public PVector wallSlide(float w1x,float w1y,float w2x,float w2y,float x,float y,float xWOS,float yWOS){
 float mA,mB;
  mA = (yWOS-y)/(xWOS-x);

  mB = (w1y-w2y)/(w1x-w2x);

  float angle = PI-abs(atan(mA) - atan(mB));
 
  angle = degrees(angle)-90;
  
  //float angleB = angle-90;
  
  text("angle: " + angle, w2x-10, w2y-40);
  //text("angleb: " + angleB, w2x-10, w2y-20);
  
  if( true ){//!(80 < angle && 100 > angle)){
    
    PVector pV = new PVector(x-xWOS,y-yWOS).rotate(radians(angle));
    //PVector pN = new PVector(x-xWOS,y-yWOS).rotate(radians(angleB));
    fill(200,220,20);
    PVector pVV = new PVector(xWOS+pV.x,yWOS+pV.y);
   
    fill(250,60,0);
    ellipse(xWOS+5*pV.x,yWOS+5*pV.y,5 ,5);
    ellipse(xWOS+15*pV.x,yWOS+15*pV.y,6 ,6);
    ellipse(xWOS+25*pV.x,yWOS+25*pV.y,7 ,7);
    /*
    fill(40,20,250);
    ellipse(xWOS+5* pN.x,yWOS+5*pN.y,5 ,5);
    ellipse(xWOS+15*pN.x,yWOS+15*pN.y,6 ,6);
    ellipse(xWOS+25*pN.x,yWOS+25*pN.y,7 ,7);
    */
    fill(0,0,0,0);
    return(pVV);
  }
  
  return(new PVector(0,0));
}
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
class Taytay extends Player{
  
  public Taytay(int x, int y){
    super(x,y);
    
    
    
    
    aList.add(abRemote);
    aList.add(abScared);
  }
}
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
class Wall{
  int x,y,j,k;
  int c;
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
  public void settings() {  size(1000,600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DeathG" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
