
public class DongziYear{
	//----------------冬至年部分
	
    public static final int SEARCH_STEP=4;//
    public static final long dzPosition[]={//天文台数据，基于东经120(呼伦贝尔|秦皇岛|青岛|杭州|丽水|台南)。
        //4个一组，分别表示：回归年，回归日，当天到冬至点的回归秒余，朔积。所有数据都是基于0.
        //冬至年数，    玄黄日数，   回归余秒，    朔积
        //    0,    292756227,         0,        0, //0  //average year length=365.24220723187403
              0,    292734712,         0,        0, //0  //average year length=365.24300008682616
        2757783,   1300014953,         0,        0, //   黄帝历元，子时子正冬至，正朔，
        2758380,   1300233026,     48900,        0, //  西前2100年1月6日13：35冬至
        2758386,   1300235218,      3240,     2043, //1 BC2094 1/4 19:50丙寅日环食食甚 1/7  0:54己巳冬至
        2758405,   1300242157,     56160,     1127, //2 BC2075 1/4 19:13丙午日全食食甚 1/6 15:36戊申冬至
        2759475,   1300632967,     69900,     3117, //3 BC1006 12/26 16:24甲戌日全食食甚 12/30 19:25戊寅冬至
        2760434,   1300983235,     81060,     7954, //4 BC47 12/14 12:54丁巳日环食食甚 12/23 22:31丙寅冬至
        2760499,   1301006976,     63120,     7138, //5 AD19 12/15  9:45已亥日环食食甚 12/23 17:32丁未冬至
        2762380,   1301693998,     52894,      626, //6 AD1900 12/22 8:1:15己巳日初一，14：41：34冬至
        2762679,   1301803206,     17336,        0, //8 AD2199/12/22 冬至
        3888000,   1712818261,         0,        0, //9 3888000 冬至
        3999999,   1753725055,         0,        0, //10
    };
    public static int searchDzPositionIndex(long dzYear) { //2分法快速查找已排序数组。
        if(dzYear<0)dzYear=0;if(dzYear>3888000)dzYear=3888000;
        if(dzAverageYearLength[0]<1) {//still not initialized.
            initDzAverageYearLength();
        }
        int min=0,max=dzPosition.length/SEARCH_STEP-1,mid=(min+max)/2;
        while(min!=mid) { if(dzPosition[mid*SEARCH_STEP]<=dzYear)  min=mid;
            else  max=mid;  mid=(min+max)/2; } return min;
    }

