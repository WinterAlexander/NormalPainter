package net.jumpai.util;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Consumer;

/**
 * Map that links consumers to the type of object they are accepting. The whole
 * TypeConsumerMap then acts as a consumer that can accept anything that is an
 * instance of the base type B.
 * <p>
 * Created on 2019-01-30.
 *
 * @author Alexander Winter
 */
public class TypeConsumerMap<B> implements Consumer<B>
{
	private Consumer<B> defaultConsumer = o -> {};
	private final ObjectMap<Class, Consumer> map = new ObjectMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public void accept(B o)
	{
		if(o == null)
			defaultConsumer.accept(null);
		else
			map.get(o.getClass(), defaultConsumer).accept(o);
	}

	public <T extends B> void set(Class<T> key, Consumer<T> consumer)
	{
		map.put(key, consumer);
	}

	public void setDefault(Consumer<B> defaultConsumer)
	{
		this.defaultConsumer = defaultConsumer;
	}
}
