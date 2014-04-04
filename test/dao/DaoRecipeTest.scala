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
  "return sth after insert" in   {
	  val originalSize= DaoRecipe.getAllItems.size
      val postSize=originalSize+1
      val recipeID=DaoTestUtils.getSampleRecipe().id
      DaoRecipe.getAllItems must have size(postSize)
  }
  "getRecipiesForClientID " in   {
      val originalSize= DaoRecipe.getAllItems.size
      val postSize=originalSize+4
	  val clientID:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID, new Date())
	  val clientID2:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
	  DaoRecipe.createRecipe("recipeLabel", "recipeDescription", clientID2, new Date())
	 DaoRecipe.getAllItems must have size(postSize)
	  DaoRecipe.getRecipiesForClientID(clientID) must have size(3)
	  DaoRecipe.getRecipiesForClientID(clientID2) must have size(1)
  }
  
  "getRecipiesFor None ClientID " in   {
	  DaoRecipe.getRecipiesForClientID(None) must throwA[IllegalArgumentException]
  }
}


"createRecipe" should {
   "create recipe" in   {
     val originalSize= DaoRecipe.getAllItems.size
      val postSize=originalSize+2
     DaoTestUtils.getSampleRecipe()
     DaoTestUtils.getSampleRecipe()
     DaoRecipe.getAllItems must have size(postSize)
  }
   
    "save created recipe" in   {
      val clientID:Option[Long]=Some(DaoClient.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName",new Date()).id)
     val recipeID=DaoRecipe.createRecipe("label", "description", clientID, new Date()).id
     val recipe:Recipe=DaoRecipe.getItemForID(recipeID)
     recipe.label.contentEquals("label")
     recipe.description.contentEquals("description")
     clientID.get.equals(recipe.creatorID)
  }
   
  }

"deleteRecipe" should {
   "delete empty recipe" in {
     val originalSize= DaoRecipe.getAllItems.size
      val postSize=originalSize+1
     val recipeID=DaoTestUtils.getSampleRecipe().id
    DaoRecipe.getAllItems must have size(postSize) 
     DaoRecipe.deleteRecipe(Some(recipeID))
     DaoRecipe.getAllItems must have size(originalSize) 
  }
   
    "delete non empty recipe" in  {
       val originalSize= DaoRecipe.getAllItems.size
      val postSize=originalSize+1
     val recipeID=DaoTestUtils.getSampleRecipe().id
     val clientID=DaoTestUtils.getSampleClient().id
     DaoVine.createVineWithRecipe("label", "description",Some(clientID),Some(recipeID), new Date())
     DaoVine.createVineWithRecipe("label2","description", Some(clientID),Some(recipeID), new Date())
     DaoRecipe.getAllItems must have size(postSize) 
     DaoRecipe.deleteRecipe(Some(recipeID))
     DaoRecipe.getAllItems must have size(originalSize) 
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
     val recipeID=DaoTestUtils.getSampleRecipe().id
     DaoRecipe.getItemForID(recipeID).visible must beTrue
     DaoRecipe.deactivateRecipe(Some(recipeID))
     DaoRecipe.getItemForID(recipeID).visible must beFalse
     DaoRecipe.activateRecipe(Some(recipeID))
     DaoRecipe.getItemForID(recipeID).visible must beTrue
  }
   "throw errow when activating None recipe" in  {
     DaoRecipe.activateRecipe(None) must
            throwA[IllegalArgumentException]
  }
}

"deactivateRecipe" should {
   "deactivateRecipe" in {
     val recipeID=DaoTestUtils.getSampleRecipe().id
     DaoRecipe.getItemForID(recipeID).visible must beTrue
     DaoRecipe.deactivateRecipe(Some(recipeID))
      DaoRecipe.getItemForID(recipeID).visible must beFalse
  }
   
   "throw errow when deactivating None recipe" in  {
     DaoRecipe.deactivateRecipe(None) must
            throwA[IllegalArgumentException]
  }
}

}