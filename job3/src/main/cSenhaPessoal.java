/*
 * PUC-Rio - Pontificia Universidade Catolica
 * Bacharelado em Sistemas de Informacao
 *
 * INF 1416 - Seguranca da Informacao 2010.1     Turma: 3WA
 * Trabalho P1
 * Professor: Anderson Oliveira da Silva
 *
 * Grupo: Henrique Taunay
 *        Luiz Felipe Machado
 */
package main;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luizfelipe
 */
public class cSenhaPessoal {

  private String senhaHex;

  private MessageDigest messageDigest = null;

  public cSenhaPessoal(String senhaHex) {
    this.senhaHex = senhaHex;
  }
  public cSenhaPessoal() {
  }

  public String getSenhaHex() {
    return senhaHex;
  }

  public void setSenhaHex(String senhaHex) {
    this.senhaHex = senhaHex;
  }

  public void setSenhaTextoPlano(String login, int uid, String senhaTextoPlano) {
    senhaHex = converteHexadecimal(calculaDigest(senhaTextoPlano + String.valueOf(uid) + login));
  }

  public boolean verificaSenha(String login, int id, Vector<String> lst_fonemas) {

    if(messageDigest == null) {
      try {
        messageDigest = MessageDigest.getInstance("MD5");
      } catch(NoSuchAlgorithmException ex) {
        Logger.getLogger(cSenhaPessoal.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    byte[] digest = null;

    String senha = "";
    for(int i = 0; i < 4; i++) {

      for(int j = 4; j < 8; j++) {

        for(int k = 8; k < 12; k++) {

          // concatena os fonemas
          senha = lst_fonemas.get(i) + lst_fonemas.get(j) + lst_fonemas.get(k);

          digest = calculaDigest(senha + String.valueOf(id) + login);
          senha = converteHexadecimal(digest);

          if(this.senhaHex.compareTo(senha) == 0)
            return true;
        
        }
      }
    }
    
    return false;
  }

  private String converteHexadecimal(byte[] digest) {
   StringBuilder buf = new StringBuilder();
    for(int i = 0; i < digest.length; i++) {
       String hex = Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1);
       buf.append(hex.length() < 2 ? "0" : "").append(hex);
    }
    return buf.toString();
  }

  private byte[] calculaDigest(String senha) {

    if(messageDigest == null) {
      try {
        messageDigest = MessageDigest.getInstance("MD5");
      } catch(NoSuchAlgorithmException ex) {
        Logger.getLogger(cSenhaPessoal.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    try {
      messageDigest.reset();
      messageDigest.update(senha.getBytes("UTF8"));
    } catch(UnsupportedEncodingException ex) {
      Logger.getLogger(cSenhaPessoal.class.getName()).log(Level.SEVERE, null, ex);
    }

    return messageDigest.digest();
  }
}
