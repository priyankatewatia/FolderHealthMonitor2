package com.utility;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TimerTask;

public class FolderMonitoringTask extends TimerTask {

	Path securedFolder = Paths.get("/Users/pt/secured");
 
	Path archiveFolder = Paths.get("/Users/pt/archive");
	
	@Override
	public void run() {	
	
		System.out.println("Monitoring Folder every 5 minutes....");
		
		long currentFolderSize = 0;
 
		try{				
			currentFolderSize = getSecuredFolderSize(securedFolder.toString());
 
			System.out.println("Current Size of Secured Folder is :" + currentFolderSize);
 
			if (currentFolderSize > 990000){
 
				archiveOlderFiles(securedFolder.toString(), archiveFolder.toString());
			}
	
		} catch (Exception e){
			e.printStackTrace();
		}
      
	}
 
	private static void archiveOlderFiles(String securedFolderPath, 
			String archiveFolderPath) {
 
		System.out.println("Archiving Older Files Started.....");
 
		long folderSize = 0;
 
		File folder = new File(securedFolderPath);
 
		File[] listOfFiles = folder.listFiles();
 
		List<File> listOfArchiveFiles = new ArrayList<File>();	
		
		//Sorting array of files based on last modified date
		Arrays.sort(listOfFiles, new Comparator<File>(){
 
			@Override
			public int compare(File file1, File file2) {
 
				return Long.valueOf(file2.lastModified()).compareTo(file1.lastModified());
			}			
		});
		
		//retain latest files up to 990000 in the secured folder and
		//archive the older files to archive folder
		if (listOfFiles.length > 0) {	
		
			for (File file : listOfFiles){		
		
				folderSize += file.length();	
			
				if (folderSize > 990000){	
				
					listOfArchiveFiles.add(file);
				}
			}
			System.out.println("No. of files to be archived : " + listOfArchiveFiles.size());
 
			moveFilesToArchiveFolder(listOfArchiveFiles.toArray(), securedFolderPath, archiveFolderPath);
		}		
	}
	
	private static void moveFilesToArchiveFolder(
			Object[] listOfArchiveFiles, String securedFolderPath, 
			String archiveFolderPath) {
 
		System.out.println("Moving older files to archive folder...");
	
		int archivedFilesCount = 0;
 
		try{
			for(int i=0; i<listOfArchiveFiles.length; i++){	
			
				File sharedFolderFile = new File(listOfArchiveFiles[i].toString());	
			
				File source = new File(securedFolderPath + "//" + sharedFolderFile.getName());
 
				File destination = new File(archiveFolderPath + "//" + sharedFolderFile.getName());
 
				//Move the file as an atomic file system operation.
				Files.move(source.toPath(), destination.toPath(), 
						StandardCopyOption.ATOMIC_MOVE);
 
				archivedFilesCount += 1;
 
				System.out.println("Number of files archived: " + archivedFilesCount);
			}
			System.out.println("Total count of archived files : " + archivedFilesCount);
		}
		catch (IOException e) {			
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}		
	}
 
	private static long getSecuredFolderSize(String securedFolderPath) {	
	
		System.out.println("Fetching current secured folder size...");
 
		long size = 0;
 
		File folder = new File(securedFolderPath);
 
		File[] listOfFiles = folder.listFiles();
 
		if (listOfFiles.length > 0) {
 
			for (File file : listOfFiles){
 
				if (file.toString().contains(".bat") || file.toString().contains(".sh")){
 
					file.delete();
 
					System.out.println("File deleted :" + file.getName());
 
					continue;
				}					
				if(file.isFile()){	
						
					size += file.length();
				}				
			}			
		}		
		return size;
		
	}
 
}