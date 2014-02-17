package com.github.painie.rxlambda;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit.RestAdapter;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 *
 */
public class MainActivity extends Activity {

    private static final String API_URL = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Button button = (Button) findViewById(R.id.button_ok);
        final EditText edit = (EditText) findViewById(R.id.edit_text);

        final Observable<Void> buttonClick = clicksFrom(button);
        final Observable<String> textInput = textFrom(edit);

        GithubApi api = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build()
                .create(GithubApi.class);
        api.getRepositories("square");

        // disable button for empty text
        textInput.map(text -> TextUtils.isEmpty(text.trim()))
                .retry(2)
                .subscribe(isEmpty -> button.setEnabled(!isEmpty));

        buttonClick.subscribe(v -> {
            // TODO
        });

    }

    public static <T extends View> Observable clicksFrom(T view) {
        PublishSubject publishSubject = PublishSubject.create();
        view.setOnClickListener((v) -> publishSubject.onNext(null));
        return publishSubject.asObservable();
    }

    public static Observable<String> textFrom(TextView view) {
        String currentText = String.valueOf(view.getText());
        final BehaviorSubject<String> subject = BehaviorSubject.create(currentText);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
        return subject;
    }
}
