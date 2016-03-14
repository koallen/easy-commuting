package sg.edu.ntu.cz2006.seproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    public void buttonOnClickListener(View view) {
        TextView text = (TextView) findViewById(R.id.textview);
        text.setText("I just clicked a button");
    }
}
