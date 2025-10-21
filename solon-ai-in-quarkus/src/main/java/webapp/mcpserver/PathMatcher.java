package webapp.mcpserver;

public class PathMatcher {
    public static boolean isMatch(String patternPath, String realPath) {
        // 分割路径为片段（过滤空字符串）
        String[] patternSegments = splitPath(patternPath);
        String[] realSegments = splitPath(realPath);

        // 片段数量不同，直接不匹配
        if (patternSegments.length != realSegments.length) {
            return false;
        }

        // 逐个对比片段
        for (int i = 0; i < patternSegments.length; i++) {
            String patternSeg = patternSegments[i];
            String realSeg = realSegments[i];

            // 动态参数片段（:xxx）可匹配任意非空片段
            if (patternSeg.startsWith(":")) {
                // 确保动态参数对应的值非空（根据业务需求可调整）
                if (realSeg.isEmpty()) {
                    return false;
                }
            } else {
                // 静态片段必须完全相等
                if (!patternSeg.equals(realSeg)) {
                    return false;
                }
            }
        }

        return true;
    }

    // 分割路径为片段，过滤空字符串（处理连续/或首尾/的情况）
    private static String[] splitPath(String path) {
        return path.split("/");
    }

    public static void main(String[] args) {
        String path = "/api/hello/:name";
        String realPath = "/api/hello/MrYang";
        System.out.println(isMatch(path, realPath)); // 输出：true
    }
}
