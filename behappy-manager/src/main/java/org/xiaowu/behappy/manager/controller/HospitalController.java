package org.xiaowu.behappy.manager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.HttpRequestHelper;
import org.xiaowu.behappy.manager.service.ApiService;
import org.xiaowu.behappy.manager.service.HospitalService;

import java.util.Map;

/**
 *
 */
@Tag(name = "订单管理接口")
@RestController
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ApiService apiService;

	/**
	 * 预约下单
	 * @param request
	 * @return
	 */
	@PostMapping("/order/submitOrder")
	public Result AgreeAccountLendProject(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
			if(!HttpRequestHelper.isSignEquals(paramMap, apiService.getSignKey())) {
				throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
			}

			Map<String, Object> resultMap = hospitalService.submitOrder(paramMap);
			return Result.ok(resultMap);
		} catch (HospitalException e) {
			return Result.failed(e);
		}
	}

	/**
	 * 更新支付状态
	 * @param request
	 * @return
	 */
	@PostMapping("/order/updatePayStatus")
	public Result updatePayStatus(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
			if(!HttpRequestHelper.isSignEquals(paramMap, apiService.getSignKey())) {
				throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
			}

			hospitalService.updatePayStatus(paramMap);
			return Result.ok();
		} catch (HospitalException e) {
			return Result.failed(e);
		}
	}

	/**
	 * 更新取消预约状态
	 * @param request
	 * @return
	 */
	@PostMapping("/order/updateCancelStatus")
	public Result updateCancelStatus(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
			if(!HttpRequestHelper.isSignEquals(paramMap, apiService.getSignKey())) {
				throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
			}

			hospitalService.updateCancelStatus(paramMap);
			return Result.ok();
		} catch (HospitalException e) {
			return Result.failed(e);
		}
	}
}
