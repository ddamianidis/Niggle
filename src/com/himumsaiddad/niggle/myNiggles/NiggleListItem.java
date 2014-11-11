package com.himumsaiddad.niggle.myNiggles;

public class NiggleListItem {
	public String id;
    public String nigglename;
    public String niggledate;
    public String whatnowknow;
    public String whatiwilldo;
    
    public NiggleListItem(){
        super();
    }
   
    public NiggleListItem(String nigglename, String niggledate) {
        super();
        this.nigglename = nigglename;
        this.niggledate = niggledate;
    }
}