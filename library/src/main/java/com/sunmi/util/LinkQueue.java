//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.util;

public class LinkQueue<T> {
  private Node front;
  private Node rear;
  private int size = 0;

  public LinkQueue() {
    Node n = new Node((T)null, (Node)null);
    n.next = null;
    this.front = this.rear = n;
  }

  public void r_push_queue(T data) {
    Node s = new Node(data, (Node)null);
    this.rear.next = s;
    this.rear = s;
    ++this.size;
  }

  public void l_push_queue(T data) {
    Node s = new Node(data, (Node)null);
    if(this.front.next == null) {
      this.front.next = s;
      this.rear = s;
    } else {
      Node p = this.front.next;
      this.front.next = s;
      s.next = p;
    }

    ++this.size;
  }

  public T l_pop_queue() {
    if(this.rear == this.front) {
      return null;
    } else {
      Node p = this.front.next;
      T x = p.data;
      this.front.next = p.next;
      if(p.next == null) {
        this.rear = this.front;
      }

      p = null;
      --this.size;
      return x;
    }
  }

  public void clear() {
    while(this.l_pop_queue() != null) {
      ;
    }

  }

  public int size() {
    return this.size == 0?-1:this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public String toString() {
    if(this.isEmpty()) {
      return "[]";
    } else {
      StringBuilder sb = new StringBuilder("[");

      for(Node len = this.front.next; len != null; len = len.next) {
        sb.append(len.data.toString() + ", ");
      }

      int len1 = sb.length();
      return sb.delete(len1 - 2, len1).append("]").toString();
    }
  }

  private class Node {
    public T data;
    public Node next;

    public Node() {
    }

    public Node(T var1, Node data) {
      this.data = var1;
      this.next = data;
    }
  }
}
