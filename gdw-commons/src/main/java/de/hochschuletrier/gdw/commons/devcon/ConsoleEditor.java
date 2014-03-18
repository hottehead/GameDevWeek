package de.hochschuletrier.gdw.commons.devcon;

import java.util.ArrayList;

/**
 * Abstract class for console editors
 *
 * @author Santo Pfingsten
 */
public abstract class ConsoleEditor {

    final ArrayList<String> completionList = new ArrayList<String>();
    final ArrayList<String> completionListLower = new ArrayList<String>();
    int inputHistoryPos;
    String inputSavedText;
    String lastMatch;

    public abstract void setText(String str);

    public abstract void setSelection(int selectionStart, int selectionEnd);
}
