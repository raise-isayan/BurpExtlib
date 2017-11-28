/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.view.base;

import extend.util.SwingUtil;
import extend.util.Util;
import java.awt.Color;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author isayan
 */
public class MatchItem {

    /**
     * @return the regexp
     */
    public boolean isRegexp() {
        return this.regexp;
    }

    /**
     * @param regexp the regexp to set
     */
    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
    }

    public enum TargetTool {

        PROXY, REPEATER, SPIDER, INTRUDER, SCANNER, SEQUENCER;

        public static TargetTool parseValue(String value) {            
            TargetTool eval = (TargetTool) Util.parseEnumValue(TargetTool.class, value);
            if (eval == null) {
                return TargetTool.PROXY;
            } else {
                return eval;
            }
        }

        private static final Pattern ENUM_SPLIT = Pattern.compile("\\w+");

        public static EnumSet<TargetTool> enumSetValueOf(String s) {
            EnumSet<TargetTool> values = EnumSet.noneOf(TargetTool.class);
            Matcher m = ENUM_SPLIT.matcher(s.toUpperCase());
            while (m.find()) {
                values.add((TargetTool)Util.parseEnumValue(TargetTool.class, m.group()));
            }
            return values;
        }
        
        @Override
        public String toString() {
            String value = name().toLowerCase();
            return value.replace('_', ' ');
        }
    }

    public enum NotifyType {

        ALERTS_TAB, TRAY_MESSAGE, ITEM_HIGHLIGHT, COMMENT;

        public static NotifyType parseValue(String value) {            
            NotifyType eval = (NotifyType) Util.parseEnumValue(NotifyType.class, value);
            if (eval == null) {
                return NotifyType.ALERTS_TAB;
            } else {
                return eval;
            }
        }

        private static final Pattern ENUM_SPLIT = Pattern.compile("\\w+");

        public static EnumSet<NotifyType> enumSetValueOf(String s) {
            EnumSet<NotifyType> values = EnumSet.noneOf(NotifyType.class);
            Matcher m = ENUM_SPLIT.matcher(s.toUpperCase());
            while (m.find()) {
                values.add((NotifyType)Util.parseEnumValue(NotifyType.class, m.group()));
            }
            return values;
        }
        
        @Override
        public String toString() {
            String value = name().toLowerCase();
            return value.replace('_', ' ');
        }
        
    };

    public enum HighlightColor {

        WHITE, RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PINK, MAGENTA, GRAY;

        private final static EnumMap<HighlightColor, Color> namedColor = new EnumMap<HighlightColor, Color>(HighlightColor.class);
        private final static EnumMap<HighlightColor, ImageIcon> namedIcon = new EnumMap<HighlightColor, ImageIcon>(HighlightColor.class);

        static {
            // WHITE == nonselect
            namedColor.put(RED, Color.RED);
            namedColor.put(ORANGE, Color.ORANGE);
            namedColor.put(YELLOW, Color.YELLOW);
            namedColor.put(GREEN, Color.GREEN);
            namedColor.put(CYAN, Color.CYAN);
            namedColor.put(BLUE, Color.BLUE);
            namedColor.put(PINK, Color.PINK);
            namedColor.put(MAGENTA, Color.MAGENTA);
            namedColor.put(GRAY, Color.GRAY);

            namedIcon.put(RED, SwingUtil.createSquareIcon(Color.RED, 12, 12));
            namedIcon.put(ORANGE, SwingUtil.createSquareIcon(Color.ORANGE, 12, 12));
            namedIcon.put(YELLOW, SwingUtil.createSquareIcon(Color.YELLOW, 12, 12));
            namedIcon.put(GREEN, SwingUtil.createSquareIcon(Color.GREEN, 12, 12));
            namedIcon.put(CYAN, SwingUtil.createSquareIcon(Color.CYAN, 12, 12));
            namedIcon.put(BLUE, SwingUtil.createSquareIcon(Color.BLUE, 12, 12));
            namedIcon.put(PINK, SwingUtil.createSquareIcon(Color.PINK, 12, 12));
            namedIcon.put(MAGENTA, SwingUtil.createSquareIcon(Color.MAGENTA, 12, 12));
            namedIcon.put(GRAY, SwingUtil.createSquareIcon(Color.GRAY, 12, 12));

        }

        public Color toColor() {
            return namedColor.get(this);
        }

        public ImageIcon toIcon() {
            return namedIcon.get(this);
        }

        public static HighlightColor parseValue(String value) {            
            HighlightColor eval = (HighlightColor) Util.parseEnumValue(HighlightColor.class, value);
            if (eval == null) {
                return HighlightColor.WHITE;
            } else {
                return eval;
            }
        }

        private static final Pattern ENUM_SPLIT = Pattern.compile("\\w+");

        public static EnumSet<HighlightColor> enumSetValueOf(String s) {
            EnumSet<HighlightColor> values = EnumSet.noneOf(HighlightColor.class);
            Matcher m = ENUM_SPLIT.matcher(s.toUpperCase());
            while (m.find()) {
                values.add((HighlightColor)Util.parseEnumValue(HighlightColor.class, m.group()));
            }
            return values;
        }
        
        @Override
        public String toString() {
            String value = name().toLowerCase();
            return value.replace('_', ' ');
        }
    };
    private boolean selected = true;
    private String type;
    private boolean regexp = true;
    private boolean ignoreCase = false;
    private boolean format = false;
    private String match = "";
    private Pattern regex;

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the ignoreCase
     */
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    /**
     * @param ignoreCase the ignoreCase to set
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        this.compileRegex(false);
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return this.match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
        this.regex = compileRegex(false);
    }
    
    public Pattern compileRegex(boolean quote) {
        int flags = Pattern.MULTILINE;
        Pattern newregex;
        if (this.ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (quote) {
            newregex = Pattern.compile(Pattern.quote(this.match), flags);
        } else {
            newregex = Pattern.compile(this.match, flags);
        }
        return newregex;
    }

    public void recompileRegex(boolean quote) {
       this.regex = compileRegex(quote);
    }

    /**
     * @return the regex
     */
    public Pattern getRegexPattern() {
        return this.regex;
    }
    
    private String replace = "";

    /**
     * @return the replace
     */
    public String getReplace() {
        return this.getReplace(false);
    }
    
    /**
     * @param quote
     * @param metachar
     * @return the replace
     */
    public String getReplace(boolean quote) {
        if (quote) {
            return Matcher.quoteReplacement(this.replace);
        } else {
            return this.replace;
        }
    }

    /**
     * @param replace the replace to set
     */
    public void setReplace(String replace) {
        this.replace = replace;
    }
    
}
