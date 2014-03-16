package de.hochschuletrier.gdw.commons.devcon;

import de.hochschuletrier.gdw.commons.devcon.completers.IConsoleCompleter;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.utils.QuietUtils;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Developer console backend
 *
 * @author Santo Pfingsten
 */
public class DevConsole {

    private static final Logger logger = LoggerFactory.getLogger(DevConsole.class);

    public static CVarBool com_allowCheats = new CVarBool("com_allowCheats", false, CVarFlags.SYSTEM, "Allow Cheats");
    public static CVarBool com_developer = new CVarBool("com_developer", false, CVarFlags.SYSTEM, "Developer mode");

    private final ArrayList<String> arguments = new ArrayList<String>();

    private HashMap<String, ConsoleCmd> cmdList = new HashMap<String, ConsoleCmd>();
    private HashMap<String, CVar> cvarList = new HashMap<String, CVar>();
    private long waitTime;
    private int minWaitTime;
    final ArrayList<String> inputHistory = new ArrayList<String>();

    private LinkedList<String> cmdQueue = new LinkedList<String>();

    public DevConsole(int minWaitTime) {
        this.minWaitTime = minWaitTime;

        register(com_allowCheats);
        register(com_developer);

        register(listCmds_f);
        register(help_f);
        register(wait_f);
        register(exec_f);
        register(echo_f);
        register(quit_f);

        register(listCVars_f);
        register(toggle_f);
        register(inc_f);
        register(dec_f);
        register(reset_f);
    }

    /**
     * Register a console command
     *
     * @param cmd The command
     */
    public final void register(ConsoleCmd cmd) {
        if (cmdList.containsKey(cmd.name)) {
            logger.warn("Trying to add command '{}', which already exists!", cmd.getName());
            return;
        }
        cmdList.put(cmd.name, cmd);
    }

    /**
     * Unregister a command previously registered by register
     *
     * @param cmd The command
     */
    public final void unregister(ConsoleCmd cmd) {
        cmdList.remove(cmd.name);
    }

    /**
     * Register a console var
     *
     * @param cvar The cvar
     */
    public final void register(CVar cvar) {
        if (cvarList.containsKey(cvar.getName())) {
            logger.warn("Trying to add cvar '{}', which already exists!", cvar.getName());
            return;
        }
        cvarList.put(cvar.getName(), cvar);
    }

    /**
     * Unregister a cvar previously registered by register
     *
     * @param cvar The cvar
     */
    public final void unregister(CVar cvar) {
        cvarList.remove(cvar.getName());
    }

    /**
     * Unregister commands by flags
     *
     * @param flags The flags to look for
     */
    public void unregisterCmdsByFlags(int flags) {
        for (ConsoleCmd cmd : cmdList.values()) {
            if ((cmd.getFlags() & flags) != 0) {
                cmdList.remove(cmd.getName());
            }
        }
    }

    /**
     * Unregister cvars by flags
     *
     * @param flags The flags to look for
     */
    public void unregisterCVarsByFlags(int flags) {
        for (CVar cvar : cvarList.values()) {
            if ((cvar.getFlags() & flags) != 0) {
                cmdList.remove(cvar.getName());
            }
        }
    }

    public void completeInput(String buffer, int selectionStart, int selectionEnd, ConsoleEditor editor) {
        if (buffer.isEmpty()) {
            return;
        }

        int selStart = Math.min(selectionStart, selectionEnd);
        String match = buffer.substring(0, selStart);
        if (match.equals(editor.lastMatch)) {
            if (editor.completionList.size() > 1) {
                int i = 0;
                for (String value : editor.completionList) {
                    i++;
                    if (value.equals(buffer)) {
                        if (i == editor.completionList.size()) {
                            i = 0;
                        }
                        String text = editor.completionList.get(i);
                        editor.setText(text);
                        editor.setSelection(editor.lastMatch.length(), text.length());
                        return;
                    }
                }
            }
        } else {
            editor.lastMatch = completeBuffer(match, editor);
            if (editor.completionList.size() == 1) {
                editor.setText(editor.lastMatch);
            } else if (!editor.completionList.isEmpty()) {
                String text = editor.completionList.get(0);
                editor.setText(text);
                editor.setSelection(editor.lastMatch.length(), text.length());

                logger.info("{}..", editor.lastMatch);
                for (String value : editor.completionList) {
                    logger.info("..{}", value);
                }
            }
        }
    }

