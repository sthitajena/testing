package com.akka.fileread;

/**
 * Description: Aggregator process files.
 * 
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.akka.message.FileProcessMessage;
import com.akka.message.ResultData;
import com.akka.util.GlobalConstants;

import akka.actor.UntypedActor;


public class Aggregator extends UntypedActor
{
    @Override
    public void onReceive( Object message ) throws Exception
    {
    	if( message instanceof FileProcessMessage )
    	{
    		FileProcessMessage msg = ( FileProcessMessage )message;
			Set<String> filesWithPath = msg.getFilesWithPath();
			String receivedMsg = msg.getMessage();
			if(GlobalConstants.START_OF_FILE.equals(receivedMsg)){
				
				ResultData result = new ResultData();
				for(String file : filesWithPath){
					result.getResults().put(file, countLines(file));
				}
				
				 // Send a notification back to the sender
	            getSender().tell( result, getSelf() );
			}
    		

    	}else if( message instanceof ResultData )
    	{
    		ResultData result = ( ResultData )message;

    		System.out.println( "Results: " );
    		for (String key : result.getResults().keySet()) {

    			System.out.print("File Name ---> "+key+" ---> ");
            	System.out.println("Lines ---> "+result.getResults().get(key));
    		}
    		System.out.println();

    		// Exit
    		getContext().system().shutdown();
    	}
    	else
    	{
    		unhandled( message );
    	}
    }
    
    
    public int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    
}