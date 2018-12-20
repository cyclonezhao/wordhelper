
 package zhaoyi.wordhelper;
 
 import java.util.HashSet;
 import org.junit.Assert;
 import org.junit.Test;
 import zhaoyi.wordhelper.Util;
 
 
 public class UtilTest {
    @Test
    public void testKRandom() {
       byte k = 7;
       for(int i = 0; i < 10; ++i) {
          int val = Util.kRandom(k);
 
          if(val < 0 || val >= k) {
             Assert.fail("发现大于k的数");
          }      }
 
    }
 
    @Test
    public void testNKRandom() {
       byte n = 5;
       byte k = 7;      int i;      int[] arr;      int len;      int j;      int arg7;
       for(i = 0; i < 10; ++i) {
          arr = Util.nkRandom(n, k, false);
          len = arr.length;
          Assert.assertTrue("结果数量和n不一致", n == len);         int[] arg8 = arr;         arg7 = arr.length;
          for(j = 0; j < arg7; ++j) {            int s = arg8[j];
             if(s < 0 || s >= k) {
                Assert.fail("发现大于k的数");
             }
          }
       }
 
       for(i = 0; i < 10; ++i) {
          arr = Util.nkRandom(n, k, true);
          System.out.println(this.join(arr));
          len = arr.length;
          Assert.assertTrue("结果数量和n不一致", n == len);
 
          HashSet arg10 = new HashSet();         int[] arg9 = arr;         int arg11 = arr.length;
          for(arg7 = 0; arg7 < arg11; ++arg7) {            j = arg9[arg7];
             if(j < 0 || j >= k) {
                Assert.fail("发现大于k的数");
                if(arg10.contains(Integer.valueOf(j))) {
                   Assert.fail("发现重复的数");
                }
                arg10.add(Integer.valueOf(j));
             }         }
       }
 
    }
 
    private String join(int[] arr) {
       String str = "";      int[] arg5 = arr;      int arg4 = arr.length;
       for(int arg3 = 0; arg3 < arg4; ++arg3) {         int i = arg5[arg3];
          str = str + i + ",";
       }
       return str;
    }
 }
