package com.utility;
 
import java.util.Timer;
 
public class Scheduler {
	
	public static void main (String[] args){
		
		Timer time = new Timer();
 
		FolderMonitoringTask folderTask = new FolderMonitoringTask();
		FileCopyTask copyTask = new FileCopyTask();
 
		time.schedule(copyTask,0,120000); //every 2 minutes
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		time.schedule(folderTask,0,300000); //every 5 minutes	
		
		
	}
 
}