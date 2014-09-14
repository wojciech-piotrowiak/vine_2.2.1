package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date
import securesocial.core.Identity


case class Client(id: Long,gid: Long,login: String, firstName: String,lastName: String,registered:Date,active:Boolean,password: String) extends Model 
