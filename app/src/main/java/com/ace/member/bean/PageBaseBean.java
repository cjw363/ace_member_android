package com.ace.member.bean;


import com.ace.member.base.BaseBean;

import java.util.List;

public class PageBaseBean<T> extends BaseBean {
	protected int total;
	protected int page;
	protected int size;
	protected List<T> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "PageBaseBean{" + "total=" + total + ", page=" + page + ", size=" + size + ", list=" + list + '}';
	}
}
