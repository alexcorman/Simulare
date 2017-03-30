package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    private Button navigateToSecondaryActivityButton = null;

    private EditText leftEditText = null, rightEditText = null;
    private Button leftButton = null, rightButton = null;
    private int serviceStatus = Constants.SERVICE_STOPPED;

    private IntentFilter intentFilter = new IntentFilter();

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            int leftNumberOfClicks = Integer.parseInt(leftEditText.getText().toString());
            int rightNumberOfClicks = Integer.parseInt(rightEditText.getText().toString());

            switch (view.getId()) {
                case R.id.left_button:
                    leftNumberOfClicks++;
                    leftEditText.setText(String.valueOf(leftNumberOfClicks));
                    break;

                case R.id.right_button:

                    rightNumberOfClicks++;
                    rightEditText.setText(String.valueOf(rightNumberOfClicks));
                    break;

                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int numberOfClicks = Integer.parseInt(leftEditText.getText().toString()) +
                            Integer.parseInt(rightEditText.getText().toString());
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }

            if (leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED)
            {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicks);
                intent.putExtra("secondNumber", rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

        leftEditText = (EditText)findViewById(R.id.left_edit_text);
        rightEditText = (EditText)findViewById(R.id.right_edit_text);
        leftEditText.setText(String.valueOf(0));
        rightEditText.setText(String.valueOf(0));

        leftButton = (Button)findViewById(R.id.left_button);
        rightButton = (Button)findViewById(R.id.right_button);

        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);

        navigateToSecondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        navigateToSecondaryActivityButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString("leftCount", leftEditText.getText().toString());
        savedInstanceState.putString("rightCount", rightEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if(savedInstanceState.containsKey("leftCount"))
        {
            leftEditText.setText(savedInstanceState.getString("leftCount"));
        }
        else
        {
            leftEditText.setText(String.valueOf(0));
        }

        if(savedInstanceState.containsKey("rightCount"))
        {
            rightEditText.setText(savedInstanceState.getString("rightCount"));
        }
        else
        {
            rightEditText.setText(String.valueOf(0));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == SECONDARY_ACTIVITY_REQUEST_CODE)
        {
            Toast.makeText(this, "Activity returned with result" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
