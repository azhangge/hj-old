package com.huajie.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESTool {

    private SecretKey key;
    private String keyLoc = "";
    private final String keySerCode = "rO0ABXNyAB9qYXZheC5jcnlwdG8uc3BlYy5TZWNyZXRLZXlTcGVjW0cLZuIwYU0CAAJMAAlhbGdv\ncml0aG10ABJMamF2YS9sYW5nL1N0cmluZztbAANrZXl0AAJbQnhwdAADREVTdXIAAltCrPMX+AYI\nVOACAAB4cAAAAAiJ2W12MmRbBw==";

    public DESTool() {
        try {
            BASE64Decoder d = new BASE64Decoder();
            byte[] t = d.decodeBuffer(this.keySerCode);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(t));
            this.key = ((SecretKey) ois.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DESTool(String keyLocation) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(keyLocation));
            this.key = ((SecretKey) ois.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DESTool(SecretKey key) {
        this.key = key;
    }

    public SecretKey getKey() {
        return this.key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public String getKeyLoc() {
        return this.keyLoc;
    }

    public void setKeyLoc(String keyLoc) {
        this.keyLoc = keyLoc;
    }

    public String encrypt(String str) {
        try {
            Cipher c = Cipher.getInstance("DES");
            c.init(1, this.key);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(bos, c);
            cos.write(str.getBytes());
            cos.close();
            byte[] tt = bos.toByteArray();
            bos.close();
            BASE64Encoder enc = new BASE64Encoder();
            String t = enc.encode(tt);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CipherOutputStream encryptOutputStream(OutputStream os) {
        try {
            Cipher en = Cipher.getInstance("DES");
            en.init(1, this.key);
            CipherOutputStream cos = new CipherOutputStream(os, en);
            return cos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encryptedStr) {
        try {
            Cipher de = Cipher.getInstance("DES");
            de.init(2, this.key);
            BASE64Decoder den = new BASE64Decoder();
            byte[] te = den.decodeBuffer(encryptedStr);
            ByteArrayInputStream bis = new ByteArrayInputStream(te);
            CipherInputStream cis = new CipherInputStream(bis, de);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            int j = 0;
            byte[] t2 = new byte[512];
            while ((j = cis.read(t2)) != -1) {
                bos2.write(t2, 0, j);
            }
            cis.close();
            bis.close();
            String ss = new String(bos2.toByteArray());
            bos2.close();
            return ss;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream decryptInputStream(InputStream is) {
        try {
            Cipher de = Cipher.getInstance("DES");
            de.init(2, this.key);
            CipherInputStream cis = new CipherInputStream(is, de);
            return cis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String showKeySerCode(SecretKey k) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(k);
            oos.close();
            byte[] t = bos.toByteArray();
            BASE64Encoder e = new BASE64Encoder();
            String s = e.encode(t);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SecretKey genKey() {
        try {
            SecretKey sk = null;
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(56);
            sk = kg.generateKey();
            return sk;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        DESTool ct = new DESTool();
        ct.setKey(ct.genKey());
        String s = "www.huajie.com";
        String t = ct.encrypt(s);
        System.out.println(t);
        String ss = ct.decrypt(t);
        System.out.println(ss);
        System.out.println(ct.keySerCode.length());
    }
}
