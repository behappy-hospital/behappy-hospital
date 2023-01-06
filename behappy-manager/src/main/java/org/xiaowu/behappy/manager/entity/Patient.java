package org.xiaowu.behappy.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

/**
 * <p>
 * Patient
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "Patient")
public class Patient extends BaseEntity {

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
	private String birthdate;

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

}

