kapre-create
============

kapre-create is my attempt to create a Command-based API for the iRobot Create.

My testing device is a raspberry pi connected to the irobot create via the UART over USB.

How to Use
==========

1. Connect the serial usb cable from computer to robot.
2. Build the source code [ mvn clean package ]
3. Run the shell [ java -jar target/irobotcreate-1.0-SNAPSHOT-jar-with-dependencies.jar ]
4. Choose with serial port to use.

The Shell
=========

* The first command to be sent should be [ start ] followed by [ safe ] or [ full ]
* [ moveto 2000 100 ] moves the robot 2000mm forward with 100mm/sec velocity
* [ turn 180 50 ] Turn-around using 50mm/sec velocity
