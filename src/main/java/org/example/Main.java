package org.example;
import org.json.*;

import java.math.BigInteger;
import java.nio.file.*;
import java.security.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        String prn = args[0].toLowerCase().replaceAll("\\s", "");
        String filePath = args[1];

        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(jsonContent);

        String destination = getDestination(jsonObject);
        String randomString = generateRandomString(8);

        String toHash = prn + destination + randomString;
        String hashed = getMD5Hash(toHash);

        System.out.println(hashed + ";" + randomString);
    }

    private static String getDestination(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                String destination = getDestination((JSONObject) value);
                if (destination != null) {
                    return destination;
                }
            } else if (key.equals("destination")) {
                return value.toString();
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String getMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
