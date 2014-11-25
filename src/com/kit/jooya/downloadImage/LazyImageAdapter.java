package com.kit.jooya.downloadImage;

import com.androidexample.lazyimagedownload.R;
import com.androidexample.lazyimagedownload.LazyImageLoadAdapter.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyImageAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	String[] data;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	
	public LazyImageAdapter(Context context, String[] data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder vh;
		if (convertView == null) {
			view = inflater.inflate(R.layout.listview_row, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			vh = new ViewHolder();
			vh.text = (TextView) view.findViewById(R.id.text);
			vh.text1 = (TextView) view.findViewById(R.id.text1);
			vh.image = (ImageView) view.findViewById(R.id.image);

			/************ Set holder with LayoutInflater ************/
			view.setTag(vh);
		} else
			vh = (ViewHolder) view.getTag();

		vh.text.setText("Company " + position);
		vh.text1.setText("company description " + position);
		ImageView image = vh.image;

		// DisplayImage function from ImageLoader Class
		imageLoader.DisplayImage(data[position], image);

		return view;

	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public static class ViewHolder {
		public TextView text;
		public TextView text1;
		public TextView textWide;
		public ImageView image;
	}

}
