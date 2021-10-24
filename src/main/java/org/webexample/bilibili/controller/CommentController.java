package org.webexample.bilibili.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webexample.bilibili.mapper.UserinfoMapper;
import org.webexample.bilibili.mapper.Works_commnetMapper;
import org.webexample.bilibili.model.Userinfo;
import org.webexample.bilibili.model.Works_comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    Works_commnetMapper works_commnetMapper;
    @Autowired
    UserinfoMapper userinfoMapper;
    //每个works的评论
    @RequestMapping("list")
    @ResponseBody
    public Object list(int worksid){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        List<Works_comment> works_comments = works_commnetMapper.selectList(queryWrapper);
        List<Map> list = new ArrayList();
        for(Works_comment works_comment:works_comments){
            Map<String,Object> remap = new HashMap<>();
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("userid",works_comment.getUserid());
            Userinfo userinfo = userinfoMapper.selectOne(queryWrapper);

            remap.put("userinfo",userinfo);
            remap.put("commentid",works_comment);
//            remap.put("nickname",userinfo.getNickname());
//            remap.put("comment",works_comment.getContent());

//            System.out.println(remap);
            list.add(remap);
        }
        map.put("res",list);
        return map;

    }
}
