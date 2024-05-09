package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> prev;


        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<Task> head;
    private Node<Task> tail;

    Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        historyMap.put(newNode.data.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            historyList.add(current.data);
            current = current.next;
        }
        return historyList;
    }

    private void removeNode(Node<Task> node) {
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        historyMap.remove(node.data.getId());
    }


    @Override
    public void add(Task task) {
        if (historyMap.get(task.getId()) == null) {
            linkLast(task);
        } else {
            removeNode(historyMap.get(task.getId()));
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
