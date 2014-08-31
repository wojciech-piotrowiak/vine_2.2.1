package controllers

import securesocial.core.java.BaseUserService
import play.api.{Logger, Application}
import securesocial.core._
import securesocial.core.providers.Token
import securesocial.core.IdentityId
import dao.DaoClient
import services.DataService
import models.SocialSecurityClient
import models.SocialSecurityIdentityId
import models.Client

  class SqlUserService(application: Application) extends UserServicePlugin(application) {
    private var users = Map[String, Identity]()
  private var tokens = Map[String, Token]()
    
    def find(id: IdentityId): Option[Identity] = {
      
      

  DataService.getClientForLogin(id.userId)
   match{
          case Some(client:Client)=> val i:SocialSecurityClient=new SocialSecurityClient(id,client.firstName,client.lastName,"",Some(client.login),None,AuthenticationMethod.UserPassword,None,None)
          							Some(i)
		   case None=> return None
		  }
  
  
     
          
//    if ( Logger.isDebugEnabled ) {
//      Logger.debug("users = %s".format(users))
//    }
//    users.get(id.userId + id.providerId)
  }

  def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = {
    
        DataService.getClientForLogin(email) 
          match{
          case Some(client:Client)=> val i:SocialSecurityClient=new SocialSecurityClient(new SocialSecurityIdentityId(email,""),client.firstName,client.lastName,"",Some(client.login),None,AuthenticationMethod.UserPassword,None,None)
          							Some(i)
		   case None=> return None
		  }
    
//    if ( Logger.isDebugEnabled ) {
//      Logger.debug("users = %s".format(users))
//    }
//    users.values.find( u => u.email.map( e => e == email && u.identityId.providerId == providerId).getOrElse(false))
  }

  def save(user: Identity): Identity = {
    DataService.getClientForLogin(user.identityId.userId)   match{
      
    case None=>  DataService.createClient(user.identityId.userId, user.firstName, user.lastName)
    case Some(_)=>  DataService.saveClient(user.identityId.userId.toLong)
  }
   
    
   // users = users + (user.identityId.userId + user.identityId.providerId -> user)
    // this sample returns the same user object, but you could return an instance of your own class
    // here as long as it implements the Identity trait. This will allow you to use your own class in the protected
    // actions and event callbacks. The same goes for the find(id: IdentityId) method.
    user
  }

  def save(token: Token) {
    tokens += (token.uuid -> token)
  }

  def findToken(token: String): Option[Token] = {
    tokens.get(token)
  }

  def deleteToken(uuid: String) {
    tokens -= uuid
  }

  def deleteTokens() {
    tokens = Map()
  }

  def deleteExpiredTokens() {
    tokens = tokens.filter(!_._2.isExpired)
  }
}