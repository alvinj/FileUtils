import java.io.File


/**
 * A little “test” to verify this method works as desired.
 */
object Driver extends App {

    import com.alvinalexander.utils.FileUtils

    val filename = "/Users/al/Projects/HelloScala2ScalaLang.org/docs.scala-lang/hello-scala/LIST_OF_FILES_IN_ORDER"
    val lines = FileUtils.readFileWithoutCommentsOrBlankLines(filename)
    lines.foreach(println)

}