package modelscase class Article(  id: Option[Long],  title: String,  presentation: String,  body: String,  author: String)object ArticleFormat {  import play.api.libs.json.Json  implicit val format = Json.format[Article]}