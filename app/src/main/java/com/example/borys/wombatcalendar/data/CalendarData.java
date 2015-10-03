package com.example.borys.wombatcalendar.data;

/**
 * Information of single calendar on device
 */
public class CalendarData {
    private long mId;
    private String mName;
    private String mAccountName;
    private String mAccountType;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getDisplayName() {
        return mName;
    }

    public void setDisplayName(String displayName) {
        mName = displayName;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public String getAccountType() {
        return mAccountType;
    }

    public void setAccountType(String accountType) {
        mAccountType = accountType;
    }
}
