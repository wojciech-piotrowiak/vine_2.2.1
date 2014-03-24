package pojo

import java.util.Date

case class CommentData(var id: Long,var content:String,var vine:VineData,var creator:ClientData,var created:Date,var restricted:Boolean){
  def this() = this(0,"",null,null,new Date(),false)
}
object CommentData extends Data {


  
}