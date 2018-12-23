
package zhaoyi.wordhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

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
	
	public static String join(Collection<String> col, String separator){
		return col.stream().collect(Collectors.joining(separator));
	}
}
