package tesi.marco.paddingXBee;

import java.util.Enumeration;
import java.util.Scanner;

import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.models.XBeeMessage;

public class MyLauncher {
	private MyPacket packet;
	private MyPacket packet1;
	private MyLocalDevice local;
	private MyRemoteDevice remote;
	private MyRemoteDevice remote1;
	private SendPacket channel;

	private Scanner input = new Scanner(System.in);

	private int MAX_DEVICES = 5; //max no. devices that can join the network simultaneously
	private int acceptedDevice = 0;

	private String padding;

	private static int BAUD_RATE = 9600;

	private static XBee64BitAddress LOCAL = new XBee64BitAddress("0013A2004150F3E4");
	private static XBee64BitAddress REMOTE = new XBee64BitAddress("0013A20041073FB0");

	public void run() throws InterruptedException {

		/* Creation of local and remote device to beginning the communication*/
		local = new MyLocalDevice("/dev/ttyUSB0", BAUD_RATE);
		remote = new MyRemoteDevice(local, REMOTE);
		
		while (true) {
			
			System.out.println("Would you like to send or receive data?\n 0:send\n 1:receive\n 2:send padding\n");
			int a = input.nextInt();
			
			XBeeMessage receiveData = null;
			if (a == 0) {
				if (padding == null) {
					readPadding();
				}
				packet = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
				String message = "ON";
				packet.setPayload(message, padding);
				channel = new SendPacket(local, remote);
				channel.sending(packet);
			} else if (a == 1) {
				while (true) {

					readPadding(); //send the padding in the Association_response
					for (int i = 0; i < 10; i++) {
						do {
							receiveData = local.readData();
							waitingForResponse(receiveData);

						} while (receiveData == null);

					}
					break;
				}
			} else if (a == 2) {
				readPadding();
				do {
					receiveData = local.readData();
					waitingForResponse(receiveData);
				} while (receiveData == null);
			}
		}
	}

	//implementation of randomyc padding
	public void readPadding() { 
		if (padding == null) {
			System.out.println("Please, insert the padding?");
			padding = input.next();
		}
	}

	public void sendPlainMessage(String msg) {
		packet = new MyPacket(0xE8, 0xA1, 0x0006, 0x0104);
		packet.setPayloadPlain(msg);
		channel = new SendPacket(local, remote);
		channel.sendingPlain(packet);
	}

	public void waitingForResponse(XBeeMessage receiveData) {
		if (receiveData != null) {
			System.out.println("Data has been received: " + receiveData.getDataString());
			if (receiveData.getDataString().equals("Association_request")) {
				if (acceptedDevice < MAX_DEVICES) {
					sendPlainMessage("Assocition_response, Successful." + padding);
					acceptedDevice++;
					// }
				} else {
					sendPlainMessage("Assocition_response, Unsuccessful");
				}
			} else if (receiveData.getDataString().equals("OK")) {
				System.out.println("Command has been executed");
			} else if (receiveData.getDataString().equals("Join")) {
				if (acceptedDevice < MAX_DEVICES) {
					sendPlainMessage("Assocition_response, Successful." + padding);
					acceptedDevice++;
				}

			}
		}

	}

	public static void main(String[] args) throws InterruptedException {
		MyLauncher launcher = new MyLauncher();
		launcher.run();
	}

	public static void waitEnter() {
		System.out.println("Press Enter to continue");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}
}
