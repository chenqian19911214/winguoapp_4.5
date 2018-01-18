package com.winguo.bean;

/**
 * 个人资料
 * Created by admin on 2017/5/8.
 */

public class PersonInfo {

    public String accountName;
    public String accountSex;
    public String accountAge;
    public String accountTel;
    public String accountAddressDef;
    public String accountUUID;
    public String accountSpaceName;

    public PersonInfo() {
    }

    public PersonInfo(String accountName, String accountSex, String accountAge, String accountTel, String accountAddressDef, String accountUUID, String accountSpaceName) {
        this.accountName = accountName;
        this.accountSex = accountSex;
        this.accountAge = accountAge;
        this.accountTel = accountTel;
        this.accountAddressDef = accountAddressDef;
        this.accountUUID = accountUUID;
        this.accountSpaceName = accountSpaceName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountSex() {
        return accountSex;
    }

    public void setAccountSex(String accountSex) {
        this.accountSex = accountSex;
    }

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public String getAccountTel() {
        return accountTel;
    }

    public void setAccountTel(String accountTel) {
        this.accountTel = accountTel;
    }

    public String getAccountAddressDef() {
        return accountAddressDef;
    }

    public void setAccountAddressDef(String accountAddressDef) {
        this.accountAddressDef = accountAddressDef;
    }

    public String getAccountUUID() {
        return accountUUID;
    }

    public void setAccountUUID(String accountUUID) {
        this.accountUUID = accountUUID;
    }

    public String getAccountSpaceName() {
        return accountSpaceName;
    }

    public void setAccountSpaceName(String accountSpaceName) {
        this.accountSpaceName = accountSpaceName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        PersonInfo p = (PersonInfo)obj;
        if (accountName == null) {
            if(p.accountName != null) {
                return false;
            }
        } else if (!accountName.equals(p.accountName)) {
            return false;
        }

        return true;
    }


}
