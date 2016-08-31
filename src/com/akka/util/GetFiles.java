package com.akka.util;

/**
 * Description: Get Files. Get files with absolute path
 *
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class GetFiles {
	private final static Set<String> files = new HashSet<String>();
	private static GetFiles meObj = null;
	
	public static synchronized GetFiles getInstance() {
		if(meObj == null) {
			meObj = new GetFiles();
		}
		
		return meObj;
	}

	public Set<String> getFiles() {
		return files;
	}
	
	public void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            files.add(fileEntry.getAbsolutePath());
	        }
	    }
	}
	
	

}
