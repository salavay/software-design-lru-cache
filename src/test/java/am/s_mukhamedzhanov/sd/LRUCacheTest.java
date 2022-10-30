package am.s_mukhamedzhanov.sd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LRUCacheTest {

    LRUCache<String, Integer> lruCache;

    @Before
    public void setUp() {
        lruCache = new LRUCache<>(3);
    }

    private static final Entity
            Entity_1 = new Entity("key1", 1),
            Entity_2 = new Entity("key2", 2),
            Entity_3 = new Entity("key3", 3),
            Entity_4 = new Entity("key4", 4);

    @Test
    public void singlePut() {
        put(Entity_1);
        Assert.assertEquals(1, lruCache.size());
        Assert.assertEquals(Entity_1.value, get(Entity_1));
    }

    @Test
    public void putTwo() {
        put(Entity_1);
        put(Entity_2);

        Assert.assertEquals(2, lruCache.size());
        Assert.assertEquals(Entity_1.value, get(Entity_1));
        Assert.assertEquals(Entity_2.value, get(Entity_2));
    }

    @Test
    public void deleteLastUsed() {
        put(Entity_1);
        put(Entity_2);
        put(Entity_3);
        put(Entity_4);

        Assert.assertEquals(3, lruCache.size());
        Assert.assertNull(get(Entity_1));
        Assert.assertEquals(Entity_2.value, get(Entity_2));
        Assert.assertEquals(Entity_3.value, get(Entity_3));
        Assert.assertEquals(Entity_4.value, get(Entity_4));
    }

    @Test
    public void deleteLastUsedWithGet() {
        put(Entity_1);
        put(Entity_2);
        put(Entity_3); // 3 2 1

        get(Entity_1); // 1 3 2

        put(Entity_4); // 4 1 3

        Assert.assertEquals(3, lruCache.size());
        Assert.assertNull(get(Entity_2));
        Assert.assertEquals(Entity_1.value, get(Entity_1));
        Assert.assertEquals(Entity_3.value, get(Entity_3));
        Assert.assertEquals(Entity_4.value, get(Entity_4));

    }

    private void put(Entity entity) {
        lruCache.put(entity.key, entity.value);
    }

    private Integer get(Entity entity) {
        return lruCache.get(entity.key);
    }

    private static class Entity {
        String key;
        Integer value;

        public Entity(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }
}