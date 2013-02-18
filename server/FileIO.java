package de.tet.inputmaskTemplate.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class FileIO {
	/**
	 * Loads a File on the Server and returns it.
	 * @param path Path to file
	 * @return
	 */
	public static String readFile(String path)
	{			
		String result = "";
		File f = new File(path);
		
		if(f.exists())
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(path));
				String line = reader.readLine();
				
				while(line != null)
				{				
					result += line + "\n";
					line = reader.readLine();
				}
			}
			catch (Exception e)
			{
			}
			
			
			return result;
		}
		else 
			return "";
	}
	
	/**
	 * Returns an array with all filenames in the specific folder.
	 * @param folder
	 * @return
	 */
	public static String[] getFiles(String folder)
	{
		File f = new File(folder);
		if(f.isDirectory())
			return f.list();
		
		return null;
	}
	
	/**
	 * Returns an array with all filenames in the specific folder filtered by filter.
	 * @param folder
	 * @param filter
	 * @return
	 */
	public static String[] getFiles(String folder, FilenameFilter filter)
	{
		File f = new File(folder);
		if(f.isDirectory())
			return f.list(filter);
		
		return null;
	}
	
	/**
	 * Deletes the given File on the server
	 * @param path
	 */
	public static void deleteFile(String path)
	{
		File f = new File(path);
		if(f.exists())
			f.delete();
	}
}
