package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefUtil {


    private static String IM_SERVER_TYPE = "im-server-type";
    private static String CHAT_STATUS_SAVE = "chat-status-save";
    private static String CHAT_STATUS_PUSH = "chat-status-push";
    private static String CHAT_STATUS_HISTORY = "chat-status-history";
    private static String CHAT_STATUE_FOCUS = "chat-status-focus";
    private static String CHAT_STATUS_SENDTIMEOUT = "chat-status-sendTimeout";
    private static String CHAT_STATUS_INCREASEUNREAD = "chat-status-increaseUnread";


    private static SharedPreferences preferences = AppContext.getAppContext().getSharedPreferences("photon_im_demo", Context.MODE_MULTI_PROCESS);

    public static void saveServerType(int Type){
        preferences.edit().putInt(IM_SERVER_TYPE,Type).commit();
    }
    public static int getServerType(int defaultType){
        return preferences.getInt(IM_SERVER_TYPE,defaultType);
    }

    public static void saveChatStausSave(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUS_SAVE,userId,chatWith);
    }

    public static boolean getChatStausSave(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUS_SAVE,userId,chatWith);
    }

    public static void removeChatStatusSave(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUS_SAVE,userId,chatWith);
    }


    public static void saveChatStausPush(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUS_PUSH,userId,chatWith);
    }

    public static boolean getChatStausPush(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUS_PUSH,userId,chatWith);

    }

    public static void removeChatStatusPush(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUS_PUSH,userId,chatWith);

    }


    public static void saveChatStausHistory(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUS_HISTORY,userId,chatWith);
    }

    public static boolean getChatStausHistory(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUS_HISTORY,userId,chatWith);

    }

    public static void removeChatStatusHistory(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUS_HISTORY,userId,chatWith);
    }

    public static void saveFocusStatus(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUE_FOCUS,userId,chatWith);
    }

    public static void removeFocusStatus(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUE_FOCUS,userId,chatWith);

    }

    public static boolean getFocusStatus(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUE_FOCUS,userId,chatWith);
    }


    public static void saveSendTimeoutStatus(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUS_SENDTIMEOUT,userId,chatWith);
    }

    public static void removeSendTimeoutStatus(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUS_SENDTIMEOUT,userId,chatWith);
    }

    public static boolean getSendTimeoutStatus(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUS_SENDTIMEOUT,userId,chatWith);
    }

    public static void saveIncreaseUnreadStatus(String userId,String chatWith){
        saveStatus(userId+CHAT_STATUS_INCREASEUNREAD,userId,chatWith);
    }

    public static void removeIncreaseUnreadStatus(String userId,String chatWith){
        removeStatus(userId+CHAT_STATUS_INCREASEUNREAD,userId,chatWith);
    }

    public static boolean getIncreaseUnreadStatus(String userId,String chatWith){
        return getStatus(userId+CHAT_STATUS_INCREASEUNREAD,userId,chatWith);
    }

    private static void saveStatus(String key,String userId,String chatWith){
        Set<String> set = preferences.getStringSet(key, null);
        Set<String> newSet = new HashSet<>();
        if(set != null){
            newSet.addAll(set);
        }
        newSet.add(chatWith);
        preferences.edit().putStringSet(key,newSet).commit();
    }

    private static void removeStatus(String key,String userId,String chatWith){
        Set<String> set = preferences.getStringSet(key, null);
        if(set != null){
            Set<String> newSet = new HashSet<>();
            newSet.addAll(set);
            newSet.remove(chatWith);
            preferences.edit().putStringSet(key,newSet).commit();
        }

    }

    private static boolean getStatus(String key,String userId,String chatWith){
        Set<String> set = preferences.getStringSet(key,null);
        if(set != null){
            return set.contains(chatWith);
        }
        return false;
    }
}
