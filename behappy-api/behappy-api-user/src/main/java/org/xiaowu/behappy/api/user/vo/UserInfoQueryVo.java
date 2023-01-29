package org.xiaowu.behappy.api.user.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 94391
 */
@Data
@Schema(description="会员搜索对象")
public class UserInfoQueryVo {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "认证状态")
    private Integer authStatus;

    @Schema(description = "创建时间")
    private String createTimeBegin;

    @Schema(description = "创建时间")
    private String createTimeEnd;

}
