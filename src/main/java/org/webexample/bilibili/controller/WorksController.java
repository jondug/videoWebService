package org.webexample.bilibili.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.webexample.bilibili.mapper.SubareaMapper;
import org.webexample.bilibili.mapper.UserinfoMapper;
import org.webexample.bilibili.mapper.WorksMapper;
import org.webexample.bilibili.mapper.Works_commnetMapper;
import org.webexample.bilibili.model.Subarea;
import org.webexample.bilibili.model.Userinfo;
import org.webexample.bilibili.model.Works;
import org.webexample.bilibili.model.Works_comment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/works")
public class WorksController {
    @Autowired
    Works_commnetMapper works_commnetMapper;
    @Autowired
    WorksMapper worksMapper;
    @Autowired
    UserinfoMapper userinfoMapper;
    @Autowired
    SubareaMapper subareaMapper;
    //首页
    @RequestMapping("listworks")
    @ResponseBody
    public Object listWorks(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.last("limit 0,8");
        List<Works> works = worksMapper.selectList(queryWrapper);
        List arr= new ArrayList();
        for(Works works1:works){
            Map<String,Object> remap = new HashMap<>();
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("userid",works1.getUserid());
            Userinfo userinfo = userinfoMapper.selectOne(queryWrapper);
            remap.put("userinfo",userinfo);
            remap.put("works",works1);
            arr.add(remap);

        }
        map.put("list",arr);

        //热门
        queryWrapper = new QueryWrapper();
        queryWrapper.last("limit 0,8");
        List<Works> works1 = worksMapper.selectList(queryWrapper);
        map.put("hot",works1);

        //分区
        List<Subarea> subareas = subareaMapper.selectList(null);
        map.put("subarea",subareas);
        return map;
    }
    //登录成功首页显示用户信息
    @RequestMapping("userinfo")
    @ResponseBody
    public Object userinfo(String userid){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
//        List<Works> works = worksMapper.selectList(queryWrapper);
        Userinfo info = userinfoMapper.selectOne(queryWrapper);
        map.put("info",info);
        System.out.println(info.getNickname());
        return map;
    }
    //通过worksid查找视频内容
    @RequestMapping("works")
    @ResponseBody
    public Object works(int worksid){


        //content
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        Works work = worksMapper.selectOne(queryWrapper);
        work.setVv(work.getVv()+1);
        worksMapper.update(work,queryWrapper);
        map.put("work",work);

        //用户信息
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",work.getUserid());
        Userinfo info = userinfoMapper.selectOne(queryWrapper);
        map.put("info",info);

        //评论
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        List<Works_comment> works_comments = works_commnetMapper.selectList(queryWrapper);
        List<Map> list = new ArrayList();
        for(Works_comment works_comment:works_comments){
            Map<String,Object> remap = new HashMap<>();
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("userid",works_comment.getUserid());
            Userinfo userinfo = userinfoMapper.selectOne(queryWrapper);
            remap.put("userinfo",userinfo);
            remap.put("comment",works_comment);
            list.add(remap);
        }
        map.put("comment",list);

        //热门
        queryWrapper = new QueryWrapper();
        queryWrapper.last("limit 0,8");
        List<Works> works = worksMapper.selectList(queryWrapper);
        map.put("hot",works);

        //关注
        return map;

    }
    //分区
    @RequestMapping("subarea")
    @ResponseBody
    public Object subarea(){
        Map<String,Object> map = new HashMap<>();
//        QueryWrapper queryWrapper = new QueryWrapper();
        List<Subarea> subareas = subareaMapper.selectList(null);
        map.put("subarea",subareas);
        return map;
    }
    //通过各分区视频
    @RequestMapping("subareaWorks")
    @ResponseBody
    public Object subareaWorks(){
        Map<Object,Object> map = new HashMap<>();
//        QueryWrapper queryWrapper = new QueryWrapper();
        List<Subarea> subareas = subareaMapper.selectList(null);
        for (Subarea subarea:subareas){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("subareaid",subarea.getSubareaid());
            List<Works> worksList = worksMapper.selectList(queryWrapper);
            map.put(subarea.getName(),worksList);
            System.out.println(subarea.getName());
        }

        return map;
    }
    //用户主页查看自己的视频
    @RequestMapping("uservideo")
    @ResponseBody
    public Object uservideo(int userid){
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        List<Works> worksLists = worksMapper.selectList(queryWrapper);
        map.put("worksLists",worksLists);
        return map;
    }

