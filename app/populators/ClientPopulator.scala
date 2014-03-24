package populators

import pojo.ClientData
import models.Client
import play.api.Logger
import com.sun.xml.internal.ws.util.xml.CDATA

class ClientPopulator {

}

object ClientPopulator extends BasePopulator[Client,ClientData] {

  
 override def populate(clientModel:Option[Client]):ClientData=
 {
  val cData:ClientData =super.populate(clientModel)
  if(clientModel.isDefined)
  {
   var cModel:Client=clientModel.get
   cData.id=cModel.id
   cData.firstName=cModel.firstName
   cData.lastName=cModel.lastName
   cData.login=cModel.login
   cData.registered=cModel.registered
  }
   return cData;
    
 }  
  
 override def create:ClientData={
    new  ClientData()
  }
}