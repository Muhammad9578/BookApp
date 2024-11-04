package com.mtechsoft.bookapp.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.gson.JsonArray;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.dialogs.SearchViewDialog;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.models.User;
import com.mtechsoft.bookapp.utils.AppExt;
import com.mtechsoft.bookapp.utils.FileDownloader;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import net.gotev.speech.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ChapterDetailFragment extends BaseFragment implements View.OnClickListener, OnPageChangeListener, NetworkListener {
    private TextView tvPageCount;
    private String parsedTxt = "";
    private PDFView pdfView;
    private ImageButton bPlay, ivSearch;
    private ImageView ivBack;
    private SearchView searchView;
    private Button btnQuiz;
    public static String chapter_name;
    private PdfReader reader;
    private File pdfFile;
    private ProgressDialog progressDialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chapter_detail, container, false);
        Toolbar toolbar = v.findViewById(R.id.ctoolbar);
        bPlay = toolbar.findViewById(R.id.bPlay);
        ivSearch = toolbar.findViewById(R.id.ivSearch);
        ivBack = toolbar.findViewById(R.id.ivBack);
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.showSoftInput(ivSearch, InputMethodManager.RESULT_HIDDEN);

        verifyStoragePermissions(getActivity());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        Speech.init(getActivity(), getActivity().getPackageName());

        User user_data = SharedPref.getUser(getContext());
        String user_id = user_data.getId();


        GetPdfFile(user_id);


