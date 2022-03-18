package com.demo.controller;

import com.demo.entity.ItemInfo;
import com.demo.service.ItemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemInfo")
@CrossOrigin
public class ItemInfoController {

    @Autowired
    private ItemInfoService itemInfoService;

    /**
     * 获取商品List
     *
     * @return
     */
    @GetMapping(value = "/getList")
    public List<ItemInfo> getList() {

        return itemInfoService.getList();
    }

    /**
     * 添加商品的秒杀库存，并存到redis中
     *
     * @param id
     * @param count
     * @param price
     * @return
     */
    @PostMapping(value = "/addLimitStock")
    public Map<String, Object> addLimitStock(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "price") int price) {

        return itemInfoService.addLimitStock(id, count, price);
    }

    /**
     * 库存递减
     *
     * @param id
     * @param count
     * @param isLimit
     * @return
     */
    @PostMapping(value = "/decrCount")
    public Map<String, Object> decrCount(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "isLimit") boolean isLimit) {

        return itemInfoService.decrCount(id, count, isLimit);
    }
}
