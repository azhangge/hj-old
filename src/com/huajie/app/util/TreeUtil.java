package com.huajie.app.util;

import org.primefaces.model.TreeNode;

public class TreeUtil {
	public static void setParentExpanded(TreeNode tn,TreeNode root){
		TreeNode ptn = tn.getParent();
		if(ptn!=root){
			ptn.setExpanded(true);
//			ptn.setSelected(true);
			setParentExpanded(ptn,root);
		}else{
			return;
		}
	}
}
