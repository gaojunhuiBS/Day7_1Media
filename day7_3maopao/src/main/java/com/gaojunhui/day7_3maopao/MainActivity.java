package com.gaojunhui.day7_3maopao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_text;
    private int arr[]={4,2,5,1,6,2,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text= (TextView) findViewById(R.id.tv_text);
        maoPaoText(arr,tv_text);
    }
    public void maoPaoText(int[] arr,TextView tv_text){
        int temp;
        for (int i=arr.length-1;i>=0;i--){
            for (int j=0;j<i;j++){
                if (arr[j]>arr[j+1]){
                    temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }
        }
        String str="冒泡：";
        for (int i=0;i<arr.length;i++){
            str=str+arr[i];
            tv_text.setText(str);
            Log.i("tag", "maoPaoText:"+tv_text.getText());
        }
        Log.i("ccc", "maoPaoText: "+str);
    }
}
