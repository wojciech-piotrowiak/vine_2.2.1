package pojo

import java.util.Date



case class ClientData(var id: Long,var login: String,var  firstName: String,var lastName: String,var registered:Date){
  def this() = this(0,"","","",new Date())
}

object ClientData extends Data {
  
}

