package blasta.bullsandcows;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Math.random;

/**
 * Created by 1 on 18.12.2014.
 */
public class Game {

    public final int digitsCount = 4;

    private int[] guessing;
    private ArrayList<int[]> guesses;

    public String getAnswer() {
        if (guessing == null)
            return "";
        else {
            String result = "";
            for (int aGuessing : guessing)
                result += String.valueOf(aGuessing);
            return result;
        }
    }

    public boolean allDigitsAreDifferent(int[] digits) {
        for (int i = 0; i < digits.length; i++)
            for (int j = 0; j < digits.length; j++)
                if (i != j && digits[i] == digits[j])
                    return false;
        return true;
    }

    private boolean allDigitsAreSame(int[] digits1, int[] digits2) {
        for (int i = 0; i < min(digits1.length, digits2.length); i++) {
            if (digits1[i] != digits2[i])
                return false;
        }
        return true;
    }

    public int[] getGuess(int position) {
        if (position < guesses.size())
            return guesses.get(position);
        else
            return null;
    }

    public boolean alreadyGuessed(int[] newGuess) {
        if (newGuess == null)
            return false;
        for (int i = 0; i < guesses.size(); i++) {
            if (allDigitsAreSame(newGuess, guesses.get(i)))
                return true;
        }
        return false;
    }

    public boolean addGuess(int[] newGuess) {
        return !(newGuess == null || newGuess.length == 0 ||
                newGuess.length != guessing.length || !allDigitsAreDifferent(newGuess) ||
                alreadyGuessed(newGuess)) && guesses.add(Arrays.copyOf(newGuess, newGuess.length));
    }

    public int getBulls(int[] guess) {
        if (guess == null || guess.length != guessing.length)
            return 0;
        int result = 0;

        for (int i = 0; i < guess.length; i++)
            if (guess[i] == guessing[i])
                result++;
        return result;
    }

    public int getCows(int[] guess) {
        if (guess == null || guess.length != guessing.length)
            return 0;
        int result = 0;

        for (int i = 0; i < guess.length; i++)
            for (int j = 0; j < guessing.length; j++)
                if (guess[i] == guessing[j] && i != j) {
                    result++;
                    break;
                }

        return result;
    }

    public boolean checkGuess(int[] guess) {
        return allDigitsAreSame(guess, guessing);
    }

    public boolean thinkOfNumber(int[] number) {
        if (number != null && allDigitsAreDifferent(number)) {
            guesses = new ArrayList<>();

            guessing = new int[number.length];
            System.arraycopy(number, 0, guessing, 0, number.length);

            return true;
        }

        return false;
    }

    public int getIterationsCount() {
        return guesses.size();
    }

    public boolean thinkOfRandomNumber() {
        int[] number = new int[digitsCount];
        Random random = new Random();
        boolean differentToAll;

        for (int i = 0; i < number.length; i++) {
            do {
                differentToAll = true;
                number[i] = Math.abs(random.nextInt()) % 10;

                for (int j = 0; j < i; j++)
                    if (number[i] == number[j])
                        differentToAll = false;

            }
            while (!differentToAll);
        }

        return thinkOfNumber(number);
    }

    public Game() {
        thinkOfRandomNumber();
    }
}
