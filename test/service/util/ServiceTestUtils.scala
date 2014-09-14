package service.util

import models.Client
import dao.util.DaoTestUtils
import dao.DaoClient
import java.util.Date

object ServiceTestUtils {
def getSampleClientModel():Client=
{
  val login=DaoTestUtils.getNextClientLogin
  val date=new Date()
  val id=DaoClient.createClient(login, "firstName", "lastName",date,"password")
  val client:Client= new Client(id.id,id.gid, login,"firstName","lastName",date,true,"password")
   client.setGid(id.gid)
   return client;
}

}