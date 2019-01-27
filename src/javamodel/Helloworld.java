
public class Helloworld{
	public class Tropicyear {
		public long yearId;
		public long xhYearId;
		public long xhDay;
		public Tropicyear() {}
		public void Loadyear(long year) {
			yearId=year;
			xhYearId = year+801465;
			xhDay=xhYearId*365+xhYearId/4;
		}

		public String toString() {
			StringBuilder sb=new StringBuilder();
			sb.append("--------流年倒幕参照表-----------------------------------\n");
			sb.append("玄黄流日：#"+xhDay + " 流年#"+xhYearId+" 12/23 1/365\n");
			sb.append("冬至流年：#"+yearId + " 天数 1/365，年长365.24255555, 冬至点155秒\n");
			sb.append("阳历节气：冬至1天， 12：00始   四九1天：\n");
			sb.append("朔望阴历：冬月某某  初一朔月，子正朔积-235 纯阴\n");
			
			sb.append("中华纪元："+(yearId-484) + " 纯阳\n");
			sb.append("伏羲纪元："+(yearId-57484) + " 纯阳\n");
			sb.append("轩辕纪元："+(yearId-2757484) + " #12345日 纯阳\n");
			sb.append("黄帝纪元："+(yearId-2757784) + "甲子年甲子月 土狗年 #12345日 \n");
			sb.append("黄历建寅："+(yearId-2757784) + "甲子年十一月 土猪年\n");
			sb.append("拙劣流日：#"+(yearId-2755768) + ".5 2018年3月25日\n");
			sb.append("割利西历："+(yearId-2760482) + "年2月1日\n");
			
			sb.append("流日：甲子日甲子时起， 建日， 东方青龙1角木狗，火曜一\n");
			sb.append("流年：太岁：某某神君\n");
			sb.append("黄道：某某星座\n");
			sb.append("白道：某某月朔月，近地点远地点月食日食\n");
			
			sb.append("五运六气：\n");
			sb.append("  岁运：土运不及\n");
			sb.append("  主运：少角（初运）\n");
			sb.append("  客运：少宫（初运）\n");
			sb.append("  岁气：厥阴风木（司天）少阳相火（在泉）\n");
			sb.append("  主气：厥阴风木（初气）\n");
			sb.append("  客气：阳明燥金（初气）\n");

			
			return sb.toString();
		}
	}

	public void test() {
		Tropicyear tr=new Tropicyear();

		int end=2762512;
		int length=60;
		int start=end-length;
		for(int i=start;i<end;i++) {
			tr.Loadyear(i);
			System.out.println(tr.toString());
		}

	}

	public static Helloworld thisInstance=null;
	public Helloworld() {}
	public static void main(String[] args){
		if(thisInstance==null)
			thisInstance = new Helloworld();
		System.out.println("HELLO WORLD!");
		thisInstance.test();
	}
}
