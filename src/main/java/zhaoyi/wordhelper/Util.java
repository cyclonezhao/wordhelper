
package zhaoyi.wordhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Util {
	public static int kRandom(int k) {
		double rawRandom = Math.random();
		rawRandom = (double) k * rawRandom;
		return Double.valueOf(Math.floor(rawRandom)).intValue();
	}

	public static int[] nkRandom(int n, int k, boolean distinct) {
		Collection<Integer> col = null;
		col = distinct ? new HashSet() : new ArrayList();
		while (col.size() < n) {
			int val = Util.kRandom(k);
			col.add(val);
		}
		return col.stream().mapToInt(Integer::valueOf).toArray();
	}
}
