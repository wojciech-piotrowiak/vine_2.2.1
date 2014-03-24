package populators
import pojo.RecipeData
import models.Recipe
import models.VineHistory
import pojo.VineHistoryData
import pojo.VineData

object VineHistoryPopulator extends BasePopulator[VineHistory,VineHistoryData] {
override def populate(vineHistoryModel:Option[VineHistory]) :VineHistoryData=
{
   val cData:VineHistoryData =super.populate(vineHistoryModel)
  if(vineHistoryModel.isDefined)
  {
   var cModel:VineHistory=vineHistoryModel.get
   cData.label=cModel.label
   cData.description=cModel.description
   
   val vData:VineData =new VineData()
   vData.id=cModel.vineID
   
   cData.vine=Some(vData)
  }
   return cData;
 }  
  
 override def create:VineHistoryData={
    new  VineHistoryData()
  }
}