package estruturas;

public class LinkedQueue<E>{
    private Node<E> front;
    private Node<E> rear;
    private int size;

    public void enqueue (E element){
        Node<E> newNode = new Node<>(element, null);
        if(isEmpty()){
            front = newNode;
        }else{
            rear.setNext(newNode);
        }
        rear = newNode;
        size++;
    }

    public E dequeue(){
        if(isEmpty()) 
            throw new RuntimeException("Fila vazia");
        E value = front.getElement();
        front = front.getNext();
        if (front == null)
            rear = null;
        size--;
        return value;
    }

    public E peekFirst(){
        if(isEmpty())
            throw new RuntimeException("Fila vazia");
        return front.getElement();
    }

    
    public void display() {
        if (isEmpty()) {
            System.out.println("Fila vazia.");
            return;
        }
        Node<E> current = front;
        while (current != null) {
            System.out.println(current.getElement());
            current = current.getNext();
        }
    }

    public boolean isEmpty(){
        return front == null;
    }

    public int getSize(){
        return size;
    }

    
}