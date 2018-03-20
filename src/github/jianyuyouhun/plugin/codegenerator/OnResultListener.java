package github.jianyuyouhun.plugin.codegenerator;

/**
 * 结果回调监听器
 * Created by wangyu on 2017/4/18.
 */
public interface OnResultListener<Info> {
    /** 请求成功 */
    int RESULT_SUCCESS = 0;

    /** 请求失败 */
    int RESULT_FAILED = -1;

    /** 无数据 */
    int RESULT_NO_DATA = -2;

    /**
     * 请求结果回调
     *
     * @param result 请求结果
     * @param data   结果
     */
    void onResult(int result, Info data);

}
