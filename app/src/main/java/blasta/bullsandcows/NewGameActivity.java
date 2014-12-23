package blasta.bullsandcows;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

/**
 * Created by 1 on 17.12.2014.
 */
public class NewGameActivity extends GameRulesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int[] numpickersId = {R.id.newgame_numberPicker0,R.id.newgame_numberPicker1,R.id.newgame_numberPicker2,R.id.newgame_numberPicker3};
        GameRulesActivity.numpickersvalues[0] = 0;
        GameRulesActivity.numpickersvalues[1] = 1;
        GameRulesActivity.numpickersvalues[2] = 2;
        GameRulesActivity.numpickersvalues[3] = 3;
        super.onCreate(savedInstanceState, R.layout.new_game_layout,R.id.newgame_thinkof_label,numpickersId);
    }

    public void onClick(View view){

        Intent intent;
        switch (view.getId()){
            case R.id.newgame_thinkof_button:
                if (GameRulesActivity.game.thinkOfNumber(numpickersvalues)){
                    GameActivity.list.clear();
                    GameRulesActivity.numpickersvalues[0] = 0;
                    GameRulesActivity.numpickersvalues[1] = 1;
                    GameRulesActivity.numpickersvalues[2] = 2;
                    GameRulesActivity.numpickersvalues[3] = 3;
                    intent = new Intent(NewGameActivity.this,GameActivity.class);
                    startActivity(intent);
                    NewGameActivity.this.finish();
                }else{
                    thinkof_label.setText(getString(R.string.alert_digitsnotdifferent));
                    thinkof_label.setTextColor(ColorStateList.valueOf(0xffff0000));
                    numbers_error = true;
                }
                break;
            case R.id.newgame_thinkof_random_button:
                if (GameRulesActivity.game.thinkOfRandomNumber()){
                    GameActivity.list.clear();
                    GameRulesActivity.numpickersvalues[0] = 0;
                    GameRulesActivity.numpickersvalues[1] = 1;
                    GameRulesActivity.numpickersvalues[2] = 2;
                    GameRulesActivity.numpickersvalues[3] = 3;
                    intent = new Intent(NewGameActivity.this, GameActivity.class);
                    startActivity(intent);
                    NewGameActivity.this.finish();
                }
                break;
        }

    }

}
