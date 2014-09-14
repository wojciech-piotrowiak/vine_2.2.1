package services

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.{Logger, Application}
import java.util.Date
import models.Client
import models.Vine
import models.VineHistory
import models.Recipe
import dao.DaoRecipe
import dao.DaoVineHistory
import dao.DaoVine
import dao.DaoClient
import models.VineComment
import dao.DaoComment
import models.Model
import dao.DaoUtils
import dao.BaseEntity
import anorm.RowParser
import dao.BaseDao
import dao.DaoRecipe



case class DataService()

object DataService {
  
 def createClient(login: String, firstName: String, lastName: String,password:String) :Client= {
   val registered:Date=new Date()
   val id:BaseEntity=DaoClient.createClient(login, firstName, lastName, registered,password)
   val client:Client= new Client(id.id,id.gid, login,firstName,lastName, registered,true,password)
   client.setGid(id.gid)
   return client;
 }
 
  def saveClient(clientID:Long) :Client= {
   val client:Client= DaoClient.getItemForID(clientID)
   return client;
 }
 
 
  def getClientForLogin(id:String) :Option[Client]= {
   try{
    return DaoClient.getClientForLogin(id)
   }
   catch
   {
     //SqlMappingError
     case e: Exception => return None
   }
 }
 
  def createVine(label: String,description:String,client: Client,recipe:Option[Recipe]) :Vine= {
   val created:Date=new Date()
   var id:BaseEntity=null
   var  vine:Vine=null
   if(recipe.isEmpty){
     id=DaoVine.createVine(label,description,Option(client.id), created)
    vine=new Vine(id.id,id.gid, label,description,  client.id,created,None,true)
      }
   else{
      id=DaoVine.createVineWithRecipe(label,description, Option(client.id),Option(recipe.get.id), created)
     vine= new Vine(id.id,id.gid, label,description,client.id,created,Option(recipe.get.id),true)
      }  
   
   vine.setGid(id.gid)
   return vine;
 }
  
  
   def getVineForID(id:Long) :Vine= {
    try{
    	return getModelForID(DaoVine, id).asInstanceOf[Vine]
    }
   catch
   {
     case e: Exception => throw new IllegalArgumentException("Vine does not exist")
   }
  return null
 }
  
   def createVineHistory(vine: Vine,label: String,description:String) :VineHistory= {
   val created:Date=new Date()
   val id:BaseEntity=DaoVineHistory.createVineHistory(label,description, vine.id, created);
   val vineHistory=new  VineHistory(id.id,id.gid,vine.id, label, description,created,true);
   vineHistory.setGid(id.gid)
   return vineHistory;
 }
   
    def getVineHistoryForID(id:Long) :VineHistory= {
    try{
    return  getModelForID(DaoVineHistory, id).asInstanceOf[VineHistory]
    }
   catch
   {
     case e: Exception => throw new IllegalArgumentException("Vine History does not exist")
   }
  return null
 }
   
 def createComment(content: String,vine:Vine,client: Client) :VineComment= {
		 val created:Date=new Date()
 val id:BaseEntity=DaoComment.createComment(content,Option(vine.id), Option(client.id), created)
 val vineComment= new VineComment(id.id,id.gid, content,vine.id,client.id,created,false,true)
		 vineComment.setGid(id.gid)
		 return vineComment
 }
 
 def getCommentForID(id:Long) :VineComment= {
    try{
    return	getModelForID(DaoComment, id).asInstanceOf[VineComment]
    }
   catch
   {
     case e: Exception => throw new IllegalArgumentException("Comment does not exist")
   }
  return null
 }
 
   def createRecipe(client: Client,label: String,description:String) :Recipe= {
   val created:Date=new Date()
   val id:BaseEntity=DaoRecipe.createRecipe(label,description, Option(client.id), created);
   val recipe= new Recipe(id.id,id.gid, label, description,client.id,created,true);
   recipe.setGid(id.gid)
   return recipe;
 }
   
   def getRecipeForID(id:Long) :Recipe= {
		 try{
			 return  getModelForID(DaoRecipe, id).asInstanceOf[Recipe]
		 }
		 catch
		 {
		 case e: Exception => throw new IllegalArgumentException("Recipe does not exist")
		 }
		 return null
 }
   
    
  def deleteModel(model:Model)
  {
	 DaoUtils.deleteModel(model)
  }
  
  private def  getModelForID(dao:BaseDao[Model],id:Long) :Model=
  {
  return DaoUtils.getModelForID(dao, id)
  }
  
}