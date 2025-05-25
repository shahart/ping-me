package edu.pingme.messaging_stomp_websocket;

import net.fellbaum.jemoji.EmojiManager;
import net.fellbaum.jemoji.IndexedEmoji;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static String deEnCrypt(String input, String password) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++ i) {
            sb.append((char)(input.charAt(i) ^ password.charAt(i % password.length())));
        }
        return sb.toString();
    }

    public static String getBrowser(String userAgent) {
        if (userAgent.contains("iPhone")) {
            return userAgent.contains("CriOS") ? "iPhone" : "iWatch";
        }
        else if (userAgent.contains("Postman")) {
            return "Postman";
        }
        else if (userAgent.contains("Windows")) {
            return "Windows";
        }
        else if (userAgent.contains("Samsung")) {
            return "Samsung Android";
        }
        else if (userAgent.contains("Edg")) {
            return "Edge";
        }
        return "Some browser";
    }

    public static String stripEmoji(String text) {
        // TODO consider using Java 21 Character.isEmoji
        String result = EmojiManager.replaceAllEmojis(text,"<Emoji>");
        if (! result.equals(text)) {
            List<IndexedEmoji> emojis = EmojiManager.extractEmojisInOrderWithIndex(text);
            for (IndexedEmoji emoji: emojis) {
                List<String> aliases = emoji.getEmoji().getAllAliases();
                String replacement = aliases.isEmpty() ? "<Emoji>" : aliases.getFirst();
                result = result.replaceFirst("<Emoji>", replacement);
            }
            log.warn("Emojis were found. New text: " + result);
        }
        return result;
    }
}
