package org.xiaowu.behappy.api.hosp.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Hospital")
public class HospitalQueryVo implements Serializable {

	private static final long serialVersionUID = -60606553099405951L;
	@Schema(description = "医院编号")
	private String hoscode;

	@Schema(description = "医院名称")
	private String hosname;

	@Schema(description = "医院类型")
	private String hostype;

	@Schema(description = "省code")
	private String provinceCode;

	@Schema(description = "市code")
	private String cityCode;

	@Schema(description = "区code")
	private String districtCode;

	@Schema(description = "状态")
	private Integer status;
}

