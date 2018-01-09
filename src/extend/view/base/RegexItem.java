/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.view.base;

import java.util.regex.Pattern;

/**
 *
 * @author isayan
 */
public class RegexItem {
    private String match = "";
    private Pattern regex;
    
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
        
    private boolean regexp = true;
    private boolean ignoreCase = false;

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
    
}
