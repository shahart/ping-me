package edu.pingme.messaging_stomp_websocket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
