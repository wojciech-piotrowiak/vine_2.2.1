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

object Application extends Controller {

 val vineForm = Form(
  mapping(
    "label" -> text,
    "description" -> text,
     "client" -> number
  )((label, description,client) => VineData(0,label,description,null,null,new Date()))
  //id: Long,label: String,description:String,clientID:String,recipe:RecipeData,created:Date
  ((t: VineData) => None))
  
def vines = Action {
   Ok
//  Ok(views.html.index(DaoVine.getVines, vineForm))
}
  
  def index = Action {
  Redirect(routes.Application.vines)
}
  
  def newVine = Action { 
    Redirect("TODO")
//    implicit request =>
//  vineForm.bindFromRequest.fold(
//    errors => BadRequest(views.html.index(DaoVine.getVines, vineForm)
//        ),
//    label => {
//      
//   val  vine:VineData= vineForm.bindFromRequest().get
////   DaoVine.createVine(vine.label,vine.description,Option(vine.client),vine.created)
//      Redirect(routes.Application.vines)
//    }
//  )
}
  def deleteVine(id: Long) = Action {
  DaoVine.deleteVine(id)
  Redirect(routes.Application.vines)
}
 

}