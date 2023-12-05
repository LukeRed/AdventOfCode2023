package eu.lukered.aoc2023;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record GroupMatcher(Pattern pattern, int group) {
    public String get(final String input) {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    public int getInt(final String input) {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(group));
        }
        return 0;
    }
}