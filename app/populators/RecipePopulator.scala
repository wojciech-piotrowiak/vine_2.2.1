package populators
import pojo.RecipeData
import models.Recipe
import pojo.ClientData

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
   
   val client:ClientData=new ClientData();
   client.id=recipeModel.get.creatorID
   cData.creator=Some(client)
   
  }
   return cData;
    
 }  
  
 override def create:RecipeData={
    new  RecipeData()
  }
}