package dao

import play.api.db.DB
import anorm._
import play.api.Play
import anorm.SqlParser._
import play.api.Play.current
import models.Model
import anorm.SimpleSql;

object DaoUtils {
 def  getGid():Long=
return DB.withConnection { implicit c =>
	   SQL("select nextval('gid_seq');").as(scalar[Long].single)}

def deleteModel(model:Model)
{
  val  className:String=model.getClass().getSimpleName()
    DB.withConnection { implicit c =>
    SQL("delete from "+className.toLowerCase()+" where gid = {id}").on(
      'id -> model.getGID
    ).executeUpdate()
  }
}

     def getModelForID(dao:BaseDao[Model],id:Long):Model= {
  DB.withConnection { implicit c =>
   return SQL("select * from "+dao.getEntityName+" where id={id}").on(
    'id -> id).single(dao.getRowParser)
   }}
}