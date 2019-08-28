package com.alvinalexander.utils

import java.io._
import java.util.Properties
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

// for the move/rename function
import java.nio.file.{Files, Paths, StandardCopyOption}

object FileUtils {
    
    val getFilepathSeparator = System.getProperty("file.separator")
    val getUserHomeDir = System.getProperty("user.home")
    val SLASH = System.getProperty("file.separator")
    
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
    def readFileWithoutCommentsOrBlankLines(filename: String): List[String] = {
        val bufferedSource = io.Source.fromFile(filename)
        val lines = (
            for {
                line <- bufferedSource.getLines()
                if !line.trim.matches("")
                if !line.trim.matches("#.*")
            } yield line
        ).toList
        bufferedSource.close
        lines
    }
    
    /**
    * Read `filename` into a sequence of strings.
    */
    def readFile(filename: String): Seq[String] = {
        val bufferedSource = io.Source.fromFile(filename)
        val lines = (for (line <- bufferedSource.getLines()) yield line).toList
        bufferedSource.close
        lines
    }
    
    /**
    * TODO untested
    * Read `filename` into a single String.
    */
    def readFileAsString(filename: String): String = {
        val bufferedSource = io.Source.fromFile(filename)
        val lines = (for (line <- bufferedSource.getLines()) yield line).toList
        bufferedSource.close
        lines.mkString
    }
    
    /**
    * write a `Seq[String]` to the `filename`.
    */
    def writeFile(filename: String, lines: Seq[String]): Unit = {
        val file = new File(filename)
        val bw = new BufferedWriter(new FileWriter(file))
        for (line <- lines) {
            bw.write(line)
        }
        bw.close()
    }
    
    /**
    * write a `String` to the `filename`.
    */
    def writeFile(filename: String, s: String): Unit = {
        val file = new File(filename)
        val bw = new BufferedWriter(new FileWriter(file))
        bw.write(s)
        bw.close()
    }
    
    def moveRenameFile(source: String, destination: String): Unit = {
        val path = Files.move(
            Paths.get(source),
            Paths.get(destination),
            StandardCopyOption.REPLACE_EXISTING
        )
        // TODO could return `path`
    }
    
    /**
    * Read a Java properties file and return it as a Properties object.
    */
    def readPropertiesFile(filename: String):Properties = {
        val properties = new Properties
        properties.load(new FileInputStream(filename))
        return properties
    }
    
    /**
    * Returns a list of all files in the given dir.
    */
    def getListOfFilesInDirectory(dirName: String): Seq[String] = {
        val dir = new File(dirName)
        dir.list
    }
    
    /**
    * Returns an Array[File] of all files that match the FileFilter,
    * such as the SoundFileFilter included in this class.
    * 
    * Related answer from StackOverflow:
    * new java.io.File(dirName).listFiles.filter(_.getName.endsWith(".txt"))
    */
    def getListOfFiles(dirName: String, fileFilter:FileFilter):Array[File] = {
        return new File(dirName).listFiles(fileFilter)
    }
    
    /**
     * This shows an example of how to use a FileFilter in Scala.
     */
    def soundFileFilter = new FileFilter {
        val okFileExtensions = Array("wav", "mp3")
        def accept(file: File):Boolean = {
            for (extension <- okFileExtensions) {
                if (file.getName.toLowerCase.endsWith(extension)) return true
            }
            return false
        }
    }
    
    /**
    * Get a recursive listing of all files underneath the given directory.
    * from stackoverflow.com/questions/2637643/how-do-i-list-all-files-in-a-subdirectory-in-scala
    */
    def getRecursiveListOfFiles(dir: File): Seq[File] = {
        val fileArray = dir.listFiles
        fileArray ++ fileArray.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
    }
    
    def getListOfFilesInDirectoryAsOption (dirName: String): Option[Seq[String]] = {
        if (dirName==null || dirName.trim=="") {
            None
        } else {
            Some(getListOfFilesInDirectory(dirName))
        }
    }

    /**
     * Create the given directory name. Only creates one directory; use
     * `createDirs` to create multiple subdirectory levels at one time.
     */
    def createDir(canonDirName: String): Boolean = (new File(canonDirName)).mkdir
    
    /**
     * Create the given directory name, including all directories in between
     * that need to be created (like `mkdir foo/bar/baz`).
     */
    def createDirs(canonDirName: String): Boolean = (new File(canonDirName)).mkdirs    
    
}

