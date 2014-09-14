package populators

import socialsecurity.SocialSecurityClient
import models.Client
import socialsecurity.SocialSecurityIdentityId
import securesocial.core.AuthenticationMethod
import securesocial.core.PasswordInfo
import socialsecurity.SocialSecurityPasswordInfo

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
    client.passwordInfo=Some(new SocialSecurityPasswordInfo("",clientModel.get.password,None))
//    PasswordInfo pi;
//    pi.
//    client.passwordInfo=Option(new PasswordInfo ))
  }
   return client;
    
 }  
  
 override def create:SocialSecurityClient={
    new SocialSecurityClient(new SocialSecurityIdentityId("",""),"","","",Some(""),None,AuthenticationMethod.UserPassword,None,None)
  }
}