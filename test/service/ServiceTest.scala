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
import dao.util.BaseTest
import models.VineComment
import dao.DaoClient
import dao.DaoVine
import dao.DaoVineHistory
import dao.DaoComment
import dao.DaoRecipe
import dao.util.DaoTestUtils

class ServiceTest extends BaseTest {
   
import anorm._



  "createClient" should {
   
    "createClient" in   {
        val login= DaoTestUtils.getNextClientLogin()
	    val c:Client=DataService.createClient(login, "firstName", "lastName")
	    c.login.contentEquals(login)
	    c.firstName.contentEquals("firstName")
	    c.lastName.contentEquals("lastName")
	    c.active==true
    }
    
    "removeClient" in   {
        val originalSize= DaoClient.getAllItems.size
        val preSize=originalSize+1
	    val c:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
	    DaoClient.getAllItems.size==preSize
	    DataService.deleteModel(c);
	    DaoClient.getAllItems.size==originalSize
    }
    
    "getClientForID -existing" in   {
	    val login:String=DaoTestUtils.getNextClientLogin()
	    val c:Client=DataService.createClient(login, "firstName", "lastName")
	    val client:Client=DataService.getClientForID(c.id)
	    client.login.contentEquals(login)
	    client.firstName.contentEquals("firstName")
	    client.lastName.contentEquals("lastName")
    }
    
     "getClientFor nonexisting ID" in   {
	   DataService.getClientForID(12345)must
            throwA[IllegalArgumentException]
    }
}

"createVine" should {
  
   "createVine without recipe" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      vine.label.contentEquals("label")
      vine.clientID==client.id
      vine.description.contentEquals("description")
      vine.visible==true
    }  
   
   "createVine with recipe" in  {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val recipe:Recipe=DataService.createRecipe(client, "label", "description")
      val vine:Vine=DataService.createVine("label","description",client,Option(recipe));
      vine.label.contentEquals("label")
      vine.clientID==client.id
      vine.visible==true
      vine.recipe.get==recipe.id
      vine.description.contentEquals("description")
    }
   "removeVine" in   {
	  val originalSize= DaoVine.getAllItems.size
      val preSize=originalSize+1
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
	  DaoVine.getAllItems.size==preSize
	  DataService.deleteModel(vine);
	  DaoVine.getAllItems.size==originalSize
    }
   
   "getVineForID" in   {
	  val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
	    
	   val sVine:Vine=DataService.getVineForID(vine.id)
	   sVine.label.contentEquals(vine.label)
	   sVine.description.contentEquals(vine.description)
       sVine.clientID==vine.clientID

    }
   
    "getVineFor nonexisting ID" in   {
    	DataService.getVineForID(12345) must
            throwA[IllegalArgumentException]
    }
}


"createVineHistory" should {
  
   "createVineHistory" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val vineHistory:VineHistory=DataService.createVineHistory(vine, "label", "description")
      vineHistory.label.contentEquals("label");
      vineHistory.description.contentEquals("description");
    }  
   
   "removeVineHistory" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val vineHistory:VineHistory=DataService.createVineHistory(vine, "label", "description")
	  DaoVineHistory.getVineHistory(vine.id).size==1
	  DataService.deleteModel(vineHistory);
	  DaoVineHistory.getVineHistory(vine.id).size==0
    }
   
   "getVineHistoryForID" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val vineHistory:VineHistory=DataService.createVineHistory(vine, "label", "description")
      
      val sVineHistory:VineHistory=DataService.getVineHistoryForID(vineHistory.id)
	  sVineHistory.label.contentEquals("label");
      sVineHistory.description.contentEquals("description");
    }
   
    "getVineHistoryFor not existing ID" in   {
     DataService.getVineHistoryForID(12345)  must
            throwA[IllegalArgumentException]
    }
}


"createComment" should {
  
   "createComment" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val comment:VineComment=DataService.createComment("content",vine,client)
      comment.content=="content"
      comment.vineID==vine.id
      comment.creatorID==client.id;
    }  
   
   "removeComment" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val comment:VineComment=DataService.createComment("content",vine,client)
      
	  DaoComment.getCommentsForClient(Some(client.id)).size==1
	  DataService.deleteModel(comment);
	  DaoComment.getCommentsForClient(Some(client.id)).size==0
    }
   
   
    "getCommentForID" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val vine:Vine=DataService.createVine("label","description",client,None);
      val comment:VineComment=DataService.createComment("content",vine,client)
      val sComment:VineComment=DataService.getCommentForID(comment.id)
      
      sComment.content.contentEquals("content")
      sComment.vineID==vine.id
      sComment.creatorID==client.id
    }  
    
    "getCommentFor not existing ID" in   {
		 DataService.getCommentForID(12345) must     
		 throwA[IllegalArgumentException]
    }  
}


"createRecipe" should {
  

   "createRecipe" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val recipe:Recipe=DataService.createRecipe(client,"label","description")
      recipe.creatorID==client.id
      recipe.label.contentEquals("label")
      recipe.description.contentEquals("description")
    }  
   
   "removeRecipe" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val recipe:Recipe=DataService.createRecipe(client,"label","description")
      
	  var size= DaoRecipe.getAllItems.size
	  size=size-1
	  DataService.deleteModel(recipe);
	  DaoRecipe.getAllItems.size==size
    }
   
    
   "getRecipeForID" in   {
      val client:Client=DataService.createClient(DaoTestUtils.getNextClientLogin(), "firstName", "lastName")
      val recipe:Recipe=DataService.createRecipe(client,"label","description")
      val sRecipe:Recipe=DataService.getRecipeForID(recipe.id)
     
	 sRecipe.creatorID==client.id
      sRecipe.label.contentEquals("label")
      sRecipe.description.contentEquals("description")
    }
   
     
   "getRecipeFor non existing ID" in   {
	DataService.getRecipeForID(123445)  must     		 throwA[IllegalArgumentException]
    }

}

}