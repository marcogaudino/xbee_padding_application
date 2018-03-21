package tesi.marco.paddingXBee;

import com.digi.xbee.api.RemoteZigBeeDevice;
import com.digi.xbee.api.models.XBee64BitAddress;


public class MyRemoteDevice 
{
	private MyLocalDevice local;
	private XBee64BitAddress address;
	
	private RemoteZigBeeDevice remote;
	
	public MyRemoteDevice(MyLocalDevice local, XBee64BitAddress addr){
		this.setAddress(addr);
		this.setMyLocal(local);
		this.remote = new RemoteZigBeeDevice(this.local.getZigBeeLocalDevice(), addr);
	}

//	public MyLocalDevice getLocal() {
//		return local;
//	}
//
	public void setMyLocal(MyLocalDevice local) {
		this.local = local;
	}

	public XBee64BitAddress getAddress() {
		return address;
	}

	public void setAddress(XBee64BitAddress address) {
		this.address = address;
	}

	public RemoteZigBeeDevice getRemoteZigBeeDevice() {
		return remote;
	}
}
