package br.com.adley.whatsnextseries.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.adley.whatsnextseries.BuildConfig;
import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Utils;

public class AboutAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);
        activateToolbarWithHomeEnabled();

        ImageView githubIcon = (ImageView) findViewById(R.id.about_github_icon);
        ImageView theMovieDbIcon = (ImageView) findViewById(R.id.pref_partners_icon);
        Button contactButton = (Button) findViewById(R.id.pref_contact_button);
        TextView versionName = (TextView) findViewById(R.id.version_name);
        ImageView whatsnextIcon = (ImageView) findViewById(R.id.about_whatsnext_icon);

        if (whatsnextIcon != null){
            whatsnextIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(AppConsts.SITE_WHATSNEXT));
                    startActivity(intent);
                }
            });
        }

        if (theMovieDbIcon != null) {
            theMovieDbIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(AppConsts.SITE_THEMOVIEDB));
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
                    intent.setData(Uri.parse(AppConsts.SITE_GITHUB_WHATSNEXT));
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

                    email.putExtra(Intent.EXTRA_EMAIL  , AppConsts.CONTACT_EMAILS);

                    try{
                        startActivity(Intent.createChooser(email, getString(R.string.choose_client_email)));
                    }catch (ActivityNotFoundException ex){
                        Toast.makeText(AboutAppActivity.this, getString(R.string.no_client_email), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (versionName != null){
            versionName.setText(BuildConfig.VERSION_NAME);
        }
    }
}
