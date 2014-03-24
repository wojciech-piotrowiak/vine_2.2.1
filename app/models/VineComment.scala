package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class VineComment(id: Long,gid:Long,content:String,vineID:Long,creatorID:Long,created:Date,restricted:Boolean,visible:Boolean) extends Model
