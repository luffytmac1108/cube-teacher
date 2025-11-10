package com.yxw.cube.teacher.service;

import com.alibaba.excel.EasyExcel;
import com.yxw.cube.teacher.dto.InputUser;
import com.yxw.cube.teacher.dto.OutputUser;
import com.yxw.cube.teacher.processor.WordProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service // 如果在Spring Boot项目中使用，可以添加此注解
public class UserService {

    private final String EXCEL_INPUT_PATH = "C:\\xwdata\\HXX\\input_users.xlsx";
    private final String EXCEL_OUTPUT_PATH = "C:\\xwdata\\HXX\\output_users.xlsx";
    private final String WORD_DIR_PATH = "C:\\xwdata\\HXX\\docs";

    /**
     * 核心业务逻辑：读取Excel，遍历处理，写入新的Excel
     */
    public void processData() {
        // 1. 读取所有输入 Excel 数据
        List<InputUser> inputUsers = EasyExcel.read(EXCEL_INPUT_PATH)
                .head(InputUser.class)
                .sheet()
                .doReadSync(); // 同步读取

        log.info("成功读取 {} 条用户记录。", inputUsers.size());

        // 2. 遍历处理每条数据
        List<OutputUser> outputUsers = inputUsers.stream()
                .map(this::processSingleUser)
                .collect(Collectors.toList());

        // 3. 写入新的 Excel
        EasyExcel.write(EXCEL_OUTPUT_PATH, OutputUser.class)
                .sheet("整合后的数据")
                .doWrite(outputUsers);

        log.info("数据处理完成，结果已写入: " + EXCEL_OUTPUT_PATH);
    }

    /**
     * 处理单个用户，查找对应Word文档并整合数据
     */
    private OutputUser processSingleUser(InputUser inputUser) {
        OutputUser outputUser = new OutputUser();
        // 复制 Excel 中的基础信息
        outputUser.setName(inputUser.getName());
        outputUser.setAge(inputUser.getAge());
        outputUser.setDepartment(inputUser.getDepartment());

        // 假设 Word 文档以“姓名.docx”命名，例如：张三.docx
        String wordFileName = inputUser.getName() + ".docx";
        Path wordFilePath = Paths.get(WORD_DIR_PATH, wordFileName);

        if (!Files.exists(wordFilePath)) {
            log.error("未找到对应的Word文档: {}", wordFilePath);
            // 可以设置为默认值或空
            outputUser.setAddress("N/A");
            outputUser.setPreference("N/A");
            return outputUser;
        }

        try {
            // 调用Word解析工具类
            Map<String, String> wordData = WordProcessor.extractDataFromWord(wordFilePath);

            outputUser.setAddress(wordData.getOrDefault("address", "Word解析失败"));
            outputUser.setPreference(wordData.getOrDefault("preference", "Word解析失败"));

        } catch (IOException e) {
            log.error("处理Word文档时发生错误: {}. 错误: {}", wordFilePath, e.getMessage());
            outputUser.setAddress("Word处理异常");
            outputUser.setPreference("Word处理异常");
        }

        return outputUser;
    }

    public static void main(String[] args) {
        // 模拟运行
        new UserService().processData();
    }
}
