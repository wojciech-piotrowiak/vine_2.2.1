package controllers

import securesocial.core.java.BaseUserService
import play.api.{Logger, Application}
import securesocial.core._
import securesocial.core.providers.Token
import securesocial.core.IdentityId
import dao.DaoClient
import services.DataService
import socialsecurity.SocialSecurityClient
import socialsecurity.SocialSecurityIdentityId
import models.Client
import populators.IdentityPopulator

  class SqlUserService(application: Application) extends UserServicePlugin(application) {
    private var users = Map[String, Identity]()
  private var tokens = Map[String, Token]()
    
    def find(id: IdentityId): Option[Identity] = {
  DataService.getClientForLogin(id.userId)
   match{
          case Some(client:Client)=> return Option(IdentityPopulator.populate(Some(client)))
		   case None=> return None
		  }
  }

  def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = {
    
        DataService.getClientForLogin(email) 
          match{
          case Some(client:Client)=> return Option(IdentityPopulator.populate(Some(client)))
		   case None=> return None
		  }
  }

  def save(user: Identity): Identity = {
    DataService.getClientForLogin(user.identityId.userId)   match{
    case None=>  DataService.createClient(user.identityId.userId, user.firstName, user.lastName,user.passwordInfo.get.password)
    case Some(_)=>  DataService.saveClient(user)
  }
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