package mx.axtel.connectedcar.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import mx.axtel.connectedcar.models.User;

/**
 * Created by marinsalinas on 3/23/15.
 */
@SuppressLint("CommitPrefEdits")
public class Session {
        static final String KEY_USER = "user";
        static final String IS_LOGIN = "IsLoggedIn";
        static final String PREF_NAME = "ccarPref";

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        Context ctx;

        int PRIVATE_MODE = 0;

        public Session(Context context){
            this.ctx = context;
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        public void createLoginSession(User user){
            editor.putBoolean(IS_LOGIN, true);
            editor.putString(KEY_USER, new Gson().toJson(user, User.class));
            editor.commit();
        }

        public User getUserSession(){
            return new Gson().fromJson(pref.getString(KEY_USER, null), User.class);
        }

        public boolean isLoggedIn() {
            return pref.getBoolean(IS_LOGIN, false);
        }


}
