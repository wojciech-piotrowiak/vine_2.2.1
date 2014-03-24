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


class DaoClientTest extends BaseTest  {

"getClient"  should {
   "be empty after clean " in   {
	  DaoTesting.cleanAll()
      DaoClient.getClients() must beEmpty
  }
  "return sth after insert" in   {
     DaoTesting.cleanAll()
     DaoClient.createClient("1", "firstName", "lastName",new Date())
     DaoClient.createClient("2", "firstName", "lastName",new Date())
     DaoClient.getClients() must have size(2)
  }
  
}

"create client" should {
   "create client" in  {
     DaoTesting.cleanAll()
     DaoClient.createClient("1", "firstName", "lastName",new Date())
     DaoClient.createClient("2", "firstName", "lastName",new Date())
     DaoClient.createClient("3", "firstName", "lastName",new Date())
     DaoClient.getClients() must have size(3)
  }
   "cannot create repeated user" in  {
     DaoTesting.cleanAll()
     DaoClient.createClient("1", "firstName", "lastName",new Date())
     DaoClient.createClient("1", "firstName", "lastName",new Date())  must
            throwA[IllegalArgumentException]
   }
   
    "save created client" in  {
     DaoTesting.cleanAll()
     val id:Long=  DaoClient.createClient("1", "firstName", "lastName",new Date()).id
     val client:Client=DaoClient.getClients().head
     client.firstName.contentEquals("firstName")
     client.lastName.contentEquals("lastName")
     client.login==4
     client.id==id
   }
    }

"deleteClient" should {
    "delete client" in {
     DaoTesting.cleanAll()
     DaoClient.createClient("4", "firstName", "lastName",new Date())
     DaoClient.getClients() must have size(1)           
     DaoClient.deleteClient("4");
     DaoClient.getClients() must have size(0)           
  }
     "delete not existing client" in   {
     DaoClient.getClients() must beEmpty
     DaoClient.deleteClient("4")
     true
  }
}

"activateClient" should {
    "activate client" in {
     DaoTesting.cleanAll()
     DaoClient.createClient("4", "firstName", "lastName",new Date())
     val client:Client=DaoClient.getClients().head     
     DaoClient.activateClient("4");
     val activatedClient=DaoClient.getClients().head
     client.active must beFalse //new born users are NOT active
     activatedClient.active must  beTrue
}
}

"deactivateClient" should {
    "deactivate client" in {
      DaoTesting.cleanAll()
      DaoClient.createClient("4", "firstName", "lastName",new Date())
     val client:Client=DaoClient.getClients().head     
     DaoClient.activateClient("4");
     val activatedClient=DaoClient.getClients().head
     DaoClient.deactivateClient("4");
     val deactivatedClient=DaoClient.getClients().head
     client.active must beFalse //new born users are NOT active
     activatedClient.active must  beTrue
     deactivatedClient.active must beFalse 
  }
    
    //TODO - consider deactivating client recipe,vine,comments...
}



}

