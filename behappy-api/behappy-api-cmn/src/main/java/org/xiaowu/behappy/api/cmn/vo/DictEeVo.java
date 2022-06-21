package org.xiaowu.behappy.api.cmn.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Dict
 * excel导入导出
 */
@Data
public class DictEeVo {

	@ExcelProperty(value = "id" ,index = 0)
	private Long id;

	@ExcelProperty(value = "上级id" ,index = 1)
	private Long parentId;

	@ExcelProperty(value = "名称" ,index = 2)
	private String name;

	@ExcelProperty(value = "值" ,index = 3)
	private String value;

	@ExcelProperty(value = "编码" ,index = 4)
	private String dictCode;

}

