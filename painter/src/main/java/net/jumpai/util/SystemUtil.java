package net.jumpai.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Utility class for system related methods
 * <p>
 * Created on 2018-03-31.
 *
 * @author Alexander Winter
 */
public class SystemUtil
{
	public static boolean supportsCursorBlending()
	{
		return !System.getProperty("os.name").toUpperCase(Locale.ENGLISH).contains("WIN");
	}

	public static void sleepIfRequired(long millis)
	{
		if(millis > 0)
			sleep(millis);
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(InterruptedException ignored) {}
	}

	/**
	 * @see <a href="https://stackoverflow.com/a/32170974/4048657">See SO related answer</a>
	 */
	public static String getMacAddress() throws SocketException, UnknownHostException
	{
		InetAddress lanIp = null;

		String ipAddress;
		Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();

		while(net.hasMoreElements())
		{
			NetworkInterface element = net.nextElement();
			Enumeration<InetAddress> addresses = element.getInetAddresses();

			while(addresses.hasMoreElements() && element.getHardwareAddress() != null && element.getHardwareAddress().length > 0)
			{
				InetAddress ip = addresses.nextElement();
				if(ip instanceof Inet4Address)
				{
					if(ip.isSiteLocalAddress())
					{
						ipAddress = ip.getHostAddress();
						lanIp = InetAddress.getByName(ipAddress);
					}
				}
			}
		}

		if(lanIp == null)
			throw new UnknownHostException();

		NetworkInterface network = NetworkInterface.getByInetAddress(lanIp);

		if(network == null)
			throw new UnknownHostException();

		return toString(network.getHardwareAddress());
	}

	/**
	 * Takes a mac address as a byte array and creates a string that represents it
	 * @param macAddress address to transform to string
	 * @return mac address as string
	 */
	public static String toString(byte[] macAddress)
	{
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < macAddress.length; i++)
			sb.append(String.format("%02X%s", macAddress[i], (i < macAddress.length - 1) ? "-" : ""));

		return sb.toString();
	}
}
