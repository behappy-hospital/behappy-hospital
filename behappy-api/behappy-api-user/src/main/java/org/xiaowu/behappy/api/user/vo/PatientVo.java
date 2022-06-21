package org.xiaowu.behappy.api.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Patient
 * </p>
 */
@Data
@ApiModel(description = "Patient")
public class PatientVo {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "姓名")
	private String name;

	@ApiModelProperty(value = "证件类型")
	private String certificatesType;

	@ApiModelProperty(value = "证件编号")
	private String certificatesNo;

	@ApiModelProperty(value = "性别")
	private Integer sex;

	@ApiModelProperty(value = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthdate;

	@ApiModelProperty(value = "手机")
	private String phone;

	@ApiModelProperty(value = "是否结婚")
	private Integer isMarry;

	@ApiModelProperty(value = "省code")
	private String provinceCode;

	@ApiModelProperty(value = "市code")
	private String cityCode;

	@ApiModelProperty(value = "区code")
	private String districtCode;

	@ApiModelProperty(value = "详情地址")
	private String address;

	@ApiModelProperty(value = "联系人姓名")
	private String contactsName;

	@ApiModelProperty(value = "联系人证件类型")
	private String contactsCertificatesType;

	@ApiModelProperty(value = "联系人证件号")
	private String contactsCertificatesNo;

	@ApiModelProperty(value = "联系人手机")
	private String contactsPhone;

	@ApiModelProperty(value = "是否有医保")
	private Integer isInsure;

	@ApiModelProperty(value = "就诊卡")
	private String cardNo;

	@ApiModelProperty(value = "状态（0：默认 1：已认证）")
	private String status;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
	private Integer isDeleted;

	@ApiModelProperty(value = "其他参数")
	private Map<String,Object> param = new HashMap<>();
}

