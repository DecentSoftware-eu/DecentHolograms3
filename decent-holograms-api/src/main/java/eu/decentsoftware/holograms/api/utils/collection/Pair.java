package eu.decentsoftware.holograms.api.utils.collection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<K, V> {

	private K key;
	private V value;

}