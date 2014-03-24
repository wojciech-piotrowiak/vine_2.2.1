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


class DaoRecipeTest extends BaseTest {

"getRecipies" should {
  "be empty after clean " in  {
	  DaoTesting.cleanAll()
      DaoRecipe.getRecipies() must beEmpty
  }
  "return sth after insert" in   {
	  DaoTesting.cleanAll()
      val recipeID=DaoTestUtils.getSampleRecipe().id
      DaoRecipe.getRecipies() must have size(1)
  }
  "getRecipiesForClientID " in   {
	  DaoTesting.cleanAll()
	  val clientID:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  val clientID2:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID2, new Date())
	  DaoRecipe.getRecipies() must have size(4)
	  DaoRecipe.getRecipiesForClientID(clientID) must have size(3)
	  DaoRecipe.getRecipiesForClientID(clientID2) must have size(1)
  }
  
  "getRecipiesFor None ClientID " in   {
	  DaoRecipe.getRecipiesForClientID(None) must throwA[IllegalArgumentException]
  }
}


"createRecipe" should {
   "create recipe" in   {
     DaoTesting.cleanAll()
     DaoTestUtils.getSampleRecipe()
     DaoTestUtils.getSampleRecipe()
     DaoRecipe.getRecipies() must have size(2)
  }
   
    "save created recipe" in   {
     DaoTesting.cleanAll()
      val clientID:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
     DaoRecipe.createRecipe("label", "description", clientID, new Date())
     val recipe:Recipe=DaoRecipe.getRecipies().head
     recipe.label.contentEquals("label")
     recipe.description.contentEquals("description")
     clientID.get.equals(recipe.creatorID)
  }
   
  }

"deleteRecipe" should {
   "delete empty recipe" in {
     DaoTesting.cleanAll()
     val recipeID=DaoTestUtils.getSampleRecipe().id
     DaoRecipe.getRecipies() must have size(1) 
     DaoRecipe.deleteRecipe(Some(recipeID))
     DaoRecipe.getRecipies() must have size(0) 
  }
   
    "delete non empty recipe" in  {
    DaoTesting.cleanAll()
     val recipeID=DaoTestUtils.getSampleRecipe().id
     val clientID=DaoTestUtils.getSampleClient().id
     DaoVine.createVineWithRecipe("label", "description",Some(clientID),Some(recipeID), new Date())
     DaoVine.createVineWithRecipe("label2","description", Some(clientID),Some(recipeID), new Date())
     DaoRecipe.getRecipies() must have size(1) 
     DaoRecipe.deleteRecipe(Some(recipeID))
     DaoRecipe.getRecipies() must have size(0) 
  }
     "delete not existing recipe" in   {
     DaoRecipe.deleteRecipe(Some(154))
     true
  }
     
    "throw errow when deleting None recipe" in  {
     DaoRecipe.deleteRecipe(None) must
            throwA[IllegalArgumentException]
  }
}
"activateRecipe" should {
   "activateRecipe" in {
     DaoTesting.cleanAll()
     val recipeID=DaoTestUtils.getSampleRecipe().id
     DaoRecipe.getRecipies().head.visible must beTrue
     DaoRecipe.deactivateRecipe(Some(recipeID))
     DaoRecipe.getRecipies().head.visible must beFalse
     DaoRecipe.activateRecipe(Some(recipeID))
     DaoRecipe.getRecipies().head.visible must beTrue
  }
   "throw errow when activating None recipe" in  {
     DaoRecipe.activateRecipe(None) must
            throwA[IllegalArgumentException]
  }
}

"deactivateRecipe" should {
   "deactivateRecipe" in {
     DaoTesting.cleanAll()
     val recipeID=DaoTestUtils.getSampleRecipe().id
     DaoRecipe.getRecipies().head.visible must beTrue
     DaoRecipe.deactivateRecipe(Some(recipeID))
     DaoRecipe.getRecipies().head.visible must beFalse
  }
   
   "throw errow when deactivating None recipe" in  {
     DaoRecipe.deactivateRecipe(None) must
            throwA[IllegalArgumentException]
  }
}

}