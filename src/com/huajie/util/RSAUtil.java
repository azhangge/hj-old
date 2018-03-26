package com.huajie.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.io.*;
import java.math.BigInteger;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * 
*/
public class RSAUtil {

    /**
     * 生成密钥对
     *
     * @return KeyPair
     * @throws EncryptException
     */
    public static KeyPair generateKeyPair() throws EncryptException {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final int KEY_SIZE = 64;//没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.genKeyPair();
            return keyPair;
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
    }

    /**
     * 生成公钥
     *
     * @param modulus
     * @param publicExponent
     * @return RSAPublicKey
     * @throws EncryptException
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws EncryptException {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new EncryptException(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new EncryptException(ex.getMessage());
        }
    }

    /**
     * 生成私钥
     *
     * @param modulus
     * @param privateExponent
     * @return RSAPrivateKey
     * @throws EncryptException
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws EncryptException {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new EncryptException(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new EncryptException(ex.getMessage());
        }
    }

    /**
     * 加密
     *
     * @param key 加密的密钥
     * @param data 待加密的明文数据
     * @return 加密后的数据
     * @throws EncryptException
     */
    public static byte[] encrypt(Key key, byte[] data) throws EncryptException {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();//获得加密块大小，如：加密前数据为128个byte，而key_size=1024 加密块大小为127 byte,加密后为128个byte;因此共有2个加密块，第一个127 byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);//获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                }
//这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。

                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
    }

    /**
     * 解密
     *
     * @param key 解密的密钥
     * @param raw 已经加密的数据
     * @return 解密后的明文
     * @throws EncryptException
     */
    public static byte[] decrypt(Key key, byte[] raw) throws EncryptException {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(cipher.DECRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;

            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
    }
    
    public static String toHexString(byte[] b) {   
        StringBuilder sb = new StringBuilder(b.length * 2);   
        for (int i = 0; i < b.length; i++) {   
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);   
            sb.append(HEXCHAR[b[i] & 0x0f]);   
        }   
        return sb.toString();   
    }   
  
    public static final byte[] toBytes(String s) {   
        byte[] bytes;   
        bytes = new byte[s.length() / 2];   
        for (int i = 0; i < bytes.length; i++) {   
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),   
                    16);   
        }   
        return bytes;   
    }   
  
    private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7',   
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };   

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String s1 = "test";

        byte[] orgData = s1.getBytes();
        KeyPair keyPair = RSAUtil.generateKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();

        byte[] pubModBytes = pubKey.getModulus().toByteArray();
        byte[] pubPubExpBytes = pubKey.getPublicExponent().toByteArray();

        byte[] priModBytes = priKey.getModulus().toByteArray();
        byte[] priPriExpBytes = priKey.getPrivateExponent().toByteArray();
        
        String k11=RSAUtil.toHexString(pubModBytes);
        String k12=RSAUtil.toHexString(pubPubExpBytes);
        
        String k21="0089466bb3dbb587b2f4d3294d29156cd0228e571d2474eaa9af78033c66cf8e8cfd9ff6803a5ffa472e7bb2f68a271c27d8ad299b577b592a2ea83633198b8997";
        String k22="57e3e681396278877388df20aff52fbdf2e1ff94810afa53ba2cb4a5af8da44e6074ad46cb4cd325a4639eb1e1209da51853fcaf073a3aa464e358911d74bd81";
        
        System.out.println(k11);
        System.out.println(k12);
        
        System.out.println(k21);
        System.out.println(k22);

        RSAPublicKey recoveryPubKey = RSAUtil.generateRSAPublicKey(RSAUtil.toBytes(SpringHelper.getSpringBean("pubMod").toString()), RSAUtil.toBytes(SpringHelper.getSpringBean("PubExp").toString()));
        RSAPrivateKey recoveryPriKey = RSAUtil.generateRSAPrivateKey(RSAUtil.toBytes(k21), RSAUtil.toBytes(k22));
        
        

        byte[] raw = RSAUtil.encrypt(recoveryPriKey, orgData);
        String s2 = RSAUtil.toHexString(raw);
        byte[] data = RSAUtil.decrypt(recoveryPubKey, RSAUtil.toBytes(s2));
        String s3 = new String(data);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
    }
}
