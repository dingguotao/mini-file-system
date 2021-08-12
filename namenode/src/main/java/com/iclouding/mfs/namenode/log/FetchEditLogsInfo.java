package com.iclouding.mfs.namenode.log;

import java.util.List;

/**
 * FetchEditLogsInfo
 * 封装拉取到的editlog
 * @author: iclouding
 * @date: 2021/8/11 15:53
 * @email: clouding.vip@qq.com
 */
public class FetchEditLogsInfo {
    private List<String> fetchEditLogs;
    private boolean hasMore;

    public List<String> getFetchEditLogs() {
        return fetchEditLogs;
    }

    public void setFetchEditLogs(List<String> fetchEditLogs) {
        this.fetchEditLogs = fetchEditLogs;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
