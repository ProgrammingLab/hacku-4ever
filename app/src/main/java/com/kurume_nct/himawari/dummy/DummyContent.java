package com.kurume_nct.himawari.dummy;

import com.kurume_nct.himawari.StoreData;
import com.kurume_nct.himawari.WayTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<StoreData> DATA_ITEMS = new ArrayList<StoreData>();
    public static final List<WayTime> TIME_ITEMS = new ArrayList<WayTime>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        StoreData a = new StoreData();
        a.setStoreName("現在地");
        DATA_ITEMS.add(a);
        for (int i = 1; i < COUNT - 1; i++) {
            List<String> types = new ArrayList<String>(Arrays.asList("book_store"));
            StoreData data = new StoreData();
            data.setStoreName("STORE NAME");
            data.setTypes(types);
            DATA_ITEMS.add(data);
        }
        StoreData b = new StoreData();
        b.setStoreName("目的地");
        DATA_ITEMS.add(b);

        for (int i = 0; i < COUNT - 1; i++) {
            WayTime data = new WayTime(10, 500);
            TIME_ITEMS.add(data);
        }
    }
}
