package com.kapre.irobot;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class Executor {
  private SerialPort serialPort;

  public void open(String address) throws SerialPortException {
    serialPort = new SerialPort(address);
    serialPort.openPort();
    serialPort.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8,
        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

  }

  public void close() throws SerialPortException {
    serialPort.closePort();
  }

  public void send(Command command) throws SerialPortException {
    System.out.println(CreateUtils.toHexString(command.getCommand()));
    serialPort.writeBytes(command.getCommand());
  }

  public byte[] recv() throws SerialPortException {
    return serialPort.readBytes();
  }

  public byte[] recv(int numBytes) throws SerialPortException {
    return serialPort.readBytes(numBytes);
  }

  public byte[] recv(int numBytes, int timeout) throws SerialPortException,
      SerialPortTimeoutException {
    return serialPort.readBytes(numBytes, timeout);
  }
}
