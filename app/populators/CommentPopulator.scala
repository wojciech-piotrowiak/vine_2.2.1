package populators

import pojo.CommentData
import models.VineComment

object CommentPopulator extends BasePopulator[VineComment,CommentData] {
override def populate(commentModel:Option[VineComment]) :CommentData=
{
   val cData:CommentData =super.populate(commentModel)
  if(commentModel.isDefined)
  {
   var cModel:VineComment=commentModel.get
   cData.id=cModel.id
   cData.content=cModel.content
   cData.created=cModel.created
  }
   return cData;
    
 }  
  
 override def create:CommentData={
    new  CommentData()
  }
}