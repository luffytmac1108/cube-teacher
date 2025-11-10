package com.yxw.cube.teacher.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WordProcessor {

    /**
     * 从Word文档中提取特定的表格数据。
     * @param wordFilePath Word文档的完整路径
     * @return 包含户籍地址和个人喜好的Map
     * @throws IOException 文件读取异常
     */
    public static Map<String, String> extractDataFromWord(Path wordFilePath) throws IOException {
        Map<String, String> data = new HashMap<>();

        try (InputStream fis = new FileInputStream(wordFilePath.toFile());
             XWPFDocument document = new XWPFDocument(fis)) {

            // 假设所有目标数据都在Word的第一个表格中
            if (document.getTables().isEmpty()) {
                log.warn("Word文档中没有找到表格: {}", wordFilePath);
                return data;
            }

            XWPFTable table = document.getTables().get(0);

            // 这里的索引是关键，需要根据你的实际Word表格结构来调整
            // 假设：
            // 户籍地址 在第 1 行 (索引 0)，第 2 列 (索引 1)
            // 个人喜好 在第 2 行 (索引 0)，第 2 列 (索引 1)

            // 户籍地址
            try {
                // 假设：户籍地址在第0行，第1个单元格（从0开始计数）
                XWPFTableRow addressRow = table.getRow(0);
                String address = addressRow.getCell(1).getText().trim();
                data.put("address", address);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                log.error("无法从Word获取户籍地址: {}", wordFilePath);
            }

            // 个人喜好
            try {
                // 假设：个人喜好在第1行，第1个单元格
                XWPFTableRow preferenceRow = table.getRow(1);
                String preference = preferenceRow.getCell(1).getText().trim();
                data.put("preference", preference);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                log.error("无法从Word获取个人喜好: {}", wordFilePath);
            }
        }
        return data;
    }
}