package dao.util

import org.specs2.mutable.Specification
import org.specs2.specification.Fragments
import org.specs2.specification.Step
import play.api.test._
import play.api.test.Helpers._
import play.api.Play

class BaseTest extends Specification   {

  // see http://bit.ly/11I9kFM (specs2 User Guide)
  override def map(fragments: =>Fragments) = {
    beforeAll()
    fragments ^ Step(afterAll)
  }
//sequential
 lazy val app : FakeApplication = {
        FakeApplication(additionalConfiguration = inMemoryDatabase())
        //additionalConfiguration = inMemoryDatabase()
    }

    def beforeAll(){
        Play.start(app)
    }

    def afterAll(){
        Play.stop()
    }

}