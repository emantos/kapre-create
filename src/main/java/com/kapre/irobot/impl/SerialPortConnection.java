package com.kapre.irobot.impl;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import com.kapre.irobot.Connection;

public class SerialPortConnection implements Connection {

  private SerialPort serialPort;
  private int recvTimeout;

  public SerialPortConnection(String portAddress) {
    serialPort = new SerialPort(portAddress);
  }

  public void open() {
    try {
      serialPort.openPort();
      serialPort.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8,
          SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    } catch (SerialPortException e) {
      throw new RuntimeException("Error opening serial port", e);
    }
  }

  public void send(byte[] bytes) {
    try {
      serialPort.writeBytes(bytes);
    } catch (SerialPortException e) {
      throw new RuntimeException("Error sending through serial port", e);
    }
  }

  public byte[] recv(int length) {
    try {
      return serialPort.readBytes(length, recvTimeout);
    } catch (SerialPortTimeoutException e) {
      throw new RuntimeException("Not able to receive " + length
          + " amount of bytes from serial port within " + recvTimeout, e);
    } catch (SerialPortException e) {
      throw new RuntimeException("Error receiving through serial port", e);
    }
  }

  @Override
  public void close() {
    try {
      serialPort.closePort();
    } catch (SerialPortException e) {
      throw new RuntimeException("Error closing serial port", e);
    }
  }

}
