package cool;

public class ReportError {
	public static boolean ef = false;
	public void report(String filename, int lineNo, String error) {
		ef = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
}