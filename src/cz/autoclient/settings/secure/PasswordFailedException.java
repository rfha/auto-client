/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.autoclient.settings.secure;

/**
 *
 * @author Jakub
 */
public class PasswordFailedException extends Exception {
  public PasswordFailedException() {}
  public PasswordFailedException(String msg) {super(msg);}
  public PasswordFailedException(String message, Throwable cause) {super(message, cause);}
}
