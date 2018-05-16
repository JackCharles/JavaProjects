package com.zjee.voiceassistant.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.zjee.voiceassistant.pojo.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserInfoUtil {
    public static Map<String, String> getAppList(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveinfoList = pm.queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages = new HashSet();
        for (ResolveInfo resolveInfo : resolveinfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }

        Map<String, String> map = new HashMap<>();
        for (ApplicationInfo app : appList) {
            if (allowPackages.contains(app.packageName))
                map.put(pm.getApplicationLabel(app).toString(), app.packageName);
        }

        return map;
    }

    public static List<Contact> getContacts(Context context) {
        List<Contact> list = new ArrayList<>();
        if (!PermissionUtil.permissionCheck(context, Manifest.permission.READ_CONTACTS)) {
            return list;
        }
        Cursor cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{"display_name", "sort_key", "contact_id", "data1"},
                        null, null, "sort_key");
        while (cursor.moveToNext()) {
            list.add(new Contact(cursor.getString(cursor.getColumnIndex("display_name")),
                    cursor.getString(cursor.getColumnIndex("data1"))));
        }
        return list;
    }

    public static Set<String> getContactsName(Context context) {
        List<Contact> contactList = getContacts(context);
        Set<String> nameSet = new HashSet();
        for (Contact contact : contactList)
            nameSet.add(contact.getUserName());
        return nameSet;
    }
}