package com.winguo.product.modle.productattribute;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
    public List<String> aliasName = new ArrayList<>();
    public List<String> FailureAliasName = new ArrayList<>();

    public List<String> getAliasName() {
        return aliasName;
    }

    public void setAliasName(List<String> aliasName) {
        this.aliasName = aliasName;
    }

    public List<String> getFailureAliasName() {
        return FailureAliasName;
    }

    public void setFailureAliasName(List<String> failureAliasName) {
        FailureAliasName = failureAliasName;

    }
}
