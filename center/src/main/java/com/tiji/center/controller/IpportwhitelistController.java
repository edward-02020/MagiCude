package com.tiji.center.controller;

import com.tiji.center.pojo.Ipportwhitelist;
import com.tiji.center.pojo.Ipwhitelist;
import com.tiji.center.service.IpportwhitelistService;
import com.tiji.center.service.IpwhitelistService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ipportwhitelist控制器层
 *
 * @author 贰拾壹
 */
@RestController
@CrossOrigin
@RequestMapping("/ipportwhitelist")
public class IpportwhitelistController {

    @Autowired
    private IpportwhitelistService ipportwhitelistService;
    @Autowired
    private IpwhitelistService ipwhitelistService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", ipportwhitelistService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", ipportwhitelistService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Ipportwhitelist> pageList = ipportwhitelistService.findSearch(searchMap, page, size);
        pageList.stream().parallel().forEach(ipportwhitelist -> {
            String ipwhitelistid = ipportwhitelist.getIpwhitelistid();
            if(!StringUtils.isEmpty(ipwhitelistid)){
                Ipwhitelist ipwhitelist = ipwhitelistService.findById(ipwhitelistid);
                if(!Objects.isNull(ipwhitelist)){
                    ipportwhitelist.setIpwhitelistid(ipwhitelist.getIp());
                }
            }
        });
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Ipportwhitelist>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", ipportwhitelistService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param ipportwhitelist
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Ipportwhitelist ipportwhitelist) {
        String port = ipportwhitelist.getPort();
        Ipportwhitelist ipportwhitelistInDb = ipportwhitelistService.findByIpwhitelistidAndPort(ipportwhitelist.getIpwhitelistid(), port);
        if (Objects.isNull(ipportwhitelistInDb)) {
            ipportwhitelistService.add(ipportwhitelist);
            return new Result(true, StatusCode.OK, "增加成功");
        } else {
            return new Result(false, StatusCode.ERROR, "增加失败：端口重复");
        }
    }

    /**
     * 修改
     *
     * @param ipportwhitelist
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Ipportwhitelist ipportwhitelist, @PathVariable String id) {
        ipportwhitelist.setId(id);
        ipportwhitelistService.update(ipportwhitelist);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        ipportwhitelistService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    /**
     * 根据ids批量删除
     *
     * @param ids
     */
    @RequestMapping(value = "/deleteids", method = RequestMethod.POST)
    public Result deleteAllByIds(@RequestBody List<String> ids) {
        ipportwhitelistService.deleteAllByIds(ids);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
