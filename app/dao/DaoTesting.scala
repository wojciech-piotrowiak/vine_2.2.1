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



case class DaoTesting()

object DaoTesting {
 

  def cleanAll()={
     DB.withConnection { implicit c =>
      val xyz:Option[String]=Play.application.configuration.getString("db.default.driver")
      //org.h2.Driver
     if(xyz.get.contentEquals("org.postgresql.Driver"))
     {
  SQL( "truncate table vinecomment cascade;truncate table vinehistory cascade; truncate table vine cascade; truncate table recipe cascade; truncate table client cascade;"
    ).execute()
     }
      
     else{
           SQL( "SET REFERENTIAL_INTEGRITY	false; truncate table vinecomment;truncate table vinehistory; truncate table vine;truncate table recipe;truncate table client;"
        ).execute()
     }
       Logger.info("all db cleaned")
       println("all db cleaned")
  }
}
   

}

