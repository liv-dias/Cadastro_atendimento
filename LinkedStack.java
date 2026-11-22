package estruturas;

public class LinkedStack<E> {

    private SLinkedList<E> elements;

    public LinkedStack() {
        elements = new SLinkedList<>();
    }

    public void push(E element) {
        elements.addFirst(element);
    }

    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha vazia");
        }
        return elements.removeFirst();
    }

    public E top() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha vazia");
        }
        return elements.first();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    /** Exibe do topo (mais recente) para a base (mais antigo): ordem LIFO. */
    public void printStack() {
        if (isEmpty()) {
            System.out.println("Pilha vazia!");
            return;
        }
        System.out.println("Histórico de operações (MAIS RECENTE AO MAIS ANTIGO):");
        Node<E> atual = elements.head();
        while (atual != null) {
            System.out.println(atual.getElement());
            atual = atual.getNext();
        }
    }

    public void printStackInverso() {
        if (isEmpty()) {
            System.out.println("Pilha vazia!");
            return;
        }
        System.out.println("Histórico de operações (MAIS ANTIGO AO MAIS RECENTE):");

        // O head é o topo (mais recente); copiamos para um vetor para inverter a ordem
        int n = size();
        Object[] temp = new Object[n];
        Node<E> atual = elements.head();

        int i = 0;
        while (atual != null) {
            temp[i++] = atual.getElement();
            atual = atual.getNext();
        }

        for (int j = n - 1; j >= 0; j--) {
            System.out.println(temp[j]);
        }
    }
}