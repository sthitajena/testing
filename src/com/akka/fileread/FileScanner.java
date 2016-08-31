package com.akka.fileread;

/**
 * Description: FileScanner checks files presents,
 * and send message to File parser to parse files
 *
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */

import java.io.File;
import com.akka.message.FileProcessMessage;
import com.akka.util.GetFiles;
import com.akka.util.GlobalConstants;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class FileScanner extends UntypedActor
{


	@Override
	public void onReceive( Object message )
	{
		if( message instanceof FileProcessMessage )
		{
			FileProcessMessage msg = ( FileProcessMessage )message;
			String folderPath = msg.getFolderpath();
			String receivedMsg = msg.getMessage();
			if(GlobalConstants.SCAN.equals(receivedMsg)){
				final File folder = new File(folderPath);
				GetFiles.getInstance().listFilesForFolder(folder);
			}
			if(GetFiles.getInstance().getFiles().size()>0){
				final ActorRef aggregator = this.getContext().actorOf( new Props( Aggregator.class ), "aggregator" );
				ActorRef fileParser = this.getContext().actorOf( new Props( new UntypedActorFactory() {
					public UntypedActor create() {
						return new FileParser( 10, aggregator );
					}
				}), "fileParser" );

				fileParser.tell(new FileProcessMessage(folderPath,GlobalConstants.PARSE), fileParser);

			}else{
				System.out.println("Warning : No Files present to process !!");
			}

		}
		else
		{
			unhandled( message );
		}
	}



}
