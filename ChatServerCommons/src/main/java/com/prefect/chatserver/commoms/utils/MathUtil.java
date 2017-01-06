package com.prefect.chatserver.commoms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * 数学计算工具类
 * Created by zhangkai on 2017/1/6.
 */
public class MathUtil {
    private static Logger logger= LoggerFactory.getLogger(MathUtil.class);

    public String worldChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private MathUtil() {
    }

    private static class MathUtilHandle {
        private static MathUtil instance = new MathUtil();
    }

    public static MathUtil getInstance() {
        return MathUtilHandle.instance;
    }

    /**
     * 生成定常的随机字符串
     * @param length
     * @return
     */
    public String getRandomStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(worldChar.charAt(random.nextInt(58)));
        }
        return sb.toString();
    }

    public String getMD5(String str){
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            logger.error("MD5加密失败:"+e.getMessage(),e);
            return null;
        }
    }

    public static void main(String[] args){
        String password="this is a test";
        System.out.println(MathUtil.getInstance().getMD5(password));
        System.out.println(MathUtil.getInstance().getMD5(password));
//        System.out.println(MathUtil.getInstance().getRandomStr(1000));
    }

}
