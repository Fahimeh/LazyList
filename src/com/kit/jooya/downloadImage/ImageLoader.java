package com.kit.jooya.downloadImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    MemoryCache memoryCache=new MemoryCache();
    ExecutorService executorService;
    FileCache fileCache;
    
	public ImageLoader(Context context) {
		// TODO Auto-generated constructor stub
		fileCache=new FileCache(context);
		
		executorService=Executors.newFixedThreadPool(5);
	}
	
	public void DisplayImage(String url,ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap=memoryCache.get(url);
		if(bitmap!=null)
			imageView.setImageBitmap(bitmap);
		else{
			queuePhoto(imageView, url);
			
			//////
			imageView.setImageResource(R.drawable.btn_default);
		}
		
	}
	
	public void queuePhoto(ImageView imageView,String url){
		PhotoToLoad photoToLoad=new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(photoToLoad));
	}
	
	private class PhotoToLoad{
		String url;
		ImageView imageView;
		public PhotoToLoad(String url,ImageView imageView) {
			// TODO Auto-generated constructor stub
			this.url=url;
			this.imageView=imageView;
		}
	}
	
	class PhotosLoader implements Runnable{

		PhotoToLoad photoToLoad;
		public PhotosLoader(PhotoToLoad photoToLoad) {
			// TODO Auto-generated constructor stub
			this.photoToLoad=photoToLoad;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(imageViewReused(photoToLoad))
				return;
			
		}
		
	}
	
	public Bitmap getBitmap(String url){
		File file=fileCache.getFile(url);
		Bitmap bitmap=decodeFile(file);
		if(bitmap!=null)
			return bitmap;

		try {
			URL imageUrl=new URL(url);
			HttpURLConnection httpUrlConnection=(HttpURLConnection) imageUrl.openConnection();
			httpUrlConnection.setConnectTimeout(30000);
			httpUrlConnection.setReadTimeout(30000);
			httpUrlConnection.setInstanceFollowRedirects(true);
			InputStream inputStream=httpUrlConnection.getInputStream();
			OutputStream outputStream=new FileOutputStream(file);
			
			copyStream(outputStream, inputStream);
			
			outputStream.close();
			httpUrlConnection.disconnect();
			
			bitmap=decodeFile(file);
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Bitmap decodeFile(File f){
		 try {
	        	
	            //Decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            FileInputStream stream1=new FileInputStream(f);
	            BitmapFactory.decodeStream(stream1,null,o);
	            stream1.close();
	            
	          //Find the correct scale value. It should be the power of 2.
	         
	            // Set width/height of recreated image
	            final int REQUIRED_SIZE=85;
	            
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            
	            //decode with current scale values
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize=scale;
	            FileInputStream stream2=new FileInputStream(f);
	            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
	            stream2.close();
	            return bitmap;
	            
	        } catch (FileNotFoundException e) {
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	}
	
	private void copyStream(OutputStream os,InputStream in){
		int buffer_size=1024;
		byte[] bytes=new byte[buffer_size];
		try{
		while(true){
			int count=in.read(bytes, 0, buffer_size);
			if(count==-1)
				break;
			os.write(bytes,0,count);
		}
		}catch(IOException io){
			Log.e("ImageLoader-->copyStream",  io.toString());
		}
	}
	
	public boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag=imageViews.get(photoToLoad.imageView);
		if(tag==null|| !tag.equals(photoToLoad.url))
			return true;
		return false;
	}
}
