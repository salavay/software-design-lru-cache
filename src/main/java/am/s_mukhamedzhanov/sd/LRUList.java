package am.s_mukhamedzhanov.sd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LRUList<K> {

    Node<K> head, tail;
    int size;

    public LRUList() {
        head = new Node<>(null);
        size = 0;
    }

    public static class Node<K> {
        K key;

        Node<K> prev;
        Node<K> next;

        public Node(K key) {
            this.key = key;
        }


        public Node() {

        }

        @Override
        public String toString() {
            return key.toString();
        }
    }

    Node<K> addLast(K key) {
        assert key != null;
        size++;
        if (tail == null) {
            head.key = key;
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
            return head;
        } else {
            tail.key = key;
            tail.next = new Node<>();
            Node<K> prev = tail;
            tail = tail.next;
            tail.prev = prev;
            return prev;
        }
    }

    Node<K> addFirst(K key) {
        assert key != null;
        size++;
        if (head.key == null) {
            head.key = key;
            head.next = tail = new Node<>();
            tail.prev = head;
        } else {
            Node<K> newHead = new Node<>(key);
            head.prev = newHead;
            newHead.next = head;
            head = newHead;
        }
        return head;
    }

    K deleteLast() {
        assert size > 0;
        K lastKey = tail.prev.key;
        deleteNode(tail.prev);
        return lastKey;
    }

    void propagateNode(Node<K> node) {
        deleteNode(node);
        size++;
        head.prev = node;
        node.next = head;
        head = node;
        head.prev = null;
    }

    void deleteNode(Node<K> node) {
        assert size > 0;
        assert node.prev != null || node.next != null;
        assert node.key != null;
        size--;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    List<Node<K>> toList() {
        List<Node<K>> result = new ArrayList<>();
        Node<K> cur = head;
        while (cur.key != null) {
            result.add(cur);
            cur = cur.next;
        }
        return result;
    }

    List<Node<K>> toListByPrevs() {
        List<Node<K>> result = new ArrayList<>();
        Node<K> cur = tail.prev;
        while (cur != null && cur.key != null) {
            result.add(cur);
            cur = cur.prev;
        }
        Collections.reverse(result);
        return result;
    }
}
