package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ensisa.group5.confined.R;

/**
 * Author VALLERICH Vincent on 04-06-2020
 */

public class LoginValidation {

    private Context context;
    private static SharedPreferences preferences;

    public static final int MIN_LEN_INPUT_USERNAME = 2;
    public static final int MIN_LEN_INPUT_PASSWORD = 6;
    String usernameKey;
    String mailKey;

    /**
     * @param context
     * @param preferences
     */
    public LoginValidation(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;

        usernameKey = context.getResources().getString(R.string.PREF_KEY_USERNAME);
        mailKey = context.getResources().getString(R.string.PREF_KEY_MAIL);
    }

    /**
     * @param username
     * @return true if user exist false otherwise
     * */
    public boolean isUsernameExist(String username) {
        if (isEmailValid(username))
            return preferences.getString(mailKey,null) != null;
        else
            return preferences.getString(usernameKey, null) != null;
    }

    /**
     * @param username
     * @return true if the username pattern is correct false otherwise
     * */
    public boolean isUsernameFormatCorrect(String username) {
        return username.length() >= MIN_LEN_INPUT_USERNAME;
    }

    /**
     * @param password
     * @return true if the password pattern is correct false otherwise
     * */
    public boolean isPasswordFormatCorrect(String password) {
        return password.length() >= MIN_LEN_INPUT_PASSWORD;
    }

    /**
     * @param password
     * @param confirm
     * @return true if password and confirm same false otherwise
     */
    public boolean isPasswordConfirmMatch(String password, String confirm) {
        return password == confirm;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
