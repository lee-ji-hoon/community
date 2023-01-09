package com.community.app.util;

public class Script {
    public static String href(String path, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert(' ").append(msg).append(" ');");
        sb.append("location.href='").append(path).append("';");
        sb.append("</script>");

        return sb.toString();
    }
}
