package dao

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.{Logger, Application}
import java.util.Date
import scala.language.postfixOps
import play.api.Play
import com.typesafe.config.ConfigValue
import models.Recipe
import models.Model


object DaoRecipe extends BaseDao[Recipe] {
	
	
 override def getEntityName :String={
  	 return "recipe";
   }
 
   override def getRowParser: RowParser[Recipe]={
	 return   {
  get[Long]("id") ~ 
  get[Long]("gid") ~ 
  get[String]("label") ~
  get[String]("description") ~ 
  get[Long]("creator") ~
  get[Date]("created")~
  get[Boolean]("visible") map{  
  case id~gid~label~description~creator~created~visible => Recipe(id,gid,label,description,creator,created,visible)
  } }
 }
	
   
   def createRecipe(label: String,description:String,creator:Option[Long],created:Date) :BaseEntity= {
      if(!creator.isDefined) throw new IllegalArgumentException
     
   val  token:Long= DB.withConnection { implicit c =>
	  SQL("select nextval('recipe_id_seq');").as(scalar[Long].single)
 	}
   
   val gid=DaoUtils.getGid()   
      
   val  seq:Long=  DB.withConnection { implicit c =>
   	SQL("insert into recipe (id,gid,label,description,creator,created,visible) values ({id},{gid},{label},{description},{creator},{created},{visible})").on(
   	'id->token,'gid -> gid,'label -> label,'description->description,'creator->creator,'created->created,'visible->true).executeUpdate()
  }
  return new BaseEntity(token,gid);
}

    def getRecipiesForClientID(clientID: Option[Long]) : List[Recipe] = DB.withConnection {  implicit c =>
       if(!clientID.isDefined) throw new IllegalArgumentException
     SQL("select * from recipe where creator={creator} ").on('creator->clientID).as(getRowParser *)
  }
    
     def activateRecipe(id: Option[Long]) {
       if(!id.isDefined) throw new IllegalArgumentException
   DB.withConnection { implicit c =>
    SQL("update  recipe set visible={visible} where id = {id}").on(
      'visible->true,'id -> id
    ).execute()
  }
}
     
       def deactivateRecipe(id: Option[Long]) {
        if(!id.isDefined) throw new IllegalArgumentException
   DB.withConnection { implicit c =>
    SQL("update  recipe set visible={visible} where id = {id}").on(
      'visible->false,'id -> id
    ).execute()
  }
}
       
       
          def getRecipeForID(id: Long):Recipe= {
  DB.withConnection { implicit c =>
   return SQL("select * from recipe where id={id}").on(
    'id -> id).single(getRowParser)
   }}
    
          
           def deleteRecipe(id: Option[Long]) {
  if(!id.isDefined) throw new IllegalArgumentException
  DB.withConnection { implicit c =>
    SQL("delete from recipe where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}
}

