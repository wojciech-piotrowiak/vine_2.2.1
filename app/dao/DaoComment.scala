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
import models.VineComment




object DaoComment extends BaseDao[VineComment] {

 
  override def getEntityName :String={
  	 return "vinecomment";
   }
 
   override def getRowParser: RowParser[VineComment]={
	 return  {
  get[Long]("id") ~ 
   get[Long]("gid") ~ 
  get[String]("content") ~
  get[Long]("vine") ~
  get[Long]("creator") ~
  get[Date]("created")~
  get[Boolean]("restricted")~
  get[Boolean]("visible") map{  
  case id~gid~content~vine~creator~created~restricted~visible => VineComment(id,gid,content,vine,creator,created,restricted,visible)
  }
}
 }

    
    def getCommentsForVine(vine: Option[Long]): List[VineComment] = DB.withConnection { implicit c =>
       if(!vine.isDefined) throw new IllegalArgumentException
  SQL("select * from vinecomment where vine={vine} order by created ASC").on('vine->vine).as(getRowParser *)
}
    
   def getCommentsForClient(client: Option[Long]): List[VineComment] = DB.withConnection { implicit c =>
      if(!client.isDefined) throw new IllegalArgumentException
  SQL("select * from vinecomment where creator={client} order by created ASC").on('client->client).as(getRowParser *)
}
   
   
     def createComment(content: String,vineID:Option[Long],creatorID:Option[Long],created:Date) :BaseEntity= {
   val  id:Long= DB.withConnection { implicit c =>
	  SQL("select nextval('vine_comment_id_seq');").as(scalar[Long].single)
 	 }
      
   val gid=DaoUtils.getGid()
   val  seq:Long=  DB.withConnection { implicit c =>
   	SQL("insert into vinecomment (id,gid,content,vine,creator,created,restricted,visible) values ({id},{gid},{content},{vine},{creator},{created},{restricted},{visible})").on(
   	'id->id,'gid -> gid,'content -> content,'vine->vineID,'creator->creatorID,'created->created,'restricted->false,'visible->true).executeUpdate()
  }
  return new BaseEntity(id,gid);
}
     
     def activateVineComment(id: Option[Long]) {
        if(!id.isDefined) throw new IllegalArgumentException
   DB.withConnection { implicit c =>
    SQL("update  vinecomment set visible={visible} where id = {id}").on(
      'visible->true,'id -> id
    ).execute()
  }
}
     
       def deactivateVineComment(id: Option[Long]) {
          if(!id.isDefined) throw new IllegalArgumentException
   DB.withConnection { implicit c =>
    SQL("update  vinecomment set visible={visible} where id = {id}").on(
      'visible->false,'id -> id
    ).execute()
  }
}

}

