package edu.pingme.messaging_stomp_websocket;

import net.fellbaum.jemoji.Emoji;
import net.fellbaum.jemoji.EmojiManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.Base64;

public class UtilsTest {

    String password = "1234";

    @Test
    public void testDeCrypt() {
        String enc = Utils.deEnCrypt("theQuickBrownFox", password); // EZVeD[P_s@\C_t\L
        System.out.println(enc);
        Assertions.assertEquals("theQuickBrownFox", Utils.deEnCrypt(enc, password));
    }

    @Test
    public void testBase64() {
        Assertions.assertEquals("theQuickBrownFox", new String(Base64.getDecoder().decode("dGhlUXVpY2tCcm93bkZveA==")));
    }

    @Test
    public void testEncryptedAndBase64() {
        Assertions.assertEquals("theQuickBrownFox", Utils.deEnCrypt(new String(Base64.getDecoder().decode("RVpWZURbUF9zQFxDX3RcTA==")), password));
    }

    @Test
    public void testIntegViaLocation() {
        String text = "https://www.google.com/maps/place/31.78822N+35.2029E    WAZE: https://waze.com/ul?ll=31.78822%2C35.2029";
        String expectedInput = Base64.getEncoder().encodeToString(Utils.deEnCrypt(text, password).getBytes());
        Assertions.assertEquals(text, Utils.deEnCrypt(new String(Base64.getDecoder().decode(expectedInput)), password));
    }

    @Test
    public void testStripEmoji() {
        Assertions.assertEquals("sanityCheck :face_holding_back_tears: and now :+1:", Utils.stripEmoji("sanityCheck \uD83E\uDD79 and now \uD83D\uDC4D")); // .. 🥹 .. 👍
    }

    @Disabled // Test
    public void generateDictionary() {
        // copy-paste this output to emojis.html
        for (Emoji emoji: EmojiManager.getAllEmojis()) {
            String alias = "";
            if (! emoji.getDescription().isEmpty()) {
                alias += emoji.getDescription() + " ";
            }
            for (String al: emoji.getAllAliases()) {
                if (al.contains("'")) {
                    System.out.println(al);
                }
                else if (alias.contains(al)) {
                    alias += al + " ";
                }
            }
            // if (alias.isEmpty()) { System.out.println("EMPTY " + emoji.getEmoji()); }
            System.out.println("    '" + emoji.getEmoji() + "': '" + alias + "',");
        }
        System.out.println(EmojiManager.getAllEmojis().size());
    }
}
