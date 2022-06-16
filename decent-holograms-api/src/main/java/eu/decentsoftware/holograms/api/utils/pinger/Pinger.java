package eu.decentsoftware.holograms.api.utils.pinger;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class is used to ping a server.
 */
public class Pinger {

	private static final Gson GSON = new Gson();
	private final InetSocketAddress address;
	private final int timeout;

	/*
	 *	Constructors
	 */

	/**
	 * Creates a new Pinger instance.
	 *
	 * @param address The address to ping.
	 * @param timeout The timeout in milliseconds.
	 */
	public Pinger(@NotNull InetSocketAddress address, int timeout) {
		this.address = address;
		this.timeout = timeout;
	}

	/*
	 *	General Methods
	 */

	/**
	 * Pings the server.
	 *
	 * @return The ping result.
	 * @throws IOException If an I/O error occurs.
	 */
	@SuppressWarnings("unused")
	public synchronized PingerResponse fetchData() throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(this.timeout);
		socket.connect(this.address, this.timeout);

		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		InputStream inputStream = socket.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream handshake = new DataOutputStream(b);
		handshake.writeByte(0);
		writeVarInt(handshake, 4);
		writeVarInt(handshake, this.address.getHostString().length());
		handshake.writeBytes(this.address.getHostString());
		handshake.writeShort(this.address.getPort());
		writeVarInt(handshake, 1);
		writeVarInt(dataOutputStream, b.size());
		dataOutputStream.write(b.toByteArray());
		dataOutputStream.writeByte(1);
		dataOutputStream.writeByte(0);

		DataInputStream dataInputStream = new DataInputStream(inputStream);
		int size = readVarInt(dataInputStream);
		int id = readVarInt(dataInputStream);
		if (id == -1) throw new IOException("Premature end of stream.");
		if (id != 0) throw new IOException("Invalid packetID");

		int length = readVarInt(dataInputStream);
		if (length == -1) throw new IOException("Premature end of stream.");
		if (length == 0) throw new IOException("Invalid string length.");
		byte[] in = new byte[length];
		dataInputStream.readFully(in);
		String json = new String(in);

		long now = System.currentTimeMillis();
		dataOutputStream.writeByte(9);
		dataOutputStream.writeByte(1);
		dataOutputStream.writeLong(now);

		readVarInt(dataInputStream);
		id = readVarInt(dataInputStream);
		if (id == -1) throw new IOException("Premature end of stream.");
		if (id != 1) throw new IOException("Invalid packetID");

		socket.close();
		outputStream.close();
		dataOutputStream.close();
		inputStreamReader.close();
		inputStreamReader.close();
		handshake.close();
		b.close();

		return GSON.fromJson(json, PingerResponse.class);
	}
	
	private int readVarInt(DataInputStream in) throws IOException {
		int k, i = 0;
		int j = 0;
		do {
			k  = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
		} while ((k & 0x80) == 128);
		return i;
	}

	private void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}
			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

}
