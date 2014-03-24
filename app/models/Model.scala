package models



abstract class Model {
  private var gid:Long=0;
  
 def getGID(): Long=
 {
   return gid
 }
 
 def setGid(gid:Long)
 {
   this.gid=gid
 }

}