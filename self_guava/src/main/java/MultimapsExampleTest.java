/**
 * @author kxh
 * @description
 * @date 20210629_13:58
 */

import com.google.common.collect.*;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/***************************************
 * @author:Alex Wang
 * @Date:2018/1/14
 * QQ: 532500648
 * QQ群:463962286
 ***************************************/
public class MultimapsExampleTest {
    public static void main(String[] args) {
        LinkedListMultimap<String, String> multipleMap = LinkedListMultimap.create();
        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("1", "1");
        hashMap.put("1", "2");

        multipleMap.put("2", "1");
        multipleMap.put("3", "2");
        multipleMap.put("1", "2");
        multipleMap.put("1", "1");
        //返回的值是一个list
        System.out.println(multipleMap.get("1"));

        Multiset<String> keys = multipleMap.keys();

        System.out.println(keys);
    }
}