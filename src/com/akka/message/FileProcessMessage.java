package com.akka.message;

/**
 * Description: FileProcessMessage : setter ,getter for messages
 *
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class FileProcessMessage implements Serializable
{
	private static final long serialVersionUID = -217827784411885005L;
	private String message;
	private String folderpath;
	private Set<String> filesWithPath = new HashSet<String>();
	 
	public FileProcessMessage(String folderPath,String msg) {
        this.setFolderpath(folderPath);
        this.message = msg;
    }
	
	public FileProcessMessage(Set<String> filesWithPath,String msg) {
        this.filesWithPath=filesWithPath;
        this.message = msg;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFolderpath() {
		return folderpath;
	}

	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}

	public Set<String> getFilesWithPath() {
		return filesWithPath;
	}

	public void addFilesWithPath(String filesWithPath) {
		this.filesWithPath.add(filesWithPath);
	}

}
