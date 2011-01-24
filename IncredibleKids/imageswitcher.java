package com.example.android.apis.view;
import com.example.android.apis.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class ImageSwitcher1 extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
 public int selectedIndex = -1;
 ImageAdapter ia;
 private static int[] IMAGE_SIZE={100, 70, 50, 30, 20}; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_switcher_1);
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        ia = new ImageAdapter(this);
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setCallbackDuringFling(true);
        g.setAdapter(ia);
        g.setOnItemSelectedListener(this);
        
        g.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
   
   @Override
   public void onChildViewRemoved(View parent, View child) {
    // TODO Auto-generated method stub
    
   }
   
   @Override
   public void onChildViewAdded(View parent, View child) {
    // TODO Auto-generated method stub
    child.invalidate();
   }
  });
        
    }
    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        mSwitcher.setImageResource(mImageIds[position]);
        ia.setSelectedPosition(position);
        ia.notifyDataSetChanged();
    }
    public void onNothingSelected(AdapterView parent) {
     
    }
    public View makeView() {
        ImageView i = new ImageView(this);
//        i.setBackgroundColor(0xFF000000);
        i.setBackgroundColor(0x00000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        return i;
    }
    private ImageSwitcher mSwitcher;
    public class ImageAdapter extends BaseAdapter {
     int mSelectedPosition = -1;
     
        public ImageAdapter(Context c) {
            mContext = c;
        }
        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
         ImageView imageView;
         if(convertView!=null){
             imageView = (ImageView)convertView;
            }else{
             imageView = new ImageView(mContext);
            }
         
         imageView.setImageResource(mThumbIds[position]);
            imageView.setAdjustViewBounds(false);
            
            if(position == mSelectedPosition){
             imageView.setLayoutParams(new Gallery.LayoutParams(
                     100, 100));
            }else if(Math.abs(position-mSelectedPosition)==1 && mSelectedPosition!=-1){
             imageView.setLayoutParams(new Gallery.LayoutParams(
                     70, 70));
             
            }else if(Math.abs(position-mSelectedPosition)>=2 && mSelectedPosition!=-1){
             int size = 0;
             if(Math.abs(position-mSelectedPosition) >= IMAGE_SIZE.length){
              size = IMAGE_SIZE[IMAGE_SIZE.length-1];
             }else{
              size = IMAGE_SIZE[Math.abs(position-mSelectedPosition)];
             }
             
             imageView.setLayoutParams(new Gallery.LayoutParams(
               size, size));
             
             if(mSelectedPosition < position){
              Bitmap tmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
              Bitmap bmp = tmp.copy(tmp.getConfig(),true);
              Bitmap newBmp = cropImage(bmp, 0, 0, 15, size, size, size);
              imageView.setImageBitmap(newBmp);
                    bmp.recycle();
             }
            
            }else{
             imageView.setLayoutParams(new Gallery.LayoutParams(
                     LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
             
            }
            
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            
            
            return imageView;
        }
        private Context mContext;
        public void setSelectedPosition(int p){
         mSelectedPosition = p;
        }
    }
    
    //선택된것 보다 오른쪽에 있는 이미지들의 위아래가 바뀌어서 이미지의 좌측 일부를 잘라낼때 쓰는 함수
    private Bitmap cropImage (Bitmap bmp, int cropLeft, int cropTop, int cropRight, int cropBottom, int width, int height){
     Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
     Canvas canvas = new Canvas(newBmp);
  
  
  canvas.drawColor(Color.TRANSPARENT);
  canvas.save();
  canvas.translate(0, 0);
  canvas.clipRect(0, 0, width, height);
  canvas.clipRect(cropLeft, cropTop, cropRight, cropBottom, Op.DIFFERENCE);
  canvas.clipRect(0, 0, width, height);
        canvas.drawBitmap(bmp, new Rect(0, 0, width, height), new Rect(0, 0, width, height), new Paint());
        canvas.restore();
        
        
        
        return newBmp;
    }
    private Integer[] mThumbIds = {
            R.drawable.sample_thumb_0, R.drawable.sample_thumb_1,
            R.drawable.sample_thumb_2, R.drawable.sample_thumb_3,
            R.drawable.sample_thumb_4, R.drawable.sample_thumb_5,
            R.drawable.sample_thumb_6, R.drawable.sample_thumb_7};
    private Integer[] mImageIds = {
            R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7};
}
