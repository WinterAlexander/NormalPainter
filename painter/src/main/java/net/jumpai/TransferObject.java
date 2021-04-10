package net.jumpai;

/**
 * A TransferObject is an object which only purpose is to transfer data. The
 * implementation of a TransferObject should have no logic in it. Methods
 * offered by the implementation should all be linked to transfering data. All
 * fields should be public to fullfil this contract.
 * <p>
 * Created on 2018-08-06.
 *
 * @author Alexander Winter
 */
public interface TransferObject<T extends TransferObject<T>> extends CustomSerializable
{
	/**
	 * Sets the content of specified object into this one
	 * @param other object to dump data into this one
	 */
	void set(T other);
}
