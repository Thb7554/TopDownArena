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
  public color c;
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

float sqr(float x) { return x * x; }
float dist2(PVector v,PVector w) { return sqr(v.x - w.x) + sqr(v.y - w.y); }
float distToSegmentSquared(PVector p,PVector v,PVector w) {
  float l2 = dist2(v, w);
  if (l2 == 0) return dist2(p, v);
  float t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
  t = Math.min(Math.max(0, Math.min(1, t)),1);
  
  return dist2(p, new PVector(v.x + t * (w.x - v.x), v.y + t * (w.y - v.y)));
}
PVector pointOnLine(float radius, PVector p,PVector v,PVector w){
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
float distToSegment(PVector p,PVector v,PVector w) {
  float Dist2 = distToSegmentSquared(p, v, w);
  //print("Dist2: " + Dist2);
  //print("Dist:  " + sqrt(Dist2));
  return sqrt(Dist2); 
}

PVector wallCollision(float radius, float w1x,float w1y,float w2x,float w2y,float x,float y,float xWOS,float yWOS){
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

PVector wallSlide(float w1x,float w1y,float w2x,float w2y,float x,float y,float xWOS,float yWOS){
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
