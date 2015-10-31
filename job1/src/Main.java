/*
 * PUC-Rio - Pontificia Universidade Catolica
 *
 * Seguranca da Informcao 2010.2
 * Professor: Anderson O. da Silva
 *
 * Nome: Luiz Felipe Machado
 *       Henrique Taunay
 */
//package trabalhosi;

import java.security.*;
import javax.crypto.Cipher;

/**
 *
 * @author luizfelipe
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //
        // verifica args e recebe o texto plano
        if (args.length != 1) {
            System.err.println("Usage: java DigitalSignatureExample text");
            System.exit(1);
        }
        byte[] plainText = args[0].getBytes("UTF8");
        //
        // pega o objeto de digest usando o algoritmo MD5
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //
        // imprime informações sobre o provider do objeto digest
        System.out.println("\nDigest provider:\n" + messageDigest.getProvider().getInfo());
        //
        // calcula o digest do texto plano
        messageDigest.update(plainText);
        byte[] digest = messageDigest.digest();
        System.out.println("\nDigest length: " + digest.length * 8 + "bits");
        // converte o digist para hexadecimal
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1);
            buf.append(hex.length() < 2 ? "0" : "").append(hex);
        }
        //
        // imprime o digest em hexadecimal
        System.out.println("\nDigest(hex): ");
        System.out.println(buf.toString());
        //
        // gera um par de chaves RSA utilizando 1024 bits
        System.out.println("\nStart generating RSA key");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");
        //
        // define o objeto de cifra RSA e imprime o provider utilizado
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        System.out.println("\nCipher provider:\n" + cipher.getProvider().getInfo());
        //
        // encripta o digest utilizando a chave privada
        System.out.println("\nStart encryption");
        cipher.init(Cipher.ENCRYPT_MODE, key.getPrivate());
        byte[] cipherText = cipher.doFinal(digest);
        System.out.println("Finish encryption");
        //
        // converte o digist para hexadecimal
        buf = new StringBuilder();
        for (int i = 0; i < cipherText.length; i++) {
            String hex = Integer.toHexString(0x0100 + (cipherText[i] & 0x00FF)).substring(1);
            buf.append(hex.length() < 2 ? "0" : "").append(hex);
        }
        //
        // imprime a assinatura digital em hexadecimal
        System.out.println("\nDigital Signature(hex): ");
        System.out.println(buf.toString());
        //
        // desencripta o digest utilizando a chave publica
        System.out.println("\nStart decryption");
        cipher.init(Cipher.DECRYPT_MODE, key.getPublic());
        byte[] newDigest = cipher.doFinal(cipherText);
        System.out.println("Finish decryption");
        //
        // converte o digist para hexadecimal
        buf = new StringBuilder();
        for (int i = 0; i < newDigest.length; i++) {
            String hex = Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1);
            buf.append(hex.length() < 2 ? "0" : "").append(hex);
        }

        // imprime o digest em hexadecimal
        System.out.println("\nDigest(hex): ");
        System.out.println(buf.toString());
        //
        // Compara o digest recuperado com o digest original
        boolean verifyOK = true;
        System.out.println("\nStart signature verification");
        for (int i = 0; i < digest.length; ++i) {
            if (digest[i] != newDigest[i]) {
                verifyOK = false;
            }
        }
        System.out.println("\nStop signature verification");

        if (verifyOK) {
            System.out.println("\nSignature verified\n");
        } else {
            System.out.println("\nSignature verification process failed\n");
        }
    }
}
