package com.example.dw.helloword;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by DW on 2016/7/27.
 */
public class RightView extends View  {
    private String[] letters={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    private int len=letters.length;

    private OnTextViewChanges mOnTextViewChanges;

    public RightView(Context context, AttributeSet attr){
        super(context,attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        int x=getWidth();
        int y=getHeight();

         int singleHeight=y/len;

        for(int i=0;i<len;i++){
            paint.setColor(getResources().getColor(R.color.black));
            paint.setTextSize(25.0F);
            canvas.drawText(letters[i],25,singleHeight*(i+1),paint);
        }

    }

    public interface OnTextViewChanges{
        void onTextChange(String  ch);
    }

    public void setOnTextChanges(OnTextViewChanges onTextChanges){
        mOnTextViewChanges =onTextChanges;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x=(int)event.getX();
        float y=(int)event.getY();
         int i;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                i=(int)((y/(float)getHeight()) *(float)len);
                if(mOnTextViewChanges !=null) {
                    mOnTextViewChanges.onTextChange(letters[i]);
                }
                break;
            default:break;
        }
        return true;
    }

}
