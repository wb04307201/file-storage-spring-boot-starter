package cn.wubo.file.storage.utils;

import java.util.Arrays;

public class UrlUtils {


    private UrlUtils() {
    }

    /**
     * 获取父路径
     */
    public static String getParent(String path) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length() - 1);
        }
        int endIndex = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
        return endIndex > -1 ? path.substring(0, endIndex) : null;
    }

    /**
     * 合并url
     */
    public static String join(String... paths) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(paths).forEach(path -> {
            String left = sb.toString();
            boolean leftHas = left.endsWith("/") || left.endsWith("\\");
            boolean rightHas = path.endsWith("/") || path.endsWith("\\");

            if (leftHas && rightHas) {
                sb.append(path.substring(1));
            } else if (!left.isEmpty() && !leftHas && !rightHas) {
                sb.append("/").append(path);
            } else {
                sb.append(path);
            }
        });
        return sb.toString();
    }
}
