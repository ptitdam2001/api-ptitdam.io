package controllers

import org.slf4j._
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


object Articles extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger("article")

  def collection: JSONCollection = db[JSONCollection]("articles")

  import models.MongoArticle

  def create = Action.async(parse.json) { request =>
    request.body.validate[MongoArticle].map {
      article =>
        // `user` is an instance of the case class `models.User`
        collection.insert(article).map {
          lastError =>
            logger.debug(s"Successfully inserted with LastError: $lastError")
            Created(s"Article Created")
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def getAll = Action.async {request =>
    val cursor = collection.find(Json.obj()).cursor[MongoArticle]

    val futureArticlesList: Future[List[MongoArticle]] = cursor.collect[List]()

    futureArticlesList.map { articles =>
      Ok(Json.obj("articles" -> (articles map MongoArticle.format.writes)))
    }
  }
}
