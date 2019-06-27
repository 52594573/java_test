package com.ktp.project.service.realName;

public interface SdAuthRealNameApi {
    /**
     * 添加/更新采集人脸图片ID
     * @param userId 用户ID
     */
    public void saveOrUpdateFaceAcquisition(Integer userId);

    /**
     * 添加/更新考勤机
     * @param projectId
     */
    public void saveOrUpdateAttendanceMachine(Integer projectId);

    /**
     * 批量上传考勤记录
     * @param projectId
     */
    public void synWorkerAttendanceMore(Integer projectId);

    /**
     * 同步上传文件
     */
    public void uploadFile();

    /**
     * 根据项目创建文件夹(一个项目一个文件夹)
     */
    public void createFileDirForProject(Integer projectId);
}
