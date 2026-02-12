package com.yupi.yupao.once.importuser;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 导入 Excel 示例（读取 Excel 并输出解析结果）。
 *
 * @author Ethan
 */
public class ImportExcel {

    /**
     * 读取数据（示例入口）。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // todo 记得改为自己的测试文件
        String fileName = "E:\\星球项目\\yupao-backend\\src\\main\\resources\\testExcel.xlsx";
//        readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 监听器读取（按行回调处理）。
     *
     * @param fileName Excel 文件路径
     */
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读取（一次性把数据读入内存）。
     *
     * @param fileName Excel 文件路径
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<XingQiuTableUserInfo> totalDataList =
                EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();
        for (XingQiuTableUserInfo xingQiuTableUserInfo : totalDataList) {
            System.out.println(xingQiuTableUserInfo);
        }
    }

}
