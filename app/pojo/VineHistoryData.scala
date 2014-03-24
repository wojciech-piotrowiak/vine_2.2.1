package pojo

import java.util.Date

case class VineHistoryData(id: Long,var vine:Option[VineData],var label: String,var description: String,created:Date)
{ def this() = this(0,None,"","",new Date())
}
object VineHistoryData extends Data {


  
}