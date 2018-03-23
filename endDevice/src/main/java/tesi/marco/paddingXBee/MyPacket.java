package tesi.marco.paddingXBee;

public class MyPacket {

	private int sourceEndPoint, destinationEndPoint, clusterID, profileID;

	public static String PADDING_CHAR = "\0";

	private byte[] payload;

	private String message;

	public MyPacket(int srcEndPnt, int destEndPnt, int clrID, int prfID) {
		this.sourceEndPoint = srcEndPnt;
		this.destinationEndPoint = destEndPnt;
		this.clusterID = clrID;
		this.profileID = prfID;

	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.setMessage(payload);
		this.payload = payload.getBytes();
	}
	//
	// public byte[] setPayload(String payload) {
	// this.setMessage(payload);
	// return this.payload = payload.getBytes();
	// }

	public int getSourceEndPoint() {
		return sourceEndPoint;
	}

	public void setSourceEndPoint(int sourceEndPoint) {
		this.sourceEndPoint = sourceEndPoint;
	}

	public int getDestinationEndPoint() {
		return destinationEndPoint;
	}

	public void setDestinationEndPoint(int destinationEndPoint) {
		this.destinationEndPoint = destinationEndPoint;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public int getProfileID() {
		return profileID;
	}

	public void setProfileID(int profileID) {
		this.profileID = profileID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
