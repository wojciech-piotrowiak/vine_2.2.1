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


class DaoVineTest extends BaseTest  {
"getVines" should {
  "be empty after clean " in  {
	  DaoTesting.cleanAll()
      DaoVine.getAllItems must beEmpty
  }
  "return plain vines" in   {
	  DaoTesting.cleanAll()
	  val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
	  DaoVine.createVine("test2","description",clientID,new Date())
	  DaoVine.createVine("test3","description",clientID,new Date())
      DaoVine.getAllItems must have size(2)
  }
  
  "return recipe auto vines" in  {
	  DaoTesting.cleanAll()
	  val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
	  val recipeID=DaoRecipe.createRecipe("label", "description", clientID, new Date())
	  val minVal:Option[Long]=Some(recipeID.id)
	  DaoVine.createVineWithRecipe("test2","description",clientID,minVal,new Date())
	  DaoVine.createVineWithRecipe("test3","description",clientID,minVal,new Date())
      DaoVine.getAllItems must have size(2)
  }
  
   "return recipe non-auto vines" in  {
	  DaoTesting.cleanAll()
	  val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
	  val recipeID=Some(DaoTestUtils.getSampleRecipe().id)
	  DaoVine.createVineWithRecipe("test2","description",clientID,recipeID,new Date())
	  DaoVine.createVineWithRecipe("test3","description",clientID,recipeID,new Date())
     DaoVine.getAllItems must have size(2)
  }
  
  "return VinesForRecipeID" in  {
	  DaoTesting.cleanAll()
	  val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
	  val recipeID:Option[Long]=Some(DaoTestUtils.getSampleRecipe().id)
	  DaoVine.createVineWithRecipe("test2","description",clientID,recipeID,new Date())
	  DaoVine.createVineWithRecipe("test3","description",clientID,recipeID,new Date())
	  DaoVine.createVine("test3","description",clientID,new Date())
      DaoVine.getVinesForRecipeID(recipeID) must have size(2)
	  DaoVine.getAllItems must have size(3)
  }
  
  "throw error when none VinesForRecipeID" in  {
      DaoVine.getVinesForRecipeID(None) must  throwA[IllegalArgumentException]
  }
}

"createVine" should {
   "create vine without recipe" in  {
     DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
     DaoVine.createVine("test1","description",clientID,new Date())
     DaoVine.createVine("test2","description",clientID,new Date())
     DaoVine.getAllItems must have size(2)
  }
   
    "create vine with recipe" in   {
     DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
     val recipeID:Option[Long]=Some(DaoRecipe.createRecipe("label", "description", clientID, new Date()).id)
     DaoVine.createVineWithRecipe("test1","description",clientID,recipeID,new Date())
     DaoVine.createVineWithRecipe("test2","description",clientID,recipeID,new Date())
     DaoVine.getAllItems must have size(2)
  }
   
    "save created vine without recipe" in  {
     DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
     val firstId:Long=   DaoVine.createVine("test1","description",clientID,new Date()).id
     val vine:Vine=DaoVine.getAllItems.head
     vine.label.contentEquals("test1")
     vine.id.equals(firstId)
     vine.clientID.equals(clientID)
     vine.recipe.equals(None)
  }
    
    "save created non-auto vine with recipe" in {
     DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
     val recipeID:Option[Long]=Some(DaoTestUtils.getSampleRecipe().id)
     val firstId:Long=   DaoVine.createVineWithRecipe("test1","description",clientID,recipeID,new Date()).id
     val vine:Vine=DaoVine.getAllItems.head
     vine.label.contentEquals("test1")
     vine.id.equals(firstId)
     vine.clientID.equals(clientID)
     vine.recipe.equals(recipeID)
  }
  }

"deleteVine" should {
   "delete vine without recipe" in  {
     DaoTesting.cleanAll()
     val id:Long=DaoTestUtils.getSampleVine().id
     DaoVine.deleteVine(id)
    DaoVine.getAllItems must have size(0)      
  }
   
   "delete vine with recipe" in   {
     DaoTesting.cleanAll()
     val id:Long=DaoTestUtils.getSampleRecipeVine().id
     DaoVine.deleteVine(id)
    DaoVine.getAllItems must have size(0)      
  }
     "delete not existing vine" in   {
     DaoTesting.cleanAll()
     DaoVine.deleteVine(22)
     true
  }
}

"getUserVines" should {
   
    "return nothing for new user" in {
     DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient().id)
     DaoVine.getVinesForClientID(clientID) must beEmpty
    }
     
    "return sth for active client" in   {
     DaoTesting.cleanAll()
      val clientID:Option[Long]=Some(DaoClient.createClient("1", "firstName", "lastName",new Date()).id)
     DaoVine.createVine( "testVine","description",clientID,new Date())
      val clientID2:Option[Long]=Some(DaoClient.createClient("2", "firstName", "lastName",new Date()).id)
     DaoVine.createVine( "testVine","description",clientID2,new Date())
     DaoVine.getVinesForClientID(clientID).size.equals(1)
    }
    
    "return nothing for not existing user" in   {
     DaoTesting.cleanAll()
     DaoVine.getVinesForClientID(Some(222)) must beEmpty
    }
    
    "throw error for none" in   {
     DaoTesting.cleanAll()
     DaoVine.getVinesForClientID(None)  must
            throwA[IllegalArgumentException]
    }
  
}

"deactivateVine" should {
  
  "deactivateVine" in {
    DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoClient.createClient("1", "firstName", "lastName",new Date()).id)
     val id:Long=DaoVine.createVine( "testVine","description",clientID,new Date()).id
     DaoVine.getVinesForClientID(clientID).head.visible must beTrue
     DaoVine.deactivateVine(Some(id));
     DaoVine.getVinesForClientID(clientID).head.visible must beFalse
  }
  "deactivate None Vine" in {
     DaoVine.deactivateVine(None)  must
            throwA[IllegalArgumentException]
  }
}

"activateVine" should {
  
  "activateVine" in {
    DaoTesting.cleanAll()
     val clientID:Option[Long]=Some(DaoClient.createClient("1", "firstName", "lastName",new Date()).id)
     val id:Long=DaoVine.createVine( "testVine","description",clientID,new Date()).id
     DaoVine.getVinesForClientID(clientID).head.visible must beTrue
     DaoVine.deactivateVine(Some(id))
     DaoVine.getVinesForClientID(clientID).head.visible must beFalse
     DaoVine.activateVine(Some(id))
     DaoVine.getVinesForClientID(clientID).head.visible must beTrue
  }
   "activate None Vine" in {
     DaoVine.activateVine(None)  must
            throwA[IllegalArgumentException]
  }
}



}
