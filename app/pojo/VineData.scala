package pojo

import java.util.Date

case class VineData(var id: Long,var label: String, var description:String,var client:Option[ClientData],recipe:Option[RecipeData],created:Date)
{ def this() = this(0,"","",None,None,new Date())
}
object Vine extends Data {


  
}