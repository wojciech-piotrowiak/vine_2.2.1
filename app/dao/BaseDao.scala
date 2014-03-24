package dao

import play.api.db.DB
import anorm._
import play.api.Play.current
import scala.language.postfixOps

trait BaseDao[T] {
	def getAllItems(): List[T]= DB.withConnection { implicit c =>
		SQL("select * from "+getEntityName).as(getRowParser *)
}
	
   def getItemForID(id: Long):T= {
  DB.withConnection { implicit c =>
   return SQL("select * from "+getEntityName+" where id={id}").on(
    'id -> id).single(getRowParser)
   }}
	
	def getRowParser :RowParser[T]
	
	def getEntityName :String
}