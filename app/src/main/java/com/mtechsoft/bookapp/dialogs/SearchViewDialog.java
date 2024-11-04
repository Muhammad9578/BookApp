package com.mtechsoft.bookapp.dialogs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mtechsoft.bookapp.R;

import java.io.IOException;
import java.io.InputStream;

public class SearchViewDialog extends DialogFragment {
    ImageButton ivSearch;
    EditText etSearchWord;
    TextView tvEnglishMeaning, tvUrduMeaning;
    ImageView ivClose;

    public static SearchViewDialog newInstance(int title) {
        SearchViewDialog frag = new SearchViewDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.search_view_dialog_box, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        ivSearch = view.findViewById(R.id.ivSearch);
        ivClose = view.findViewById(R.id.cancel_action);
        etSearchWord = view.findViewById(R.id.etSearchWord);
        tvEnglishMeaning = view.findViewById(R.id.tvEnglishMeaning);
        tvUrduMeaning = view.findViewById(R.id.tvUrduMeaning);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Find Difficult Word Meaning");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etSearchWord.requestFocus();
        getDialog().setCancelable(false);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (checkInternetConnection()) {
//
//                    //If there is internet connection, get translate service and start translation:
//                    getTranslateService();
//                    translate();
//
//                } else {
//
//                    //If not, display "no connection" warning:
//                    translatedTv.setText(getResources().getString(R.string.no_connection));
//                }

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });



    }


//
//    public void getTranslateService() {
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {
//
//            //Get credentials:
//            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);
//
//            //Set credentials and get translate service:
//            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
//            translate = translateOptions.getService();
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//
//        }
//    }
//
//    public void translate() {
//
//        //Get input text to be translated:
//        originalText = inputToTranslate.getText().toString();
//        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("tr"), Translate.TranslateOption.model("base"));
//        translatedText = translation.getTranslatedText();
//
//        //Translated text and original text are set to TextViews:
//        translatedTv.setText(translatedText);
//
//    }
//
//    public boolean checkInternetConnection() {
//
//        //Check internet connection:
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        //Means that we are connected to a network (mobile or wi-fi)
//        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
//
//        return connected;
//    }

}