    public static double dzYearStartOffset[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static double dzAverageYearLength[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static void initDzAverageYearLength() {
        if(dzAverageYearLength[0]<1) {//still not initialized.
            int min,max=dzPosition.length/SEARCH_STEP-1;
            for(min=0;min<max;min++) {
                dzYearStartOffset[min]=dzPosition[min*SEARCH_STEP+2]/86400.0d;
                dzAverageYearLength[min]=(
                        (dzPosition[min*SEARCH_STEP+5]-dzPosition[min*SEARCH_STEP+1])-1 //总天数-1
                        +  (86400+dzPosition[min*SEARCH_STEP+SEARCH_STEP+2]-
                                dzPosition[min*SEARCH_STEP+2])/86400.0d) //秒数再借一天，转天数
                        /((dzPosition[min*SEARCH_STEP+SEARCH_STEP]-
                          dzPosition[min*SEARCH_STEP]));//除以年数，换成平均年长。
                //Log.d("xinhe", "["+min+"] average year length="+dzAverageYearLength[min]);
            }
        }
        /** result:
         [0] average year length=365.24299156242535
         [1] average year length=365.28235506234876
         [2] average year length=365.24525462962964
         [3] average year length=365.24276315789473
         [4] average year length=365.243139278297
         [5] average year length=365.2430961070559
         [6] average year length=365.2429594017094
         [7] average year length=365.2428929524288
         [8] average year length=365.2427707326892
         [9] average year length=365.24249951733935
         [10]average year length=365.24249323654675
         */
    }

    //查表计算结果寄丰器。
    public int seekTableIndex=0; //查dzPosition[]表的行号位置，每SEARCH_STEP，即4个元素为一行
    public double seekYearDoublePos = 0;//本年冬至的天位置
    public double seekYearDoubleNextPos=0;//下年冬至的天位置
    public double seekYearDoubleOffset=0;//本年冬至日的当天的小数部分
    public long seekYearPos=0;//本年冬至的天位置
    public long seekYearNextPos=0;//下年冬至的天位置
    public long seekYearLength=0;//365, 366
    public long seekYearSecondOffset=0;//冬至天冬至点的秒数位置，由seekYearDoubleOffset*86400得
    public long seekYearSuBaseAcc =0;//朔月的查表基准朔积
    public long seekYearSuBasePos =0;//朔月的查表基准日位置

    //本年查表，算出来玄黄日开始天数，和小数部分
    public long getDzPosition(long dzYear) {
        seekTableIndex=searchDzPositionIndex(dzYear);
        seekYearSuBasePos =dzPosition[seekTableIndex*SEARCH_STEP+1];
        seekYearSuBaseAcc =dzPosition[seekTableIndex*SEARCH_STEP+3];
        seekYearDoublePos = dzPosition[seekTableIndex*SEARCH_STEP+1] + dzYearStartOffset[seekTableIndex]+
                (dzYear-dzPosition[seekTableIndex*SEARCH_STEP])*dzAverageYearLength[seekTableIndex];
        seekYearDoubleNextPos=seekYearDoublePos+dzAverageYearLength[seekTableIndex];
        seekYearPos=(long)seekYearDoublePos;
        seekYearNextPos=(long)seekYearDoubleNextPos;
        seekYearLength=seekYearNextPos-seekYearPos;
        seekYearDoubleOffset=seekYearDoublePos-seekYearPos;
        seekYearSecondOffset=(long)(86400*(seekYearDoubleOffset));
        return seekYearPos;
    }
    public String toSeekDzString() {
		StringBuilder sb=new StringBuilder();
		//sb.append("玄黄流日：#"+xhDay + " 流年#"+xhYearId+" 12/23 1/365\n");
		sb.append("冬至表格：#"+ seekTableIndex + " avg="+dzAverageYearLength[seekTableIndex]+"\n");
		sb.append("    xhDay=#"+seekYearPos+" dzOffset="+seekYearDoubleOffset+" days="+seekYearLength+"\n"
				+"    "+JiaziName[jiaziFromDay(seekYearPos)]
				+"日  "+seekYearDoublePos+" to "+seekYearDoubleNextPos);
		return sb.toString();
    }

  //----------------计日历部分
    public static final String[] JiaziName = {
        //"0A","0B","0C","0D","0E","0F","0G","0H","0I","0J",
        //"1K","1L","1A","1B","1C","1D","1E","1F","1G","1H",
        //"2I","2J","2K","2L","2A","2B","2C","2D","2E","2F",
        //"3G","3H","3I","3J","3K","3L","3A","3B","3C","3D",
        //"4E","4F","4G","4H","4I","4J","4K","4L","4A","4B",
        //"5C","5D","5E","5F","5G","5H","5I","5J","5K","5L",
        "甲子","乙丑","丙寅","丁卯","戊辰","己巳","庚午","辛未","壬申","癸酉",
        "甲戌","乙亥","丙子","丁丑","戊寅","己卯","庚辰","辛巳","壬午","癸未",
        "甲申","乙酉","丙戌","丁亥","戊子","己丑","庚寅","辛卯","壬辰","癸巳",
        "甲午","乙未","丙申","丁酉","戊戌","己亥","庚子","辛丑","壬寅","癸卯",
        "甲辰","乙巳","丙午","丁未","戊申","己酉","庚戌","辛亥","壬子","癸丑",
        "甲寅","乙卯","丙辰","丁巳","戊午","己未","庚申","辛酉","壬戌","癸亥",

        "甲子","乙丑","丙寅","丁卯","戊辰","己巳","庚午","辛未","壬申","癸酉","甲戌","乙亥",
        "丙子","丁丑","戊寅","己卯","庚辰","辛巳","壬午","癸未","甲申","乙酉","丙戌","丁亥",
        "戊子","己丑","庚寅","辛卯","壬辰","癸巳","甲午","乙未","丙申","丁酉","戊戌","己亥",
        "庚子","辛丑","壬寅","癸卯","甲辰","乙巳","丙午","丁未","戊申","己酉","庚戌","辛亥",
        "壬子","癸丑","甲寅","乙卯","丙辰","丁巳","戊午","己未","庚申","辛酉","壬戌","癸亥",
    };
    //玄黄日推算甲子日
    public static int jiaziFromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+7)%60);
    }
    //玄黄日推算二十八星宿日
    public static int star28FromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+13)%28);
    }
    //玄黄日推算建除12神。
    public static int jianchu12FromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+9)%12);//
    }
    //玄黄日推算七星，七曜序列，星期几
    public static int qiyaoFromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+3)%7);
    }

    //----------------玄黄参考历部分
    //每个周期是4年，1461天,每天是第？年月日，规律是固定的，就不用计算了，直接查表就好。
    //根据天数查4年1461天的每天所在的日期
    public static final int[] julianDate1461 = {
            //--------第0年--------
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 1
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,         //28 2
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 3
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 4
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 5
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 6
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 7
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 8
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 9
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 10
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 11
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 12
            //--------第1年--------
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 1
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,         //28 2
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 3
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 4
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 5
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 6
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 7
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 8
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 9
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 10
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 11
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 12
            //--------第2年--------
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 1
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,         //28 2
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 3
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 4
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 5
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 6
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 7
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 8
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 9
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 10
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 11
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 12
            //--------第3年--------闰年
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 1
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,      //28 2 闰日
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 3
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 4
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 5
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 6
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 7
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 8
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 9
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 10
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,   //30 11
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 12
    };
    //根据天数查4年1461天的每天所在的月份
    public static final int[] julianMonth1461 = {
            //--------第0年--------
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,      //28 2
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 3
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 4
            4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,//31 5
            5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,  //30 6
            6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,//31 7
            7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,//31 8
            8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,  //30 9
            9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,//31 10
            10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,  //30 11
            11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,//31 12
            //--------第1年--------
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,      //28 2
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 3
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 4
            4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,//31 5
            5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,  //30 6
            6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,//31 7
            7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,//31 8
            8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,  //30 9
            9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,//31 10
            10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,  //30 11
            11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,//31 12
            //--------第2年--------
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,      //28 2
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 3
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 4
            4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,//31 5
            5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,  //30 6
            6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,//31 7
            7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,//31 8
            8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,  //30 9
            9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,//31 10
            10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,  //30 11
            11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,//31 12
            //--------第3年--------闰年
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,    //29 2  闰日
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 3
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 4
            4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,//31 5
            5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,  //30 6
            6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,//31 7
            7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,//31 8
            8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,  //30 9
            9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,//31 10
            10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,  //30 11
            11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,//31 12
    };
    //根据天数查4年1461天的每天所在的年份
    public static final int[] julianYear1461 = {
            //--------第0年--------
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,      //28 2
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 3
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //30 4
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 5
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //30 6
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 7
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 8
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //30 9
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 10
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //30 11
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 12
            //--------第1年--------
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,      //28 2
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 3
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,  //30 4
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 5
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,  //30 6
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 7
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 8
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,  //30 9
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 10
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,  //30 11
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,//31 12
            //--------第2年--------
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 1
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,      //28 2
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 3
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  //30 4
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 5
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  //30 6
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 7
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 8
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  //30 9
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 10
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  //30 11
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,//31 12
            //--------第3年--------闰年
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 1
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,    //29 2  闰日
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 3
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 4
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 5
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 6
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 7
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 8
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 9
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 10
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,  //30 11
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 12
    };

    public static final long XH_TROPICAL_START_DAY = 292755180;
    //玄黄历的唯一一个数据：xhDay;
    public long xhDay;//xhYearStartDay<=xhDay&&xhDay<xhYearEndDay
    //参考用的4分历
    public long xhYearPos;//基于0，历元比有巢早801520年，计日从0点算起
    public long xhYearStartDay;//基于0，年长365.25天,4年一闰，计日为主。提供一个可靠的计日参考。
    public long xhYearEndDay;//=xhYearStartDay+xhYearDays
    public long xhYearDays;//一年的天数，平年365，闰年366。
    public long xhYearQs;//=xhYearPos/4;第几个4倍年数。
    public long xhYearQPos;//=(xhYearPos/4)*4;第几个4倍年位置
    public long xhYearQOff;//=xhYearPos%4，4分历4年中的第几年，0，1，2，3.其中3为闰年位置
    public long xyYearQStartDay;//=xhYearQs*1461
    public boolean xhYearLeap;//是不是leap year，闰年，凡(xhYearPos+1)4的倍数都是闰年

    public int xhQDayOffset;//=xhDay-xyYearQStartDay
    public int jurefMonth, jurefDay;//这是用来方便使用基于Julian的现有资料
    public void xhLoadYear(long zeroBasedYear) {
        if (zeroBasedYear < 0) zeroBasedYear = 0;
        xhYearPos = zeroBasedYear;
        xhYearQs = xhYearPos / 4;
        xhYearQPos = xhYearQs * 4;
        xhYearQOff = xhYearPos % 4;
        xhYearLeap = (xhYearQOff == 3);
        if (xhYearLeap) xhYearDays = 366;
        else xhYearDays = 365;
        xhYearStartDay = xhYearQs * 1461 + xhYearQOff * 365;
        xhYearEndDay = xhYearStartDay + xhYearDays - 1;
        xhDay = xhYearStartDay;
        xyYearQStartDay = xhYearQs * 1461;

        xhQDayOffset = (int)(xhDay -xyYearQStartDay);
        jurefMonth = julianMonth1461[xhQDayOffset];
        jurefDay = julianDate1461[xhQDayOffset];
    }
    public void xhLoadDay(long zbDay) {
        if (zbDay < 0) zbDay = 0;
        xhDay = zbDay;
        xhYearQs = xhDay / 1461;
        xhYearQPos = xhYearQs * 4;
        xyYearQStartDay = xhYearQs * 1461;
        xhYearQOff = julianYear1461[(int)(xhDay %1461)];
        xhYearLeap = (xhYearQOff == 3);
        if (xhYearLeap) xhYearDays = 366;
        else xhYearDays = 365;
        xhYearPos = xhYearQPos + xhYearQOff;
        xhYearStartDay = xhYearQs * 1461 + xhYearQOff * 365;
        xhYearEndDay = xhYearStartDay + xhYearDays - 1;

        xhQDayOffset = (int)(xhDay -xyYearQStartDay);
        jurefMonth = julianMonth1461[xhQDayOffset];
        jurefDay = julianDate1461[xhQDayOffset];
    }
    public void xhLoadTropicalDay(long dzDay) {
        if (dzDay < 0) dzDay = 0;
        xhLoadDay(XH_TROPICAL_START_DAY + dzDay);
    }
    public void xhAdvanceDay() {
        xhDay++;
        xhQDayOffset++;
        if (xhDay > xhYearEndDay)//goes to next year.
            xhLoadYear(xhYearPos + 1);
        else {
            /*
            if(xhQDayOffset>=1461) {
                Log.d("xinhe","ArrayIndexOutOfBoundsException "+xhQDayOffset+
                        " y "+xhYearPos+
                        " s "+xhYearStartDay+" /c "+ xhDay +" /e "+xhYearEndDay);
            }*/
            jurefMonth = julianMonth1461[xhQDayOffset];
            jurefDay = julianDate1461[xhQDayOffset];
        }
    }
	public String toXuanhuangString() {
		StringBuilder sb=new StringBuilder();
		//sb.append("玄黄流日：#"+xhDay + " 流年#"+xhYearId+" 12/23 1/365\n");
		sb.append("玄黄流年：#"+xhYearPos+" 参考拙历日月"+(jurefMonth+1) +"/"+(jurefDay+1));
		return sb.toString();
	}

	//----------------拙历历和割利历部分。
    public static final long XH_JULIAN_START_DAY=1299278623;
    public static final long XH_JESUS_START_DAY =1301000046;
    public static final long XH_OLIX1015_START_DAY =1301577783;
    //stream 是从0点开始算的
    //Julian Day从西元前4713年1月1日中午算，只0.5天。这天被称为Julia第1天。
    //这天在岁月流日中的位置用JDAY0101POS标记，这个值是个软件设定值，可以根据设定做各种修改。
    //public static final long JDAY0101POS = 1006522418+1025;//儒略日的位置。XH2755766年
    //西元历元开始天的流日位置，二者相差了1721423天，这天在岁月流日中的位置用OLI00101POS标记
    //public static final long OLI00101POS = JDAY0101POS + 1721423;//儒略纪元日的位置。
    //西元1582年10月4日的流日位置，这是西历跳历前的最后一日。
    //public static final long OLIX1004POS = JDAY0101POS + 2299159;
    //西元1582年10月15日的流日位置，这是西历跳历后的第一日。
    //public static final long OLIX1015POS = JDAY0101POS + 2299160;
    public static final long QUARTER4YEARDAYS = 1461;//四分历4年周期的天数

    //----------------割利历部分
    //以下是OliConvert的代码移植
    public static final int JULIAN_DAYS = 1721423;//
    public static final int JULIAN_YEARS = 4713;//
    public static final int SHORT_YEAR = 1582;//虚构欧历1582/10.5-10.14都是不存在的，算10.15.
    public static final int JUMP_DAY = 577737;
    public static final int LEAP_UNSAFE2=3200;
    public static final int LEAP_SAFE2=76800;//172800;100多万年后的效果还是76800能保持较接近的日期。
    public static final int[] monthStartSmall={0,31,59,90,120,151,181,212,243,273,304,334,365};
    public static final int[] monthStartBig  ={0,31,60,91,121,152,182,213,244,274,305,335,366};
    public static final int[] monthDaysSmall={31,28,31,30,31,30,31,31,30,31,30,31,31};
    public static final int[] monthDaysBig  ={31,29,31,30,31,30,31,31,30,31,30,31,31};

    
    /**
     * 查看西历某年是不是闰年。
     * @param year 西元year年，比如year=2000,就是西元2000年，-100,表示西元前100年。
     * @param isJulian，是不是Julian模式，也就是年均长按365.25天简单计算。
     * @return true，本年闰年，当有366年，else 365天。
     */
    public static boolean isJulianLeapYear(int year, boolean isJulian) {
        if(year<=0) {//西元前，都采用酉劣历。year当不为0，为0是计算错了。这里不做检查。
            year=-year-1;
            if((year%4)==0)
                return true;
        }
        int mod=year%4;
        if(year<0 || mod!=0) return false;//不是4的位数，都是平年。
        if(year< SHORT_YEAR || isJulian) return  true;//1582年前，酉劣历，4的倍数都是闰年。
        mod=year%100;
        if(mod!=0) return true;//是4的倍数，但是不是100的位数，都是闰年
        mod=year%400;
        if(mod!=0) return  false;//是100的倍数，但是不是400的位数，都不是闰年
        if(year<LEAP_UNSAFE2)return true; //LEAP_UNSAFE2年前，都是闰年
        mod=year%LEAP_UNSAFE2;
        if(mod!=0) return true;//不是LEAP_UNSAFE2的倍数，都是闰年
        mod=year%LEAP_SAFE2;
        if(mod!=0) return false;//是LEAP_UNSAFE2的倍数，但是不是LEAP_SAFE2的位数，都不是闰年
        return true;
    }

    /**
     * 计算从西历1年1月1日到西历某年之前的所有整年的天数，不含这年的第一天。
     * @param year ：见isLeapYear()该参数介绍
     * @param isJulian ：见isLeapYear()该参数介绍
     * @return 从西历1年1月1日到西历某年之前的所有整年的天数，不含这年的第一天。
     */
    public static long xiyuanGetBeforeYearAccDays(int year, boolean isJulian) {
        //1582年以前好算。
        //1582年只有355天。
        //1582年后按平年355天算，加上每个闰年的1天，减去1582年少的10天。
        //由于之前闰了12天，实际天数提了10天，还有2天被去掉了。因此最后要再-2.
        //最后加上本年的部分，days+=dayYearOffset；
        int past=year-1;
        int nr4=past/4;
        int nr100=past/100;
        int nr400=past/400;
        int nrUnsafe2=past/LEAP_UNSAFE2;
        int nrSafe2=past/LEAP_SAFE2;
        long beforeDays;
        beforeDays=past*365;
        beforeDays+=nr4;
        //if(year< SHORT_YEAR || (year== SHORT_YEAR && (month<10 || (month==10&&day<5))))
        //    return beforeDays+dayYearOffset;//之前，4的倍数都是闰年。
        if(year<= SHORT_YEAR || isJulian) return beforeDays;//之前，4的倍数都是闰年。
        beforeDays-=nr100;
        beforeDays+=nr400;
        //1582年前beforeDays-=nr100（15），+=nr400（3）多减了12天，这里补回来;
        //但是1582年只有355天，少了10天，这里还是要去掉，所以，只能多加2天。
        beforeDays+=2;
        beforeDays-=nrUnsafe2;
        beforeDays+=nrSafe2;
        //return beforeDays+dayYearOffset;
        return beforeDays;
    }
    private boolean oliShowJulianMode=false;//julianMode:儒略历模式：只要是4的倍数，都是闰年。
    private int oliShowYear=1,oliShowMonth=12, oliShowDay=22;//基数都是1，参见setYearMonthDay。
    private int oliShowDayYearOffset=0;//基于0，一年内的天数，0~365
    private long oliShowAccDays=0;//基于0，这天之前的所有天数。也是这天的offset.
    private boolean oliShowIsLeapYear=false;//是不是闰年
    void oliShowSyncDays() {
        oliShowAccDays = xiyuanGetBeforeYearAccDays(oliShowYear,oliShowJulianMode) +oliShowDayYearOffset;
    }
    void oliShowSyncDayYearOffset() {
        if(!oliShowIsLeapYear) {//平年占3/4多，放前面，提高执行效率。
            if(oliShowYear!= SHORT_YEAR || oliShowJulianMode)//非特殊年份占绝大多数，放前面，提高执行效率。
                oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+oliShowDay-1;
            else {
                //special for 1582.
                if(oliShowMonth<10 || (oliShowMonth==10 && oliShowDay<=4)) {
                    oliShowDayYearOffset = monthStartSmall[oliShowMonth - 1] + oliShowDay - 1;
                } else if(oliShowMonth>10 || (oliShowMonth==10 && oliShowDay>=15)){
                    oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+oliShowDay-1 - 10;
                } else {//虚构欧历1582/10.5-10.14都是不存在的，算10.15.
                    oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+15-1 - 10;
                }
            }
        } else {
            oliShowDayYearOffset=monthStartBig[oliShowMonth-1]+oliShowDay-1;
        }
    }
    /**
     * 根据总天数倒推是哪一年哪一月哪一日。
     * @param oliAccDays 天数的位置，0代表元年1月1号，也就是索引上的0，第一天。之前的不推。
     */
    private void oliShowLoadByOliAccDays(long oliAccDays) {
        if(oliAccDays<0)
            return;
        //从days倒推年月日。
        oliShowYear=1+(int)(oliAccDays/366);
        long beforeDays=xiyuanGetBeforeYearAccDays(oliShowYear,oliShowJulianMode);
        long leftDays=oliAccDays-beforeDays;
        oliShowIsLeapYear=isJulianLeapYear(oliShowYear,oliShowJulianMode);
        for(;;) {
            if(oliShowIsLeapYear && leftDays>=366) {
                oliShowYear++;
                leftDays-=366;
                oliShowIsLeapYear=false;
            } else if(!oliShowIsLeapYear && oliShowYear==SHORT_YEAR && leftDays>=355) {
                oliShowYear++;
                leftDays-=355;
                oliShowIsLeapYear=isJulianLeapYear(oliShowYear,oliShowJulianMode);
            } else if(!oliShowIsLeapYear && leftDays>=365) {
                oliShowYear++;
                leftDays-=365;
                oliShowIsLeapYear=isJulianLeapYear(oliShowYear,oliShowJulianMode);
            } else {
                break;
            }
        }
        int[] monthPos=monthStartSmall;
        if(oliShowIsLeapYear)
            monthPos=monthStartBig;

        if(oliShowYear!=SHORT_YEAR || leftDays<=276) for (int i = 0; i < 12; i++) {
            if (monthPos[i] <= leftDays && leftDays < monthPos[i + 1]) {//find.
                oliShowMonth = i + 1;
                oliShowDay = (int)(leftDays - monthPos[i] + 1);
                break;
            }
        } else for (int i = 9; i < 12; i++){
            leftDays+=10;
            if (monthPos[i] <= leftDays && leftDays < monthPos[i + 1]) {//find.
                oliShowMonth = i + 1;
                oliShowDay = (int)(leftDays - monthPos[i] + 1);
                break;
            }
            leftDays-=10;
        }
        //syncLeapYear();
        oliShowSyncDayYearOffset();
        oliShowSyncDays();
    }
    public void oliShowAdvanceDay() {//快速前进到明天，给外部使用的一个高效方法。
        //若是西元前的年份，也能凑合着推算一下，需要自行设定是否闰年
        boolean isMonthEnd;
        oliShowAccDays++;
        oliShowDayYearOffset++;

        if(oliShowIsLeapYear) {
            isMonthEnd=(monthDaysBig[oliShowMonth-1]==oliShowDay);
        } else {
            isMonthEnd=(monthDaysSmall[oliShowMonth-1]==oliShowDay);
        }

        if(!isMonthEnd) {
            oliShowDay++;
            if(SHORT_YEAR==oliShowYear && 10==oliShowMonth && 5==oliShowDay) {
                oliShowDay=15;//虚构欧历1582/10.5-10.14都是不存在的，算10.15.
            }
        } else {
            oliShowDay=1;
            oliShowMonth++;
            if(13==oliShowMonth) {//跨年的一天。
                oliShowMonth=1;
                oliShowYear++;
                if(oliShowYear==0)//从西元前1年到西元1年的跨度。没有西元0年一说。
                    oliShowYear=1;
                oliShowIsLeapYear=isJulianLeapYear(oliShowYear,oliShowJulianMode);
                oliShowDayYearOffset = 0;
            }
        }
    }
    private void oliShowLoadFromXiyuan(){
        oliShowYear = (int)(xiyuanYear + 1);
        oliShowMonth=(int)(xiyuanMomth +1);
        oliShowDay=(int)(xiyuanDay +1);
        oliShowDayYearOffset=(int)xiyuanYoff;
        oliShowIsLeapYear=xiyuanLeap;
    }
    private void xiyuanLoadFromOliShow(){
        xiyuanYear = oliShowYear-1;
        xiyuanMomth = oliShowMonth-1;
        xiyuanDay = oliShowDay-1;
        xiyuanYoff = oliShowDayYearOffset;
        xiyuanLeap = oliShowIsLeapYear;
        if(xiyuanLeap)
            xiyuanYearLength = 366;
        else
            xiyuanYearLength = 365;
        if(xiyuanYear == 1581)
            xiyuanYearLength = 355;
        xiyuanStartAccDayPos = xhDayPos- XH_JESUS_START_DAY;
    }
	public String toOliString() {
		StringBuilder sb=new StringBuilder();
		oliShowSyncDays();
		if(oliShowYear>0)
			sb.append("西元纪年："+oliShowYear+"年");
		else
			sb.append("西前纪年："+(1-oliShowYear)+"年");
		sb.append(""+oliShowMonth+"月"+oliShowDay+"日 #"+oliShowAccDays+" day");

		return sb.toString();
	}

    //以下都是算法数据，都基于0,显示给用户时请自行转换为基于1的。
    public long xhDayPos;//对应的玄黄历的天数拉位置。
    public long julianStartAccDayPos, julianYear, julianMomth, julianDay, julianYoff, julianYearLength;
    public long xiyuanStartAccDayPos, xiyuanYear, xiyuanMomth, xiyuanDay, xiyuanYoff, xiyuanYearLength;
    //boolean streamLeap;
    public boolean julianLeap;
    public boolean xiyuanLeap;

    void syncJulianDateByDay() {//同步拙劣历部分，不含割利历部分
    	//关键信息：julianStartAccDayPos，拙劣积日。
        long nrLeaps;
        int nrOffset;

        if(julianStartAccDayPos >=0) {
            if(julianStartAccDayPos <365) {//first year
                julianYear=0;
                julianMomth=julianMonth1461[(int) julianStartAccDayPos];
                julianDay=julianDate1461[(int) julianStartAccDayPos];
                julianYoff= julianStartAccDayPos;
                julianYearLength=365;
                julianLeap=false;
                return;
            }
            nrLeaps = (julianStartAccDayPos - 365) / QUARTER4YEARDAYS;
            nrOffset = (int) ((julianStartAccDayPos - 365) % QUARTER4YEARDAYS);
            julianYear = 1 + nrLeaps * 4 + julianYear1461[nrOffset];
            julianMomth = julianMonth1461[nrOffset];
            julianDay = julianDate1461[nrOffset];

            if (julianYear1461[nrOffset] == 3) {
                julianYoff = nrOffset - 365 * 3;
                julianYearLength = 366;
                julianLeap = true;
            } else {
                julianYoff = nrOffset % 365;
                julianYearLength = 365;
                julianLeap = false;
            }
            return;
        }

        long negPos=-julianStartAccDayPos;
        if(negPos<=365*3) {
            nrOffset = (int) (365*3-negPos);
            julianYear = julianYear1461[nrOffset]-3;
            julianMomth = julianMonth1461[nrOffset];
            julianDay = julianDate1461[nrOffset];
            julianYoff = nrOffset % 365;
            julianYearLength = 365;
            julianLeap = false;
            return;
        }

        negPos--;

        nrLeaps = (negPos - 365*3) / QUARTER4YEARDAYS;
        nrOffset = (int) (QUARTER4YEARDAYS-1-(negPos - 365*3) % QUARTER4YEARDAYS);

        julianYear = -1 * nrLeaps * 4 + julianYear1461[nrOffset] - 4 -3;
        julianMomth = julianMonth1461[nrOffset];
        julianDay = julianDate1461[nrOffset];

        if (julianYear1461[nrOffset] == 3) {
            julianYoff = nrOffset - 365 * 3;
            julianYearLength = 366;
            julianLeap = true;
        } else {
            julianYoff = nrOffset % 365;
            julianYearLength = 365;
            julianLeap = false;
        }
    }
    private void syncXiyuanDateByDay() {//同步割利历部分，不含拙劣历部分
    	//关键信息：xhDayPos指定的玄黄日。
        if(xhDayPos<XH_OLIX1015_START_DAY) {
            xiyuanYear = julianYear - 4713;
            xiyuanMomth = julianMomth;
            xiyuanDay = julianDay;
            xiyuanYoff = julianYoff;
            xiyuanYearLength = julianYearLength;
            xiyuanLeap = julianLeap;
            if(xiyuanYear == 1581)
                xiyuanYearLength = 355;
            oliShowLoadFromXiyuan();
            return;
        }
        long allDays = xhDayPos- XH_JESUS_START_DAY;
        oliShowLoadByOliAccDays(allDays);
        //sync from setDays' result.
        xiyuanLoadFromOliShow();
    }
    public void julianAdvanceDay(){//day++, then
    	//usage: after julianLoadXunahuangDay(), call this 365 times.
        xhDayPos++; julianStartAccDayPos++; xiyuanStartAccDayPos++;
        
        syncJulianDateByDay();//同步拙劣历部分，不含割利历部分
        
        //同步割利历部分，不含拙劣历部分
        if(xhDayPos >XH_JESUS_START_DAY) {//西元纪元以后。
            oliShowAdvanceDay();
            //sync from setDays' result.
            xiyuanLoadFromOliShow();
        } else {//西元纪元前的部分。
            syncXiyuanDateByDay();
        }
    }
    public void julianLoadXunahuangDay(long xhday) {
        xhDayPos=xhday;
        julianStartAccDayPos = xhDayPos-XH_JULIAN_START_DAY;
        xiyuanStartAccDayPos = xhDayPos- XH_JESUS_START_DAY;

        //after julianStartAccDayPos, xiyuanStartAccDayPos is ready
        syncJulianDateByDay();
        syncXiyuanDateByDay();
    }
	public String toJulianString() {
		StringBuilder sb=new StringBuilder();
		sb.append("拙劣流年：#"+julianYear+" "+(julianMomth+1)+"/"+(julianDay+1)+" #"+julianStartAccDayPos+".5 day");
		return sb.toString();
	}
	public String toXiyuanString() {
		StringBuilder sb=new StringBuilder();
		sb.append("割利流年：#"+xiyuanYear+" "+(xiyuanMomth+1)+"/"+(xiyuanDay+1)+" #"+xiyuanStartAccDayPos+" day");
		return sb.toString();
	}

    public static final int LUNER_DAY_DIV940 =940;
    public static final int LUNER_DAY_MOD499 =499;
    public static final int LUNER_SUO_LEN=940*29+499;//=27759
    public static final int SU_PIECES=27759;
    public static final int BU_DAYS=27759;
    public static final int suStarts[]=new int[940];
    public static final int suAccs[]=new int[940];
    private static boolean suTableInitialized =false;
    public static void initSuTable() {
        if(suTableInitialized)
            return;
        suTableInitialized =true;

        int i;
        for(i=0;i<940;i++) {
            int suPiecePos=i*SU_PIECES;
            int suDayPos=suPiecePos/ LUNER_DAY_DIV940;
            int suStart=suPiecePos% LUNER_DAY_DIV940;
            int suAcc= LUNER_DAY_DIV940 -suStart;
            suStarts[suStart]=i;
            suAccs[suAcc-1]=i;
            if(suAcc<= LUNER_DAY_MOD499) {//30 days
                //Log.e("xinhe", " "+i+" 30, "+suStart +", "+suAcc);
            } else { //29 days
                //Log.e("xinhe", " "+i+" 29, "+suStart +", "+suAcc);
            }
        }
    }
    /**
     * 向后推算某年的朔分值。
     * @param baseDayPos 参考流日
     * @param baseSuAcc 参考流日所在的朔月的朔日的累计朔分值
     * @param targetDayPos 目标参考流日
     * @return targetSuAcc, -939 ~ LUNER_SUO_LEN-940， 参考流日所在的朔月的朔日的累计朔分值
     */
    public static long getSuAcc(long baseDayPos, long baseSuAcc, long targetDayPos) {
        if(targetDayPos<=baseDayPos)
            return baseSuAcc;

        long days=targetDayPos-baseDayPos;//先算出差数
        days=days%BU_DAYS;//去年整数部分：每一蔀4章，76年，940月，27759日

        long piece=days*940+baseSuAcc;//将剩余的部分转化为朔分
        piece=piece%LUNER_SUO_LEN;//再将余下的去年整朔月部分。
        if(piece>LUNER_SUO_LEN-940)//调整到当月朔分所在的范围。
            piece-=LUNER_SUO_LEN;
        return piece;
    }
    public long baseDayPos, baseSuAcc, targetDayPos, targetDayAcc;
    public int suDay, suIndex, suFirstDayAcc,suMills,suSeconds;
    public boolean suIsBig;
    public int loadSuAcc(long baseDayPos, long baseSuAcc, long targetDayPos) {
        this.baseDayPos=baseDayPos;
        this.baseSuAcc=baseSuAcc;
        this.targetDayPos=targetDayPos;
        this.targetDayAcc=getSuAcc(baseDayPos, baseSuAcc, targetDayPos);
        suDay=(int)(targetDayAcc+939)/940;
        suIndex=0;
        suFirstDayAcc=(int)((targetDayAcc+940)%940);
        if(suFirstDayAcc==0)
            suFirstDayAcc=940;
        suMills=(86400000*(940-suFirstDayAcc)/940);//maybe overflow, not use it.
        suSeconds=(86400*(940-suFirstDayAcc)/940);
        suIsBig=suFirstDayAcc<=499;
        return suDay;
    }
    public int advanceSuDay() {
        targetDayAcc += LUNER_DAY_DIV940;
        //implement suDay=(int)(targetDayAcc+939)/940;
        if(targetDayAcc<=LUNER_SUO_LEN- LUNER_DAY_DIV940) {//this month
            suDay++;
        } else {//next su
            targetDayAcc-=LUNER_SUO_LEN;
            suDay=0;
            suIndex++;

            suFirstDayAcc=(int)((targetDayAcc+940)%940);
            if(suFirstDayAcc==0)
                suFirstDayAcc=940;
            suMills=(86400000*(940-suFirstDayAcc)/940);
            suIsBig=suFirstDayAcc<=499;
        }
        return suDay;
    }
	public String toSuString() {
		StringBuilder sb=new StringBuilder();
		sb.append("朔望阴历：冬月"+(suIsBig?"大":"小")+" "+(suDay+1)+"日，朔点："
		    +secToTime(suSeconds)+" 至始朔积："+targetDayAcc);
		return sb.toString();
	}
	public String toStageTableItemString() {
		StringBuilder sb=new StringBuilder();
		sb.append("查表基数："+dzYear+",  "+seekYearPos+",  "+dzSecond+",  "+targetDayAcc);
		return sb.toString();
	}
	
	
	
    //----------------加载具体日期部分
    public long dzYear;//0 based;
	public long dzSecond;//the first day's DZ start seconds.
	public long dzDay=0;//0based, 0~366
	public DongziYear() {}
	public DongziYear(long dzYearId) {loadYear(dzYearId);}
	public void loadYear(long dzYearId) {
		dzYear=dzYearId;
		getDzPosition(dzYear);
		xhLoadDay(seekYearPos);
		julianLoadXunahuangDay(seekYearPos);
		dzSecond=(long)(86400*seekYearDoubleOffset);
		loadSuAcc(seekYearSuBasePos,
                seekYearSuBaseAcc,seekYearPos);
	}
	public String secToTime(long sec) {
		int hour=(int)(sec/3600);
		sec %=3600;
		int minute=(int)(sec/60);
		sec %=60;
		return ""+hour+":"+minute+":"+sec;
	}
	public String toCompareString() {
		StringBuilder sb=new StringBuilder();
		//sb.append("玄黄流日：#"+xhDay + " 流年#"+xhYearId+" 12/23 1/365\n");
		sb.append("冬至流年：#"+dzYear+" 冬至点 "+secToTime(dzSecond) +" ("+dzSecond+")秒");
		return sb.toString();
	}

    public void println(String string) {
    	System.out.println(string);
    }
    public void println(StringBuilder sb) {
    	System.out.println(sb.toString());
    }
	public void test() {
		int end=2762512;
		int length=7000;
		int start=end-length;
		for(int i=start;i<end;i++) {
			loadYear(i);
			println("--------流年倒幕参照表  dzYear#"+i+" xhDay=#"+seekYearPos+" --------");
			println(toSeekDzString());
			println(toCompareString());
			println(toXuanhuangString());
			println(toJulianString());
			println(toXiyuanString());
			println(toOliString());
			println(toSuString());
			println(toStageTableItemString());
			
			println("\n");
		}
	}

	//Module test debug code
	public static DongziYear thisInstance=null;
	public static void main(String[] args){
		initSuTable();
		if(thisInstance==null)
			thisInstance = new DongziYear();
		System.out.println("冬至流年算法调试报表\n");
		thisInstance.test();
	}
}
