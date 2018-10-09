package com.ace.member.base;


import java.io.Serializable;

/**
 * 所有bean的父类，实现Serializable接口，以便可以通过Intent在各组件中传递数据
 */
public class BaseBean implements Serializable {
	protected static final long serialVersionUID=1;
}
