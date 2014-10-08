package com.kapre.irobot.shell;

public class InterpreterException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InterpreterException(Throwable e) {
    super("Interpreter choked!", e);
  }
}
