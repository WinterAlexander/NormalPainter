package net.jumpai.util.io;

import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.SocketHints;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Util class for sockets
 * <p>
 * Created on 2018-01-18.
 *
 * @author Alexander Winter
 */
public class SocketUtil
{
	public static void applyHints(Socket socket, SocketHints hints) throws SocketException
	{
		socket.setPerformancePreferences(
				hints.performancePrefConnectionTime,
				hints.performancePrefLatency,
				hints.performancePrefBandwidth);
		socket.setTrafficClass(hints.trafficClass);
		socket.setTcpNoDelay(hints.tcpNoDelay);
		socket.setKeepAlive(hints.keepAlive);
		socket.setSendBufferSize(hints.sendBufferSize);
		socket.setReceiveBufferSize(hints.receiveBufferSize);
		socket.setSoLinger(hints.linger, hints.lingerDuration);
		socket.setSoTimeout(hints.socketTimeout);
	}

	public static void applyHints(ServerSocket serverSocket, ServerSocketHints hints) throws SocketException
	{
		serverSocket.setPerformancePreferences(
				hints.performancePrefConnectionTime,
				hints.performancePrefLatency,
				hints.performancePrefBandwidth);
		serverSocket.setReuseAddress(hints.reuseAddress);
		serverSocket.setSoTimeout(hints.acceptTimeout);
		serverSocket.setReceiveBufferSize(hints.receiveBufferSize);
	}
}
