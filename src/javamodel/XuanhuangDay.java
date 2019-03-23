/**
 * Debug method:Top menu->Run->Debug As->Java Application
 * F5 enter, F6 over, F7 return, Ctrl+R to cursor.
 * @author xinhe
 *
 */
public class XuanhuangDay{
	public static final long ZHYEARPOS=129600*12;
	public static final long TOTALYEARS=5800000;
    long dzYear;
    long zhYear;
    long xhDay;
    void loadDZYear(long year) {
    	dzYear = year;
		zhYear=dzYear-ZHYEARPOS;
		xhDay = (long)(dzYear * (double)365.25d);
    }
    public void println(String string) {
    	System.out.print(string+"\n");
    }
    public void print(String string) {
    	System.out.print(string);
    }
    public void println(StringBuilder sb) {
    	System.out.println(sb.toString());
    }
    
	public void test() {
		long i;
		for(i=0;i<TOTALYEARS;i+=100000) {
			loadDZYear(i);
			if(zhYear>0)
				break;
			print("冬至年 "+dzYear);
			println("  玄黄日 " + xhDay);
		}
		for(i=ZHYEARPOS;i<TOTALYEARS;i+=100000) {
			loadDZYear(i);
			print("冬至年 "+dzYear);
			if(zhYear>=0)
				print("  中华年 "+zhYear);
			println("  玄黄日 " + xhDay);
		}
	}

	//Module test debug code
	public static XuanhuangDay thisInstance=null;
	public static void main(String[] args){
		if(thisInstance==null)
			thisInstance = new XuanhuangDay();
		System.out.println("玄黄流年算法调试报表\n");
		thisInstance.test();
	}
}
