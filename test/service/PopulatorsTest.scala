package service

import org.specs2.mutable.Specification
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models.Client
import models.Vine
import services.DataService
import models.VineHistory
import models.Recipe
import play.api.Play
import dao.DaoTesting
import populators.ClientPopulator
import pojo.ClientData
import dao.util.BaseTest
import models.VineComment
import pojo.CommentData
import populators.CommentPopulator
import populators.RecipePopulator
import populators.VineHistoryPopulator
import populators.RecipePopulator
import populators.VinePopulator
import populators.RecipePopulator
import models.Model
import dao.util.DaoTestUtils
import pojo.VineData
import populators.IdentityPopulator
import securesocial.core.AuthenticationMethod

class PopulatorsTest extends BaseTest {
   
import anorm._


  "populate client" should {
   
    "populate none object" in   {
     ClientPopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
    
    " populate full  object" in   {
       val client:Option[Client]=Some(DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName"))
       val clientData=ClientPopulator.populate(client)
       clientData.firstName equals client.get.firstName
       clientData.lastName equals client.get.lastName
       clientData.login equals client.get.login
       clientData.id equals client.get.id
       clientData.registered equals client.get.registered
    }
}
  
  "populate comment " should {
   
    "populate full object" in   {
      val client:Client=(DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName"))
      val vine:Vine=DataService.createVine("label","description",client,None);
      val comment:Option[VineComment]=Some(DataService.createComment("login",vine, client))
      val commentData=CommentPopulator.populate(comment)
      commentData.id equals comment.get.id
      commentData.created equals comment.get.created
      commentData.content.contentEquals(comment.get.content)
//       commentData.creator.id equals comment.get.creatorID
//       commentData.vine.id equals comment.get.vineID
    }
    
    "populate none object" in   {
     CommentPopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
    
}
  
  "populate recipe " should {
   
    "populate full object" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val recipe:Recipe=DataService.createRecipe(client,"label","description")
      val recipeData=RecipePopulator.populate(Some(recipe))
      assert(recipeData.creator.get.id.equals(client.id))
      assert(recipeData.creator.get.firstName.equals(client.firstName))
      assert( recipeData.creator.get.lastName.equals(client.lastName))
      recipeData.label equals recipe.label
      recipeData.description equals recipe.description
    }
    
   "populate none object" in   {
     RecipePopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
}
  
  "populate vine_history" should {
   
    "populate full object" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val vineHistory:VineHistory=DataService.createVineHistory(vine, "label", "description")
      val vineHistoryData= VineHistoryPopulator.populate(Some(vineHistory))
      vineHistoryData.label.contentEquals("label")
	  vineHistoryData.description.contentEquals("description")
	  vineHistoryData.vine.get.id equals vine.id
    }
    
    "try populate none object" in   {
     VineHistoryPopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
}
  
  "populate vine" should {
   
    "populate full object" in   {
     val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
     val vineModel:Vine=DataService.createVine("label","description",client,None);
     val vineData :VineData=VinePopulator.populate(Some(vineModel))
      assert( vineModel.id.equals(vineData.id))
     vineData.label.contentEquals("label")
     vineData.description.contentEquals("description")
     vineData.client.get.id equals client.id      
     vineData.client.get.firstName equals client.firstName
     vineData.client.get.lastName equals client.lastName
     vineData.client.get.login equals client.login
//     System.out.println(vineData.client.get.registered)
//     System.out.println(client.registered)
//     vineData.client.get.registered.equals(client.registered)
//     FIX-ME!!!!
    }
    
    "try populate none object" in   {
     VinePopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
}
  
  
  
  
  
    "populate identity" should {
   
    "populate SocialSecurityClient from client" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val identity= IdentityPopulator.populate(Some(client))
      identity.firstName equals "firstName"
      identity.lastName equals "lastName"
      identity.email.get equals client.login
      identity.oAuth1Info equals None
      identity.oAuth2Info equals None
      identity.avatarUrl equals None
      identity.authMethod equals AuthenticationMethod.UserPassword
      identity.identityId.providerId equals ""
      identity.identityId.userId equals client.login
    }
    
    "try populate none object" in   {
     IdentityPopulator.populate(None) must
            throwA[IllegalArgumentException]
    }
}
  
  
  
  

}



