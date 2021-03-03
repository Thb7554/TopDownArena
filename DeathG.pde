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

SprintBuff sB = new SprintBuff("Sprint",1.5);

void setup(){
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
  size(1000,600);
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

void draw(){
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

void debugKeys(){
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


void manageKeys(){
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

void mousePressed(){
  for(Button b : buttonList){
    if(b.enabled){b.Click();};
  };
}

void keyPressed(){
  if(!keyList.contains(keyCode)){
    print(keyCode);
    keyList.add(keyCode);
  }
}

void keyReleased(){
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
