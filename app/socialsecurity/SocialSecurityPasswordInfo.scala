package socialsecurity

import securesocial.core.PasswordInfo

class SocialSecurityPasswordInfo(hasher:String,password:String,salt:Option[String]) extends PasswordInfo(hasher:String,password:String,salt:Option[String]){
  
}