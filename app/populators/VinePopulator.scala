package populators
import pojo.RecipeData
import models.Recipe
import models.VineHistory
import pojo.VineHistoryData
import models.Vine
import pojo.VineData
import pojo.ClientData
import dao.DaoVine
import dao.DaoClient

object VinePopulator extends BasePopulator[Vine,VineData] {
override def populate(vineModel:Option[Vine]) :VineData=
{
   val vData:VineData =super.populate(vineModel)
  if(vineModel.isDefined)
  {
   var cModel:Vine=vineModel.get
   vData.id=cModel.id
   vData.label=cModel.label
   vData.description=cModel.description
   
   var clientModel= DaoClient.getItemForID(vineModel.get.clientID)
   var clientData=  ClientPopulator.populate(Some(clientModel))
   vData.client=Some(clientData)
  }
   return vData;
 }  
  
 override def create:VineData={
    new  VineData()
  }
}