package dao.util

import org.specs2.mutable.Specification
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import java.util.Date
import org.specs2.specification.Fragments
import org.specs2.specification.Step
import play.api.Play
import dao.DaoClient
import dao.DaoVine
import dao.DaoVineHistory
import dao.DaoRecipe
import dao.DaoComment
import dao.DaoTesting


class DaoTest extends BaseTest   {
   
  sequential
   
  

  "DataBase.cleanAll()" should {
   
    "client empty" in   {
       DaoTesting.cleanAll()
       DaoTestUtils.getSampleClient();
       DaoTesting.cleanAll()
       DaoClient.getAllItems must beEmpty
    }
     
    "vine empty" in   {
      DaoTesting.cleanAll()
      DaoTestUtils.getSampleVine();
      DaoTesting.cleanAll()
      DaoVine.getAllItems must beEmpty
    }
    "vine history empty" in   {
      DaoTesting.cleanAll()
      val vineID=DaoTestUtils.getSampleVine().id
      DaoVineHistory.createVineHistory("history", "description", vineID, new Date())
      DaoTesting.cleanAll()
      //TODO deleted because of cascade truncate
      DaoVineHistory.getVineHistory(vineID) must beEmpty
    }
    
     "recipe empty" in   {
      DaoTesting.cleanAll()
      DaoTestUtils.getSampleRecipe();
      DaoTesting.cleanAll()
      DaoRecipe.getAllItems must beEmpty
    }
     
     "comments empty" in   {
      DaoTesting.cleanAll()
      val clientID=DaoTestUtils.getSampleClient().id
      val vineID=DaoTestUtils.getSampleVine().id
      DaoComment.createComment("content", Some(vineID), Some(clientID), new Date())
      DaoTesting.cleanAll()
      DaoComment.getCommentsForClient(Some(clientID)) must beEmpty
      DaoComment.getCommentsForVine(Some(vineID)) must beEmpty
    }
     
  }
     
     
     
  
}
