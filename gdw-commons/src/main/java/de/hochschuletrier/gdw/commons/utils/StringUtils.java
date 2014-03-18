package de.hochschuletrier.gdw.commons.utils;

import java.util.List;

public class StringUtils {

    public static String trimFront(String s) {
        for (int x = 0; x < s.length(); x++) {
            if (!Character.isWhitespace(s.charAt(x))) {
                return s.substring(x, s.length());
            }
        }
        return "";
    }

    public static String stripFrontOnce(String base, String front) {
        if (base.startsWith(front)) {
            return base.substring(front.length());
        }
        return base;
    }

    public static int cmpn(String a, String b, int n) {
        // fixme: better way ?
        return a.substring(0, n).compareTo(b.substring(0, n));
    }

    public static int icmpn(String a, String b, int n) {
        // fixme: better way ?
        return a.substring(0, n).compareToIgnoreCase(b.substring(0, n));
    }

    public static String untokenize(List<String> list, int start, int end, boolean escape) {
        if (end < 0 || end >= list.size()) {
            end = list.size() - 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i > 0) {
                if (i > start) {
                    sb.append(" ");
                }
                if (escape) {
                    sb.append("\"");
                }
            }
            sb.append(list.get(i));
            if (i > 0) {
                if (escape) {
                    sb.append("\"");
                }
            }
        }
        return sb.toString();
    }
    
    public static void tokenize(String str, List<String> result) {
        int bufPos = 0;
        result.clear();

        if (str.isEmpty()) {
            return;
        }

        char lookForQuote = 0;
        char buffer[] = new char[2048];

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (lookForQuote != 0) {
                if (ch == lookForQuote) {
                    result.add(new String(buffer, 0, bufPos));
                    bufPos = 0;
                    lookForQuote = 0;
                    continue;
                }
                buffer[bufPos++] = ch;
            } else if (Character.isWhitespace(ch)) {
                if (bufPos != 0) {
                    result.add(new String(buffer, 0, bufPos));
                    bufPos = 0;
                }
                
                // skip rest of whitespaces
                while (++i < str.length() && Character.isWhitespace(str.charAt(i))) {
                }
                i--;
            } else if (ch == '\"' || ch == '\'') {
                lookForQuote = ch;
                if (bufPos != 0) {
                    result.add(new String(buffer, 0, bufPos));
                    bufPos = 0;
                }
            } else {
                buffer[bufPos++] = ch;
            }
        }
        if (lookForQuote != 0 || bufPos != 0) {
            result.add(new String(buffer, 0, bufPos));
        }
    }
}
