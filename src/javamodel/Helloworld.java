
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
			sb.append("-------------------------------------------\n");
			sb.append("�������գ�"+xhDay + "/"+xhYearId+"\n");
			sb.append("�������꣺"+yearId + "\n");
			sb.append("��ԯ��Ԫ��"+(yearId-2757484) + "\n");
			sb.append("�Ƶ۽��ӣ�"+(yearId-2757784) + "\n");
			sb.append("����������"+(yearId-2757784) + "\n");
			sb.append("׾�����գ�"+(yearId-2755768) + "\n");
			sb.append("��������  "+(yearId-2760482) + "\n");
			return sb.toString();
		}
	}

	public void test() {
		Tropicyear tr=new Tropicyear();

		for(int i=2755444;i<2762512;i++) {
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
