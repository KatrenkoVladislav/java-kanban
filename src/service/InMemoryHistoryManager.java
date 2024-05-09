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

    @Override
    public void add(Task task) {
        if (task == null){
            return;
        }
        if (history.size() == MAX_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) history;
    }
}
