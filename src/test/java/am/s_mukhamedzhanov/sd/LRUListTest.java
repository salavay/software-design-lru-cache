package am.s_mukhamedzhanov.sd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LRUListTest {

    LRUList<String> list;
    int nodeId;

    @Before
    public void setUp() {
        list = new LRUList<>();
        nodeId = 0;
    }

    @Test
    public void addOne() {
        list.addLast("key1");
        this.assertEqualsWithListByKeys(List.of("key1"));
    }

    @Test
    public void addTwo() {
        list.addLast("key1");
        list.addLast("key2");
        this.assertEqualsWithListByKeys(List.of("key1", "key2"));
    }

    @Test
    public void deleteHead() {
        List<LRUList.Node<String>> nodes = addN(4);

        list.deleteNode(nodes.get(0));
        assertEqualsWithList(nodes.subList(1, 4));
    }

    @Test
    public void deleteTail() {
        List<LRUList.Node<String>> nodes = addN(4);

        list.deleteNode(nodes.get(3));
        assertEqualsWithList(nodes.subList(0, 3));
    }

    @Test
    public void deleteSecond() {
        List<LRUList.Node<String>> nodes = addN(4);
        list.deleteNode(nodes.get(1));

        assertEqualsWithList(List.of(
                nodes.get(0),
                nodes.get(2),
                nodes.get(3)));
    }

    @Test
    public void deleteThird() {
        List<LRUList.Node<String>> nodes = addN(4);
        list.deleteNode(nodes.get(2));

        assertEqualsWithList(List.of(
                nodes.get(0),
                nodes.get(1),
                nodes.get(3)
        ));
    }

    @Test
    public void propagateWithTwo() {
        List<LRUList.Node<String>> nodes = addN(2);
        

        list.propagateNode(nodes.get(1));
        assertEqualsWithList(List.of(nodes.get(1), nodes.get(0)));

        list.propagateNode(nodes.get(1));
        assertEqualsWithList(List.of(nodes.get(1), nodes.get(0)));

        list.propagateNode(nodes.get(0));
        assertEqualsWithList(List.of(nodes.get(0), nodes.get(1)));
    }

    @Test
    public void propagateWithThree() {
        List<LRUList.Node<String>> nodes = addN(4); // 0 1 2 3

        list.propagateNode(nodes.get(3)); // 3 0 1 2
        assertEqualsWithList(List.of(
                nodes.get(3),
                nodes.get(0),
                nodes.get(1),
                nodes.get(2)
        ));


        list.propagateNode(nodes.get(1)); // 1 3 0 2
        assertEqualsWithList(List.of(
                nodes.get(1),
                nodes.get(3),
                nodes.get(0),
                nodes.get(2)
        ));

        list.propagateNode(nodes.get(2)); // 2 1 3 0
        assertEqualsWithList(List.of(
                nodes.get(2),
                nodes.get(1),
                nodes.get(3),
                nodes.get(0)
        ));

    }

    @Test
    public void deleteLastHead() {
        list.addLast("key1");
        list.deleteLast();

        Assert.assertEquals(0, list.size);
    }

    @Test
    public void deleteLast() {
        List<LRUList.Node<String>> nodes = addN(5);
        list.deleteLast();

        assertEqualsWithList(nodes.subList(0, 4));
    }

    @Test
    public void deletionsThenAdds() {
        List<LRUList.Node<String>> nodes = addN(5); // 0 1 2 3 4

        list.deleteLast();
        assertEqualsWithList(nodes.subList(0, 4));
        list.deleteLast();
        assertEqualsWithList(nodes.subList(0, 3)); // 0 1 2

        list.deleteNode(nodes.get(0));
        assertEqualsWithList(nodes.subList(1, 3)); // 1 2

        list.deleteNode(nodes.get(1));
        assertEqualsWithList(List.of(nodes.get(2))); // 2

        list.deleteLast();
        assertEqualsWithList(List.of()); // empty

        nodes = addN(5);
        assertEqualsWithList(nodes);
    }

    @Test
    public void assertionsTest() {
        Assert.assertThrows(AssertionError.class, () -> list.deleteLast());

        Assert.assertThrows(AssertionError.class, () -> list.deleteNode(null));

        Assert.assertThrows(AssertionError.class, () -> list.propagateNode(null));

        LRUList.Node<String> randomNode = new LRUList.Node<>();
        Assert.assertThrows(AssertionError.class, () -> list.propagateNode(randomNode));

        Assert.assertThrows(AssertionError.class, () -> list.deleteNode(randomNode));

        Assert.assertThrows(AssertionError.class, () -> list.addLast(null));

        Assert.assertThrows(AssertionError.class, () -> list.addFirst(null));
    }

    private List<LRUList.Node<String>> addN(int n) {
        List<LRUList.Node<String>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(list.addLast("key" + nodeId++));
        }
        return result;
    }

    private void assertEqualsWithList(List<LRUList.Node<String>> expected) {
        Assert.assertEquals(expected.size(), list.size);
        Assert.assertEquals(expected, list.toList());
        Assert.assertEquals(expected, list.toListByPrevs());
    }

    private void assertEqualsWithListByKeys(List<String> expected) {
        Assert.assertEquals(expected.size(), list.size);
        Assert.assertEquals(expected, list.toList()
                .stream().map(n -> n.key).toList());
        Assert.assertEquals(expected, list.toListByPrevs()
                .stream().map(n -> n.key).toList());
    }
}