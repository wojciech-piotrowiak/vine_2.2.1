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
import models.Vine



case class DaoVine()

object DaoVine {
  
  val vine = {
  get[Long]("id") ~ 
  get[Long]("gid") ~ 
  get[String]("label")~
  get[String]("description")~
  get[Long]("client") ~
  get[Date]("created") ~
  get[Option[Long]]("recipe")~
  get[Boolean]("visible") map{
  case id~gid~label~description~client~created~recipe~visible => Vine(id,gid,label,description,client,created,recipe,visible)
  }
}
  
  def getVines() : List[Vine] = DB.withConnection { implicit c =>
  SQL("select * from vine").as(vine *)
}
  
  def createVine(label: String,description:String,client:Option[Long],created:Date) :BaseEntity= {
     if(!client.isDefined) throw new IllegalArgumentException
     
  val  token:Long= DB.withConnection { implicit c =>
	  SQL("select nextval('vine_id_seq');").as(scalar[Long].single)
 	}
     
   val gid=DaoUtils.getGid()  
      
  val  seq:Long=  DB.withConnection { implicit c =>
   	SQL("insert into vine (id,gid,label,description,client,created,visible) values ({id},{gid},{label},{description},{client},{created},{visible})").on(
   	'label -> label,'gid -> gid,'description->description,'client->client,'id->token,'created->created,'visible->true).executeUpdate()
  }
  return new BaseEntity(token,gid);
}
  
  def createVineWithRecipe(label: String,description:String,client:Option[Long],recipe:Option[Long],created:Date) :BaseEntity= {
     if(!recipe.isDefined) throw new IllegalArgumentException
     if(!client.isDefined) throw new IllegalArgumentException
      
  val  token:Long= DB.withConnection { implicit c =>
	  SQL("select nextval('vine_id_seq');").as(scalar[Long].single)
 	}
     
      val gid=DaoUtils.getGid()  
      
  val  seq:Long=  DB.withConnection { implicit c =>
   	SQL("insert into vine (id,gid,label,description,client,recipe,created,visible) values ({id},{gid},{label},{description},{client},{recipe},{created},{visible})").on(
   	'label -> label,'gid -> gid,'description->description,'client->client,'recipe->recipe,'id->token,'created->created,'visible->true).executeUpdate()
  }
  return new BaseEntity(token,gid);
}
  
  def deleteVine(id: Long) {
  DB.withConnection { implicit c =>
    SQL("delete from vine where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}

  def getVinesForClientID(clientID: Option[Long]) : List[Vine] = DB.withConnection { implicit c =>
      if(!clientID.isDefined) throw new IllegalArgumentException
  SQL("select * from vine where client={client}").on(
      'client -> clientID).as(vine *)
  }
   
  def getVinesForRecipeID(recipeID: Option[Long]) : List[Vine] = DB.withConnection { implicit c =>
      if(!recipeID.isDefined) throw new IllegalArgumentException
  SQL("select * from vine where recipe={recipe}").on(
      'recipe -> recipeID).as(vine *)
  }
   
  def activateVine(id: Option[Long]) {
     if(!id.isDefined) throw new IllegalArgumentException
 DB.withConnection { implicit c =>
    SQL("update  vine set visible={visible} where id = {id}").on(
      'visible->true,'id -> id
    ).execute()
  }
}
     
  def deactivateVine(id: Option[Long]) {
     if(!id.isDefined) throw new IllegalArgumentException
 DB.withConnection { implicit c =>
    SQL("update  vine set visible={visible} where id = {id}").on(
      'visible->false,'id -> id
    ).execute()
  }
}
  
    def getVineForID(id: Long):Vine= {
  DB.withConnection { implicit c =>
   return SQL("select * from vine where id={id}").on(
    'id -> id).single(vine)
   }}
  
       
}

