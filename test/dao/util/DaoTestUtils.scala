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
import models.Client
import dao.BaseEntity


object DaoTestUtils {
  
def getNextClientLogin() :String={
  var clientNumber=DaoClient.getAllItems.size
  clientNumber=clientNumber+10
  return clientNumber.toString()
}  
   
def getSampleClient():BaseEntity=
{
  return  DaoClient.createClient(getNextClientLogin(), "firstName", "lastName",new Date())
}

def getSampleVine():BaseEntity=
{
  val clientID= getSampleClient().id
  return   DaoVine.createVine("test2","description",Some(clientID),new Date())
}

def getSampleRecipeVine():BaseEntity=
{
  val clientID= getSampleClient().id
  val recipeID=getSampleRecipe().id
  return   DaoVine.createVineWithRecipe("label", "description", Some(clientID), Some(recipeID), new Date())
}
def getSampleRecipeAutoVine():BaseEntity=
{
  val clientID= getSampleClient().id
  val recipeID=DaoRecipe.createRecipe("recipeLabel", "recipeDescription", Some(clientID), new Date()).id
 return   DaoVine.createVineWithRecipe("label", "description", Some(clientID), Some(recipeID), new Date())
}

def getSampleVineHistory():BaseEntity=
{
  val vineID=getSampleVine().id
  return  DaoVineHistory.createVineHistory("history", "description", vineID, new Date())
}

def getSampleRecipe():BaseEntity=
{
  val clientID= getSampleClient().id
  return   DaoRecipe.createRecipe("recipeLabel", "recipeDescription", Some(clientID), new Date())
}
def getSampleComment():BaseEntity=
{
  val clientID= getSampleClient().id
  val vineID=getSampleVine().id
  return  DaoComment.createComment("content", Some(vineID), Some(clientID), new Date())
} 
def getSampleAutoComment():BaseEntity=
{
  val clientID= getSampleClient().id
  val vineID=DaoVine.createVine("test2","description",Some(clientID),new Date()).id
  return  DaoComment.createComment("content", Some(vineID), Some(clientID), new Date())
}
}
