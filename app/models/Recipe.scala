package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class Recipe(id: Long,gid:Long,label:String,description:String,creatorID:Long,created:Date,visible:Boolean) extends Model
