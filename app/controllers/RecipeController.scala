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
import pojo.RecipeData
import dao.DaoRecipe
import populators.RecipePopulator
import models.Recipe

object RecipeController extends Controller  with securesocial.core.SecureSocial {

 val recipeForm = Form(
  mapping(
    "label" -> text,
    "description" -> text
  )((label, description) => RecipeData(0,label,description,None,new Date()))
  ((t: RecipeData) => None))
  
def recipes = Action {
  val  recipes:ListBuffer[RecipeData]= ListBuffer();
   for( v :Recipe <- DaoRecipe.getAllItems)
   {
     recipes+= RecipePopulator.populate(Option(v))
     
   }
  Ok(views.html.recipes(recipes.toList, recipeForm))
}
  
//  def index = Action {
//  Redirect(routes.Application.vines)
//}
  
  def newRecipe =SecuredAction  { 

    implicit request =>
      
		val user:Identity = request.user 
		
  recipeForm.bindFromRequest.fold(
      
    errors => BadRequest(views.html.recipes(List[RecipeData](), recipeForm)
        ),
    label => {
      
   val  recipe:RecipeData= recipeForm.bindFromRequest().get
  val client=DataService.getClientForLogin(user.identityId.userId)
   DataService.createRecipe(client.get, recipe.label, recipe.description)
      Redirect(routes.RecipeController.recipes)
    }
  )
}
  def deleteRecipe(id: Long) = Action {
  DaoRecipe.deleteRecipe(Some(id))
  Redirect(routes.RecipeController.recipes)
}
 

}