package com.cosmos.photonim.imbase.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static SpannableString changeColor(String content, String matchPrefixPattern, String matchPostfixPatten,
                                              String matchPrefix, String matchPostfix, int color) {
        final String MATCH = String.format("%s.*?%s", matchPrefixPattern, matchPostfixPatten);
        Pattern pattern = Pattern.compile(MATCH);
        Matcher matcher = pattern.matcher(content);
        ArrayList<Position> positions = null;
        while (matcher.find()) {
            if (positions == null) {
                positions = new ArrayList<>();
            }
            positions.add(new Position(matcher.start(), matcher.end()));
        }
        SpannableString contentShow = null;
        BackgroundColorSpan colorSpan;
        if (positions != null) {
            content = content.replaceAll(matchPrefixPattern, "");
            content = content.replaceAll(matchPostfixPatten, "");
            contentShow = new SpannableString(content);

            int matchLength = matchPrefix.length() + matchPostfix.length();
            for (int i = 0; i < positions.size(); i++) {
                positions.get(i).start -= i * matchLength;
                positions.get(i).end -= (i + 1) * matchLength;
                colorSpan = new BackgroundColorSpan(color);
                contentShow.setSpan(colorSpan, positions.get(i).start, positions.get(i).end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        } else {
            contentShow = new SpannableString(content);
        }
        return contentShow;
    }

    static class Position {
        int start;
        int end;

        public Position(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
