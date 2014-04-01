import org.specs2.mutable.Specification
import scala.language.postfixOps
import org.specs2.execute.Pending
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.db.DB
import java.lang.Boolean
import play.api.Play.current
import models.Client
import models.Vine
import java.util.Date
import models.Recipe
import scala.AnyVal
import org.specs2.specification.Fragments
import org.specs2.specification.Step
import play.api.Play
import dao.DaoTesting
import dao.DaoClient
import dao.DaoVine
import dao.DaoVineHistory
import dao.DaoRecipe
import dao.DaoComment
import dao.util.BaseTest
import dao.util.DaoTestUtils


class DaoClientTest extends BaseTest  {


"create client" should {
   "create client" in  {
     val originalSize= DaoClient.getAllItems.size
     val postSize=originalSize+3
     DaoClient.createClient("1", "firstName", "lastName",new Date())
     DaoClient.createClient("2", "firstName", "lastName",new Date())
     DaoClient.createClient("3", "firstName", "lastName",new Date())
     DaoClient.getAllItems must have size(postSize)
  }
   "cannot create repeated user" in  {
     val loginID=DaoTestUtils.getNextClientLogin
     DaoClient.createClient(loginID, "firstName", "lastName",new Date())
     DaoClient.createClient(loginID, "firstName", "lastName",new Date())  must
            throwA[IllegalArgumentException]
   }
   
    "save created client" in  {
     val id:Long=  DaoTestUtils.getSampleClient.id
     val client:Client=DaoClient.getItemForID(id)
     client.firstName.contentEquals("firstName")
     client.lastName.contentEquals("lastName")
     client.login==4
     client.id==id
   }
    }

"deleteClient" should {
    "delete client" in {
     val originalSize= DaoClient.getAllItems.size
     val preSize=originalSize+1
     
    val loginID=DaoTestUtils.getNextClientLogin
     DaoClient.createClient(loginID, "firstName", "lastName",new Date())
    DaoClient.getAllItems must have size(preSize)           
     DaoClient.deleteClient(loginID);
    DaoClient.getAllItems must have size(originalSize)           
  }
     "delete not existing client" in   {
     DaoClient.deleteClient("1111")
     true
  }
}

"activateClient" should {
    "activate client" in {
       val loginID=DaoTestUtils.getNextClientLogin
    val clientID= DaoClient.createClient(loginID, "firstName", "lastName",new Date()).id
     val client:Client=DaoClient.getItemForID(clientID) 
     DaoClient.activateClient(loginID);
     val activatedClient=DaoClient.getItemForID(clientID) 
     client.active must beFalse //new born users are NOT active
     activatedClient.active must  beTrue
}
}

"deactivateClient" should {
    "deactivate client" in {
       val loginID=DaoTestUtils.getNextClientLogin
    val clientID= DaoClient.createClient(loginID, "firstName", "lastName",new Date()).id
     val client:Client=DaoClient.getItemForID(clientID)
     DaoClient.activateClient(loginID);
     val activatedClient=DaoClient.getItemForID(clientID) 
     DaoClient.deactivateClient(loginID);
     val deactivatedClient=DaoClient.getItemForID(clientID) 
     client.active must beFalse //new born users are NOT active
     activatedClient.active must  beTrue
     deactivatedClient.active must beFalse 
  }
    
    //TODO - consider deactivating client recipe,vine,comments...
}



}

