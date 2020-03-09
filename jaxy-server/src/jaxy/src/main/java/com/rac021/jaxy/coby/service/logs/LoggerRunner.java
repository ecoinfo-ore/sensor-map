
package com.rac021.jaxy.coby.service.logs ;

/**
 *
 * @author ryahiaoui
 */

import java.io.File ;
import java.io.IOException ;
import java.io.RandomAccessFile ;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ArrayBlockingQueue ;
 

public class LoggerRunner /*implements Runnable*/ {
 
 
//	private File  crunchifyFile              = null  ;
//	private int   crunchifyRunEveryNSeconds  = 1500  ;
//        public  final BlockingQueue<String> logs         ;
//        
//	private long      lastKnownPosition      = 0     ;
//	private int       crunchifyCounter       = 0     ;
//	private boolean   shouldIRun             = true  ;
//	private boolean   debug                  = false ;
// 
//        private final int QUEUE_SIZE             = 2000  ;
//      
//      
//	public LoggerRunner(String crunchifyFile, int myInterval ) {
//	  this.crunchifyFile             = new File(crunchifyFile) ;
//	  this.crunchifyRunEveryNSeconds = myInterval              ;
//          this.logs = new ArrayBlockingQueue<>( QUEUE_SIZE )       ;
//	}
// 	
//	public void stopRunning() {
//		shouldIRun = false;
//	}
// 
//        @Override
//	public void run() {
//		try {
//			while (shouldIRun) {
//                            
//				long fileLength = crunchifyFile.length() ;
//                                
//				if (fileLength > lastKnownPosition) {
// 
//                                    try ( RandomAccessFile readWriteFileAccess = new RandomAccessFile(crunchifyFile, "rw")) {
//                                        readWriteFileAccess.seek(lastKnownPosition);
//                                        String crunchifyLine = null ;
//                                        while ((crunchifyLine = readWriteFileAccess.readLine()) != null) {
//                                            TimeUnit.MILLISECONDS.sleep(crunchifyRunEveryNSeconds) ;
//                                            logs.put(crunchifyLine) ;
//                                            crunchifyCounter++      ;
//                                        }
//                                        lastKnownPosition = readWriteFileAccess.getFilePointer();
//                                    }
//				} else {
//					if (debug) {
//                                          System.out.println(" Hmm.. Couldn't found new line after line # " + crunchifyCounter ) ;
//                                        }
//				}
//			}
//		} catch (IOException  | InterruptedException e ) {
//			stopRunning() ;
//		}
//		if (debug) {
//                    System.out.println(" Exit the program..." ) ;
//                }
//	}
        
}
