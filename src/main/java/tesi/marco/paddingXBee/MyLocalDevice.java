package tesi.marco.paddingXBee;

import com.digi.xbee.api.ZigBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBeeMessage;

public class MyLocalDevice

{
	private String port;
	private int baudRate;

	private ZigBeeDevice zigBeeLocalDevice;

	public MyLocalDevice(String port, int bRate) {
		this.port = port;
		this.baudRate = bRate;

		zigBeeLocalDevice = new ZigBeeDevice(port, baudRate);

	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int bRate) {
		this.baudRate = bRate;
	}

	public ZigBeeDevice getZigBeeLocalDevice() {
		return zigBeeLocalDevice;
	}

	public void setZigBeeLocalDevice(ZigBeeDevice local) {
		this.zigBeeLocalDevice = local;
	}

	public void openCommunication() {
		if (!this.zigBeeLocalDevice.isOpen())
			try {
				this.zigBeeLocalDevice.open();
			} catch (XBeeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void closeCommunication() {
		this.zigBeeLocalDevice.close();
	}
	// }
	/*--------------------------------------------- For Receive DATA---------------------------------------------------------*/

	public String message(XBeeMessage message) { // print the message without deleting the padding

		String str = message.getDataString();

		return str;
	}

	public String removePadding(XBeeMessage message) { // print the message deleting the padding

		String str = message.getDataString();

		String mes = str.replace("\0", "");

		return mes;
	}

	public void readInfo() {
		try {
			openCommunication();
			this.getZigBeeLocalDevice().readDeviceInfo();
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeCommunication();
		}

	}

	public String read_Explicit_Data() {
		ExplicitXBeeMessage xbeemessage = this.zigBeeLocalDevice.readExplicitData();
		return xbeemessage.getData().toString();
	}
	
	public XBeeMessage readData() {
		this.openCommunication();
		return this.getZigBeeLocalDevice().readData();
	}
}
