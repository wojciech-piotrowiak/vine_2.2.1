package populators
import pojo.RecipeData
import models.Recipe
import models.VineHistory
import pojo.VineHistoryData
import models.Vine
import pojo.VineData
import pojo.ClientData

object VinePopulator extends BasePopulator[Vine,VineData] {
override def populate(vineModel:Option[Vine]) :VineData=
{
   val vData:VineData =super.populate(vineModel)
  if(vineModel.isDefined)
  {
   var cModel:Vine=vineModel.get
   
   vData.label=cModel.label
   vData.description=cModel.description
   
   val cData:ClientData=new ClientData();
   cData.id=vineModel.get.clientID
   vData.client=Some(cData)
  }
   return vData;
 }  
  
 override def create:VineData={
    new  VineData()
  }
}