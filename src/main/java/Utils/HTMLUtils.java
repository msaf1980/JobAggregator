/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author msv
 */
public class HTMLUtils {
    public static String formatWithNewLines(String text) {
        if (text == null)
            return "";
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int i;
        while (true) {
            i = text.indexOf('\n', pos);
            if (sb.length() > 0)
                sb.append("<br>");
            if (i == -1) {
                sb.append(text.substring(pos));
                break;
            } else {
                sb.append(text.substring(pos, i));
                pos = i + 1;
            }
        }
        return sb.toString();
    }
}
