package de.hochschuletrier.gdw.commons.gdx.devcon;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import de.hochschuletrier.gdw.commons.devcon.ConsoleEditor;

/**
 * Extended TextField to have more control over selection
 *
 * @author Santo Pfingsten
 */
public class CommandField extends TextField {

    public CommandField(String text, Skin skin) {
        super(text, skin, "console");
    }
    
    @Override
    public void setText(String str) {
        super.setText(str);
        hasSelection = false;
        cursor = str.length();
    }

    @Override
    public void setSelection(int selectionStart, int selectionEnd) {
        if (selectionStart < 0) {
            throw new IllegalArgumentException("selectionStart must be >= 0");
        }
        if (selectionEnd < 0) {
            throw new IllegalArgumentException("selectionEnd must be >= 0");
        }
        selectionStart = Math.min(text.length(), selectionStart);
        selectionEnd = Math.min(text.length(), selectionEnd);
        if (selectionEnd == selectionStart) {
            cursor = selectionEnd;
            clearSelection();
            return;
        }
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }

        hasSelection = true;
        this.selectionStart = selectionStart;
        cursor = selectionEnd;
    }

    public boolean hasSelection() {
        return hasSelection;
    }

    @Override
    public int getSelectionStart() {
        return hasSelection ? selectionStart : cursor;
    }

    private final ConsoleEditor consoleEditor = new ConsoleEditor() {
        @Override
        public void setText(String str) {
            CommandField.this.setText(str);
        }

        @Override
        public void setSelection(int selectionStart, int selectionEnd) {
            CommandField.this.setSelection(selectionStart, selectionEnd);
        }
    };

    ConsoleEditor getConsoleEditor() {
        return consoleEditor;
    }
}
