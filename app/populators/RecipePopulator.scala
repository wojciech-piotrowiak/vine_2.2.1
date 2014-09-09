package populators
import pojo.RecipeData
import models.Recipe
import pojo.ClientData
import dao.DaoClient

object RecipePopulator extends BasePopulator[Recipe,RecipeData] {
override def populate(recipeModel:Option[Recipe]) :RecipeData=
{
   val cData:RecipeData =super.populate(recipeModel)
  if(recipeModel.isDefined)
  {
   var cModel:Recipe=recipeModel.get
   cData.id=cModel.id
   cData.label=cModel.label
   cData.description=cModel.description
   
   var clientModel= DaoClient.getItemForID(recipeModel.get.creatorID)
   var clientData=  ClientPopulator.populate(Some(clientModel))
   cData.creator=Some(clientData)
  }
   return cData;
    
 }  
  
 override def create:RecipeData={
    new  RecipeData()
  }
}