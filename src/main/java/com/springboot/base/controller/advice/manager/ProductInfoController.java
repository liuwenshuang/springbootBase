package com.springboot.base.controller.advice.manager;

import com.springboot.base.data.base.Page;
import com.springboot.base.data.enmus.ErrorInfo;
import com.springboot.base.data.entity.ProductInfo;
import com.springboot.base.data.entity.UserInfo;
import com.springboot.base.data.exception.PrivateException;
import com.springboot.base.data.vo.ProductVO;
import com.springboot.base.data.vo.UserVO;
import com.springboot.base.service.ProductService;
import com.springboot.base.util.BindingResutlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * 描述：产品管理
 * Created by jay on 2017-9-29.
 */
@RestController
@RequestMapping("user/productInfo")
@Slf4j
public class ProductInfoController {

    @Inject
    private ProductService productService;

    /**
     * 产品查询
     *
     * @param limit
     * @param offset
     * @param searchStr
     * @param status
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page list(@RequestParam(value = "limit", defaultValue = "10") int limit,
                     @RequestParam(value = "offset", defaultValue = "0") int offset,
                     @RequestParam(value = "searchStr", defaultValue = "-1") String searchStr,
                     @RequestParam(value = "status", defaultValue = "-1") int status,
                     @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                     @RequestParam(value = "desc") boolean desc) {
        return productService.listPage(limit, offset, searchStr, status, orderBy, desc);
    }

    /**
     * 添加产品
     *
     * @param productInfo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody @Validated(ProductInfo.BaseInfo.class) ProductInfo productInfo, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            log.info("添加验证信息{}", BindingResutlUtils.getMessage(bindingResult));
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        productService.save(productInfo);
    }

    /**
     * 产品详情
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ProductVO detail(@RequestParam(value = "productId") Long productId) {
        return productService.getDetail(productId);
    }

    /**
     * 产品修改
     * @param userInfo
     * @param bindingResult
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(@RequestBody @Validated(ProductInfo.Modify.class) ProductInfo userInfo, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            log.info("添加验证信息{}", BindingResutlUtils.getMessage(bindingResult));
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        productService.update(userInfo);
    }

}
