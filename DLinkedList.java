package estruturas;

public class DLinkedList<E> {
    private DNode<E> head;
    private DNode<E> tail;
    private int size;

    public DLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E first() {
        if (isEmpty()) {
            throw new IllegalStateException("Lista vazia");
        }
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) {
            throw new IllegalStateException("Lista vazia");
        }
        return tail.getElement();
    }

    public DNode<E> getHead() {
        return head;
    }

    public DNode<E> getTail() {
        return tail;
    }

    public void addFirst(E element) {
        DNode<E> novo = new DNode<>(element, null, head);
        if (isEmpty()) {
            tail = novo;
        } else {
            head.setPrev(novo);
        }
        head = novo;
        size++;
    }

    public void addLast(E element) {
        DNode<E> novo = new DNode<>(element, tail, null);
        if (isEmpty()) {
            head = novo;
        } else {
            tail.setNext(novo);
        }
        tail = novo;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Lista vazia");
        }
        E element = head.getElement();
        head = head.getNext();
        size--;
        if (isEmpty()) {
            tail = null;
        } else {
            head.setPrev(null);
        }
        return element;
    }

    public E removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("Lista vazia");
        }
        E element = tail.getElement();
        tail = tail.getPrev();
        size--;
        if (isEmpty()) {
            head = null;
        } else {
            tail.setNext(null);
        }
        return element;
    }

    public void removeNode(DNode<E> node) {
        if (node == null || isEmpty()) {
            return;
        }
        if (node == head) {
            removeFirst();
        } else if (node == tail) {
            removeLast();
        } else {
            DNode<E> anterior = node.getPrev();
            DNode<E> proximo = node.getNext();
            anterior.setNext(proximo);
            proximo.setPrev(anterior);
            size--;
        }
    }

    public void imprime() {
        DNode<E> atual = head;
        while (atual != null) {
            System.out.println(atual.getElement());
            atual = atual.getNext();
        }
    }
}