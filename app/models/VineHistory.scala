package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class VineHistory(id: Long,gid:Long,vineID:Long,label: String, description: String,created:Date,visible:Boolean) extends Model