    //创作视频
    @RequestMapping("createvideo")
    @ResponseBody
    public Object createvideo(int userid,String title,String subarea, String brief,  MultipartFile video,MultipartFile photo) throws IOException {
        Map<Object,Object> map = new HashMap<>();
        //上传视频
        String originalFilename = video.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + suffix;
        String osName = System.getProperty("os.name").toLowerCase();
        //保存到开发环境的resources目录下
        if(osName.indexOf("win") != -1){
            String url = "Bilibili/src/main/resources/static/videos/" + fileName;
            File winFiledir = new File(url);
            FileUtil.writeBytes(video.getBytes(),winFiledir);
            System.out.println("保存文件:"+ winFiledir.getAbsolutePath());
        }
        //编译环境的目录下
        String uploadFolderPath = ResourceUtils.getURL("classpath:").getPath()+"static/videos/" ;
        File dest = new File(uploadFolderPath + fileName);
        FileUtil.writeBytes(video.getBytes(), dest);

        //上传封面
        String originalFilename1 = photo.getOriginalFilename();
        String suffix1 = originalFilename1.substring(originalFilename1.lastIndexOf("."));
        String fileName1 = System.currentTimeMillis() + suffix1;
        String osName1 = System.getProperty("os.name").toLowerCase();
        //保存到开发环境的resources目录下
        if(osName1.indexOf("win") != -1){
            String url = "Bilibili/src/main/resources/static/imgs/" + fileName1;
            File winFiledir = new File(url);
            FileUtil.writeBytes(photo.getBytes(),winFiledir);
            System.out.println("保存文件:"+ winFiledir.getAbsolutePath());
        }
        //编译环境的目录下
        String uploadFolderPath1 = ResourceUtils.getURL("classpath:").getPath()+"static/imgs/" ;
        File dest1 = new File(uploadFolderPath1 + fileName1);
        FileUtil.writeBytes(photo.getBytes(), dest1);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",subarea);
        Subarea subarea1 = subareaMapper.selectOne(queryWrapper);


        Works works = new Works();
        works.setUserid(userid);
        works.setTitle(title);
        works.setBrief(brief);
        works.setPhoto("imgs/"+fileName1);
        works.setVideo("videos/"+fileName);
        works.setSubareaid(subarea1.getSubareaid());
        worksMapper.insert(works);

        map.put("res",1);
        map.put("msg","上传成功");
        return map;


    }
    //删除视频
    @RequestMapping("delworks")
    @ResponseBody
    public Object delworks( String worksid){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        worksMapper.delete(queryWrapper);
        works_commnetMapper.delete(queryWrapper);
        map.put("res",1);
        map.put("msg","删除成功");
        return map;

    }
    //修改标题和简介
    @RequestMapping("updataworks")
    @ResponseBody
    public Object updataworks(String worksid, String title ,String brief){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        Works works = worksMapper.selectOne(queryWrapper);

        works.setTitle(title);
        works.setBrief(brief);
        worksMapper.update(works,queryWrapper);
        map.put("res",1);
        map.put("msg","修改成功");
        return map;

    }
    //发布评论
    @RequestMapping("publishCommnet")
    @ResponseBody
    public Object publishCommnet(int userid,int worksid,String content){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        Works_comment worksComment = new Works_comment();
        worksComment.setUserid(userid);
        worksComment.setWorksid(worksid);
        worksComment.setContent(content);
        works_commnetMapper.insert(worksComment);
        map.put("res",1);
        queryWrapper.eq("worksid",worksid);
        List<Works_comment> works_comments = works_commnetMapper.selectList(queryWrapper);//评论
        List<Map> list = new ArrayList();

        for(Works_comment works_comment:works_comments){//for  用户名 id 内容 》》》通过remap 添加到 list
            Map<String,Object> remap = new HashMap<>();
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("userid",works_comment.getUserid());
            Userinfo userinfo = userinfoMapper.selectOne(queryWrapper);
            remap.put("userinfo",userinfo);
            remap.put("comment",works_comment);
            list.add(remap);
        }
        map.put("comment",list);
        return map;
    }
    //搜索
    @RequestMapping("seek")
    @ResponseBody
    public Object seek(String word){
        Map<String ,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("title",word);
//        queryWrapper.eq("title",word);
        List<Works> list = worksMapper.selectList(queryWrapper);
        map.put("works",list);
        System.out.println(list.size());
        return map;


    }
    //subarea 通过id查找
    @RequestMapping("sub")
    @ResponseBody
    public Object sub(int subareaid) {
        Map<String ,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("subareaid",subareaid);
        List<Works> works = worksMapper.selectList(queryWrapper);
        map.put("works",works);
        System.out.println(works.size());
        return map;
    }
}
