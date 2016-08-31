package com.akka.fileread;

/**
 * Description: FileReader main class which invokes Actors
 *
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */


import com.akka.message.FileProcessMessage;
import com.akka.util.GlobalConstants;

import akka.actor.*;

public class FileReader {
	
	public static void main( String[] args )
    {
        if( args.length < 1 )
        {
            System.out.println( "Usage: java  Akka required folder <path>" );
            System.exit( 0 );
        }

        String folderPath = args[0];       

        FileReader fileReader = new FileReader();
        fileReader.process(folderPath,GlobalConstants.SCAN);
    }
	
	public void process(String folderPath,String message)
    {
        // Create our ActorSystem
        ActorSystem actorSystem = ActorSystem.create( "fileReader" );

        // Create the FileScanner        
        ActorRef fileScanner = actorSystem.actorOf( new Props( FileScanner.class ), "fileScanner" );

        // Check Files present or not , send message
        fileScanner.tell(new FileProcessMessage(folderPath,message), fileScanner);
    }

}
