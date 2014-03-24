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
import models.VineHistory



case class DaoVineHistory()

object DaoVineHistory {
  

    
  val vine_history = {
  get[Long]("id") ~ 
  get[Long]("gid") ~ 
  get[Long]("vine") ~
  get[String]("label") ~
  get[String]("description") ~ 
  get[Date]("created")~
  get[Boolean]("visible") map{  
  case id~gid~vine~label~description~created~visible => VineHistory(id,gid, vine,label,description,created,visible)
  }
}
  
def getVineHistory(vine: Long): List[VineHistory] = DB.withConnection { implicit c =>
  SQL("select * from vinehistory where vine={vine} order by created ASC").on('vine->vine).as(vine_history *)
}
    
def createVineHistory(label: String,description:String,vine:Long,created:Date) :BaseEntity= {
  val  id:Long= DB.withConnection { implicit c =>
	  SQL("select nextval('vine_history_id_seq');").as(scalar[Long].single)
 	 }
  
  val gid=DaoUtils.getGid()
      
  val  seq:Long=  DB.withConnection { implicit c =>
   	SQL("insert into vinehistory (id,gid,label,description,vine,created,visible) values ({id},{gid},{label},{description},{vine},{created},{visible})").on(
   	'id->id,'gid -> gid,'label -> label,'description -> description,'vine->vine,'created->created,'visible->true).executeUpdate()
  }
  return new BaseEntity(id,gid);
}
 
  
def deleteVineHistory(id: Option[Long]) {
   if(!id.isDefined) throw new IllegalArgumentException
  DB.withConnection { implicit c =>
    SQL("delete from vinehistory where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}

def activateVineHistory(id: Option[Long]) {
   if(!id.isDefined) throw new IllegalArgumentException
   DB.withConnection { implicit c =>
    SQL("update  vinehistory set visible={visible} where id = {id}").on(
      'visible->true,'id -> id
    ).execute()
  }
}

def deactivateVineHistory(id: Option[Long]) {
   if(!id.isDefined) throw new IllegalArgumentException
  DB.withConnection { implicit c =>
    SQL("update  vinehistory set visible={visible} where id = {id}").on(
      'visible->false,'id -> id
    ).execute()
  }
}

   def getVineHistoryForID(id: Long):VineHistory= {
  DB.withConnection { implicit c =>
   return SQL("select * from vinehistory where id={id}").on(
    'id -> id).single(vine_history)
   }}

 
}

