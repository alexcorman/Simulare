package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    private TextView textView = null;
    private Button okButton = null, cancelButton = null;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.ok_button:
                    setResult(RESULT_OK);
                    break;

                case R.id.cancel_button:
                    setResult(RESULT_CANCELED);
                    break;
            }

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        textView = (TextView)findViewById(R.id.number_of_clicks_text_view);
        Intent intent = getIntent();
        if(intent != null && intent.getExtras().containsKey("numberOfClicks"))
        {
            int numberOfClicks = intent.getIntExtra("numberOfClicks", -1);
            textView.setText(String.valueOf(numberOfClicks));
        }

        okButton = (Button)findViewById(R.id.ok_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        okButton.setOnClickListener(buttonClickListener);
        cancelButton.setOnClickListener(buttonClickListener);
    }

}