    public void historyBack(String buffer, int selectionStart, int selectionEnd, ConsoleEditor editor) {
        if (editor.inputHistoryPos == 0) {
            editor.inputSavedText = buffer;
        }
        int len = inputHistory.size();
        if (editor.inputHistoryPos != len) {
            editor.inputHistoryPos++;
            editor.lastMatch = null;
            editor.setText(inputHistory.get(len - editor.inputHistoryPos));
        }
    }

    public void historyForward(String buffer, int selectionStart, int selectionEnd, ConsoleEditor editor) {
        if (editor.inputHistoryPos == 1) {
            editor.inputHistoryPos--;
            editor.lastMatch = null;
            editor.setText(editor.inputSavedText);
            editor.inputSavedText = null;
        } else if (editor.inputHistoryPos > 1) {
            int len = inputHistory.size();
            editor.inputHistoryPos--;
            editor.lastMatch = null;
            editor.setText(inputHistory.get(len - editor.inputHistoryPos));
        }
    }

    public void submitInput(String buffer, int selectionStart, int selectionEnd, ConsoleEditor editor) {
        // Trim both leading and trailing spaces
        buffer = buffer.trim();
        if (!buffer.isEmpty()) {
            inputHistory.add(buffer);
            editor.inputHistoryPos = 0;
            logger.info("]{}", buffer);
            executeCmd(buffer, false);
            editor.lastMatch = null;
        }
        editor.setText("");
    }

