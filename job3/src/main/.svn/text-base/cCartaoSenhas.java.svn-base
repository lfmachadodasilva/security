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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luizfelipe
 */
public class cCartaoSenhas {

  private Vector<String> lstSenhaHex = null;

  private MessageDigest messageDigest = null;

  private String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  public cCartaoSenhas() {
    lstSenhaHex = new Vector<String>();
  }

  public void setLstSenhas(Vector<String> lstSenhaHex) {
    this.lstSenhaHex = lstSenhaHex;
  }

  public int getQuantidadeSenhas() {
    return lstSenhaHex.size();
  }

  public Vector<String> getLstSenhaHex() {
    return lstSenhaHex;
  }

  /*
   * Funcao verifica se o que o usuario digitou tem apenas letras maiuculas ou numeros
   * A funcao tb verifica se a senha contem a quantidade necessaria (4 caracteres)
   */
  public boolean verificaCaracteresDaSenha(String senhaDigitada) {

    if(senhaDigitada.length() != 4)
      return false;

    Character c = null;
    for(int i = 0; i < senhaDigitada.length(); i++) {
      c = senhaDigitada.charAt(i);

      if(Character.isLetter(c)) {
        if(Character.isLowerCase(c))
          return false;
      } else {
        if(!Character.isDigit(c))
          return false;
      }
    }
    
    return true;
  }

  /*
   * Funcao que verifica se a senha digitada é iqual a senha pedida
   */
  public boolean verificaSenha(String login, int uid, String senhaDigitada, int indexSenhaPedida) {

    String senhaHexPedida = lstSenhaHex.get(indexSenhaPedida);

    byte[] digest = calculaDigest(senhaDigitada + String.valueOf(uid) + login);
    String senhaHexDigitada = converteHexadecimal(digest);

    if(senhaHexPedida.equals(senhaHexDigitada))
      return true;
    
    return false;
  }

  public void criaCartaoSenhas(String login, int uid, int quantidadeSenhas, String caminhoArquivo) {
    String senha = null;

    Vector<String> lstSenhas = new Vector<String>();

    // utiliza como semente o tempo em milisegundos
    Random gerador = new Random(new Date().getTime());

    lstSenhaHex.clear();

    for(int i = 0; i < quantidadeSenhas; i++) {
      gerador.nextInt(alfabeto.length() - 1);
      senha =  String.valueOf(alfabeto.charAt(gerador.nextInt(alfabeto.length() - 1)));
      senha += String.valueOf(alfabeto.charAt(gerador.nextInt(alfabeto.length() - 1)));
      senha += String.valueOf(alfabeto.charAt(gerador.nextInt(alfabeto.length() - 1)));
      senha += String.valueOf(alfabeto.charAt(gerador.nextInt(alfabeto.length() - 1)));

      lstSenhas.add(senha);

      byte[] digest = calculaDigest(senha + String.valueOf(uid) +login);
      senha = converteHexadecimal(digest);

      lstSenhaHex.add(senha);
    }

    criaArquivo(caminhoArquivo, lstSenhas);
  }

  private void criaArquivo(String caminhoArquivo, Vector<String> lstSenhas) {
    File arquivo = new File(caminhoArquivo);
    
    try {
      arquivo.createNewFile();
    } catch(IOException ex) {
      Logger.getLogger(cCartaoSenhas.class.getName()).log(Level.SEVERE, null, ex);
    }

    String suv = "";

    for(int i = 0; i < lstSenhas.size(); i++) {
      suv += String.valueOf(i + 1) + "\t" + lstSenhas.get(i);
      if(i != lstSenhas.size() - 1) suv += "\n";
    }

    writeToEOF(suv, arquivo);
  }

  /*
   * Pequei do trabalho 2
   */
  private static void writeToEOF(String s, File f) {

    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
      bw.write(s);
      bw.close();

    } catch(IOException e) {
      System.out.println("Problems writing to file!"
                         + e.getStackTrace().toString());
    }
  }

  /*
   * Funcao que converte o digest para Hexadecimal
   */
  private String converteHexadecimal(byte[] digest) {
   StringBuilder buf = new StringBuilder();
    for(int i = 0; i < digest.length; i++) {
       String hex = Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1);
       buf.append(hex.length() < 2 ? "0" : "").append(hex);
    }
    return buf.toString();
  }

  /*
   * Funcao que calcula o digest
   */
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
