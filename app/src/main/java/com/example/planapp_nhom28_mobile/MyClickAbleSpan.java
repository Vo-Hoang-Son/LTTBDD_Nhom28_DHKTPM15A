package com.example.planapp_nhom28_mobile;

import android.text.style.ClickableSpan;
import android.view.View;

class MyClickAbleSpan extends ClickableSpan {

    String filePath;

    public MyClickAbleSpan(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onClick(View widget) {
       // Toast.makeText(SpannableActivity.this, filePath, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_LONG).show();
    }
}