    /**
     * Execute the command
     *
     * @param command The command to execute
     * @param force true to execute immediately, otherwise it will be added to the command queue
     */
    public void executeCmd(String command, boolean force) {
        if (command.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        boolean inquote = false;
        int i;
        for (i = 0; i < command.length(); i++) {
            if (command.charAt(i) == '\n' || (!inquote && command.charAt(i) == ';')) {
                if (sb.length() == 0) {
                    continue;
                }
                if (inquote) {
                    sb.append('\"');
                    inquote = false;
                }

                if (force) {
                    executeSingleCmd(sb.toString());
                } else {
                    cmdQueue.addLast(sb.toString());
                }
                sb = new StringBuilder();
            } else {
                if (command.charAt(i) == '\"') {
                    inquote = !inquote;
                }
                sb.append(command.charAt(i));
            }
        }
        if (sb.length() > 0) {
            if (inquote) {
                sb.append('\"');
            }
            if (force) {
                executeSingleCmd(sb.toString());
            } else {
                cmdQueue.addLast(sb.toString());
            }
        }
    }

    /**
     * Execute all commands in the specified file
     *
     * @param filename The file path
     */
    public void runFile(String filename) {
    }

    /**
     * Tries to complete a command and its arguments
     *
     * @param    buf            The already typed in characters
     * @return    All matching characters, or if no match was found, a buf
     * @see IConsoleCompleter
     */
    private String completeBuffer(String buf, ConsoleEditor editor) {
        buf = StringUtils.trimFront(buf);
        if (buf.isEmpty()) {
            return "";
        }

        editor.completionList.clear();

        if (buf.indexOf(" ") != -1) {
            StringUtils.tokenize(buf, arguments);

            assert (arguments.size() > 1);

            // Find the command and call its completion func
            String arg0 = arguments.get(0);
            ConsoleCmd cmd = cmdList.get(arg0);
            IConsoleCompleter complete = null;
            if (cmd != null) {
                if (cmd.isCallable(false)) {
                    complete = cmd;
                }
            } else {
                CVar cvar = cvarList.get(arguments.get(0));
                if (cvar != null && cvar.isVisible(false)) {
                    complete = cvar.getCompleter();
                }
            }
            if (complete != null) {
                String arg = arguments.size() > 1 ? arguments.get(1) : "";
                complete.complete(arg, editor.completionList);
                if (!editor.completionList.isEmpty()) {
                    String base = arg0 + " ";
                    for (int i = 0; i < editor.completionList.size(); i++) {
                        editor.completionList.set(i, base + editor.completionList.get(i));
                    }
                }
            }
        } else {
            cmdCompleter.complete(buf, editor.completionList);
            cvarCompleter.complete(buf, editor.completionList);
        }
        if (editor.completionList.isEmpty()) {
            return buf;
        }

        int numEntries = editor.completionList.size();

        // Only one option, so return it straight
        if (numEntries == 1) {
            return editor.completionList.get(0);
        }

        // Convert to lowercase for faster comparison, then strip the input and calculate the shortest length
        int shortest = 1023;
        String lowerBuf = buf.toLowerCase();
        editor.completionListLower.clear();
        int i;
        for (i = 0; i < numEntries; i++) {
            String s = StringUtils.stripFrontOnce(editor.completionList.get(i).toLowerCase(), lowerBuf);
            editor.completionListLower.add(s);
            if (s.length() < shortest) {
                shortest = s.length();
            }
        }

        String returnValue = editor.completionList.get(0);
        if (shortest == 0) {
            returnValue = returnValue.substring(0, buf.length());
        } else {
            // Find the first not matching char
            int pos;
            String firstStr = editor.completionListLower.get(0);
            for (pos = 0; pos < shortest; pos++) {
                // Check if all chars at this position match
                for (i = 1; i < numEntries; i++) {
                    if (StringUtils.cmpn(firstStr, editor.completionListLower.get(i), pos) != 0) {
                        break;
                    }
                }
                // One did not match
                if (i < numEntries) {
                    break;
                }
            }
            returnValue = returnValue.substring(0, buf.length() + pos - 1);
        }

        return returnValue;
    }

    /**
     * Print usage for a command
     * 
     * @param buf The command name (if arguments exist, they will be ignored)
     */
    public void printUsage(String buf) {
        if (buf.isEmpty()) {
            return;
        }

        String command = StringUtils.trimFront(buf);

        if (command.isEmpty()) {
            return;
        }

        StringUtils.tokenize(buf, arguments);

        assert (!arguments.isEmpty());

        // Find the command and call its usage func
        ConsoleCmd cmd = cmdList.get(arguments.get(0));
        if (cmd != null) {
            cmd.showUsage();
        }
    }

    public void executeCmdQueue() {
        if (waitTime <= System.currentTimeMillis()) {
            while (!cmdQueue.isEmpty()) {
                executeSingleCmd(cmdQueue.removeFirst());
                if (waitTime > System.currentTimeMillis()) {
                    break;
                }
            }
        }
    }

    public void clearCmdQueue() {
        cmdQueue.clear();
    }

    private void executeSingleCmd(String command) {
        StringUtils.tokenize(command, arguments);

        if (arguments.isEmpty()) {
            return;
        }

        String arg0 = arguments.get(0);
        ConsoleCmd cmd = cmdList.get(arg0);
        if (cmd != null) {
            if (!cmd.isCallable(true)) {
                return;
            }
            if (cmd.getMinArguments() > (arguments.size() - 1)) {
                cmd.showUsage();
            } else {
                cmd.execute(arguments);
            }
        } else {
            CVar cvar = cvarList.get(arguments.get(0));
            if (cvar != null) {
                if (arguments.size() > 1) {
                    if (cvar.isWritable(true)) {
                        cvar.set(arguments.get(1), false);
                    }
                } else {
                    logger.info("{} is: \"{}\", default: \"{}\"",
                            cvar.getName(), cvar.toString(), cvar.getDefaultValue());
                    if (!cvar.getDescription().isEmpty()) {
                        logger.info("- {}", cvar.getDescription());
                    }
                }
            } else {
                logger.warn("Unknown command '{}'.", arg0);
            }
        }
    }

    /**
     * Reset flagged CVars to their default values
     * 
     * @param flags The flags to search for
     */
    public void resetCVars(int flags) {
        for (CVar cv : cvarList.values()) {
            if ((cv.getFlags() & flags) != 0) {
                cv.reset(true);
            }
        }
    }

    public CVar getWriteableCVar(String name) {
        CVar cvar = cvarList.get(name);
        if (cvar == null) {
            logger.warn("cvar \"{}\" not found!", name);
        } else if (!cvar.isWritable(true)) {
            return null;
        }
        return cvar;
    }

    private final IConsoleCompleter cvarCompleter = new IConsoleCompleter() {

        @Override
        public void complete(String arg, List<String> results) {
            for (CVar cvar : cvarList.values()) {
                if (cvar.isVisible(false) && cvar.getName().startsWith(arg)) {
                    results.add(cvar.getName());
                }
            }
        }
    };
    
    public IConsoleCompleter getCVarCompleter() {
        return cvarCompleter;
    }

    private final IConsoleCompleter cmdCompleter = new IConsoleCompleter() {

        @Override
        public void complete(String arg, List<String> results) {
            for (ConsoleCmd cmd : cmdList.values()) {
                if (cmd.isCallable(false) && cmd.getName().startsWith(arg)) {
                    results.add(cmd.getName());
                }
            }
        }
    };
    
    public IConsoleCompleter getCmdCompleter() {
        return cmdCompleter;
    }

    private final ConsoleCmd listCmds_f = new ConsoleCmd("listcmds", CCmdFlags.SYSTEM, "Lists all commands currently available.", 0) {
        @Override
        public void showUsage() {
            showUsage("[searchstring]");
        }

        @Override
        public void execute(List<String> args) {
            boolean match = false;
            String matchstr = null;

            if (args.size() > 1) {
                match = true;
                matchstr = args.get(1);
            }

            int found = 0;
            StringBuilder sb = new StringBuilder();
            for (ConsoleCmd cmd : cmdList.values()) {
                if (!cmd.isCallable(false)) {
                    continue;
                }
                if (match && cmd.getName().indexOf(matchstr) == -1) {
                    continue;
                }
                String message = String.format("%-18.18s - %s", cmd.getName(), cmd.getDescription());
                sb.append(message).append("\n");
                found++;
            }

            logger.info(sb.toString());
            if (match) {
                logger.info("{} {} matching \"{}\" listed", found, (found == 1 ? "command" : "commands"), matchstr);
            } else {
                logger.info("{} {} listed", found, (found == 1 ? "command" : "commands"));
            }
        }

    };

    private final ConsoleCmd help_f = new ConsoleCmd("help", CCmdFlags.SYSTEM, "Show help about a command or cvar.", 1) {
        @Override
        public void showUsage() {
            showUsage("<command/cvar>");
        }

        @Override
        public void complete(String arg, List<String> results) {
            cmdCompleter.complete(arg, results);
            cvarCompleter.complete(arg, results);

            //            results.sort( StringListICmp, true );
        }

        @Override
        public void execute(List<String> args) {
            if (args.size() > 2) {
                showUsage();
                return;
            }

            // Try Command List
            ConsoleCmd cmd = cmdList.get(args.get(1));
            if (cmd != null) {
                logger.info("{}: {}", cmd.name, cmd.getDescription());
                cmd.showUsage();
            } else {
                // Not a Command, is it a CVar?
                CVar cvar = cvarList.get(args.get(1));
                if (cvar != null) {
                    logger.info("{}: {}", cvar.getName(), cvar.getDescription());
                    return;
                }
                logger.info("Command/Cvar not found: '{}'", cmd);
            }
        }
    };

    private final ConsoleCmd wait_f = new ConsoleCmd("wait", CCmdFlags.SYSTEM, "Wait n milliseconds before executing the next command.") {
        @Override
        public void showUsage() {
            showUsage("[ms]");
        }

        @Override
        public void execute(List<String> args) {
            int ms = minWaitTime;
            if (args.size() > 1) {
                ms = QuietUtils.parseInt(args.get(1), minWaitTime);
                if (ms < minWaitTime) {
                    ms = minWaitTime;
                }
            }
            waitTime = System.currentTimeMillis() + ms;
        }
    };

    private final ConsoleCmd exec_f = new ConsoleCmd("exec", CCmdFlags.SYSTEM,
            "Execute a config.", 1 /*, new ConArgCompleteFile("", ".cfg")*/) {
        @Override
        public void showUsage() {
            showUsage("<filename[.cfg]>");
        }

        @Override
        public void execute(List<String> args) {
            String filename = args.get(1);
//            filename.DefaultFileExtension(".cfg");
            logger.info("Executing '{}'", filename);
//            cmdSystem.ExecuteConfig( filename );
        }
    };

    private final ConsoleCmd echo_f = new ConsoleCmd("echo", CCmdFlags.SYSTEM, "Print text to console.", 1) {
        @Override
        public void showUsage() {
            showUsage("<text>");
        }

        @Override
        public void execute(List<String> args) {
            logger.info(StringUtils.untokenize(args, 1, -1, false));
        }
    };

    private final ConsoleCmd quit_f = new ConsoleCmd("quit", CCmdFlags.SYSTEM, "Exit without questions.") {

        @Override
        public void execute(List<String> args) {
            System.exit(-1); // fixme?
        }
    };

    private final ConsoleCmd listCVars_f = new ConsoleCmd("listcvars", CCmdFlags.SYSTEM, "Lists all cvars currently available.") {
        @Override
        public void showUsage() {
            showUsage("[search string]        = list cvar values");
            showUsage("-help [search string]  = list cvar descriptions");
            showUsage("-type [search string]  = list cvar types");
            showUsage("-flags [search string] = list cvar flags");
        }

        @Override
        public void execute(List<String> args) {

            boolean match = false;
            String matchstr = null;

            StringBuilder sb = new StringBuilder();
            int found = -1;
            if (args.size() > 1) {
                if (args.size() > 2) {
                    match = true;
                    matchstr = args.get(2);
                }
                String arg1 = args.get(1);
                if (arg1.equals("-type")) {
                    found = 0;
                    for (CVar cvar : cvarList.values()) {
                        if (!cvar.isVisible(false)) {
                            continue;
                        }
                        if (match && cvar.getName().indexOf(matchstr) == -1) {
                            continue;
                        }

                        sb.append(cvar.getTypeDescription()).append("\n");
                        found++;
                    }
                } else if (arg1.equals("-flags")) {
                    found = 0;
                    for (CVar cvar : cvarList.values()) {
                        if (!cvar.isVisible(false)) {
                            continue;
                        }
                        if (match && cvar.getName().indexOf(matchstr) == -1) {
                            continue;
                        }
                        String message = String.format("%-32.32s %s",
                                cvar.getName(), Integer.toBinaryString(cvar.getFlags()));
                        sb.append(message).append("\n");
                        found++;
                    }
                } else if (arg1.equals("-help")) {
                    found = 0;
                    for (CVar cvar : cvarList.values()) {
                        if (!cvar.isVisible(false)) {
                            continue;
                        }
                        if (match && cvar.getName().indexOf(matchstr) == -1) {
                            continue;
                        }
                        String message = String.format("%-32.32s %-32.32s", cvar.getName(), cvar.getDescription());
                        sb.append(message).append("\n");
                        found++;
                    }
                } else {
                    match = true;
                    matchstr = args.get(1);
                }
            }
            if (found == -1) {
                found = 0;
                for (CVar cvar : cvarList.values()) {
                    if (!cvar.isVisible(false)) {
                        continue;
                    }
                    if (match && cvar.getName().indexOf(matchstr) == -1) {
                        continue;
                    }
                    String message = String.format("%-32.32s %-32.32s", cvar.getName(), cvar.toString());
                    sb.append(message).append("\n");
                    found++;
                }
            }

            logger.info(sb.toString());
            if (match) {
                logger.info("{} {} matching \"{}\" listed", found, (found == 1 ? "cvar" : "cvars"), matchstr);
            } else {
                logger.info("{} {} listed", found, (found == 1 ? "cvar" : "cvars"));
            }
        }
    };

    private final ConsoleCmd toggle_f = new ConsoleCmd("toggle", CCmdFlags.SYSTEM, "Toggles a CVar's value.", 1) {
        @Override
        public void showUsage() {
            showUsage("<variable> [...]");
        }

        @Override
        public void complete(String arg, List<String> results) {
            cvarCompleter.complete(arg, results);
        }

        @Override
        public void execute(List<String> args) {
            CVar cvar = getWriteableCVar(args.get(1));
            if (cvar == null) {
                return;
            }

            // Bool got no other states, so we can ignore all other params
            if (cvar instanceof CVarBool) {
                ((CVarBool) cvar).toggle(false);
            } else if (args.size() > 3) {
                int numArgs = args.size();

                int currentIndex = -1;
                if (cvar instanceof CVarInt) {
                    CVarInt cvarI = (CVarInt) cvar;
                    int curValue = cvarI.get();
                    for (int i = 2; i < numArgs; i++) {
                        if (QuietUtils.parseInt(args.get(i), 0) == curValue) {
                            currentIndex = i;
                            break;
                        }
                    }
                } else if (cvar instanceof CVarFloat) {
                    CVarFloat cvarF = (CVarFloat) cvar;
                    float curValue = cvarF.get();
                    for (int i = 2; i < numArgs; i++) {
                        if (QuietUtils.parseFloat(args.get(i), 0) == curValue) {
                            currentIndex = i;
                            break;
                        }
                    }
                } else {
                    String curValue = cvar.toString();
                    for (int i = 2; i < numArgs; i++) {
                        if (args.get(i).equals(curValue)) {
                            currentIndex = i;
                            break;
                        }
                    }
                }
                // Not found or the last item
                if (currentIndex == -1 || currentIndex == numArgs - 1) {
                    cvar.set(args.get(2), false);
                } else {
                    cvar.set(args.get(currentIndex + 1), false);
                }
            } else if (cvar instanceof CVarEnum) {
                ((CVarEnum) cvar).toggle(false);
            }
            logger.info("set {} = \"{}\"", cvar.getName(), cvar.toString());
        }
    };

    private final ConsoleCmd inc_f = new ConsoleCmd("inc", CCmdFlags.SYSTEM, "Increases a CVar by an amount(1 per default).", 1) {
        @Override
        public void showUsage() {
            showUsage("<variable> [amount]");
        }

        @Override
        public void complete(String arg, List<String> results) {
            cvarCompleter.complete(arg, results);
        }

        @Override
        public void execute(List<String> args) {
            CVar cvar = getWriteableCVar(args.get(1));
            if (cvar == null) {
                return;
            }

            if (cvar instanceof CVarInt) {
                int amount = 1;
                if (args.size() == 3) {
                    amount = QuietUtils.parseInt(args.get(2), amount);
                }
                ((CVarInt) cvar).add(amount, false);
                logger.info("set {} = \"{}\"", cvar.getName(), cvar.toString());
            } else if (cvar instanceof CVarFloat) {
                float amount = 1.0f;
                if (args.size() == 3) {
                    amount = QuietUtils.parseFloat(args.get(2), amount);
                }
                ((CVarFloat) cvar).add(amount, false);
                logger.info("set {} = \"{}\"", cvar.getName(), cvar.toString());
            } else {
                logger.warn("This command only works on int and float cvars");
            }
        }
    };

    private final ConsoleCmd dec_f = new ConsoleCmd("dec", CCmdFlags.SYSTEM, "Decreases a CVar by an amount(1 per default).", 1) {
        @Override
        public void showUsage() {
            showUsage("<variable> [amount]");
        }

        @Override
        public void complete(String arg, List<String> results) {
            cvarCompleter.complete(arg, results);
        }

        @Override
        public void execute(List<String> args) {
            CVar cvar = getWriteableCVar(args.get(1));
            if (cvar == null) {
                return;
            }

            if (cvar instanceof CVarInt) {
                int amount = 1;
                if (args.size() == 3) {
                    amount = QuietUtils.parseInt(args.get(2), amount);
                }
                ((CVarInt) cvar).add(-amount, false);
                logger.info("set {} = \"{}\"", cvar.getName(), cvar.toString());
            } else if (cvar instanceof CVarFloat) {
                float amount = 1.0f;
                if (args.size() == 3) {
                    amount = QuietUtils.parseFloat(args.get(2), amount);
                }
                ((CVarFloat) cvar).add(-amount, false);
                logger.info("set {} = \"{}\"", cvar.getName(), cvar.toString());
            } else {
                logger.warn("This command only works on int and float cvars");
            }
        }
    };

    private final ConsoleCmd reset_f = new ConsoleCmd("reset", CCmdFlags.SYSTEM, "Resets a CVar it's the default value.", 1) {
        @Override
        public void showUsage() {
            showUsage("<variable>");
        }

        @Override
        public void complete(String arg, List<String> results) {
            cvarCompleter.complete(arg, results);
        }

        @Override
        public void execute(List<String> args) {
            CVar cvar = getWriteableCVar(args.get(1));
            if (cvar != null) {
                cvar.reset(false);
            }
        }
    };
}
