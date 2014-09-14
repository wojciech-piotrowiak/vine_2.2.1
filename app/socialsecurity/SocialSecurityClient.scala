package socialsecurity

import securesocial.core.Identity
import securesocial.core.AuthenticationMethod
import securesocial.core.IdentityId
import securesocial.core.OAuth1Info
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo

class SocialSecurityClient( 
    var identityId: IdentityId,
    var firstName: String,
    var lastName: String,
    var fullName: String,
    var email: Option[String],                 
    var avatarUrl: Option[String],
    var authMethod: AuthenticationMethod,
                       
    var oAuth1Info: Option[OAuth1Info] = None,
    var oAuth2Info: Option[OAuth2Info] = None,
    var passwordInfo: Option[PasswordInfo] = None
)  extends Identity 