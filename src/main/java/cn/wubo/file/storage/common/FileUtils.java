package cn.wubo.file.storage.common;

import org.springframework.util.StringUtils;

public class FileUtils {

    /**
     * 特殊后缀
     */
    private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

    /**
     * 路径分隔符
     */
    public static final CharSequence[] SEPARATOR = {"/", "\\"};

    public static final String DOT = ".";

    public static final String EMPTY = ".";

    /**
     * 获得文件的扩展名（后缀名），扩展名不带“.”
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        final int index = fileName.lastIndexOf(DOT);
        if (index == -1) {
            return EMPTY;
        } else {
            final int secondToLastIndex = fileName.substring(0, index).lastIndexOf(DOT);
            final String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
            if (containsAny(substr, SPECIAL_SUFFIX)) {
                return substr;
            }

            final String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return containsAny(ext, SEPARATOR) ? EMPTY : ext;
        }
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        if (!StringUtils.hasLength(str) || arrayIsEmpty(testStrs)) {
            return false;
        }
        for (CharSequence checkStr : testStrs) {
            if (str.toString().contains(checkStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组是否为空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean arrayIsEmpty(T[] array) {
        return array == null || array.length == 0;
    }
}
