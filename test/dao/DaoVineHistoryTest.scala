package dao

import org.specs2.mutable.Specification
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import java.util.Date
import play.api.Play
import dao.DaoTesting
import dao.DaoClient
import dao.DaoVine
import dao.DaoVineHistory
import dao.DaoRecipe
import dao.util.BaseTest
import dao.util.DaoTestUtils


class DaoVineHistoryTest extends BaseTest  {

"getVineHistory"  should {
   
   "return sth for active vine without recipe" in  {
	  val vineID=DaoTestUtils.getSampleVine()
      DaoVineHistory.createVineHistory("history", "description", vineID.id, new Date())
      DaoVineHistory.getVineHistory(vineID.id) must have size(1)
  }
   
    "return sth for active vine with recipe" in  {
      val vineID=  DaoTestUtils.getSampleRecipeVine().id
      DaoVineHistory.createVineHistory("history", "description", vineID, new Date())
      DaoVineHistory.getVineHistory(vineID) must have size(1)
  }
    
   "return sth for active auto vine with recipe" in  {
      val vineID=  DaoTestUtils.getSampleRecipeAutoVine().id
      DaoVineHistory.createVineHistory("history", "description", vineID, new Date())
      DaoVineHistory.getVineHistory(vineID) must have size(1)
  }  
 
  "return nothing for not existing vine" in  {
      DaoVineHistory.getVineHistory(22) must beEmpty
  }
  
   "return sth in right order-single user without recipe" in  {
	  val vineID=DaoTestUtils.getSampleVine()
	  val firstDate:Date=new Date();
	  val secondDate:Date=new Date();
	  val thirdDate:Date=new Date();
      DaoVineHistory.createVineHistory("history1", "description", vineID.id, secondDate)
      DaoVineHistory.createVineHistory("history2", "description", vineID.id, thirdDate)
      DaoVineHistory.createVineHistory("history3", "description", vineID.id, firstDate)
      DaoVineHistory.getVineHistory(vineID.id) must have size(3)
      DaoVineHistory.getVineHistory(vineID.id).head.label.contentEquals("history1")
  }
   
    "return sth in right order-multi user without recipe" in   {
	  val firstClientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
	  val firstVineID=DaoVine.createVine("test1","description",firstClientID,new Date()).id
	  
	  val secondClientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
	  val secondVineID=DaoVine.createVine("test2","description",secondClientID,new Date()).id
	  
	  val firstDate:Date=new Date();
	  val secondDate:Date=new Date();
	  val thirdDate:Date=new Date();
      DaoVineHistory.createVineHistory("history1", "description", firstVineID, secondDate)
      DaoVineHistory.createVineHistory("history2", "description", firstVineID, thirdDate)
      DaoVineHistory.createVineHistory("history3", "description", firstVineID, firstDate)
      
      DaoVineHistory.createVineHistory("history00", "description", secondVineID, secondDate)
      DaoVineHistory.createVineHistory("history01", "description", secondVineID, thirdDate)
      DaoVineHistory.createVineHistory("history02", "description", secondVineID, firstDate)
      DaoVineHistory.createVineHistory("history03", "description", secondVineID, firstDate)
      DaoVineHistory.getVineHistory(firstVineID) must have size(3)
      DaoVineHistory.getVineHistory(firstVineID).head.label.contentEquals("history1")
      
       DaoVineHistory.getVineHistory(secondVineID) must have size(4)
      DaoVineHistory.getVineHistory(secondVineID).head.label.contentEquals("history00")
  }
    
    
     "return sth in right order-single user with recipe" in  {
	  val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
	  val recipeID:Option[Long]=Some(DaoRecipe.createRecipe("label", "description", clientID, new Date()).id)
	  val vineID=DaoVine.createVineWithRecipe("test2","description",clientID,recipeID,new Date())
	  val firstDate:Date=new Date();
	  val secondDate:Date=new Date();
	  val thirdDate:Date=new Date();
      DaoVineHistory.createVineHistory("history1", "description", vineID.id, secondDate)
      DaoVineHistory.createVineHistory("history2", "description", vineID.id, thirdDate)
      DaoVineHistory.createVineHistory("history3", "description", vineID.id, firstDate)
      DaoVineHistory.getVineHistory(vineID.id) must have size(3)
      DaoVineHistory.getVineHistory(vineID.id).head.label.contentEquals("history1")
  }
   
    "return sth in right order-multi user with recipe" in   {
	  val firstClientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
	  val recipeID:Option[Long]=Some(DaoRecipe.createRecipe("label", "description", firstClientID, new Date()).id)
	  val firstVineID=DaoVine.createVineWithRecipe("test1","description",firstClientID,recipeID,new Date())
	  
	  val secondClientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
	  val secondVineID=DaoVine.createVineWithRecipe("test2","description",secondClientID,recipeID,new Date())
	  
	  val firstDate:Date=new Date();
	  val secondDate:Date=new Date();
	  val thirdDate:Date=new Date();
      DaoVineHistory.createVineHistory("history1", "description", firstVineID.id, secondDate)
      DaoVineHistory.createVineHistory("history2", "description", firstVineID.id, thirdDate)
      DaoVineHistory.createVineHistory("history3", "description", firstVineID.id, firstDate)
      
      DaoVineHistory.createVineHistory("history00", "description", secondVineID.id, secondDate)
      DaoVineHistory.createVineHistory("history01", "description", secondVineID.id, thirdDate)
      DaoVineHistory.createVineHistory("history02", "description", secondVineID.id, firstDate)
      DaoVineHistory.createVineHistory("history03", "description", secondVineID.id, firstDate)
      DaoVineHistory.getVineHistory(firstVineID.id) must have size(3)
      DaoVineHistory.getVineHistory(firstVineID.id).head.label.contentEquals("history1")
      
       DaoVineHistory.getVineHistory(secondVineID.id) must have size(4)
      DaoVineHistory.getVineHistory(secondVineID.id).head.label.contentEquals("history00")
  }
}

"deleteVineHistory" should {
   "delete vine history" in  {
     val vineID:Long=DaoTestUtils.getSampleVine().id
     val vineHistoryID=DaoVineHistory.createVineHistory("label", "description", vineID, new Date())
     DaoVineHistory.getVineHistory(vineID) must have size(1) 
     DaoVineHistory.deleteVineHistory(Some(vineHistoryID.id))
     DaoVineHistory.getVineHistory(vineID)  must have size(0)      
  }
     "delete not existing vine history" in   {
     DaoVineHistory.deleteVineHistory(Some(241))
     true
  }
      "delete NONE vine history" in   {
     DaoVineHistory.deleteVineHistory(None)  must
            throwA[IllegalArgumentException]
  }
}

"activateVineHistory" should {
   "activateVineHistory" in  {
     val vineID:Long=DaoTestUtils.getSampleVine().id
     val vineHistoryID=DaoVineHistory.createVineHistory("label", "description", vineID, new Date())
     DaoVineHistory.getVineHistory(vineID).head.visible must beTrue
     DaoVineHistory.deactivateVineHistory(Some(vineHistoryID.id))
     DaoVineHistory.getVineHistory(vineID).head.visible must beFalse
     DaoVineHistory.activateVineHistory(Some(vineHistoryID.id))
     DaoVineHistory.getVineHistory(vineID).head.visible must beTrue
  }
   
   "activate NONE VineHistory" in  {
    DaoVineHistory.activateVineHistory(None)  must
            throwA[IllegalArgumentException]
  }

}

"deactivateVineHistory" should {
   "deactivateVineHistory" in  {
     val vineID:Long=DaoTestUtils.getSampleVine().id
     val vineHistoryID=DaoVineHistory.createVineHistory("label", "description", vineID, new Date())
     DaoVineHistory.getVineHistory(vineID).head.visible must beTrue
     DaoVineHistory.deactivateVineHistory(Some(vineHistoryID.id))
      DaoVineHistory.getVineHistory(vineID).head.visible must beFalse
  }
   
    "deactivate NONE VineHistory" in  {
    DaoVineHistory.deactivateVineHistory(None)  must
            throwA[IllegalArgumentException]
  }

}


}