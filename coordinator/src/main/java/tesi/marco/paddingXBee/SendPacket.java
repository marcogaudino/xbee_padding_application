package tesi.marco.paddingXBee;

import com.digi.xbee.api.exceptions.XBeeException;


public class SendPacket 
{
	private MyLocalDevice local;
	private MyRemoteDevice remote;

	private MyPacket packet;
	
	public SendPacket(MyLocalDevice lcl, MyRemoteDevice rmt) {
		this.local = lcl;
		this.remote = rmt;
	}

	public void sending(MyPacket pck){
		
		try {
			local.openCommunication();
			
			
//			System.out.println(pck.getMessage());
			
			local.getZigBeeLocalDevice().sendExplicitData(remote.getRemoteZigBeeDevice(), pck.getSourceEndPoint(),
					pck.getDestinationEndPoint(), pck.getClusterID(), pck.getProfileID(), pck.getPayload());
			this.savePaylod(pck);
			
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			local.closeCommunication();
		}
		
		
	}
	

	public void sendingPlain(MyPacket pck) {

		try {
			local.openCommunication();

			System.out.println(pck.getMessage());

			local.getZigBeeLocalDevice().sendExplicitData(remote.getRemoteZigBeeDevice(), pck.getSourceEndPoint(),
					pck.getDestinationEndPoint(), pck.getClusterID(), pck.getProfileID(), pck.getPayload());
			this.savePaylod(pck);

		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			local.closeCommunication();
		}

	}

	public MyPacket getPakcet() {
		return this.packet;
	}

	public void savePaylod(MyPacket pck) {
		this.packet = pck;
	}
	
	public void setLocalDevice(MyLocalDevice lcl) {
		this.local = lcl;
	}
	
	public void setRemoteDevice(MyRemoteDevice rmt) {
		this.remote = rmt;
	}
}
