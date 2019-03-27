package com.example.notificationapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class CustomListView extends ArrayAdapter<String> {

    private String[] name;
    private String[] venue;
    private String[] date;
    private String[] time;
    private String[] imagepath;
    private Activity context;
    Bitmap bitmap;

    public CustomListView(Activity context, String[] name, String[] venue, String[] date, String[] time, String[] imagepath) {
        super(context, R.layout.activity_custom_list_view,name);
        this.context=context;
        this.name=name;
        this.venue=venue;
        this.date=date;
        this.time=time;
        this.imagepath=imagepath;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.activity_custom_list_view,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        viewHolder.tvw1.setText(name[position]);
        viewHolder.tvw2.setText(venue[position]);
        viewHolder.tvw3.setText(date[position]);
        viewHolder.tvw4.setText(time[position]);
        new GetImageFromURL(viewHolder.ivw).execute(imagepath[position]);

        return r;
    }

    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        TextView tvw4;
        ImageView ivw;

        ViewHolder(View v){
            tvw1=(TextView)v.findViewById(R.id.tvname);
            tvw2=(TextView)v.findViewById(R.id.tvvenue);
            tvw3=(TextView)v.findViewById(R.id.tvdate);
            tvw4=(TextView)v.findViewById(R.id.tvtime);
            ivw=(ImageView)v.findViewById(R.id.imageView);
        }

    }

    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>
    {

        ImageView imgView;
        public GetImageFromURL(ImageView imgv)
        {
            this.imgView=imgv;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay=url[0];
            bitmap=null;

            try{

                InputStream ist=new java.net.URL(urldisplay).openStream();
                bitmap= BitmapFactory.decodeStream(ist);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){

            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }




}
