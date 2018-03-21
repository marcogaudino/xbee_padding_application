package tesi.marco.paddingXBee;

import java.util.Random;

public class MyPacket {
	private int sourceEndPoint, destinationEndPoint, clusterID, profileID;

	public String PADDING_CHAR;
//	public String PADDING_CHAR_HEX;
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

	// Il payload e' di tipo string, lo passo al metodo addPaddings per aggingerci
	// il padding e successivamente
	// lo trasformo in array di byte
	public void setPayload(String payload, String padding) {
		this.setMessage(payload);
		PADDING_CHAR = padding;
		this.payload = this.addPaddings(payload, false).getBytes();
		// this.payload = payload.getBytes(); // SEND PAYLOAD WITHOUT PADDING (for the
		// association requests)
		// this.payload = hexStringToByteArray("f6c007f4e8402bbfd11088");
		// this.payload =
		// hexStringToByteArray(addPaddings("f6c007f4e8402bbfd11088",true));
	}

	public void setPayloadPlain(String payload) {
		this.setMessage(payload);
		// this.payload = this.addPaddings(payload,false).getBytes();
		this.payload = payload.getBytes(); // SEND PAYLOAD WITHOUT PADDING (for the association requests)
		// this.payload = hexStringToByteArray("f6c007f4e8402bbfd11088");
		// this.payload =
		// hexStringToByteArray(addPaddings("f6c007f4e8402bbfd11088",true));
	}


	public byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

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

//	private String addPaddingToHex(String bytes) {
//		return addPaddings(bytes, true);
//	}

	private String addPaddings(String message, boolean isHex) {
		StringBuilder sb = new StringBuilder();

		int numberOfPadding = decideNumberOfPadding(message);

//		String paddingCharacter = "";
//		if (isHex) {
//			paddingCharacter = PADDING_CHAR_HEX;
//		} else {
//			paddingCharacter = PADDING_CHAR;
//		}

		// sb.append(numberOfPadding+" ");
		for (int i = 0; i < numberOfPadding; i++) {
			// \0 ->Nothing
			sb.append(PADDING_CHAR);
		}

		sb.append(message); // add ON to the new string

		for (int i = 0; i < numberOfPadding; i++) {
			sb.append(PADDING_CHAR);
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	private int decideNumberOfPadding(String str) {
		Random r = new Random();
		int orderOfPadding = r.nextInt(8);
		System.out.println("order of Padding is: " + orderOfPadding);
		int numberOfPadding = -1;

		switch (orderOfPadding) {
		case 1:
			numberOfPadding = (13 - str.length()) / 2; // payload packet 50
			break;

		case 2:
			numberOfPadding = (12 - str.length()) / 2; // payload packet 49
			break;

		case 3:
			numberOfPadding = (15 - str.length()) / 2; // payload packet 52
			break;

		case 4:
			numberOfPadding = (17 - str.length()) / 2; // payload packet 54
			break;

		case 5:
			numberOfPadding = (20 - str.length()) / 2; // payload packet 57
			break;

		case 6:
			numberOfPadding = (24 - str.length()) / 2; // payload packet 61
			break;

		case 7:
			numberOfPadding = (89 - str.length()) / 2; // payload packet 126
			break;

		case 0:
			numberOfPadding = (11 - str.length()) / 2; // payload packet 48
			break;

		default:
			numberOfPadding = (13 - str.length()) / 2;
		}
		return numberOfPadding;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
