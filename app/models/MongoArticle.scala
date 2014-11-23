package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import reactivemongo.bson.BSONObjectID

case class MongoArticle(
                         id: Option[BSONObjectID],
                         title: String,
                         presentation: String,
                         body: String,
                         author: String)
object MongoArticle {

  def reader: Reads[MongoArticle] = (
//        (__ \ "_id").readNullable[String] and
        (__ \ "title").read[String] and
        (__ \ "presentation").read[String] and
        (__ \ "body").read[String] and
        (__ \ "author").read[String])((/*id,*/ title, prez, body, author) => MongoArticle(/*Some(BSONObjectID(id.get))*/None, title, prez, body, author))

  def writer: Writes[MongoArticle] = new Writes[MongoArticle] {
    override def writes(o: MongoArticle): JsValue = Json.obj(
    "id" -> o.id.map(_.toString()),
    "title" -> o.title,
    "presentation" -> o.presentation,
    "body" -> o.body,
    "author" -> o.author
    )
  }

  implicit val format = Format(reader, writer)


}