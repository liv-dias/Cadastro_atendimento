package estruturas;

public class SLinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public SLinkedList() {
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
        if (isEmpty()) throw new IllegalStateException("Lista vazia");
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) throw new IllegalStateException("Lista vazia");
        return tail.getElement();
    }

    public Node<E> head() {
        return head;
    }

    public void addFirst(E element) {
        Node<E> novo = new Node<>(element, head);
        head = novo;
        if (size == 0) tail = novo;
        size++;
    }

    public void addLast(E element) {
        Node<E> novo = new Node<>(element, null);
        if (isEmpty()) {
            head = novo;
        } else {
            tail.setNext(novo);
        }
        tail = novo;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) throw new IllegalStateException("Lista vazia");
        E element = head.getElement();
        head = head.getNext();
        size--;
        if (isEmpty()) tail = null;
        return element;
    }

    /**
     * Remove a primeira ocorrência do elemento (comparando com equals).
     * Cobre: lista vazia, um elemento, primeiro, intermediário, último
     * e elemento inexistente. Retorna true se removeu.
     */
    public boolean remove(E element) {
        Node<E> anterior = null;
        Node<E> atual = head;

        while (atual != null) {
            if (atual.getElement().equals(element)) {
                if (anterior == null) {
                    head = atual.getNext();          // remove o primeiro
                } else {
                    anterior.setNext(atual.getNext()); // remove do meio ou do fim
                }
                if (atual == tail) {
                    tail = anterior;                 // removeu o último
                }
                size--;
                return true;
            }
            anterior = atual;
            atual = atual.getNext();
        }
        return false;                                // não encontrou
    }
}