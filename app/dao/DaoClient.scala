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
import models.Client





object DaoClient extends BaseDao[Client] {

   override def getEntityName :String={
  	 return "client";
   }
   
   def getRowParser :RowParser[Client] = {
  get[Long]("id") ~ 
  get[Long]("gid") ~ 
  get[String]("login") ~
  get[String]("firstName") ~
  get[String]("lastName") ~ 
  get[Date]("registered")~
  get[Boolean]("active") map{  
  case id~gid~login~firstName~lastName~registered~active => Client(id,gid, login,firstName,lastName,registered,active)
  }
}
	def getCount():Long= {
  return DB.withConnection { implicit c =>
	SQL("select count(*) from client ").as(scalar[Long].single)
   }
  }
   

  def createClient(login: String, firstName: String, lastName: String,registered:Date):BaseEntity= {
	val  count:Long= DB.withConnection { implicit c =>
	SQL("select count(*) from client where login={login}").on(
    'login -> login).as(scalar[Long].single)
   }
     
  if(count>0)
    throw new IllegalArgumentException("Duplicated client");
     
    val  token:Long= DB.withConnection { implicit c =>
	   SQL("select nextval('client_id_seq');").as(scalar[Long].single)}
    val gid=DaoUtils.getGid()
     
     DB.withConnection { (implicit c =>
   SQL("insert into client (gid,login,firstName,lastName,id,registered,active) values ({gid},{login},{firstName},{lastName},{id},{registered},{active})").on(
      'gid -> gid,
      'login -> login,
	  'firstName -> firstName,
	  'lastName -> lastName,
	  'id->token,
	  'registered->registered,
	  'active->false
    ).executeUpdate()) }
	    return new BaseEntity(token,gid)
}
	  
 
  def deleteClient(login: String) {
  DB.withConnection { implicit c =>
    SQL("delete from client where login = {login}").on(
      'login -> login
    ).executeUpdate()
  }
}
  
   def deactivateClient(login: String) {
  DB.withConnection { implicit c =>
    SQL("update  client set active={active} where login = {login}").on(
      'active->false,'login -> login
    ).execute()
  }
}
   
    def activateClient(login: String) {
  DB.withConnection { implicit c =>
    SQL("update  client set active={active} where login = {login}").on(
      'active->true,'login -> login
    ).executeUpdate()
  }
    }    
}

