
import java.util.ArrayList;
import java.util.Scanner;

public class RedBlackTree<R> {
	Scanner scan = new Scanner(System.in);
	private final int RED = 0;
	private final int BLACK = 1;
	static int count = 0;

	public class Node {

		double key = -1;
		int color = BLACK;
		R id;
		String date;
		Node left = nil, right = nil, parent = nil;

		Node(R id, String date, double key) {
			this.id = id;
			this.date = date;
			this.key = key;

		}
	}

	private final Node nil = new Node(null, null, -1);
	private Node root = nil;

	public void printTree(Node node) {
		if (node == nil) {
			return;
		}
		printTree(node.left);
		System.out.print(((node.color == RED) ? "Color: Red " : "Color: Black ") + "\tKey: " + node.key + " \tParent: "
				+ node.parent.key + " \tid: " + node.id + "\n");
		printTree(node.right);
	}

	private Node findNode(Node findNode, Node node) {
		if (root == nil) {
			return null;
		}

		if (findNode.key < node.key) {
			if (node.left != nil) {
				return findNode(findNode, node.left);
			}
		} else if (findNode.key > node.key) {
			if (node.right != nil) {
				return findNode(findNode, node.right);
			}
		} else if (findNode.key == node.key) {
			return node;
		}
		return null;
	}

	public void insert(Node node) {
		Node temp = root;
		if (root == nil) {
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED;
			while (true) {
				if (node.key < temp.key) {
					if (temp.left == nil) {
						temp.left = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.left;
					}
				} else if (node.key >= temp.key) {
					if (temp.right == nil) {
						temp.right = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.right;
					}
				}
			}
			fixTree(node);
		}
	}

	// Takes as argument the newly inserted node
	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) {
				uncle = node.parent.parent.right;

				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.right) {
					// Double rotation needed
					node = node.parent;
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateRight(node.parent.parent);
			} else {
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.left) {
					// Double rotation needed
					node = node.parent;
					rotateRight(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateLeft(node.parent.parent);
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
			node.parent = node.right;
			if (node.right.left != nil) {
				node.right.left.parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {// Need to rotate root
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nil;
			root = right;
		}
	}

	void rotateRight(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != nil) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {// Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nil;
			root = left;
		}
	}

	// Deletes whole tree
	void deleteTree() {
		root = nil;
	}

	// Deletion Code .

	// This operation doesn't care about the new Node's connections
	// with previous node's left and right. The caller has to take care
	// of that.
	void transplant(Node target, Node with) {
		if (target.parent == nil) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	boolean delete(Node z) {
		if ((z = findNode(z, root)) == null)
			return false;
		Node x;
		Node y = z; // temporary reference y
		int y_original_color = y.color;

		if (z.left == nil) {
			x = z.right;
			transplant(z, z.right);
		} else if (z.right == nil) {
			x = z.left;
			transplant(z, z.left);
		} else {
			y = treeMinimum(z.right);
			y_original_color = y.color;
			x = y.right;
			if (y.parent == z)
				x.parent = y;
			else {
				transplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		if (y_original_color == BLACK)
			deleteFixup(x);
		return true;
	}

	void deleteFixup(Node x) {
		while (x != root && x.color == BLACK) {
			if (x == x.parent.left) {
				Node w = x.parent.right;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateLeft(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == BLACK && w.right.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.right.color == BLACK) {
					w.left.color = BLACK;
					w.color = RED;
					rotateRight(w);
					w = x.parent.right;
				}
				if (w.right.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					rotateLeft(x.parent);
					x = root;
				}
			} else {
				Node w = x.parent.left;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateRight(x.parent);
					w = x.parent.left;
				}
				if (w.right.color == BLACK && w.left.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.left.color == BLACK) {
					w.right.color = BLACK;
					w.color = RED;
					rotateLeft(w);
					w = x.parent.left;
				}
				if (w.left.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					rotateRight(x.parent);
					x = root;
				}
			}
		}
		x.color = BLACK;
	}

	Node treeMinimum(Node subTreeRoot) {
		while (subTreeRoot.left != nil) {
			subTreeRoot = subTreeRoot.left;
		}
		return subTreeRoot;
	}

	/// */*/*/*/*/*/*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/**/*/*
	// managementte node erisebilmek icin
	public Node tempNode(R id, String date, double age) {
		Node node = new Node(id, date, age);
		return node;
	}

	// girilen tarihten önce doganlar
	public void getNumSmallerDate(Node node, String date) {
		if (node == nil) {
			return;
		}
		getNumSmallerDate(node.left, date);
		double age = 0;
		String[] parse;
		parse = date.split("/");
		age = (31 - (Double.parseDouble(parse[0]))) + (30 * (12 - Double.parseDouble(parse[1])))
				+ (365 * (2018 - Double.parseDouble(parse[2])));
		age = age / 365;
		if (node.key < age)
			count++;
		if (node.key == age) {
			System.out.println("Smaller Number= " + count);
			count = 0;
		}
		getNumSmallerDate(node.right, date);

	}

	// girilen id den once doganlar
	public void getNumSmallerID(Node node, R id) {
		if (node == nil) {
			return;
		}
		getNumSmallerID(node.left, id);
		if (!node.id.equals(id))
			count++;
		if (node.id.equals(id)) {
			System.out.println("Smaller number= " + count);
			count = 0;
		}
		getNumSmallerID(node.right, id);
	}

	// en kücük yaþ
	public void minAge() {
		System.out.println("Age= " + treeMinimum(root).key + "\nid= " + treeMinimum(root).id + "\nBirthdate= "
				+ treeMinimum(root).date);
	}

	// en buyuk yaþ
	public void maxAge() {
		Node temp = root;
		while (temp.right.right != null) {
			temp = temp.right;
		}
		System.out.println("Age= " + temp.key + "\nid= " + temp.id + "\nBirthdate= " + temp.date);

	}

	// kaç eleman var
	public void sumElement(Node node) {
		if (node == nil)
			return;
		sumElement(node.left);
		count++;
		sumElement(node.right);
	}

	public void case1(R id, String date, double age) {
		Node node;
		node = new Node(id, date, age / 365);
		insert(node);
		printTree(root);
	}

	public void case2(R id, String date, double age) {

		Node node;
		node = new Node(id, date, age / 365);
		System.out.print("\nDeleting item " + "\nid= " + id + "\nbirthday= " + date + "\nage= " + age);

		if (delete(node)) {
			System.out.print(": deleted!");
		} else {
			System.out.print(": does not exist!");
		}
		System.out.println();
		printTree(root);

	}

	public void case3(R id, String date, double age) {
		Node node;
		node = new Node(id, date, age / 365);
		System.out.println((findNode(node, root) != null) ? "found" : "not found");
	}

	public void case4() {
		printTree(root);
	}

	public void case5() {
		deleteTree();
		System.out.println("Tree deleted!");
	}

	public void case6(String date) {
		getNumSmallerDate(root, date);
		count = 0;
	}

	public void case7(R id) {
		getNumSmallerID(root, id);
		count = 0;
	}

	public void case8() {
		minAge();
	}

	public void case9() {
		maxAge();
	}

	public void case10() {
		sumElement(root);
		System.out.println("All people= " + count);
		count = 0;
	}

}