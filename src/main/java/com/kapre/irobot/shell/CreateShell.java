package com.kapre.irobot.shell;

import java.io.Console;

import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.kapre.irobot.Command;
import com.kapre.irobot.Executor;
import com.kapre.irobot.enums.OpCode;
import com.kapre.irobot.impl.BaudCommand;
import com.kapre.irobot.impl.DemoCommand;
import com.kapre.irobot.impl.DigitalOutputCommand;
import com.kapre.irobot.impl.DriveCommand;
import com.kapre.irobot.impl.LedCommand;
import com.kapre.irobot.impl.LowSideDriver;
import com.kapre.irobot.impl.MoveToCommand;
import com.kapre.irobot.impl.OpCommand;
import com.kapre.irobot.impl.SensorCommand;
import com.kapre.irobot.impl.TurnCommand;
import com.kapre.irobot.impl.WaitCommand;

public class CreateShell {

  private static final int DEFAULT_TIMEOUT = 10;

  public static void main(String[] args) {
    Console console = System.console();

    Optional<String> serialPort = getSerialPort(console);

    if (!serialPort.isPresent()) {
      // TODO : bail out!
      console.printf("No serial port detected.\n");
    }

    Executor executor = new Executor();
    CommandInterpreter<Command> commandInterpreter = buildInterpreter();
    try {
      executor.open(serialPort.get());
      while (true) {
        String cmd = console.readLine("> ");
        if (cmd.trim().equalsIgnoreCase("quit")) {
          break;
        }

        try {
          Optional<? extends Command> result = commandInterpreter
              .interpret(cmd);
          if (!result.isPresent()) {
            console.format("'%s' is not a valid command\n", cmd);
            continue;
          }

          Command command = result.get();
          executor.send(command);
          if (command.isExpectResponse()) {
            try {
              byte[] response = executor.recv(command.getLengthResponse(),
                  DEFAULT_TIMEOUT);
              if (response != null) {
                console.format("response: %s", command.getResponse(response)
                    .toString());
              }
            } catch (SerialPortTimeoutException e) {
              console.format("No response. Expecting response of length %d\n",
                  command.getLengthResponse());
            }
          } else {
            console.format("Command successful.\n");
          }
        } catch (InterpreterException e1) {
          console.format("Exception : \n");
          e1.printStackTrace(console.writer());
          console.format("\n");
        }
      }

    } catch (SerialPortException e) {
      // TODO : handle
    } finally {

    }
  }

  public static CommandInterpreter<Command> buildInterpreter() {
    CommandInterpreter<Command> interpreter = new CommandInterpreter<Command>();
    interpreter.add("start", null, OpCommand.class,
        Lists.newArrayList(OpCode.START));
    interpreter.add("safe", null, OpCommand.class,
        Lists.newArrayList(OpCode.SAFE));
    interpreter.add("full", null, OpCommand.class,
        Lists.newArrayList(OpCode.FULL));
    interpreter.add("baud", "<i>", BaudCommand.class);
    interpreter.add("demo", "<i>", DemoCommand.class);
    interpreter.add("drive", "<s> <s>", DriveCommand.class,
        Lists.newArrayList(OpCode.DRIVE));
    interpreter.add("drivedirect", "<s> <s>", DriveCommand.class,
        Lists.newArrayList(OpCode.DRIVE_DIRECT));
    interpreter.add("led", "<f> <f> <i> <i>", LedCommand.class);
    interpreter.add("dout", "<f> <f> <f>", DigitalOutputCommand.class);
    interpreter.add("lowside", "<f> <f> <f>", LowSideDriver.class);
    interpreter.add("lowsidepwm", "<i> <i> <i>", LowSideDriver.class);
    interpreter.add("sendir", "<i>", OpCommand.class,
        Lists.newArrayList(OpCode.SEND_IR));
    interpreter.add("waittime", "<i>", OpCommand.class,
        Lists.newArrayList(OpCode.WAIT_TIME));
    interpreter.add("waitdistance", "<s>", WaitCommand.class,
        Lists.newArrayList(OpCode.WAIT_DISTANCE));
    interpreter.add("waitangle", "<s>", WaitCommand.class,
        Lists.newArrayList(OpCode.WAIT_ANGLE));
    interpreter.add("moveto", "<s> <s>", MoveToCommand.class);
    interpreter.add("turn", "<s> <s>", TurnCommand.class);
    interpreter.add("sensor", "<i>", SensorCommand.class);
    return interpreter;
  }

  public static Optional<String> getSerialPort(Console console) {
    String[] portNames = SerialPortList.getPortNames();
    if (portNames.length > 0) {
      for (int i = 0; i < portNames.length; i++) {
        console.format("[Port No] %d ==> %s\n", i, portNames[i]);
      }
      while (true) {
        try {
          int choice = Integer.valueOf(console.readLine("Enter Port No:"));
          if (choice >= 0 && choice < portNames.length) {
            return Optional.of(portNames[choice]);
          }
        } catch (NumberFormatException e) {
        }
      }
    }

    return Optional.absent();
  }
}
