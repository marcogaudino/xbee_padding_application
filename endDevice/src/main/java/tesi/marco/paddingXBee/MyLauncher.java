package tesi.marco.paddingXBee;

import java.awt.Container;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.sound.midi.Soundbank;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.models.XBeeMessage;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class MyLauncher {

	private MyPacket packet;
	private MyPacket packet1;
	private MyPacket packet2;
	private MyLocalDevice local;
	private MyRemoteDevice remote;
	private SendPacket replay;

	private Scanner input = new Scanner(System.in);

	private String padding;

	private static int BAUD_RATE = 9600;

	private static XBee64BitAddress REMOTE = new XBee64BitAddress("0013A2004150F3E4");
	private static XBee64BitAddress LOCAL = new XBee64BitAddress("0013A20041073FB0");

	private static Logger LOGGER = Logger.getLogger(MyLauncher.class.getName());

	// private static final int SOURCE_END_POINT = 0xE8;
	// private static final int destEndpoint = 0xA1;
	// private static final int clusterID = 0x0006; // On/Off
	// private static final int PROFILE_ID = 0x0104; // Home Automation

	public void run() throws InterruptedException {
		int i = 0;
		local = new MyLocalDevice("/dev/ttyUSB0", BAUD_RATE);
		local.readInfo();

		remote = new MyRemoteDevice(local, REMOTE);

		// packet = new MyPacket(srcEndPnt, destEndPnt, clrID, prfID);
		// packet = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
		// packet1 = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
		// packet2 = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);

		// String message = "OK";
		// String message1 = "Association_request";
		// String message2 = "Association_response_Unsuccessful";
		// packet.setPayload(message);
		// packet1.setPayload(message1);
		// packet2.setPayload(message2);

		replay = new SendPacket(local, remote); //OBJECT TO SEND PACKETS
		while (true) {
			System.out.println("Would you like to send or receive data?\n 0:send\n 1:receive\n 2:Join");

			int command = input.nextInt();

			if (command == 0) {
				for (int j = 0; j < 10; j++) {
					XBeeMessage responseToPacket;
					boolean received;
					
					do {
						// packet = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
						// String message1 = "Association_request";
						// packet.setPayload(message1);
						// replay.sending(packet);
						sendMessage("Association_request");
						received = false;
						local.openCommunication(); //listening to the answer

						responseToPacket = local.getZigBeeLocalDevice().readData();
						if (responseToPacket != null) {
							received = true;
							if (responseToPacket.getDataString().contains("Successful")) {
								System.out.println("Data has been received: " + responseToPacket.getDataString());
								// int indexOfPadding = responseToPacket.getDataString().indexOf(".") + 1;
								// padding =
								// String.valueOf(responseToPacket.getDataString().charAt(indexOfPadding));
								setPadding(responseToPacket);
								System.out.println("Padding is: " + padding);
							} else if (responseToPacket.getDataString().contains("Unsuccessful")) {
								System.out.println("Data has been received: " + responseToPacket.getDataString());
							}
						}

					} while (responseToPacket == null || received == false);
				}
			} else if (command == 1) {
				XBeeMessage receivedPacket;
				if (padding == null) {
					join();
				}

				do {

					System.out.println("It is waiting for packet...");
					local.openCommunication();

					receivedPacket = local.getZigBeeLocalDevice().readData();

					if (receivedPacket != null) {
						System.out.println(receivedPacket.getDataString());
						setPadding(receivedPacket);
						System.out.println("Messsage has been received!!!! It is: "
								+ local.removePadding(receivedPacket, padding));

						// LOGGER.info("Messsage has been received!!!!");
						// LOGGER.info("The received packet is: "+local.removePadding(receivedPacket));
						// blikLed();

						sendMessage("WRONG");

					} // else {
						// blikLed();
						// }
				} while (receivedPacket == null);
			} else if (command == 2) {
				join();
			}
		}
	}

	public void join() {
		XBeeMessage receivedPacket;
		if (padding == null) {
			sendMessage("Join");

			local.openCommunication();

			do {
				receivedPacket = local.getZigBeeLocalDevice().readData();
				if (receivedPacket != null) {
					System.out.println("I'm join!");

					setPadding(receivedPacket);
					sendMessage("OK");
				}
			} while (receivedPacket == null);

		}
	}
	
	public void blikLed() {
		/*---------------------BLINK LED------------------------*/

		// create gpio controller
		// final GpioController gpio = GpioFactory.getInstance();
		// // provision gpio pin #01 as an output pin and turn on
		// final GpioPinDigitalOutput pin =
		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
		// // set shutdown state for this pin
		// pin.setShutdownOptions(true, PinState.LOW);
		//
		// System.out.println("--> GPIO state should be: ON");
		//
		// Thread.sleep(5000);
		// replay.sending(packet);
		// i++;
		// if (i <= 10) {
		// packet1 = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
		// String msg = "OK";
		// packet1.setPayload(msg);
		// replay.sending(packet1);
	}

	public void setPadding(XBeeMessage pck) {
		int indexOfPadding = pck.getDataString().indexOf(".") + 1;
		if (pck.getDataString().indexOf(".") != -1) {
			padding = String.valueOf(pck.getDataString().charAt(indexOfPadding));
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MyLauncher launcher = new MyLauncher();
		launcher.run();
		// waitEnter();

	}

	public void sendMessage(String msg) {
		MyPacket packet1 = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
		// String msg = "OK";
		packet1.setPayload(msg);
		replay.sending(packet1);
	}

	public static void waitEnter() {
		System.out.println("Press Enter to continue");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}

}
