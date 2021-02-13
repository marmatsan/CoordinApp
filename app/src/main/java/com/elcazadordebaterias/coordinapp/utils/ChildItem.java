package com.elcazadordebaterias.coordinapp.utils;

public class ChildItem {

    private String ChildItemTitle;
    private String ChildItemEmail;

    public ChildItem(String childItemTitle, String ChildItemEmail)
    {
        this.ChildItemTitle = childItemTitle;
        this.ChildItemEmail = ChildItemEmail;
    }

    public String getChildItemTitle()
    {
        return ChildItemTitle;
    }

    public void setChildItemTitle(
            String childItemTitle)
    {
        ChildItemTitle = childItemTitle;
    }

    public String getChildItemEmail() {
        return ChildItemEmail;
    }

    public void setChildItemEmail(String childItemEmail) {
        ChildItemEmail = childItemEmail;
    }
}

