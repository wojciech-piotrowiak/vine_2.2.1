package populators

import models.Model
import pojo.Data
import play.api.Logger

trait BasePopulator[T,U] {
def populate(model:Option[T]):U=
{
 model match{
    case None=> throw new IllegalArgumentException
    case _=> return create()
  } 
}

def create():U
}