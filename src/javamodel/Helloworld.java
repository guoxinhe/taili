
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
			sb.append("--------���굹Ļ���ձ�-----------------------------------\n");
			sb.append("�������գ�#"+xhDay + " ����#"+xhYearId+" 12/23 1/365\n");
			sb.append("�������꣺#"+yearId + " ���� 1/365���곤365.24255555, ������155��\n");
			sb.append("��������������1�죬 12��00ʼ   �ľ�1�죺\n");
			sb.append("˷������������ĳĳ  ��һ˷�£�����˷��-235 ����\n");
			
			sb.append("�л���Ԫ��"+(yearId-484) + " ����\n");
			sb.append("���˼�Ԫ��"+(yearId-57484) + " ����\n");
			sb.append("��ԯ��Ԫ��"+(yearId-2757484) + " #12345�� ����\n");
			sb.append("�Ƶۼ�Ԫ��"+(yearId-2757784) + "����������� ������ #12345�� \n");
			sb.append("����������"+(yearId-2757784) + "������ʮһ�� ������\n");
			sb.append("׾�����գ�#"+(yearId-2755768) + ".5 2018��3��25��\n");
			sb.append("����������"+(yearId-2760482) + "��2��1��\n");
			
			sb.append("���գ������ռ���ʱ�� ���գ� ��������1��ľ��������һ\n");
			sb.append("���꣺̫�꣺ĳĳ���\n");
			sb.append("�Ƶ���ĳĳ����\n");
			sb.append("�׵���ĳĳ��˷�£����ص�Զ�ص���ʳ��ʳ\n");
			
			sb.append("����������\n");
			sb.append("  ���ˣ����˲���\n");
			sb.append("  ���ˣ��ٽǣ����ˣ�\n");
			sb.append("  ���ˣ��ٹ������ˣ�\n");
			sb.append("  ������������ľ��˾�죩���������Ȫ��\n");
			sb.append("  ������������ľ��������\n");
			sb.append("  ������������𣨳�����\n");

			
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
