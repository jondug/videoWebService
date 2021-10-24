package org.webexample.bilibili.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webexample.bilibili.mapper.UserfansMapper;
import org.webexample.bilibili.mapper.UserinfoMapper;
import org.webexample.bilibili.mapper.UserMapper;
import org.webexample.bilibili.mapper.WorksMapper;
import org.webexample.bilibili.model.User;
import org.webexample.bilibili.model.Userfans;
import org.webexample.bilibili.model.Userinfo;
import org.webexample.bilibili.model.Works;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    UserfansMapper userfansMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserinfoMapper userinfoMapper;
    @Autowired
    WorksMapper worksMapper;

    @RequestMapping("login")
    @ResponseBody
    public Object login(String account ,String pwd){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account",account);
        List<User> users = userMapper.selectList(queryWrapper);
        if(users.size()<=0){
            map.put("res",-1);
            map.put("msg","账号不存在");
            return map;
        }
        queryWrapper.eq("pwd",pwd);
        List<User> users2 = userMapper.selectList(queryWrapper);
        if(users2.size()>0){
            map.put("res",1);
            map.put("userid",users2.get(0).getUserid());
            map.put("msg","登录成功");
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("userid",users2.get(0).getUserid());
            List<Userinfo> usersInfo = userinfoMapper.selectList(queryWrapper);
            map.put("info",usersInfo.get(0));
            System.out.println(usersInfo.get(0).getNickname());
//            map.put("userid",users.get(0).getUserid());
        }else {
            map.put("res",-2);
            map.put("msg","密码错误");
        }
        return map;

    }
    @RequestMapping("register")
    @ResponseBody
    public Object register(String account,String pwd,String enterpwd){
        Map<String,Object> map = new HashMap<>();
        if(!pwd.equals(enterpwd)){
            map.put("res",-1);
            map.put("msg","两次密码输入不一致");
            return map;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account",account);
        List<User> users = userMapper.selectList(queryWrapper);
        if(users.size()>0){
            map.put("res",-1);
            map.put("msg","用户已存在");
            return map;
        }
//        queryWrapper.eq("account",account);
        User user  = new User();
        user.setAccount(account);
        user.setPwd(pwd);
        userMapper.insert(user);
        User user1 = userMapper.selectOne(queryWrapper);
        Userinfo userinfo = new Userinfo();
        userinfo.setUserid(user1.getUserid());
        userinfoMapper.insert(userinfo);
        map.put("res",1);
        map.put("msg","注册成功");
        return map;
    }
    @RequestMapping("updatauser")
    @ResponseBody
    public Object updatauser(String userid,String nickname, String signature ){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        Userinfo userinfo = new Userinfo();
        userinfo.setNickname(nickname);
        userinfo.setSignature(signature);
        userinfoMapper.update(userinfo,queryWrapper);
        map.put("res",1);
        map.put("msg","修改成功");
        return map;

    }

    @RequestMapping("attention")
    @ResponseBody
    public Object attention(int fansid,int userid){
        Map<String,Object> map = new HashMap<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("fansid",fansid);
        queryWrapper.eq("userid",userid);
        List<Userfans> userfans = userfansMapper.selectList(queryWrapper);
        if(userfans.size()>0){
            userfansMapper.delete(queryWrapper);

            map.put("res",0);
            map.put("msg","已取消关注");
            return map;
        }
        Userfans userfans1 = new Userfans();
        userfans1.setUserid(userid);
        userfans1.setFansid(fansid);
        userfansMapper.insert(userfans1);
        //用户加关注，关注数加一
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",fansid);
        Userinfo fens = userinfoMapper.selectOne(queryWrapper);
        fens.setAttention(fens.getAttention()+1);
        userinfoMapper.update(fens,queryWrapper);
        //up粉丝加一
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        Userinfo user = userinfoMapper.selectOne(queryWrapper);
        user.setFans(user.getFans()+1);
        userinfoMapper.update(user,queryWrapper);

        map.put("res",1);
        map.put("msg","关注成功");
        return map;
    }

    @RequestMapping("checkattention")
    @ResponseBody
    public Object checkattention(int fansid,int worksid){
        Map<String,Object> map = new HashMap<>();


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("worksid",worksid);
        Works works = worksMapper.selectOne(queryWrapper);
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("fansid",fansid);
        queryWrapper.eq("userid",works.getUserid());
        List<Userfans> userfans = userfansMapper.selectList(queryWrapper);
        if(userfans.size()>0){

            map.put("res",1);
            map.put("msg","已关注");
            return map;
        }
        map.put("res",0);
        map.put("msg","未关注");
        return map;
    }
}
