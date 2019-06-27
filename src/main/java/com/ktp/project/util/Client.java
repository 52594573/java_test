package com.ktp.project.util;

import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.service.AuthRealNameLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.Socket;
import java.util.Date;

@Service("client")
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private Socket socket = null;
    private OutputStream os = null;
    private InputStream is =null;

    @Resource
    private AuthRealNameLogService authRealNameLogService;

    public static void main(String[] args) {
        new Client().new SocketThread(1,"","","").start();
    }
    public void sendKaoqin(Integer projectId,String kaoqinTime,String sfz,String pictureUrl){
        new Client().new SocketThread(projectId,kaoqinTime,sfz,pictureUrl).start();
    }

    /**
     * 发送心跳包
     */
    public void sendHeartbeat() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(20 * 1000);// 10s发送一次心跳
                            //byte[] xintiao = ZHSocketSend.heartBeat();
                            //发送数据845
                            int[] bb =ZHSocketSend.heartBeat();
                            log.info("心跳发送数据：" );
                            sendByFor(os,bb);
                            System.out.println("");
                        } catch (Exception e) {
                            //e.printStackTrace();
                            log.info("socket关闭！");
                            return;
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SocketThread extends Thread{
        private String kaoqinTime;
        private String sfz;
        private String pictureUrl;
        private Integer projectId;

        @Override
        public void run() {

            long startTime = System.currentTimeMillis();
            sendHeartbeat();
            try{
                if (socket == null || socket.isClosed()) {
                    log.info("ip:"+RealNameConfig.ZHZH_BASE_PATH204+"端口："+RealNameConfig.ZHZH_PORT_PATH);
                    socket = new Socket(RealNameConfig.ZHZH_BASE_PATH204 , Integer.parseInt(RealNameConfig.ZHZH_PORT_PATH)); // 连接socket
                    //socket = new Socket("119.23.147.62", 9610); // 连接socket
                    os = socket.getOutputStream();
                }

            //发送数据843
            int[] aa =ZHSocketSend.get843();
            log.info("843数据：" );
            sendByFor(os,aa);
            }catch (Exception e){
                log.info("socket链接失败");
            }

            while (true) {
                try {
                   Thread.sleep(100);
                    is = socket.getInputStream();
                   /* DataInputStream dis = null;
                    dis = new DataInputStream(socket.getInputStream());*/
                    int size = is.available();

                    if (size <= 0) {
                        if ((System.currentTimeMillis() - startTime) > 30 * 1000) { // 如果超过30秒没有收到服务器发回来的信息，说明socket连接可能已经被远程服务器关闭
                            socket.close(); // 这时候可以关闭socket连接
                            startTime = System.currentTimeMillis();
                        }
                        continue;
                    } else {
                        startTime = System.currentTimeMillis();
                    }
                    //换行
                    byte[] resp = new byte[size];
                    log.info("开始获取数据");
                    is.read(resp);
                    //845数据太长了，不截了
                    if(getCommand(resp)==845 || getFlag(resp)){
                            if(sendSocket(getCommand(resp),os,resp,kaoqinTime,sfz,pictureUrl)){
                                //发送完成结束
                                socket.close();
                                is.close();
                                os.close();
                                return;
                            }
                    }else {
                        //同步上传考勤失败
                        AuthRealNameLog authRealNameLog = new AuthRealNameLog();
                        authRealNameLog.setCreateTime(new Date());
                        authRealNameLog.setIsSuccess(0);
                        authRealNameLog.setProjectId(projectId);
                        authRealNameLog.setRemark("同步考勤员工信息");
                        authRealNameLog.setReqBody(sfz);
                        authRealNameLog.setResMsg("失败");
                        authRealNameLogService.saveOrUpdate(authRealNameLog);
                        log.info("返回数据显示失败！结束发送！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        os.close();
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        public SocketThread(Integer projectId,String kaoqinTime,String sfz,String pictureUrl) {
            super();
            this.kaoqinTime = kaoqinTime;
            this.sfz = sfz;
            this.pictureUrl = pictureUrl;
            this.projectId = projectId;
        }

        public String getKaoqinTime() {
            return kaoqinTime;
        }

        public void setKaoqinTime(String kaoqinTime) {
            this.kaoqinTime = kaoqinTime;
        }

        public String getSfz() {
            return sfz;
        }

        public void setSfz(String sfz) {
            this.sfz = sfz;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }
    }
    /**
    *
    * @Description: 通过循环发送
    * @Author: liaosh
    * @Date: 2019/1/23 0023
    */
    private void sendByFor(OutputStream os,int[] aa)throws Exception{

            for (int i = 0; i < aa.length; i++) {
                int i1 = aa[i];
                System.out.print(i1 +" " );
                os.write(i1);
                os.flush();
            }
    }

    /**
     *判断那个接口返回的数据
     */
    private int getCommand(byte[] rep){
        byte[] bb =ByteUtil.subBytes(rep,14,2);
        short sho = ByteUtil.bytesToShort(bb);
        log.info("解析接口："+sho);
        return sho;
    }
    /**
     *判断返回成功还是失败
     */
    private boolean getFlag(byte[] rep){
        log.info("开始解析数据为：");
        for (int i = 0; i < rep.length; i++) {
            byte b = rep[i];
            System.out.print(b +" ");
        }

        byte[] bb =ByteUtil.subBytes(rep,1,4);
        int lenght = ByteUtil.bytesToInt(bb,0);
        log.info("");
        log.info("总数据长度："+rep.length);
        log.info("排除数据头尾后数据有效长度："+lenght);

        //short sho = ByteUtil.bytesToShort(bb);
        byte[] flag =ByteUtil.subBytes(rep,32+lenght,1);
        byte fla = flag[0];
        //返回状态值
        int ty = ByteUtil.byte2Int(fla);
        log.info("结束解析结果："+ty);
        if(ty==0){
            return true;
        }
        return false;
    }
    /**
     *获取人工编号
     */
    private Integer getUserCode(byte[] rep){

        byte[] bb =ByteUtil.subBytes(rep,32 ,4);
        Integer userId = ByteUtil.bytesToInt(bb,0);
        log.info("珠海整合考勤人工编号："+userId);
        return userId;
    }

    private boolean sendSocket(int sign,OutputStream os,byte[] resp,String kaoqinTime,String sfz,String pictureUrl){
        try {
            int[] dd = null;
            if(sign==843){
                //发送845
                log.info("845发送数据！");
                dd =ZHSocketSend.getUser845(sfz);
                //dd =ZHSocketSend.getUser845("340825198210284810123423");
            }else if(sign == 845){
                //发送848
                //获取人工编号usercode
                log.info("848发送数据！");
                Integer usercode = getUserCode(resp);
                dd =ZHSocketSend.datatobyte(usercode,kaoqinTime,pictureUrl);
                //dd =ZHSocketSend.datatobyte(usercode,"20180123111923","https://images.ktpis.com/images/pic/20180713184958821297033002.jpg");
            }else if(sign==65535){
                return false;
            }else if(sign == 848){
                log.info("珠海整合考勤成功！");
                return true;
            }else{
                return false;
            }
            log.info("发送字节数据为：");

            sendByFor(os,dd);

           /* for (int i = 0; i < dd.length; i++) {
                int i1 = dd[i];
                //log.info(i1 + " ");
                System.out.print(i1+" ");
                os.write(i1);
                os.flush();
            }*/
            return false;
            //log.info("");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public static byte[] getStringFromInputStream(InputStream is) {
        byte[] result = null;
        try {
        DataInputStream input = new DataInputStream(is);

            byte[] buffer;
            buffer = new byte[input.available()];
            if (buffer.length != 0) {
                //System.out.println("length=" + buffer.length);
                // 读取缓冲区
                input.read(buffer);
            }
            log.info("开始解析数据：");
            for (int i = 0; i < buffer.length; i++) {
                byte b = buffer[i];
                System.out.print(b +" ");
            }
            System.out.println("");
            return buffer;
        } catch (Exception e) {
        }
        return result;
    }

}
