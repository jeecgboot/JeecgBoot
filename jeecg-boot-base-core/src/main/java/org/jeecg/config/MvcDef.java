package org.jeecg.config;


import lombok.Data;

@Data
public class MvcDef {
    private String path;
    private String view;
    private int priority;

    public static MvcDef create(String path, String view){
        return create(path,view,0);
    }
    public static MvcDef create(String path, String view, int priority){
        MvcDef mvcMap = new MvcDef();
        mvcMap.setPath(path);
        mvcMap.setView(view);
        mvcMap.setPriority(priority);
        return mvcMap;
    }
}
