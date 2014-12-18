package blasta.bullsandcows;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by 1 on 18.12.2014.
 */
public abstract class GameNewGameActivity extends Activity implements Callback{

    protected static final Game game = new Game();
    protected final NumberPicker[]numpickers = new NumberPicker[4];
    protected final static int numpickersvalues[] = {0, 1, 2 ,3};

    protected int default_text_color;
    protected TextView thinkof_label;
    protected boolean numberserror;

    Shaker shaker;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onCreate(Bundle savedInstanceState, int layoutResId, int labelId, int[] numpickersIds) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);

        numpickers[0] = (NumberPicker) findViewById(numpickersIds[0]);
        numpickers[1] = (NumberPicker) findViewById(numpickersIds[1]);
        numpickers[2] = (NumberPicker) findViewById(numpickersIds[2]);
        numpickers[3] = (NumberPicker) findViewById(numpickersIds[3]);

        thinkof_label = (TextView)findViewById(labelId);
        default_text_color = thinkof_label.getTextColors().getDefaultColor();
        numberserror = false;

        for (int i = 0; i < numpickers.length; i++) {
            numpickers[i].setMinValue(0);
            numpickers[i].setMaxValue(9);
            numpickers[i].setValue(numpickersvalues[i]);
            numpickers[i].setOnValueChangedListener(MyValueChangeListener);
            numpickers[i].setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        }

        shaker = new Shaker(this, 1.5f, 4, this);
    }

    final NumberPicker.OnValueChangeListener MyValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            for (int i = 0; i < numpickers.length; i ++)
            if (picker == numpickers[i]){
                numpickersvalues[i] = newVal;
            }

            if (numberserror == true){
                thinkof_label.setText(getString(R.string.thinkof_label));
                thinkof_label.setTextColor(ColorStateList.valueOf(default_text_color));
                numberserror = false;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        shaker.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shaker.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shaker.stop();
    }

    public void shakingEvent() {
        Random random = new Random();
        boolean differentToAll;

        do {
            for (int i = 0; i < numpickersvalues.length; i++) {
                do {
                    differentToAll = true;
                    numpickersvalues[i] = Math.abs(random.nextInt()) % 10;

                    for (int j = 0; j < i; j++)
                        if (numpickersvalues[i] == numpickersvalues[j])
                            differentToAll = false;

                }
                while (!differentToAll);
            }
        } while (GameActivity.game.alreadyGuessed(numpickersvalues));

        for (int i = 0; i < numpickers.length; i++) {
            numpickers[i].setValue(numpickersvalues[i]);
        }

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 250, 50, 400};
        v.vibrate(pattern, -1);
    }
}
