package in.dete.oops;

import android.content.Context;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static User fetchUserInfo(Context context) {
        String userJson = context.getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("loggedInUser", "");
        User user = null;
        try {
            user = new Gson().fromJson(userJson, User.class);
        } catch (Exception e) {
        }
        return user;
    }

    public static void storeUserInfo(User user, Context context) {
        String json = new Gson().toJson(user);
        context.getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().putString("loggedInUser", json).apply();
    }

    public static String formatName(String name){
        name = " " + name.trim();
        String name1 ="";
        for(int i=1;i<name.length();i++)
        {
            if(name.charAt(i)!= ' ' && name.charAt(i-1)==' ')
                name1 += Character.toUpperCase(name.charAt(i));
            else if(name.charAt(i)== ' ' && name.charAt(i-1)==' ' ){
                continue;
            }
            else name1 += name.charAt(i);
        }
        return name1;
    }


}