package blasta.bullsandcows;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

/**
 * Created by 1 on 18.12.2014.
 */
public class GameActivity extends GameRulesActivity {
    public static final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private RatingBar bulls_bar;
    private RatingBar cows_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int[] numpickerIds = {R.id.game_numberPicker0, R.id.game_numberPicker1, R.id.game_numberPicker2, R.id.game_numberPicker3};
        super.onCreate(savedInstanceState, R.layout.game_layout, R.id.game_guess_label, numpickerIds);

        //orientation change

        LinearLayout pickerslayout = (LinearLayout) findViewById(R.id.game_pickers_layout);
        LinearLayout guesseslayout = (LinearLayout) findViewById(R.id.game_guesses_layout);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.game_main_layout);

        bulls_bar = (RatingBar) findViewById(R.id.game_bulls_bar);
        cows_bar = (RatingBar) findViewById(R.id.game_cows_bar);

        ListView guesseslist = (ListView) findViewById(R.id.game_guesses);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        guesseslist.setAdapter(adapter);
        guesseslist.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        guesseslist.setOnItemClickListener(MyItemClickListener);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainlayout.setOrientation(LinearLayout.HORIZONTAL);
            guesseslayout.setOrientation(LinearLayout.VERTICAL);
            pickerslayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.f));
            guesseslayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.f));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainlayout.setOrientation(LinearLayout.VERTICAL);
            guesseslayout.setOrientation(LinearLayout.HORIZONTAL);
            pickerslayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.f));
            guesseslayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.f));
        }

        if (GameRulesActivity.game.getIterationsCount() <= 0)
            bulls_bar.setRating(0);
        else
            bulls_bar.setRating(GameRulesActivity.game.getBulls(GameRulesActivity.game.getGuess(GameRulesActivity.game.getIterationsCount() - 1)));

        if (GameRulesActivity.game.getIterationsCount() <= 0)
            cows_bar.setRating(0);
        else
            cows_bar.setRating(GameRulesActivity.game.getCows(GameRulesActivity.game.getGuess(GameRulesActivity.game.getIterationsCount() - 1)));

    }

    public void win() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.win_title);
        alert.setMessage(R.string.win_text);
        alert.setPositiveButton(R.string.new_game, MyDialogClickListener);
        alert.setNeutralButton(R.string.dlg_exit, MyDialogClickListener);
        alert.setCancelable(false);
        alert.show();
    }

    public void lose() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.lose_title);
        alert.setMessage(
                getString(R.string.lose_text) +
                        "\n" +
                        getString(R.string.lose_answer) +
                        " " +
                        GameRulesActivity.game.getAnswer()
        );
        alert.setPositiveButton(R.string.new_game, MyDialogClickListener);
        alert.setNeutralButton(R.string.dlg_exit, MyDialogClickListener);
        alert.setCancelable(false);
        alert.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_check_button:
                if (GameRulesActivity.game.addGuess(GameRulesActivity.numpickersvalues)) {
                    bulls_bar.setRating(GameRulesActivity.game.getBulls(GameRulesActivity.numpickersvalues));
                    cows_bar.setRating(GameRulesActivity.game.getCows(GameRulesActivity.numpickersvalues));

                    list.add(
                            String.valueOf(GameRulesActivity.game.getIterationsCount()) +
                                    ": " +
                                    String.valueOf(GameRulesActivity.numpickersvalues[0]) +
                                    String.valueOf(GameRulesActivity.numpickersvalues[1]) +
                                    String.valueOf(GameRulesActivity.numpickersvalues[2]) +
                                    String.valueOf(GameRulesActivity.numpickersvalues[3])
                    );
                    adapter.notifyDataSetChanged();

                    if (GameRulesActivity.game.checkGuess(GameRulesActivity.numpickersvalues))
                        win();
                    else if (GameRulesActivity.game.getIterationsCount() >= 10)
                        lose();
                } else {
                    if (GameRulesActivity.game.alreadyGuessed(GameRulesActivity.numpickersvalues))
                        thinkof_label.setText(getString(R.string.aler_digits_already_guessed));
                    if (!GameRulesActivity.game.allDigitsAreDifferent(GameRulesActivity.numpickersvalues))
                        thinkof_label.setText(getString(R.string.alert_digitsnotdifferent));
                    thinkof_label.setTextColor(ColorStateList.valueOf(0xffff0000));
                    numbers_error = true;
                }
                break;
        }
    }

    final DialogInterface.OnClickListener MyDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            Intent intent;
            switch (id) {
                case DialogInterface.BUTTON_POSITIVE:
                    intent = new Intent(GameActivity.this, NewGameActivity.class);
                    startActivity(intent);
                    GameActivity.this.finish();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    GameActivity.this.finish();
                    break;
            }
        }
    };

    final AdapterView.OnItemClickListener MyItemClickListener = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bulls_bar.setRating(GameRulesActivity.game.getBulls(GameRulesActivity.game.getGuess(position)));
            cows_bar.setRating(GameRulesActivity.game.getCows(GameRulesActivity.game.getGuess(position)));

            for (int i = 0; i < numpickers.length; i++) {
                numpickers[i].setValue(GameRulesActivity.game.getGuess(position)[i]);
                GameRulesActivity.numpickersvalues[i] = (GameRulesActivity.game.getGuess(position)[i]);
            }
        }
    };


}