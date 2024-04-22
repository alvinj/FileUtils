package com.alvinalexander.utils

import java.io._
import java.util.Properties
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Using}

// for the move/rename function
import java.nio.file.{Files, Paths, StandardCopyOption}

object FileUtils {
    
    val getFilepathSeparator = System.getProperty("file.separator")
    val getUserHomeDir = System.getProperty("user.home")
    val SLASH = System.getProperty("file.separator")

    /**
     * Read a file as a `String`.
     */
    def readFile(filename: String): Try[String] = Using(io.Source.fromFile(filename)) { bufferedSource =>
        bufferedSource
            .getLines()
            .map(line => s"$line\n")  // need to add the "\n" character back in
            .mkString
    }

    /**
     * Read a file as a `Seq[String]`.
     */
    def readFileAsSeq(filename: String): Try[Seq[String]] =
        Using(io.Source.fromFile(filename)) { bufferedSource =>
            bufferedSource
                .getLines()
                .toSeq
        }
    
    /**
    * Get the contents of a text file while skipping over comment lines and
    * blank lines. This is useful when reading a data file that can have lines
    * beginning with '#', or blank lines, such as a file that looks like this:
    *   -------------------
    *   foo
    *   # this is a comment
    *
    *   bar
    *   -------------------
    */
    def readFileWithoutCommentsOrBlankLines(filename: String): Try[Seq[String]] = Using(io.Source.fromFile(filename)) { bufferedSource =>
        bufferedSource
            .getLines()
            .toSeq
            .filterNot(_.trim.matches(""))
            .filter(!_.startsWith("#"))
            .map(line => s"$line\n")  // need to add the "\n" character back in
    }

    /**
     * Write a `String` to the `filename`.
     */
    def writeFile(filename: String, content: String): Try[Unit] =
        Using(new BufferedWriter(new FileWriter(new File(filename), true ))) { bufferedWriter =>
            bufferedWriter.write(content)
        }

    /**
     * Write a `Seq[String]` to the `filename`.
     */
    def writeFile(filename: String, lines: Seq[String]): Try[Unit] = {
        Using(new BufferedWriter(new FileWriter(new File(filename), true ))) { bufferedWriter =>
            for (line <- lines) {
                bufferedWriter.write(line)
            }
        }
    }
    
    /**
     * Read a Java properties file and return it as a Properties object.
     */
    def readPropertiesFile(filename: String): Try[Properties] = Try{
        val properties = new Properties
        properties.load(new FileInputStream(filename))
        properties
    }
    
    /**
     * Returns a list of all files in the given dir.
     */
    def getListOfFilesInDirectory(dirName: String): Try[Seq[String]] = Try{
        val dir = new File(dirName)
        dir.list().toList
    }

    /**
     * Delete the given filename.
     */
    def deleteFile(filename: String): Try[Unit] = Try {
        (new File(filename)).delete()
    }
    
   // // TODO: needs to return `Try`
    // def moveRenameFile(source: String, destination: String): Unit = {
    //     val path = Files.move(
    //         Paths.get(source),
    //         Paths.get(destination),
    //         StandardCopyOption.REPLACE_EXISTING
    //     )
    //     // TODO could return `path`
    // }
    
    // /**
    //  * TODO: Use `Try`.
    //  * Returns an Array[File] of all files that match the FileFilter,
    //  * such as the SoundFileFilter included in this class.
    //  * 
    //  * Related answer from StackOverflow:
    //  * new java.io.File(dirName).listFiles.filter(_.getName.endsWith(".txt"))
    //  */
    // def getListOfFiles(dirName: String, fileFilter:FileFilter): Seq[File] = {
    //     return new File(dirName).listFiles(fileFilter)
    // }
    
    // /**
    //  * TODO: Use `Try`.
    //  * This shows an example of how to use a FileFilter in Scala.
    //  */
    // def soundFileFilter = new FileFilter {
    //     val okFileExtensions = Seq("wav", "mp3")
    //     def accept(file: File):Boolean = {
    //         for (extension <- okFileExtensions) {
    //             if (file.getName.toLowerCase.endsWith(extension)) return true
    //         }
    //         return false
    //     }
    // }
    
    // /**
    //  * TODO: Use `Try`.
    //  * Get a recursive listing of all files underneath the given directory.
    //  * from stackoverflow.com/questions/2637643/how-do-i-list-all-files-in-a-subdirectory-in-scala
    //  */
    // def getRecursiveListOfFiles(dir: File): Seq[File] = {
    //     val fileArray = dir.listFiles
    //     fileArray ++ fileArray.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
    // }
    
    // def getListOfFilesInDirectoryAsOption (dirName: String): Option[Seq[String]] = {
    //     if (dirName==null || dirName.trim=="") {
    //         None
    //     } else {
    //         Some(getListOfFilesInDirectory(dirName))
    //     }
    // }

    // /**
    //  * TODO: Use `Try`.
    //  * Create the given directory name. Only creates one directory; use
    //  * `createDirs` to create multiple subdirectory levels at one time.
    //  */
    // def createDir(canonDirName: String): Boolean = (new File(canonDirName)).mkdir
    
    // /**
    //  * TODO: Use `Try`.
    //  * Create the given directory name, including all directories in between
    //  * that need to be created (like `mkdir foo/bar/baz`).
    //  */
    // def createDirs(canonDirName: String): Boolean = (new File(canonDirName)).mkdirs    
    
}

