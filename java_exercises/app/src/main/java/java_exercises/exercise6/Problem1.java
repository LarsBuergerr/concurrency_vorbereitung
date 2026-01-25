package java_exercises.exercise6;

import java.util.concurrent.locks.ReentrantLock;

public class Problem1 {
  
  public static void main(String[] args) {

    LinkedList l = new LinkedList();


    l.insert(1);
    l.insert(4);
    l.insert(3);
    l.insert(2);

    System.out.println(l.toString());


    l.delete(2);
    l.delete(3);
    l.delete(10);

    System.out.println(l.toString());
  }
}




class LinkedList {

  Node head;

  public LinkedList() {
    head = null;
  }

  public void insert(int value) {
    Node newNode = new Node(value);
    if(head == null) {
      head = newNode;
      return;
    }

    Node prev = null;
    Node curr = head;
    while(true) {
      if(curr.value > newNode.value) {
        if(prev == null) {
          newNode.next = curr;
          head = newNode;
        } else {
          prev.next = newNode;
          newNode.next = curr;
        }
        break;
      }
      if (curr.next != null) {
        prev = curr;
        curr = curr.next;
        continue;
      } else {
        curr.next = newNode;
      }
    }
  }


  public void delete(int value) {
    if(head == null) {
      return;
    }
    
    if(head.value == value) {
      head = head.next;
    }

    Node curr = head;
    while(curr.next != null) {
      if(curr.next.value == value) {
        curr.next = curr.next.next;
        return;
      }
      curr = curr.next;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(head == null) {
      return "Null";
    }
    
    Node curr = head;
    sb.append(curr.value);
    while(curr.next != null) {
      sb.append("->");
        sb.append(curr.next.value);
        curr = curr.next;
    }
    return sb.toString();
  }
}

class Node {

  int value;
  Node next;

  ReentrantLock lock = new ReentrantLock();

  public Node(int value) {
    this.value = value;
    this.next = null;
  }
}