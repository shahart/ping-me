package edu.pingme.messaging_stomp_websocket;

public class Utils {

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
}
