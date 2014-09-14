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
import dao.util.BaseTest
import dao.util.DaoTestUtils


class DaoCommentTest extends BaseTest  {
  
 "getComments" should {
   //be empty after clean covered
   
   "return sth" in   {
      val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
      val vineID:Option[Long]=Some(DaoVine.createVine("test2","description",clientID,new Date()).id)
      val clientID2:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
      DaoComment.createComment("first comment", vineID, clientID, new Date())
      DaoComment.createComment("second content", vineID, clientID, new Date())
      DaoComment.createComment("extra content", vineID, clientID2, new Date())
      DaoComment.getCommentsForClient(clientID2) must have size(1)
	  DaoComment.getCommentsForClient(clientID) must have size(2)
      DaoComment.getCommentsForVine(vineID) must have size(3)
  }
  
  "throw error for none client" in   {
	   DaoComment.getCommentsForClient(None) must
            throwA[IllegalArgumentException]
  }
  
  "throw error for none vine" in   {
	  DaoComment.getCommentsForVine(None)  must
            throwA[IllegalArgumentException]
  }
} 
 
 "activateVineComment" should {
   "activateVineComment" in   {
       val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
        val vineID:Option[Long]=Some(DaoVine.createVine("test2","description",clientID,new Date()).id)
       val commentID:Long=DaoComment.createComment("first comment", vineID, clientID, new Date()).id
       DaoComment.getCommentsForClient(clientID).head.visible must beTrue
       DaoComment.deactivateVineComment(Some(commentID))
       DaoComment.getCommentsForClient(clientID).head.visible must beFalse
	   DaoComment.activateVineComment(Some(commentID))
	   DaoComment.getCommentsForClient(clientID).head.visible must beTrue
  }
   "throw error for activate none  VineComment" in   {
	  DaoComment.activateVineComment(None)  must
            throwA[IllegalArgumentException]
  }
 }
  
  "deactivateVineComment" should {
    "deactivateVineComment" in   {
       val clientID:Option[Long]=Some(DaoTestUtils.getSampleClient.id)
       val vineID:Option[Long]=Some(DaoVine.createVine("test2","description",clientID,new Date()).id)
       val commentID:Long=DaoComment.createComment("first comment", vineID, clientID, new Date()).id
       DaoComment.getCommentsForClient(clientID).head.visible must beTrue
       DaoComment.deactivateVineComment(Some(commentID))
       DaoComment.getCommentsForClient(clientID).head.visible must beFalse
  }
    "throw error for deactivate none  VineComment" in   {
	  DaoComment.deactivateVineComment(None)  must
            throwA[IllegalArgumentException]
  }
  }

}