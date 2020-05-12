package edu.sigmachi.poker;

public class LoginAttemptResponseMsg {
  private boolean success;
  private String msg;
  
  public LoginAttemptResponseMsg(boolean success, String msg) {
    this.msg = msg;
    this.success = success;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public boolean getSuccess() {
    return this.success;
  }  
}
