package de.leghast;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class PasswordCheck {
    public Boolean checkPassword(String toCheck) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        File f = new File("./test/src/main/resources/de/leghast/saves/firstRun.42");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line.equals(toHexString(toCheck))) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String toHexString(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * 
     * @param hash
     * @return
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder s = new StringBuilder();
        for (byte b : hash) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(s.toString());
        return s.toString();
    }
}
