package HW_L8_T1_CustomTreeMap.impl;

import HW_L8_T1_CustomTreeMap.CustomTreeMap;

import java.util.Comparator;
import java.util.Map;

public class CustomTreeMapImpl<K, V> implements CustomTreeMap<K, V> {
    Node<K, V> _root;
    private Comparator<K> comparator;
    private int _size = 0;


    public CustomTreeMapImpl(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public boolean isEmpty() {
        return _size == 0;
    }

    @Override
    public V get(K key) {
        Node<K, V> parent = searchNode(key);
        return parent == null ? null : parent.getValue();
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        //Блок если корень пустой
        if (this._root == null) {
            this._root = new Node<>(key, value, null); //Добавляем новый корень
            _size = 1; //Обновляем размер
            return null; //Возвращаем null для вновь добавленного значения
        }
        //Блок для поиска места для значения
        //Берём корень дерева и начинаем двигаться по веткам
        Node<K, V> parent = _root;
        Node<K, V> tempNode = _root;
        int compare = this.comparator.compare(key, tempNode.getKey()); //Производим сравнение с помощью компаратора

        while (tempNode != null) {
            parent = tempNode;
            compare = this.comparator.compare(key, tempNode.getKey()); //Производим сравнение с помощью компаратора
            if (compare < 0) {
                tempNode = tempNode.getLeft(); //Если ключ меньше - двигаемся налево
            } else if (compare > 0) {
                tempNode = tempNode.getRight(); //Если ключ больше - двигаемся направо
            } else {
                return tempNode.setValue(value); //Если нашли ключ - устанавливаем значение вместо старого
            }
        }
        //Блок для случая, если мы не нашли значение, соответственно создаем новый объект для хранения данных,
        // устанавливаем зависимость для родителя-потомка
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (compare < 0) {
            parent.setLeft(newNode); //Если ключ меньше - устанавливаем новое значение в левого потомка
        } else if (compare > 0) {
            parent.setRight(newNode); //Если ключ больше - устанавливаем новое значение в правого потомка
        }
        _size++;
        return null;

    }

    @Override
    public V remove(K key) {
        //Блок нахождения значения по ключу
        Node<K, V> parent = searchNode(key);

        if (parent == null) {
            return null;
        }

        V oldValue = parent.getValue(); //Сохраняем полученное значение для дальнейшей передачи из метода
        //Блок изменения предков при изменении порядка
        Node<K, V> childrenNode = (parent.getLeft() != null ? parent.getLeft() : parent.getRight()); //Получаем предка

        if (childrenNode != null) {
            //Если удаляем значение, у которого есть предки
            //Изменяем у предка родителя
            childrenNode.setParent(parent.getParent());
            if (parent.getParent() == null)
                _root = childrenNode;
            else if (parent == parent.getParent().getLeft())
                parent.getParent().setLeft(childrenNode);
            else
                parent.getParent().setRight(childrenNode);

            //Обнуляем ссылки для сборщика мусора
            parent.setLeft(null);
            parent.setRight(null);
            parent.setParent(null);
        } else if (parent.getParent() == null) {
            _root = null; //Если удаляем корень, просто его обнуляем
        } else {
            //Если удаляем значение без предков, перестановок не нужно
            if (parent.getParent() != null) {
                //Очищаем ссылки на значение для родителей
                if (parent == parent.getParent().getLeft())
                    parent.getParent().setLeft(null);
                else if (parent == parent.getParent().getRight())
                    parent.getParent().setRight(null);
                parent.setParent(null);
            }
        }

        _size--;
        return oldValue;

    }

    @Override
    public boolean containsKey(K key) {
        if (_root == null) {
            return false;
        }
        Node<K, V> node = _root;

        while (node != null) {
            int comparison = compare(key, node.getKey());

            if(comparison == 0) {
                return true;
            } else if(comparison <= 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;

    }

    @Override
    public Object[] keys() {
        return new Object[0];
    }

    @Override
    public Object[] values() {
        return new Object[0];
    }

    private int compare(K k1, K k2) {
        if(comparator != null) {
            return comparator.compare(k1, k2);
        } else {
          Comparable<K> comparable = (Comparable<K>) k1;
          return comparable.compareTo(k2);
        }
    }

    private Node<K, V> searchNode(K key) {
        if (key == null) {
            throw new NullPointerException();
        }

        //Берём корень дерева и начинаем двигаться по веткам
        Node<K, V> parent = _root;

        while (parent != null) {
            int compare = this.comparator.compare(key, parent.getKey()); //Производим сравнение с помощью компаратора
            if (compare < 0) {
                parent = parent.getLeft(); //Если ключ меньше - двигаемся налево
            } else if (compare > 0) {
                parent = parent.getRight(); //Если ключ больше - двигаемся направо
            } else {
                return parent; //Если нашли ключ - возвращаем значение, иначе null
            }
        }

        return parent;
    }


    private final class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> left, right,  parent;

        Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public void setParent(Node<K, V> parent) {
            this.parent = parent;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Node))
                return false;
            Node<?, ?> e = (Node<?, ?>) o;

            return (key == null ? e.getKey() == null : key.equals(e.getKey()))
                    && (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }

        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

}
