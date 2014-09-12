package populators

import models.SocialSecurityClient
import models.Client
import models.SocialSecurityIdentityId
import securesocial.core.AuthenticationMethod

object IdentityPopulator extends BasePopulator[Client,SocialSecurityClient] {
  
 override def populate(clientModel:Option[Client]):SocialSecurityClient=
 {
  val client:SocialSecurityClient =super.populate(clientModel)
  if(clientModel.isDefined)
  {
    client.firstName=clientModel.get.firstName
    client.lastName=clientModel.get.lastName
    client.email=Some(clientModel.get.login)
    client.identityId=new SocialSecurityIdentityId(clientModel.get.login,"")
  }
   return client;
    
 }  
  
 override def create:SocialSecurityClient={
    new SocialSecurityClient(new SocialSecurityIdentityId("",""),"","","",Some(""),None,AuthenticationMethod.UserPassword,None,None)
  }
}