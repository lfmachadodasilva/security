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

import controlador.cControlador;
import javax.swing.UIManager;

/**
 *
 * @author luizfelipe
 */
public class cMain {

  public static void main(String[] args) {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      e.getStackTrace();
    }

    cControlador controlador = new cControlador();

    controlador.conectaBanco();

    controlador.mostraTelaLogin(true); 
  }
}
