package com.alvinalexander.tests
// note: this^^^ is intentionally in a different package

import org.scalatest.funsuite.AnyFunSuite
import com.alvinalexander.utils.FileUtils._
import scala.util.{Try, Success, Failure}
import java.util.Properties
import java.io.File

/**
 * These are some hokey tests, but they’re a start.
 */
class WordCountTests extends AnyFunSuite {

    val currentDirectory   = System.getProperty("user.dir")
    val testFilesDir       = s"${currentDirectory}/src/test/resources"
    val testFile1          = s"${testFilesDir}/test-file-1.txt"
    val testFileWithCommentsAndBlanks = s"${testFilesDir}/file-with-comments-and-blanks.txt"
    val testPropertiesFile = s"${testFilesDir}/test-properties.props"
    val nonExistentFile    = "/foo/bar"

    test("just showing how i can print debug output to the console/stdout") {
        info(s"DEBUG: currentDirectory = $currentDirectory")   // DEBUG OUTPUT
        assert(1 == 1)
    }

    test("read a test file as a String") {
        val maybeText: Try[String] = readFile(testFile1)
        maybeText match {
            case Success(data) => assert(data.length > 10)
            case Failure(t) => fail("")
        }
    }

    test("read a test file as a Seq[String]") {
        val maybeText: Try[Seq[String]] = readFileAsSeq(testFile1)
        maybeText match {
            case Success(seqString) => assert(seqString.length > 1)
            case Failure(t) => fail("")
        }
    }

    test("try reading a file that doesn’t exist") {
        val maybeText: Try[String] = readFile(nonExistentFile)
        maybeText match {
            case Success(data) => fail("yikes, this should not succeed")
            case Failure(t) => succeed
        }
    }

    test("read a test file with comments and blank lines") {
        val maybeText: Try[Seq[String]] = readFileWithoutCommentsOrBlankLines(testFileWithCommentsAndBlanks)
        maybeText match {
            case Success(seqString) => assert(seqString.length < 5)
            case Failure(t) => fail("")
        }
    }

    test("try reading a properties file") {
        val maybeText: Try[Properties] = readPropertiesFile(testPropertiesFile)
        maybeText match {
            case Success(props) => {
                assert(props.getProperty("first_name") == "Alvin")
                assert(props.getProperty("last_name") == "Alexander")
            }
            case Failure(t) => fail("yikes, you should not see this")
        }
    }

    test("try reading a properties file that doesn’t exist") {
        val maybeText: Try[Properties] = readPropertiesFile(nonExistentFile)
        maybeText match {
            case Success(data) => fail("yikes, this should not succeed")
            case Failure(t) => succeed
        }
    }

    test("try listing test files") {
        val maybeListOfFiles: Try[Seq[String]] = getListOfFilesInDirectory(testFilesDir)
        maybeListOfFiles match {
            case Success(listOfFiles) => assert(listOfFiles.length > 2)
            case Failure(t) => fail("this should not fail")
        }
    }

    test("try listing test files in a directory that doesn’t exist") {
        val maybeListOfFiles: Try[Seq[String]] = getListOfFilesInDirectory(nonExistentFile)
        maybeListOfFiles match {
            case Success(xs) => fail("this should not succeed")
            case Failure(t)  => succeed
        }
    }

    test("test writing to a file") {
        // [0] some test info
        val testContent = """
            |line 1
            |line 2
            """.stripMargin.trim
        val testFilename = s"${testFilesDir}/writing-test.txt"

        // [1] write some content
        val res1 = writeFile(testFilename, testContent)
        res1.isInstanceOf[Success[Unit]]

        // [2] read it back in
        val maybeText: Try[String] = readFile(testFilename)
        maybeText match {
            case Success(s) => assert(s.length > 8)
            case Failure(t) => fail("")
        }

        // [3] delete the tmp file 
        val res2 = deleteFile(testFilename)
        res2.isInstanceOf[Success[Unit]]
    }

}



