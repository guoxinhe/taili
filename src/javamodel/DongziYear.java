
public class DongziYear{
	//----------------�����겿��
	
    public static final int SEARCH_STEP=4;//
    public static final long dzPosition[]={//����̨���ݣ����ڶ���120(���ױ���|�ػʵ�|�ൺ|����|��ˮ|̨��)��
        //4��һ�飬�ֱ��ʾ���ع��꣬�ع��գ����쵽������Ļع����࣬˷�����������ݶ��ǻ���0.
        //����������    ����������   �ع����룬    ˷��
        //    0,    292756227,         0,        0, //0  //average year length=365.24220723187403
              0,    292734712,         0,        0, //0  //average year length=365.24300008682616
        2757783,   1300014953,         0,        0, //   �Ƶ���Ԫ����ʱ������������˷��
        2758380,   1300233026,     48900,        0, //  ��ǰ2100��1��6��13��35����
        2758386,   1300235218,      3240,     2043, //1 BC2094 1/4 19:50�����ջ�ʳʳ�� 1/7  0:54���ȶ���
        2758405,   1300242157,     56160,     1127, //2 BC2075 1/4 19:13������ȫʳʳ�� 1/6 15:36���궬��
        2759475,   1300632967,     69900,     3117, //3 BC1006 12/26 16:24������ȫʳʳ�� 12/30 19:25��������
        2760434,   1300983235,     81060,     7954, //4 BC47 12/14 12:54�����ջ�ʳʳ�� 12/23 22:31��������
        2760499,   1301006976,     63120,     7138, //5 AD19 12/15  9:45�Ѻ��ջ�ʳʳ�� 12/23 17:32��δ����
        2762380,   1301693998,     52894,      626, //6 AD1900 12/22 8:1:15�����ճ�һ��14��41��34����
        2762679,   1301803206,     17336,        0, //8 AD2199/12/22 ����
        3888000,   1712818261,         0,        0, //9 3888000 ����
        3999999,   1753725055,         0,        0, //10
    };
    public static int searchDzPositionIndex(long dzYear) { //2�ַ����ٲ������������顣
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
                        (dzPosition[min*SEARCH_STEP+5]-dzPosition[min*SEARCH_STEP+1])-1 //������-1
                        +  (86400+dzPosition[min*SEARCH_STEP+SEARCH_STEP+2]-
                                dzPosition[min*SEARCH_STEP+2])/86400.0d) //�����ٽ�һ�죬ת����
                        /((dzPosition[min*SEARCH_STEP+SEARCH_STEP]-
                          dzPosition[min*SEARCH_STEP]));//��������������ƽ���곤��
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

    //���������ķ�����
    public int seekTableIndex=0; //��dzPosition[]����к�λ�ã�ÿSEARCH_STEP����4��Ԫ��Ϊһ��
    public double seekYearDoublePos = 0;//���궬������λ��
    public double seekYearDoubleNextPos=0;//���궬������λ��
    public double seekYearDoubleOffset=0;//���궬���յĵ����С������
    public long seekYearPos=0;//���궬������λ��
    public long seekYearNextPos=0;//���궬������λ��
    public long seekYearLength=0;//365, 366
    public long seekYearSecondOffset=0;//�����춬���������λ�ã���seekYearDoubleOffset*86400��
    public long seekYearSuBaseAcc =0;//˷�µĲ���׼˷��
    public long seekYearSuBasePos =0;//˷�µĲ���׼��λ��

    //����������������տ�ʼ��������С������
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
		//sb.append("�������գ�#"+xhDay + " ����#"+xhYearId+" 12/23 1/365\n");
		sb.append("�������#"+ seekTableIndex + " avg="+dzAverageYearLength[seekTableIndex]+"\n");
		sb.append("    xhDay=#"+seekYearPos+" dzOffset="+seekYearDoubleOffset+" days="+seekYearLength+"\n"
				+"    "+JiaziName[jiaziFromDay(seekYearPos)]
				+"��  "+seekYearDoublePos+" to "+seekYearDoubleNextPos);
		return sb.toString();
    }

  //----------------����������
    public static final String[] JiaziName = {
        //"0A","0B","0C","0D","0E","0F","0G","0H","0I","0J",
        //"1K","1L","1A","1B","1C","1D","1E","1F","1G","1H",
        //"2I","2J","2K","2L","2A","2B","2C","2D","2E","2F",
        //"3G","3H","3I","3J","3K","3L","3A","3B","3C","3D",
        //"4E","4F","4G","4H","4I","4J","4K","4L","4A","4B",
        //"5C","5D","5E","5F","5G","5H","5I","5J","5K","5L",
        "����","�ҳ�","����","��î","�쳽","����","����","��δ","����","����",
        "����","�Һ�","����","����","����","��î","����","����","����","��δ",
        "����","����","����","����","����","����","����","��î","�ɳ�","����",
        "����","��δ","����","����","����","����","����","����","����","��î",
        "�׳�","����","����","��δ","����","����","����","����","����","���",
        "����","��î","����","����","����","��δ","����","����","����","�ﺥ",

        "����","�ҳ�","����","��î","�쳽","����","����","��δ","����","����","����","�Һ�",
        "����","����","����","��î","����","����","����","��δ","����","����","����","����",
        "����","����","����","��î","�ɳ�","����","����","��δ","����","����","����","����",
        "����","����","����","��î","�׳�","����","����","��δ","����","����","����","����",
        "����","���","����","��î","����","����","����","��δ","����","����","����","�ﺥ",
    };
    //���������������
    public static int jiaziFromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+7)%60);
    }
    //�����������ʮ��������
    public static int star28FromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+13)%28);
    }
    //���������㽨��12��
    public static int jianchu12FromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+9)%12);//
    }
    //�������������ǣ��������У����ڼ�
    public static int qiyaoFromDay(long xhDayPosInput) {
        return (int)((xhDayPosInput+3)%7);
    }

    //----------------���Ʋο�������
    //ÿ��������4�꣬1461��,ÿ���ǵڣ������գ������ǹ̶��ģ��Ͳ��ü����ˣ�ֱ�Ӳ��ͺá�
    //����������4��1461���ÿ�����ڵ�����
    public static final int[] julianDate1461 = {
            //--------��0��--------
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
            //--------��1��--------
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
            //--------��2��--------
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
            //--------��3��--------����
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,//31 1
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,      //28 2 ����
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
    //����������4��1461���ÿ�����ڵ��·�
    public static final int[] julianMonth1461 = {
            //--------��0��--------
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
            //--------��1��--------
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
            //--------��2��--------
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
            //--------��3��--------����
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,//31 1
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,    //29 2  ����
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
    //����������4��1461���ÿ�����ڵ����
    public static final int[] julianYear1461 = {
            //--------��0��--------
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
            //--------��1��--------
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
            //--------��2��--------
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
            //--------��3��--------����
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,//31 1
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,    //29 2  ����
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
    //��������Ψһһ�����ݣ�xhDay;
    public long xhDay;//xhYearStartDay<=xhDay&&xhDay<xhYearEndDay
    //�ο��õ�4����
    public long xhYearPos;//����0����Ԫ���г���801520�꣬���մ�0������
    public long xhYearStartDay;//����0���곤365.25��,4��һ�򣬼���Ϊ�����ṩһ���ɿ��ļ��ղο���
    public long xhYearEndDay;//=xhYearStartDay+xhYearDays
    public long xhYearDays;//һ���������ƽ��365������366��
    public long xhYearQs;//=xhYearPos/4;�ڼ���4��������
    public long xhYearQPos;//=(xhYearPos/4)*4;�ڼ���4����λ��
    public long xhYearQOff;//=xhYearPos%4��4����4���еĵڼ��꣬0��1��2��3.����3Ϊ����λ��
    public long xyYearQStartDay;//=xhYearQs*1461
    public boolean xhYearLeap;//�ǲ���leap year�����꣬��(xhYearPos+1)4�ı�����������

    public int xhQDayOffset;//=xhDay-xyYearQStartDay
    public int jurefMonth, jurefDay;//������������ʹ�û���Julian����������
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
		//sb.append("�������գ�#"+xhDay + " ����#"+xhYearId+" 12/23 1/365\n");
		sb.append("�������꣺#"+xhYearPos+" �ο�׾������"+(jurefMonth+1) +"/"+(jurefDay+1));
		return sb.toString();
	}

	//----------------׾�����͸��������֡�
    public static final long XH_JULIAN_START_DAY=1299278623;
    public static final long XH_JESUS_START_DAY =1301000046;
    public static final long XH_OLIX1015_START_DAY =1301577783;
    //stream �Ǵ�0�㿪ʼ���
    //Julian Day����Ԫǰ4713��1��1�������㣬ֻ0.5�졣���챻��ΪJulia��1�졣
    //���������������е�λ����JDAY0101POS��ǣ����ֵ�Ǹ�����趨ֵ�����Ը����趨�������޸ġ�
    //public static final long JDAY0101POS = 1006522418+1025;//�����յ�λ�á�XH2755766��
    //��Ԫ��Ԫ��ʼ�������λ�ã����������1721423�죬���������������е�λ����OLI00101POS���
    //public static final long OLI00101POS = JDAY0101POS + 1721423;//���Լ�Ԫ�յ�λ�á�
    //��Ԫ1582��10��4�յ�����λ�ã�������������ǰ�����һ�ա�
    //public static final long OLIX1004POS = JDAY0101POS + 2299159;
    //��Ԫ1582��10��15�յ�����λ�ã���������������ĵ�һ�ա�
    //public static final long OLIX1015POS = JDAY0101POS + 2299160;
    public static final long QUARTER4YEARDAYS = 1461;//�ķ���4�����ڵ�����

    //----------------����������
    //������OliConvert�Ĵ�����ֲ
    public static final int JULIAN_DAYS = 1721423;//
    public static final int JULIAN_YEARS = 4713;//
    public static final int SHORT_YEAR = 1582;//�鹹ŷ��1582/10.5-10.14���ǲ����ڵģ���10.15.
    public static final int JUMP_DAY = 577737;
    public static final int LEAP_UNSAFE2=3200;
    public static final int LEAP_SAFE2=76800;//172800;100��������Ч������76800�ܱ��ֽϽӽ������ڡ�
    public static final int[] monthStartSmall={0,31,59,90,120,151,181,212,243,273,304,334,365};
    public static final int[] monthStartBig  ={0,31,60,91,121,152,182,213,244,274,305,335,366};
    public static final int[] monthDaysSmall={31,28,31,30,31,30,31,31,30,31,30,31,31};
    public static final int[] monthDaysBig  ={31,29,31,30,31,30,31,31,30,31,30,31,31};

    
    /**
     * �鿴����ĳ���ǲ������ꡣ
     * @param year ��Ԫyear�꣬����year=2000,������Ԫ2000�꣬-100,��ʾ��Ԫǰ100�ꡣ
     * @param isJulian���ǲ���Julianģʽ��Ҳ�����������365.25��򵥼��㡣
     * @return true���������꣬����366�꣬else 365�졣
     */
    public static boolean isJulianLeapYear(int year, boolean isJulian) {
        if(year<=0) {//��Ԫǰ����������������year����Ϊ0��Ϊ0�Ǽ�����ˡ����ﲻ����顣
            year=-year-1;
            if((year%4)==0)
                return true;
        }
        int mod=year%4;
        if(year<0 || mod!=0) return false;//����4��λ��������ƽ�ꡣ
        if(year< SHORT_YEAR || isJulian) return  true;//1582��ǰ����������4�ı����������ꡣ
        mod=year%100;
        if(mod!=0) return true;//��4�ı��������ǲ���100��λ������������
        mod=year%400;
        if(mod!=0) return  false;//��100�ı��������ǲ���400��λ��������������
        if(year<LEAP_UNSAFE2)return true; //LEAP_UNSAFE2��ǰ����������
        mod=year%LEAP_UNSAFE2;
        if(mod!=0) return true;//����LEAP_UNSAFE2�ı�������������
        mod=year%LEAP_SAFE2;
        if(mod!=0) return false;//��LEAP_UNSAFE2�ı��������ǲ���LEAP_SAFE2��λ��������������
        return true;
    }

    /**
     * ���������1��1��1�յ�����ĳ��֮ǰ�������������������������ĵ�һ�졣
     * @param year ����isLeapYear()�ò�������
     * @param isJulian ����isLeapYear()�ò�������
     * @return ������1��1��1�յ�����ĳ��֮ǰ�������������������������ĵ�һ�졣
     */
    public static long xiyuanGetBeforeYearAccDays(int year, boolean isJulian) {
        //1582����ǰ���㡣
        //1582��ֻ��355�졣
        //1582���ƽ��355���㣬����ÿ�������1�죬��ȥ1582���ٵ�10�졣
        //����֮ǰ����12�죬ʵ����������10�죬����2�챻ȥ���ˡ�������Ҫ��-2.
        //�����ϱ���Ĳ��֣�days+=dayYearOffset��
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
        //    return beforeDays+dayYearOffset;//֮ǰ��4�ı����������ꡣ
        if(year<= SHORT_YEAR || isJulian) return beforeDays;//֮ǰ��4�ı����������ꡣ
        beforeDays-=nr100;
        beforeDays+=nr400;
        //1582��ǰbeforeDays-=nr100��15����+=nr400��3�������12�죬���ﲹ����;
        //����1582��ֻ��355�죬����10�죬���ﻹ��Ҫȥ�������ԣ�ֻ�ܶ��2�졣
        beforeDays+=2;
        beforeDays-=nrUnsafe2;
        beforeDays+=nrSafe2;
        //return beforeDays+dayYearOffset;
        return beforeDays;
    }
    private boolean oliShowJulianMode=false;//julianMode:������ģʽ��ֻҪ��4�ı������������ꡣ
    private int oliShowYear=1,oliShowMonth=12, oliShowDay=22;//��������1���μ�setYearMonthDay��
    private int oliShowDayYearOffset=0;//����0��һ���ڵ�������0~365
    private long oliShowAccDays=0;//����0������֮ǰ������������Ҳ�������offset.
    private boolean oliShowIsLeapYear=false;//�ǲ�������
    void oliShowSyncDays() {
        oliShowAccDays = xiyuanGetBeforeYearAccDays(oliShowYear,oliShowJulianMode) +oliShowDayYearOffset;
    }
    void oliShowSyncDayYearOffset() {
        if(!oliShowIsLeapYear) {//ƽ��ռ3/4�࣬��ǰ�棬���ִ��Ч�ʡ�
            if(oliShowYear!= SHORT_YEAR || oliShowJulianMode)//���������ռ�����������ǰ�棬���ִ��Ч�ʡ�
                oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+oliShowDay-1;
            else {
                //special for 1582.
                if(oliShowMonth<10 || (oliShowMonth==10 && oliShowDay<=4)) {
                    oliShowDayYearOffset = monthStartSmall[oliShowMonth - 1] + oliShowDay - 1;
                } else if(oliShowMonth>10 || (oliShowMonth==10 && oliShowDay>=15)){
                    oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+oliShowDay-1 - 10;
                } else {//�鹹ŷ��1582/10.5-10.14���ǲ����ڵģ���10.15.
                    oliShowDayYearOffset=monthStartSmall[oliShowMonth-1]+15-1 - 10;
                }
            }
        } else {
            oliShowDayYearOffset=monthStartBig[oliShowMonth-1]+oliShowDay-1;
        }
    }
    /**
     * ������������������һ����һ����һ�ա�
     * @param oliAccDays ������λ�ã�0����Ԫ��1��1�ţ�Ҳ���������ϵ�0����һ�졣֮ǰ�Ĳ��ơ�
     */
    private void oliShowLoadByOliAccDays(long oliAccDays) {
        if(oliAccDays<0)
            return;
        //��days���������ա�
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
    public void oliShowAdvanceDay() {//����ǰ�������죬���ⲿʹ�õ�һ����Ч������
        //������Ԫǰ����ݣ�Ҳ�ܴպ�������һ�£���Ҫ�����趨�Ƿ�����
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
                oliShowDay=15;//�鹹ŷ��1582/10.5-10.14���ǲ����ڵģ���10.15.
            }
        } else {
            oliShowDay=1;
            oliShowMonth++;
            if(13==oliShowMonth) {//�����һ�졣
                oliShowMonth=1;
                oliShowYear++;
                if(oliShowYear==0)//����Ԫǰ1�굽��Ԫ1��Ŀ�ȡ�û����Ԫ0��һ˵��
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
			sb.append("��Ԫ���꣺"+oliShowYear+"��");
		else
			sb.append("��ǰ���꣺"+(1-oliShowYear)+"��");
		sb.append(""+oliShowMonth+"��"+oliShowDay+"�� #"+oliShowAccDays+" day");

		return sb.toString();
	}

    //���¶����㷨���ݣ�������0,��ʾ���û�ʱ������ת��Ϊ����1�ġ�
    public long xhDayPos;//��Ӧ����������������λ�á�
    public long julianStartAccDayPos, julianYear, julianMomth, julianDay, julianYoff, julianYearLength;
    public long xiyuanStartAccDayPos, xiyuanYear, xiyuanMomth, xiyuanDay, xiyuanYoff, xiyuanYearLength;
    //boolean streamLeap;
    public boolean julianLeap;
    public boolean xiyuanLeap;

    void syncJulianDateByDay() {//ͬ��׾�������֣���������������
    	//�ؼ���Ϣ��julianStartAccDayPos��׾�ӻ��ա�
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
    private void syncXiyuanDateByDay() {//ͬ�����������֣�����׾��������
    	//�ؼ���Ϣ��xhDayPosָ���������ա�
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
        
        syncJulianDateByDay();//ͬ��׾�������֣���������������
        
        //ͬ�����������֣�����׾��������
        if(xhDayPos >XH_JESUS_START_DAY) {//��Ԫ��Ԫ�Ժ�
            oliShowAdvanceDay();
            //sync from setDays' result.
            xiyuanLoadFromOliShow();
        } else {//��Ԫ��Ԫǰ�Ĳ��֡�
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
		sb.append("׾�����꣺#"+julianYear+" "+(julianMomth+1)+"/"+(julianDay+1)+" #"+julianStartAccDayPos+".5 day");
		return sb.toString();
	}
	public String toXiyuanString() {
		StringBuilder sb=new StringBuilder();
		sb.append("�������꣺#"+xiyuanYear+" "+(xiyuanMomth+1)+"/"+(xiyuanDay+1)+" #"+xiyuanStartAccDayPos+" day");
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
     * �������ĳ���˷��ֵ��
     * @param baseDayPos �ο�����
     * @param baseSuAcc �ο��������ڵ�˷�µ�˷�յ��ۼ�˷��ֵ
     * @param targetDayPos Ŀ��ο�����
     * @return targetSuAcc, -939 ~ LUNER_SUO_LEN-940�� �ο��������ڵ�˷�µ�˷�յ��ۼ�˷��ֵ
     */
    public static long getSuAcc(long baseDayPos, long baseSuAcc, long targetDayPos) {
        if(targetDayPos<=baseDayPos)
            return baseSuAcc;

        long days=targetDayPos-baseDayPos;//���������
        days=days%BU_DAYS;//ȥ���������֣�ÿһɞ4�£�76�꣬940�£�27759��

        long piece=days*940+baseSuAcc;//��ʣ��Ĳ���ת��Ϊ˷��
        piece=piece%LUNER_SUO_LEN;//�ٽ����µ�ȥ����˷�²��֡�
        if(piece>LUNER_SUO_LEN-940)//����������˷�����ڵķ�Χ��
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
		sb.append("˷������������"+(suIsBig?"��":"С")+" "+(suDay+1)+"�գ�˷�㣺"
		    +secToTime(suSeconds)+" ��ʼ˷����"+targetDayAcc);
		return sb.toString();
	}
	public String toStageTableItemString() {
		StringBuilder sb=new StringBuilder();
		sb.append("��������"+dzYear+",  "+seekYearPos+",  "+dzSecond+",  "+targetDayAcc);
		return sb.toString();
	}
	
	
	
    //----------------���ؾ������ڲ���
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
		//sb.append("�������գ�#"+xhDay + " ����#"+xhYearId+" 12/23 1/365\n");
		sb.append("�������꣺#"+dzYear+" ������ "+secToTime(dzSecond) +" ("+dzSecond+")��");
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
			println("--------���굹Ļ���ձ�  dzYear#"+i+" xhDay=#"+seekYearPos+" --------");
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
		System.out.println("���������㷨���Ա���\n");
		thisInstance.test();
	}
}
