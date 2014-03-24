package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class Vine(id: Long,gid:Long, label: String,description:String,clientID:Long,created:Date,recipe:Option[Long],visible:Boolean) extends Model
