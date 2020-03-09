
package com.rac021.jaxy.coby.io ;

import com.rac021.jaxy.api.exceptions.BusinessException;
import java.io.File ;
import java.util.List ;
import java.nio.file.Path ;
import java.io.IOException ;
import java.nio.file.Paths ;
import java.nio.file.Files ;
import java.util.Comparator ;
import java.nio.file.LinkOption ;
import java.nio.file.FileVisitOption ;
import java.nio.file.StandardOpenOption ;
import java.nio.charset.StandardCharsets ;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ryahiaoui
 */
public class Writer {

    public static String getFolder(String outputFile ) {
       Path path = Paths.get(outputFile)  ;
       return path.getParent().toString() ;
    }

    public static String getfileName(String outputFile) {
         Path path = Paths.get(outputFile)    ;
         return path.getFileName().toString() ;
    }
    
      
    public static String getFileExtension( String fileName ) {      
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0 )
        return fileName.substring(fileName.lastIndexOf(".") ) ;
        else return "" ;
    }
     
    public static String getFileWithoutExtension( String fileName ) {      
         return fileName.replaceFirst("[.][^.]+$", "") ;
    }

    public static boolean isFullStack(int size ) {
        return size >= 50 ;
    }

    public static long getTotalDirectories( String path ) throws IOException {
       
     if( ! existFile( path) ) return 0 ;
        
     long total = Files.find ( Paths.get( path ),  1 ,  // profondeur
                  ( p, attributes ) -> attributes.isDirectory() ).count() - 1 ; 
     // -1 because path is also counted in
     return total ;
    }
      
    public List<String> readTextFile(String fileName) throws IOException {
    Path path = Paths.get(fileName) ;
    return Files.readAllLines(path, StandardCharsets.UTF_8) ;
    }

    public static void writeTextFile(List<String> strLines, String fileName)  {
       Path path = Paths.get(fileName) ;
        try {
            Files.write(path, strLines, StandardCharsets.UTF_8,  StandardOpenOption.APPEND) ;
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public static void checkFile(String path ) throws IOException   {
       
       String directory = path.substring(0 , path.lastIndexOf("/")) ;
       
       boolean exists = existFile ( path ) ;
       
       if(!exists) {
          checkDirectory(directory) ;
       }
       else {
           deleteFile(path) ;
       }
       
       createFile(path) ;
    }

    public static boolean existFile( String path ) {
        
        if( path == null ) return false ;
        Path pat = Paths.get( path )    ;
        return  Files.exists( pat  ,
                              new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}) ;
    }
    
    public static void checkDirectory( String directory ) throws IOException {
      
     Path path = Paths.get(directory) ;
     if(!Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}))
       Files.createDirectory( path )  ;
    }
    
    public static void deleteFile( String path ) throws IOException {
      Path pat = Paths.get(path) ;
      Files.delete(pat) ;
    }
    
    public static void createFile( String path ) throws IOException {
        File file = new File(path) ;
        file.createNewFile()       ;
    }
                     
    public static boolean isEmptyFolder(String outputDataFolder) throws BusinessException {
       File file = new File(outputDataFolder) ;
       if(file.isDirectory()){
         return file.list().length == 0 ;
       } else {
           throw new BusinessException("\n [ + " + outputDataFolder + " ] is not a valid Directory \n ") ;
       } 
    }
    
    public static void removeDirectory(String directory) throws Exception {
     
      Path rootPath = Paths.get(directory)                      ;
      
      if ( Files.exists( rootPath, LinkOption.NOFOLLOW_LINKS) ) {

        Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete) ;
        
        if ( ! Files.exists(Paths.get(directory) , 
             new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}) )
        Files.createDirectory(Paths.get(directory))        ; 

      }
      
    }
}
