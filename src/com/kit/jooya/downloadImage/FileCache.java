package com.kit.jooya.downloadImage;

import java.io.File;

import android.content.Context;
import android.text.AndroidCharacter;

public class FileCache {

	File cacheDir;
	public FileCache(Context context) {
		// TODO Auto-generated constructor stub
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
		else
			cacheDir=context.getCacheDir();
		if(!cacheDir.exists())
			cacheDir.mkdirs();
	}
	
	public File getFile(String url) {
		String fileName=String.valueOf(url.hashCode());
		File f=new File(cacheDir, fileName);
		return f;
	}
	
	public void clear(){
		File[] files=cacheDir.listFiles();
		if(files==null)
			return;
		for(File f:files)
			f.delete();
	}
}
