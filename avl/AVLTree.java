package avl;

import java.util.LinkedList;

import avl.util.TreeToStrings;

import java.lang.Math;
public class AVLTree<T extends Comparable<T>> {

	private TreeNode<T> root;
	public int size;
	
	public AVLTree() {
	    this.root = null;
	    this.size = 0;
	}
	
	////////////////////////////////////////////////////////
	
	//
	// exists()
	// Check whether a specified value exists in the set
	//
	public boolean exists(T value) {
	    return existsHelper(value, this.root);
	}
	
	//
	// existsHelper()
	// (Optionally) recursive procedure to traverse a tree
	// rooted at "root" to find a specified value.  
	//
	// RETURNS: true if the value is found, else false
	//
	private boolean existsHelper(T value, TreeNode<T> root) {
		if (root == null) { // not found
			return false;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found
	    		return true;
	    	} else if (comparison < 0) { // still looking - go left
	    		return existsHelper(value, root.left);
	    	} else { // still looking - go right
	    		return existsHelper(value, root.right);
	    	}
	    }
	}
	
	////////////////////////////////////////////////////////
	
	//
	// min()
	// Return the minimum value in the set
	//
	// If the set is empty, result is undefined.
	//
	public T min() {
	    return minValueInSubtree(this.root);
	}
	
	//
	// minValueInSubTree()
	// Find the smallest value in the subtree rooted at
	// the specified node.
	//
	// ASSUMED: root is not null.
	//
	private T minValueInSubtree(TreeNode<T> root) {
	    while (root.left != null)
	    	root = root.left;
	    
	    return root.value;
	}

	//
	// max()
	// Return the maximum value in the set
	//
	// If the set is empty, result is undefined.
	//
	
	public T max() {
	    return maxValueInSubtree(this.root);
	}


	//
	// maxValueInSubTree()
	// Find the largest value in the subtree rooted at
	// the specified node.
	//
	// ASSUMED: root is not null.
	//
	private T maxValueInSubtree(TreeNode<T> root) {
	    while (root.right != null)
	    	root = root.right;
	    
	    return root.value;
	}
	
	////////////////////////////////////////////////////////
	
	//
	// insert()
	// Insert the specified value in the set if it does not
	// already exist.
	//
	// RETURNS: the size of the set after insertion.
	//
	public int insert(T value) 
	{
    	//System.out.println("Trying to insert "+value);

	     boolean is_present = exists(value);
	        //System.out.println("The value is present : "+is_present);
	        this.root = insertHelper(value, this.root);
	    	//System.out.println("Value inserted is "+value);

        	return size;	
	}
	
	//
	// insertHelper()
	// Recursive procedure to insert a value into the subtree
	// rooted at "root".  If value is already present in the
	// tree, nothing is inserted.
	//
	// RETURNS: root node of subtree after insertion
	//
	// FIXME: add the necessary code to this function
	// to maintain height and rebalance the tree when
	// a node is removed.
	//
	private TreeNode<T> insertHelper(T value,
					 TreeNode<T> root) {
		if (root == null) {
			// add new element as leaf of tree
			TreeNode<T> newNode = new TreeNode<T>(value); 
			newNode.height = 0;
			size++;
			//updateHeight(newNode);
			return newNode;
	    } else {	    	
   	
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) {
	    		// duplicate element -- return existing node
	    		//updateHeight(root);
	    		//System.out.println("Returned root is :" + root.value);
	    		//updateHeight(root);
		       // root = rebalance(root);
		       // System.out.println("Value inserted is" +value);
	    		return root;
	    	} else if (comparison < 0) {
	    		//System.out.println("Comparison < 0");
	    		// still looking -- go left
	    		root.setLeft(insertHelper(value, root.left));
	    		
	    	} else {
	    		// still looking -- go right
	    		//System.out.println("Comparison > 0");

	    		root.setRight(insertHelper(value, root.right));
	    		//updateHeight(root);
		    	
		        //root = rebalance(root);
		      // System.out.println("Value inserted is" +value);
		    //	return root;
	    	}
	    	 	
	    	updateHeight(root);
	    	
	        root = rebalance(root); 
	    	return root;    	
	    	//return root;  	   
	    }
	}

	////////////////////////////////////////////////////////
	
	//
	// remove()
	// Remove a value from the set if it is present
	//
	public void remove(T value) {
	    this.root = removeHelper(value, this.root);
	}
	
	//
	// removeHelper()
	// Recursive procedure to remove a value from the
	// subtree rooted at "root", if it exists.
	//
	// RETURNS root node of subtree after insertion
	//
	// FIXME: add the necessary code to this function
	// to maintain height and rebalance the tree when
	// a node is removed.
	//
	private TreeNode<T> removeHelper(T value,
					 TreeNode<T> root) {
	    
	    if (root == null) { // did not find element
	    	return null;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found element to remove
	    		if (root.left == null || root.right == null) {
	    			// base case -- root has at most one subtree,
	    			// so return whichever one is not null (or null
	    			// if both are)
	    			size--;
	    			updateHeight(root);
	    			return (root.left == null ? root.right : root.left);
	    		} else {
	    			// node with two subtrees -- replace key
	    			// with successor and recursively remove
	    			// the successor.
	    			T minValue = minValueInSubtree(root.right);
	    			root.value = minValue;
			
	    			root.setRight(removeHelper(minValue, root.right));
	    		}
	    	} else if (comparison < 0) {
	    		// still looking for element to remove -- go left
	    		root.setLeft(removeHelper(value, root.left));
	    	} else {
	    		// still looking for element to remove -- go right
	    		root.setRight(removeHelper(value, root.right));
	    	}
	    	
            updateHeight(root);
	    	
	        root = rebalance(root);
	    	
	    	return root;
	    }
	}

	
	////////////////////////////////////////////////////////
	//
	// INTERNAL METHODS FOR MAINTAINING BALANCE
	//
	
	// updateHeight()
	//
	// Recompute the height of the subtree rooted at "root",
	// assuming that its left and right children (if any)
	// have correct heights for their respective subtrees.
	//
	// EFFECT: Set the root's height field to the updated value
	//
	private void updateHeight(TreeNode<T> root) {
	    // FIXME: fill in the update code
		if (root == null) {
			//System.out.println("Function - updateHeight - root is null");
			return;
		}
		//System.out.println("you are updating height for root" + root.value);

		if (root.left == null && root.right == null){
			root.height = 0;
		//System.out.println("Update height for root" + root.value + " is" + root.height);
		//System.out.println("Function - root- left and root- right is null");
			return;
		}
		if (root.left == null) {
			root.height = root.right.height + 1;
			//System.out.println("Update height for root" + root.value + " is" + root.height);
			//System.out.println("Function updateHeight - root- left is null");

			return;
		}
		if (root.right == null) {
			root.height = root.left.height + 1;
			//System.out.println("Update height for root" + root.value + " is" + root.height);
			//System.out.println("Function updateHeight - root- right is null");

			return;
		}
		root.height =  Math.max(root.left.height, root.right.height) + 1;
		//System.out.println("Update height for root" + root.value + " is" + root.height);
		//System.out.println("Update height for this root is" + root.height);
		return;
	}

	//
	// getBalance()
	// Return the balance factor of a subtree rooted at "root"
	// (right subtree height - left subtree height)
	//
	private int getBalance(TreeNode<T> root) {
	    // FIXME: fill in the balance computation
	//System.out.println("you are getting balance");
		if (root.left == null) {
			if(root.right == null) {
				//System.out.println("Function getBalance - root- right and root left is null for root" + root.value);
				//System.out.println("Balance for root" + root.value + "is " + 0);

				return 0;
			}else {
				//System.out.println("Function getBalance - root left isnull for root" + root.value);
				int dummy = root.right.height + 1;
				//System.out.println("Balance for root" + root.value + "is " + dummy);
				return root.right.height + 1;
			}
		}
		if (root.right == null) {
			if(root.left == null) {
				//System.out.println("Function getBalance - root- right and root left is null for root" + root.value);
				return 0;
			}else {
				//System.out.println("Function getBalance - root- right is null for root" + root.value);
				int dummy = -1 - root.left.height;
				//System.out.println("Balance for root" + root.value + "is " + dummy);
				return -1 - root.left.height;
			}
		}
		//System.out.println("Function getBalance - both roots exist" + root.value);
		int dummy = root.right.height - root.left.height;
		//System.out.println("Balance for root" + root.value + "is " + dummy );
	    return root.right.height - root.left.height;
	}

	//
	// rebalance()
	//
	// Rebalance an AVL subtree, rooted at "root", that has possibly
	// been unbalanced by a single node's insertion or deletion.
	//
	// RETURNS: the root of the subtree after rebalancing
	//
	private TreeNode<T> rebalance(TreeNode<T> root) {
	    // FIXME: fill in the rebalancing code
		if (root == null) {
			return root;
		}
		//System.out.println("Rebalancing : root is " + root.value);
		int root_balance = getBalance(root);
		//System.out.println("Balance for this root is " + root_balance);
		
		if (root_balance == 0) {
			//System.out.println("No balance done");
			return root; 
		}
		if (root_balance == 1) {
			//System.out.println("No balance done");
			return root; 
		}

		if (root_balance == -1) {
			//System.out.println("No balance done");
			return root;
		}
		//System.out.println("You are rebalancing at root " + root.value);

		if (root_balance == -2) {
			int left_child_balance = getBalance(root.left);
			if(left_child_balance < 1) {
			return 	rightRotate(root);
			}
			if (left_child_balance == 1) {
			root.setLeft(leftRotate(root.left));
			return	rightRotate(root);
			}
		}
		
		if (root_balance == 2) {
			// get balance of right child of root 
			//System.out.println("Right heavy case");
			int right_child_balance = getBalance(root.right);
			if(right_child_balance > -1) {
			return 	leftRotate(root);
			}
			if (right_child_balance == -1){
			root.setRight(rightRotate(root.right));
			return	leftRotate(root);
			}
		}
		return root;		

	}
	
	//
	// rightRotate()
	// Perform a right rotation on a tree rooted at "root"
	// The tree's root is assumed to have a left child.
	//
	// RETURNS: the new root after rotation.
	//
	private TreeNode<T> rightRotate(TreeNode<T> root) {
	    // FIXME: fill in the rotation code
		//System.out.println("Rotating right at root" + root.value);

		TreeNode<T> parent = root.left;
		TreeNode<T> dummy = parent.right;
		
		root.setLeft(dummy);
		updateHeight(root);
		
		parent.setRight(root);
		updateHeight(parent);
		
		//System.out.println("New root is " + parent.value);
		//System.out.println("Left child is " + parent.left.value);
		//System.out.println("Right child is " + parent.right.value);
		
	    return parent;
	}

	//
	// leftRotate()
	// Perform a left rotation on a tree rooted at "root"
	// The tree's root is assumed to have a right child.
	//
	// RETURNS: the new root after rotation.
	//
	private TreeNode<T> leftRotate(TreeNode<T> root) {
	    // FIXME: fill in the rotation code
		//System.out.println("Rotating left at root" + root.value);
		
		TreeNode<T> parent = root.right;
		TreeNode<T> dummy =  parent.left;
		
		root.setRight(dummy);
		updateHeight(root);

		parent.setLeft(root);		
		updateHeight(parent);
		
		//System.out.println("New root is " + parent.value);
		//System.out.println("Left child is " + parent.left.value);
		//System.out.println("Right child is " + parent.right.value);
		
	    return parent;
	    
	}
	
	/////////////////////////////////////////////////////////////
	//
	// METHODS USED TO VALIDATE CORRECTNESS OF TREE
	// (You should not have to touch these)
	//

	//
	// getRoot()
	// Return the root node of the tree (for validation only!)
	//
	public TreeNode<T> getRoot() {
	    return this.root;
	}
	
		
	//
	// enumerate()
	// Return the contents of the tree as an ordered list
	//
	public LinkedList<T> enumerate() {
	    return enumerateHelper(this.root);
	}
	
	//
	// enumerateHelper()
	// Enumerate the contents of the tree rooted at "root" in order
	// as a linked list
	//
	private LinkedList<T> enumerateHelper(TreeNode<T> root) {
	    if (root == null) 
		{
		    return new LinkedList<T>();
		}
	    else
		{
		    LinkedList<T> list = enumerateHelper(root.left);
		    list.addLast(root.value);
		    list.addAll(enumerateHelper(root.right));
		    
		    return list;
		}
	}
}
