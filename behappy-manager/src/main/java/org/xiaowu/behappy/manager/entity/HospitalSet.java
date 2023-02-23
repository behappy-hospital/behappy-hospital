package org.xiaowu.behappy.manager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

/**
 * <p>
 * HospitalSet
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "HospitalSet")
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "医院编号")
	private String hoscode;

	@ApiModelProperty(value = "签名秘钥")
	private String signKey;

	@ApiModelProperty(value = "api基础路径")
	private String apiUrl;

}

