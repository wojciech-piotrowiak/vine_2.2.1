package pojo

import java.util.Date



case class RecipeData(var id: Long,var label:String,var description:String,var creator:Option[ClientData],var created:Date)
{ def this() = this(0,"","",None,new Date())
}
object RecipeData extends Data {


  
}