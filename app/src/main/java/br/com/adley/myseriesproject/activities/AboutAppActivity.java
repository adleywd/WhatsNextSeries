package br.com.adley.myseriesproject.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.adley.myseriesproject.R;

public class AboutAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);
        activateToolbarWithHomeEnabled();

        ImageView githubIcon = (ImageView) findViewById(R.id.about_github_icon);
        TextView githubText = (TextView) findViewById(R.id.about_github_text);

        if (githubText != null) {
            githubText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://github.com/adleywd/MySeriesProject"));
                    startActivity(intent);
                }
            });
        }

        if (githubIcon != null) {
            githubIcon.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://github.com/adleywd/MySeriesProject"));
                    startActivity(intent);
                }
            });
        }
    }
}
