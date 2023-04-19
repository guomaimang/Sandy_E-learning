package tech.hirsun.project.comp3334.sandy_elearning.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PageResult implements Serializable {

	//总记录数
	private int totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int currPage;
	//列表数据
	private List<?> list;

	/**
	 * 分页
	 * @param list        list data
	 * @param totalCount  Total number of records
	 * @param pageSize    records per page
	 * @param currPage    current page number
	 */
	public PageResult(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}


}
