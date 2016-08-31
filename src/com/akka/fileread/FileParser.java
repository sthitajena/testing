package com.akka.fileread;

/**
 * Description: FileParser creates workers to process multiple files.
 * 
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.akka.message.FileProcessMessage;
import com.akka.message.ResultData;
import com.akka.util.GetFiles;
import com.akka.util.GlobalConstants;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class FileParser extends UntypedActor
{
	private  Set<String> files =  new HashSet<String>();
	private final ActorRef aggregator;
	private final ActorRef workerRouter;
	private final int numWorkers;
	private ResultData finalResults = new ResultData();
	private int numberResults = 0;

	public FileParser( final int numWorkers, ActorRef aggregator )
	{
		// Save our parameters locally
		this.numWorkers = numWorkers;
		this.aggregator = aggregator;

		// Create a new router to distribute messages out to 10 workers
		workerRouter = this.getContext().actorOf( new Props(Aggregator.class ).withRouter( new RoundRobinRouter( numWorkers )), "workerRouter" );
	}

	@Override
	public void onReceive( Object message )
	{
		if( message instanceof FileProcessMessage )
		{
			FileProcessMessage msg = ( FileProcessMessage )message;
			String receivedMsg = msg.getMessage();
			if(GlobalConstants.PARSE.equals(receivedMsg)){
				files = GetFiles.getInstance().getFiles();
			}
			//Split many files to small set to process
			List<Set<String>> fileNames = new ArrayList<Set<String>>(5);
			for (int i = 0; i < 5; i++) {
				fileNames.add(new HashSet<String>());
			}

			int index = 0;
			for (String object : files) {
				fileNames.get(index++ % 5).add(object);
			}

			for( int i=0; i<numWorkers; i++ )
			{
				if(i == fileNames.size()){
					break;
				}else{
					//Send  files at a worker to process.
					workerRouter.tell( new FileProcessMessage( fileNames.get(i), GlobalConstants.START_OF_FILE ), getSelf() );
				}
			}


		}
		else if( message instanceof ResultData )
		{
			// We received results from our worker: add its results to our final results
			ResultData result = ( ResultData )message;

			for (String key : result.getResults().keySet()) {          	


				finalResults.getResults().put(key, result.getResults().get(key));


			}
			if( ++numberResults >=  5)
			{
				// Notify 
				aggregator.tell( finalResults, getSelf() );

				// Stop our actor hierarchy
				getContext().stop( getSelf() );

			}

		}
		else
		{
			unhandled( message );
		}
	}

}
