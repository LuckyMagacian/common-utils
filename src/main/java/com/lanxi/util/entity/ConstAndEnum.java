package com.lanxi.util.entity;


/**
 * Created by yangyuanjian on 2017/6/21.
 */
public class ConstAndEnum {


    /**
     * 中国 省份及直辖市枚举
     */
    public static enum ProvinceEnum{
        BeiJing  (11, "北京"),TianJin     (12,"天津")   ,HeBei   (13,"河北"),
        ShanXi   (14,"山西") ,NeiMengGu   (15,"内蒙古") ,LiaoNing(21,"辽宁"),
        JiLin    (22, "吉林"),HeiLongJiang(23, "黑龙江"),ShangHai(31, "上海"),
        JiangSu  (32, "江苏"),ZheJiang    (33, "浙江")  ,AnHui   (34, "安徽"),
        FuJian   (35, "福建"),JiangXi     (36, "江西")  ,ShanDong(37, "山东"),
        HeNan    (41, "河南"),HuBei       (42, "湖北")  ,HuNan   (43, "湖南"),
        GuangDong(44, "广东"),GuangXi     (45, "广西")  ,HaiNan  (46, "海南"),
        ChongQing(50, "重庆"),SiChuan     (51, "四川")  ,GuiZhou (52, "贵州"),
        YunNan   (53, "云南"),XiZang      (54, "西藏")  ,ShaanXi (61,"陕西") ,
        GanSu    (62, "甘肃"),QingHai     (63, "青海")  ,XinJiang(64, "新疆"),
        TaiWan   (71, "台湾"),XiangGang   (81, "香港")  ,AoMen   (82, "澳门"),
        GuoWai   (91, "外国");

        private String name;
        private int id;
        ProvinceEnum(int id,String name){
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    };

   
}
