package controllers

import play.api._
import play.api.mvc._
import models.Vine
import play.api.data._
import play.api.data.Forms._
import dao.DaoVine
import java.util.Date
import pojo.VineData
import pojo.VineData
import populators.VinePopulator
import scala.collection.mutable.ListBuffer
import securesocial.core._
import securesocial.core.java.SecureSocial.SecuredAction
import services.DataService

object VineController extends Controller  with securesocial.core.SecureSocial {

 val vineForm = Form(
  mapping(
    "label" -> text,
    "description" -> text
  )((label, description) => VineData(0,label,description,null,null,new Date()))
  ((t: VineData) => None))
  
def vines = Action {
  val  vines:ListBuffer[VineData]= ListBuffer();
   for( v :Vine <- DaoVine.getAllItems)
   {
     vines+= VinePopulator.populate(Option(v))
     
   }
  Ok(views.html.index(vines.toList, vineForm))
}
  
  def index = Action {
  Redirect(routes.VineController.vines)
}
  
  def newVine =SecuredAction  { 

    implicit request =>
      
		val user:Identity = request.user 
		
  vineForm.bindFromRequest.fold(
      
    errors => BadRequest(views.html.index(List[VineData](), vineForm)
        ),
    label => {
      
   val  vine:VineData= vineForm.bindFromRequest().get
  val client=DataService.getClientForLogin(user.identityId.userId)
   DataService.createVine(vine.label,vine.description,client.get,None)
//   DaoVine.createVine(vine.label,vine.description,Option(vine.client),vine.created)
      Redirect(routes.VineController.vines)
    }
  )
}
  def deleteVine(id: Long) = Action {
  DaoVine.deleteVine(id)
  Redirect(routes.VineController.vines)
}
 

}