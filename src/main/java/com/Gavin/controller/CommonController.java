package com.Gavin.controller;

import com.Gavin.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: Gavin
 * @description:    文件上传下载
 * @className: CommonController
 * @date: 2022/6/5 18:15
 * @version:0.1
 * @since: jdk14.0
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${file-load.path}")
    private String basepath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {    //MultipartFile 是spring封装的文件上传下载类，值得主义的是对象名不能乱写，必须和form上传的一致
        //file是个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //判断目录是否存在
        File dir=new File(basepath);

        if (!dir.exists())
            dir.mkdirs();

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName= UUID.randomUUID().toString()+originalFilename+suffix;
        file.transferTo(new File(basepath+fileName));

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {    //从response里获得流
        //输入流，通过输入流读取文件内容
        FileInputStream fileInputStream=new FileInputStream(new File(basepath+name));

        //输出流，通过输出流将文件写回浏览器
        ServletOutputStream outputStream=response.getOutputStream();

        response.setContentType("image/jpeg");
        int len=0;
        byte[] bytes=new byte[1024];
        while((len=fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭流
        outputStream.close();
        fileInputStream.close();
    }

}
