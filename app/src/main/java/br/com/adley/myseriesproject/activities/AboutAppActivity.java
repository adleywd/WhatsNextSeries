package br.com.adley.myseriesproject.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.adley.myseriesproject.R;
import br.com.adley.myseriesproject.library.Utils;

public class AboutAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);
        activateToolbarWithHomeEnabled();

        ImageView githubIcon = (ImageView) findViewById(R.id.about_github_icon);
        ImageView theMovieDbIcon = (ImageView) findViewById(R.id.pref_partners_icon);
        Button contactButton = (Button) findViewById(R.id.pref_contact_button);

        if (theMovieDbIcon != null) {
            theMovieDbIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.themoviedb.org/documentation/api"));
                    startActivity(intent);
                }
            });
        }

        if (githubIcon != null) {
            githubIcon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://github.com/adleywd/MySeriesProject"));
                    startActivity(intent);
                }
            });
        }

        if (contactButton != null) {
            contactButton.setBackgroundColor(Utils.getColor(AboutAppActivity.this, R.color.myseriesPrimaryBackgroundColor));
            contactButton.setTextColor(Utils.getColor(AboutAppActivity.this, R.color.myseriesSecondaryTextColor));
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    email.setType("message/rfc822");

                    email.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});

                    try{
                        startActivity(Intent.createChooser(email, "Choose an email cliente from..."));
                    }catch (ActivityNotFoundException ex){
                        Toast.makeText(AboutAppActivity.this, "No email cliente installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