//        new ReadTextFromPDFTask().execute();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private void GetPdfFile(String name) {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("User_id", name));
        params.add(new Parameter("con_type", MainFragment.butn));


        NetworkTask getpdf = new NetworkTask(getContext(), "GET", WebServices.GET_PDF + "?", params);
        getpdf.setListener(this);
        getpdf.setMessage("Please Wait ....");
        getpdf.execute();
    }

    @Override
    public void onSuccess(String result) {
        try {


            JSONObject jobj = new JSONObject(result);
            JSONArray objUser = jobj.getJSONArray("data");

            String msg = jobj.getString("message");
            for (int i = 0; i < objUser.length(); i++) {
                JSONObject actor = objUser.getJSONObject(i);
                String chap_name = actor.getString("chapter_name");
                if (chap_name.equals(chapter_name)) {
                    String chap_url = actor.getString("filename");
                    Uri chapter_url_main = Uri.parse(chap_url);


                    pdfFile = new File(Environment.getExternalStorageDirectory() + "/bookapp/" + chapter_name + ".pdf");
//                    Uri path = Uri.fromFile(pdfFile);
                    if (!pdfFile.exists()) {
                        new DownloadFile().execute(chap_url, chap_name + ".pdf");

                    } else {
                        AppExt.showPDFFile(pdfView,this, pdfFile);
                    }
                }
            }

        } catch (
                JSONException ex) {
            ex.printStackTrace();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        OnPageChangeListener listener;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new ProgressDialog(getActivity());

            progressDialog.setMessage("File Downloading..,");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "bookapp");
            folder.mkdir();
            File pdfFile = new File(folder, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            //   Toast.makeText(getContext(), "file downloaded", Toast.LENGTH_SHORT).show();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            AppExt.showPDFFile(pdfView, listener,  pdfFile);
        }
    }

    @Override
    public void onError(String error) {

        try {
            JSONObject jobj = new JSONObject(error);
            String msg = jobj.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void initViews(View v) {
        btnQuiz = v.findViewById(R.id.btn_quiz);
        tvPageCount = v.findViewById(R.id.tv_pageNumber);
        searchView = v.findViewById(R.id.SearchView);
        pdfView = v.findViewById(R.id.pdf4);
        btnQuiz.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_chapterDetailFragment_to_quizCategoryFragment));
        bPlay.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        bPlay.setTag(R.drawable.ic_mic_white);
    }

    private void showSearchDialog() {
        DialogFragment newFragment = SearchViewDialog.newInstance(
                R.string.search_word_meaning_dialog_title);
        newFragment.show(getFragmentManager(), "dialog");

    }

    private void getTextFromPdf() {
        try {
            StringBuilder parsedText = new StringBuilder();
            reader = new PdfReader(AppExt.getFilePath(getActivity(), chapter_name));
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                parsedText.append(PdfTextExtractor.getTextFromPage(reader, i + 1).trim()).append("\n"); //Extracting the content from the different pages
            }
            parsedTxt = parsedText.toString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ReadTextFromPDFTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
//            pd = new ProgressDialog(getActivity());
//            pd.setTitle("Please Wait");
//            pd.setMessage("Loading...");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getTextFromPdf();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            pd.dismiss();
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivSearch) {
            showSearchDialog();
            Toast.makeText(getActivity(), "searchview", Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.bPlay) {
            playNStopSpeech();
        } else if (view.getId() == R.id.ivBack) {
            getActivity().onBackPressed();

        }
    }


    private void playNStopSpeech() {
        Integer resId = (Integer) bPlay.getTag();
        resId = (resId == null) ? 0 : resId;

        switch (resId) {
            case R.drawable.ic_mic_white:
                bPlay.setImageResource(R.drawable.ic_stop_white);
                bPlay.setTag(R.drawable.ic_stop_white);
                String tts = parsedTxt;
                if (parsedTxt.length() > 4000) {
                    tts = parsedTxt.substring(0, 3000);
                    System.out.println("Length is greater");
                }
                Speech.getInstance().say(tts);
                break;
            case R.drawable.ic_stop_white:
            default:
                bPlay.setImageResource(R.drawable.ic_mic_white);
                bPlay.setTag(R.drawable.ic_mic_white);
                Speech.getInstance().stopTextToSpeech();
                break;
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        tvPageCount.setText(String.format("%s / %s", page + 1, pageCount));
        if (page == pageCount - 1) {
            showButton();
        } else if (page == pageCount - 2) {
            if (btnQuiz.getVisibility() == View.VISIBLE) {
                Animation slideUpAnimation = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_bottom_animation);
                btnQuiz.startAnimation(slideUpAnimation);
                btnQuiz.setVisibility(View.GONE);
            }
        }
    }

    private void showButton() {
        btnQuiz.setVisibility(View.VISIBLE);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up_animation);
        btnQuiz.startAnimation(slideUpAnimation);
    }

    @Override
    public void onResume() {
        super.onResume();
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    reader = new PdfReader(AppExt.getFilePath(getActivity(), chapter_name));
                    parsedTxt = PdfTextExtractor.getTextFromPage(reader, pdfView.getCurrentPage() + 1).trim() + "\n"; //Extracting the content from the different pages
                    showCustomDialog(parsedTxt, pdfView.getCurrentPage() + 1);
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        });
    }

    //    void speak(String s){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Log.v(TAG, "Speak new API");
//            Bundle bundle = new Bundle();
//            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
//            mTts.speak(s, TextToSpeech.QUEUE_FLUSH, bundle, null);
//        } else {
//            Log.v(TAG, "Speak old API");
//            HashMap<String, String> param = new HashMap<>();
//            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
//            mTts.speak(s, TextToSpeech.QUEUE_FLUSH, param);
//        }
//    }
    public void showCustomDialog(String text, int pageNum) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_view);
        dialog.setCancelable(false);

        final TextView textView = dialog.findViewById(R.id.textView);
        final TextView pageNumber = dialog.findViewById(R.id.pageNumber);
        final ImageView cancel = dialog.findViewById(R.id.cancel);
        textView.setText("");
        textView.setText(text);
        pageNumber.setText("Page " + pageNum);

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int clickedLine;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int lineHeight = textView.getLineHeight();
                    clickedLine = (int) (event.getY() / lineHeight);

                    int start = textView.getLayout().getLineStart(clickedLine);
                    int end = textView.getLayout().getLineEnd(clickedLine);
                    CharSequence substring = textView.getText().subSequence(start, end);

                    Speech.getInstance().say(substring.toString());
                    //  speak(substring.toString());
                }

                return true;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onStop() {
        super.onStop();
        Speech.init(getContext());
        Speech.getInstance().shutdown();
    }
}
