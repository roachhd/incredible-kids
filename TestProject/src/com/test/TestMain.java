package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestMain extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btnDrag = (Button)findViewById(R.id.btn_drag);
        Button btnLine = (Button)findViewById(R.id.btn_line);
        Button btnDragNE = (Button)findViewById(R.id.btn_drag_no_engine);
        Button btnDragC = (Button)findViewById(R.id.btn_drag_canvas);
        btnDrag.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent dragIntent = new Intent(TestMain.this, TouchDragExample.class);
				startActivity(dragIntent);
			}
        	
        });
        btnLine.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent dragIntent = new Intent(TestMain.this, DragLineExample.class);
				startActivity(dragIntent);
			}
        	
        });
        btnDragNE.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent dragIntent = new Intent(TestMain.this, ViewDragAndDrop.class);
				startActivity(dragIntent);
			}
        	
        });
        btnDragC.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent dragIntent = new Intent(TestMain.this, DragAndDropCanvas.class);
				startActivity(dragIntent);
			}
        	
        });
    }
}
