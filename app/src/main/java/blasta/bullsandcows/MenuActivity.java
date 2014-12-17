package blasta.bullsandcows;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Created by 1 on 17.12.2014.
 */
public class MenuActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
    }

    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.button_newgame:
                intent = new Intent(MenuActivity.this, NewGameActivity.class);
                startActivity(intent);
                break;
            case R.id.button_about:
                intent = new Intent(MenuActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.button_exit:
                System.exit(0);
                break;
        }
    }
}
