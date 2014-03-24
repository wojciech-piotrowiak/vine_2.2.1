//package models
//
//import anorm._
//import anorm.SqlParser._
//import play.api.db._
//import play.api.Play.current
//import play.api.{Logger, Application}
//import java.util.Date
//import scala.language.postfixOps
//import play.api.Play
//import com.typesafe.config.ConfigValue
//
//
//
//case class DaoYYY()
//
//object DaoYYY {
//  
//  val vine = {
//  get[Long]("id") ~ 
//  get[String]("label")~
//  get[String]("description")~
//  get[Long]("client") ~
//  get[Date]("created") ~
//  get[Option[Long]]("recipe")~
//  get[Boolean]("visible") map{
//  case id~label~description~client~created~recipe~visible => Vine(id, label,description,client,created,recipe,visible)
//  }
//}
//   
//   val client = {
//  get[Long]("id") ~ 
//  get[String]("login") ~
//  get[String]("firstName") ~
//  get[String]("lastName") ~ 
//  get[Date]("registered")~
//  get[Boolean]("active") map{  
//  case id~login~firstName~lastName~registered~active => Client(id, login,firstName,lastName,registered,active)
//  }
//}
//    
//  val vine_history = {
//  get[Long]("id") ~ 
//  get[Long]("vine") ~
//  get[String]("label") ~
//  get[String]("description") ~ 
//  get[Date]("created")~
//  get[Boolean]("visible") map{  
//  case id~vine~label~description~created~visible => VineHistory(id, vine,label,description,created,visible)
//  }
//}
//  
//  val recipe = {
//  get[Long]("id") ~ 
//  get[String]("label") ~
//  get[String]("description") ~ 
//  get[Long]("creator") ~
//  get[Date]("created")~
//  get[Boolean]("visible") map{  
//  case id~label~description~creator~created~visible => Recipe(id,label,description,creator,created,visible)
//  }
//  
//  }
//  
//  
//   val comment = {
//  get[Long]("id") ~ 
//  get[String]("content") ~
//  get[Long]("vine") ~
//  get[Long]("creator") ~
//  get[Date]("created")~
//  get[Boolean]("restricted")~
//  get[Boolean]("visible") map{  
//  case id~content~vine~creator~created~restricted~visible => Comment(id,content,vine,creator,created,restricted,visible)
//  }
//}
//  
//  def getVines() : List[Vine] = DB.withConnection { implicit c =>
//  SQL("select * from vine").as(vine *)
//}
//
//  
//  def getClients(): List[Client] = DB.withConnection { implicit c =>
//  SQL("select * from client").as(client *)
//}
//  
//   def getRecipies(): List[Recipe] = DB.withConnection { implicit c =>
//  SQL("select * from recipe").as(recipe *)
//}
//  
//    def getVineHistory(vine: Long): List[VineHistory] = DB.withConnection { implicit c =>
//  SQL("select * from vine_history where vine={vine} order by created ASC").on('vine->vine).as(vine_history *)
//}
//    
//    def getCommentsForVine(vine: Option[Long]): List[Comment] = DB.withConnection { implicit c =>
//       if(!vine.isDefined) throw new IllegalArgumentException
//  SQL("select * from vine_comment where vine={vine} order by created ASC").on('vine->vine).as(comment *)
//}
//    
//   def getCommentsForClient(client: Option[Long]): List[Comment] = DB.withConnection { implicit c =>
//      if(!client.isDefined) throw new IllegalArgumentException
//  SQL("select * from vine_comment where creator={client} order by created ASC").on('client->client).as(comment *)
//}
//   
//   
//     def createComment(content: String,vine:Option[Long],creator:Option[Long],created:Date) :Long= {
//   val  id:Long= DB.withConnection { implicit c =>
//	  SQL("select nextval('vine_comment_id_seq');").as(scalar[Long].single)
// 	 }
//      
//   val  seq:Long=  DB.withConnection { implicit c =>
//   	SQL("insert into vine_comment (id,content,vine,creator,created,restricted,visible) values ({id},{content},{vine},{creator},{created},{restricted},{visible})").on(
//   	'id->id,'content -> content,'vine->vine,'creator->creator,'created->created,'restricted->false,'visible->true).executeUpdate()
//  }
//  return id;
//}
//   
//   
//        
//     def createVineHistory(label: String,description:String,vine:Long,created:Date) :Long= {
//   val  id:Long= DB.withConnection { implicit c =>
//	  SQL("select nextval('vine_history_id_seq');").as(scalar[Long].single)
// 	 }
//      
//   val  seq:Long=  DB.withConnection { implicit c =>
//   	SQL("insert into vine_history (id,label,description,vine,created,visible) values ({id},{label},{description},{vine},{created},{visible})").on(
//   	'id->id,'label -> label,'description -> description,'vine->vine,'created->created,'visible->true).executeUpdate()
//  }
//  return id;
//}
//  
//  def createClient(login: String, firstName: String, lastName: String,registered:Date):Long= {
//	val  count:Long= DB.withConnection { implicit c =>
//	SQL("select count(*) from client where login={login}").on(
//    'login -> login).as(scalar[Long].single)
//   }
//     
//  if(count>0)
//    throw new IllegalArgumentException("Duplicated client");
//     
//    val  token:Long= DB.withConnection { implicit c =>
//	   SQL("select nextval('client_id_seq');").as(scalar[Long].single)}
//     
//     DB.withConnection { (implicit c =>
//   SQL("insert into client (login,firstName,lastName,id,registered,active) values ({login},{firstName},{lastName},{id},{registered},{active})").on(
//      'login -> login,
//	  'firstName -> firstName,
//	  'lastName -> lastName,
//	  'id->token,
//	  'registered->registered,
//	 'active->false
//    ).executeUpdate()) }
//	    return token
//}
//	  
//   
//  def createVine(label: String,description:String,client:Option[Long],created:Date) :Long= {
//     if(!client.isDefined) throw new IllegalArgumentException
//     
//   val  token:Long= DB.withConnection { implicit c =>
//	  SQL("select nextval('vine_id_seq');").as(scalar[Long].single)
// 	}
//      
//   val  seq:Long=  DB.withConnection { implicit c =>
//   	SQL("insert into vine (id,label,description,client,created,visible) values ({id},{label},{description},{client},{created},{visible})").on(
//   	'label -> label,'description->description,'client->client,'id->token,'created->created,'visible->true).executeUpdate()
//  }
//  return token;
//}
//  
//   def createVineWithRecipe(label: String,description:String,client:Option[Long],recipe:Option[Long],created:Date) :Long= {
//     if(!recipe.isDefined) throw new IllegalArgumentException
//     if(!client.isDefined) throw new IllegalArgumentException
//      
//   val  token:Long= DB.withConnection { implicit c =>
//	  SQL("select nextval('vine_id_seq');").as(scalar[Long].single)
// 	}
//      
//   val  seq:Long=  DB.withConnection { implicit c =>
//   	SQL("insert into vine (id,label,description,client,recipe,created,visible) values ({id},{label},{description},{client},{recipe},{created},{visible})").on(
//   	'label -> label,'description->description,'client->client,'recipe->recipe,'id->token,'created->created,'visible->true).executeUpdate()
//  }
//  return token;
//}
//  
//   def createRecipe(label: String,description:String,creator:Option[Long],created:Date) :Long= {
//      if(!creator.isDefined) throw new IllegalArgumentException
//     
//   val  token:Long= DB.withConnection { implicit c =>
//	  SQL("select nextval('recipe_id_seq');").as(scalar[Long].single)
// 	}
//      
//   val  seq:Long=  DB.withConnection { implicit c =>
//   	SQL("insert into recipe (id,label,description,creator,created,visible) values ({id},{label},{description},{creator},{created},{visible})").on(
//   	'id->token,'label -> label,'description->description,'creator->creator,'created->created,'visible->true).executeUpdate()
//  }
//  return token;
//}
//    
//  def deleteClient(login: String) {
//  DB.withConnection { implicit c =>
//    SQL("delete from client where login = {login}").on(
//      'login -> login
//    ).executeUpdate()
//  }
//}
//    
//  def deleteVine(id: Long) {
//  DB.withConnection { implicit c =>
//    SQL("delete from vine where id = {id}").on(
//      'id -> id
//    ).executeUpdate()
//  }
//}
//  
//   def deleteVineHistory(id: Long) {
//  DB.withConnection { implicit c =>
//    SQL("delete from vine_history where id = {id}").on(
//      'id -> id
//    ).executeUpdate()
//  }
//}
//   
//    def deleteRecipe(id: Option[Long]) {
//  if(!id.isDefined) throw new IllegalArgumentException
//  DB.withConnection { implicit c =>
//    SQL("delete from recipe where id = {id}").on(
//      'id -> id
//    ).executeUpdate()
//  }
//}
//  
//  def cleanAll()={
//     DB.withConnection { implicit c =>
//      val xyz:Option[String]=Play.application.configuration.getString("db.default.driver")
//      //org.h2.Driver
//     if(xyz.get.contentEquals("org.postgresql.Driver"))
//     {
//  SQL( "truncate table vine_comment cascade;truncate table vine_history cascade; truncate table vine cascade; truncate table recipe cascade; truncate table client cascade;"
//    ).execute()
//     }
//      
//     else{
//           SQL( "SET REFERENTIAL_INTEGRITY	false; truncate table vine_comment;truncate table vine_history; truncate table vine;truncate table recipe;truncate table client;"
//        ).execute()
//     }
//       Logger.info("all db cleaned")
//       println("all db cleaned")
//  }
//}
//   
//   def getVinesForClientID(clientID: Option[Long]) : List[Vine] = DB.withConnection { implicit c =>
//      if(!clientID.isDefined) throw new IllegalArgumentException
//  SQL("select * from vine where client={client}").on(
//      'client -> clientID).as(vine *)
//  }
//   
//   def getVinesForRecipeID(recipeID: Option[Long]) : List[Vine] = DB.withConnection { implicit c =>
//      if(!recipeID.isDefined) throw new IllegalArgumentException
//  SQL("select * from vine where recipe={recipe}").on(
//      'recipe -> recipeID).as(vine *)
//  }
//   
//    def getRecipiesForClientID(clientID: Option[Long]) : List[Recipe] = DB.withConnection {  implicit c =>
//       if(!clientID.isDefined) throw new IllegalArgumentException
//     SQL("select * from recipe where creator={creator} ").on('creator->clientID).as(recipe *)
//  }
//}
//
