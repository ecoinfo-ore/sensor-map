
package com.rac021.jaxy.unzipper ;

/**
 *
 * @author ryahiaoui
 */
import java.io.File ;
import java.util.List ;
import java.nio.file.Path ;
import java.util.Optional ;
import java.nio.file.Files ;
import java.nio.file.Paths ;
import java.io.IOException ;
import java.util.zip.ZipEntry ;
import java.util.logging.Level ;
import java.io.FileInputStream ;
import java.util.logging.Logger ;
import java.io.FileOutputStream ;
import java.util.zip.ZipInputStream ;
import java.io.BufferedOutputStream ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.api.caller.UncheckCall.uncheckCall ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

public class UnzipUtility {

    /**
     * Size of the buffer to read/write data .
     */
    private static final int    BUFFER_SIZE = 4096        ;

    private static final Logger LOGGER      = getLogger() ;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified 
     * by destDirectory (will be created if does not exists)
     * 
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */

    private static Void unzip(String zipFilePath, String destDirectory) throws IOException {

        File destDir = new File(destDirectory) ;

        if (!destDir.exists()) {

            boolean created = destDir.mkdirs() ;
            if (!created) {
                LOGGER.log(Level.SEVERE, " Can't create Folder !")   ;
                throw new RuntimeException(" Can't create Folder !") ;
            }
        }

        try ( ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {

            ZipEntry entry = zipIn.getNextEntry() ;

            /** iterates over entries in the zip file . */
            while (entry != null) {

                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    /** if the entry is a file, extracts it . */
                    extractFile(zipIn, filePath) ;
                } else {
                    /** if the entry is a directory, make the directory . */
                    File dir = new File(filePath) ;
                    dir.mkdir()                   ;
                }
                zipIn.closeEntry()                ;
                entry = zipIn.getNextEntry()      ;
            }
        }
        return null ;
    }

    /**
     * Extracts a zip entry (file entry)
     * 
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[BUFFER_SIZE]     ;
            int    read    = 0                         ;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read)            ;
            }
        }
    }
    
    public static void unzipJavaEEdependency( String searchInto , String destination) throws Exception {

        Optional<Path> javaEEPath = Files.list(Paths.get(searchInto))
                                         .filter(file -> (file.getFileName().toFile().getName().contains("javaee-api-8.")
                                                 || file.getFileName().toFile().getName().contains("javaee-api-7."))
                                                 && file.getFileName().toFile().getName().endsWith(".jar"))
                                         .findFirst();

        if (!javaEEPath.isPresent()) {

            LOGGER.log(Level.SEVERE, message("stars_4"))               ;
            LOGGER.log(Level.SEVERE, message("java_ee_dep_not_found")) ;
            LOGGER.log(Level.SEVERE, message("compilation_abort_01"))  ;
            LOGGER.log(Level.SEVERE, message("stars_4"))               ;

            throw new IllegalAccessException ( "\n   *********************************  \n" +
                                               "   Java-EE Dependency not found !       \n" +
                                               "   Compilation Abort                    \n" +
                                               "   *********************************** " )  ;
        } else {

            String JAVA_EE = javaEEPath.get().getFileName().toString().contains("javaee-api-8.") ?
                                              "JAVA EE 8 " : "JAVA EE 7 "                        ;

            LOGGER.log(Level.INFO, message("stars_4"))           ;
            LOGGER.log(Level.INFO, message("java_ee_dep"))       ;
            LOGGER.log(Level.INFO, message("code_01"), JAVA_EE ) ;
            LOGGER.log(Level.INFO, message("stars_4"))           ;
        }

        try {
            if (javaEEPath.isPresent())    {
                unzip(javaEEPath.get().toString(), destination ) ;
            }
        } catch (IOException ex )          {
            throw new RuntimeException(ex) ;
        }
    }

    public static void unzipJavaDependencies(List<Optional<Path>> deps, String destination) throws Exception {

        deps.forEach( dep -> {

                 if (!dep.isPresent()) {

                    LOGGER.log(Level.SEVERE, message("stars_5"))            ;
                    LOGGER.log(Level.SEVERE, message("dep_not_found"), dep) ;
                    LOGGER.log(Level.SEVERE, message("compilation_abort"))  ;
                    LOGGER.log(Level.SEVERE, message("stars_5"))            ;

                    throw new RuntimeException ( "   \n*********************************  \n" +
                                                 "   " + dep + " Dependency not found !   \n" +
                                                 "   Compilation Abort                    \n" +
                                                 "   *********************************** " )  ;
                } else {
                     
                    LOGGER.log(Level.INFO, message("stars_4"))            ;
                    LOGGER.log(Level.INFO, message("java_dep"))           ;
                    LOGGER.log(Level.INFO, message("code_01"), dep.get()) ;
                    LOGGER.log(Level.INFO, message("stars_4"))            ;

                    uncheckCall( () -> UnzipUtility.unzip(dep.get().toString(), destination)) ;
                 }
        }) ;
        
    }
    
}

